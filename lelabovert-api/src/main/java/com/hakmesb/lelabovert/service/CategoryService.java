package com.hakmesb.lelabovert.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Category;
import com.hakmesb.lelabovert.payload.CategoryDto;
import com.hakmesb.lelabovert.payload.mapper.CategoryDtoMapper;
import com.hakmesb.lelabovert.repository.CategoryRepository;
import com.hakmesb.lelabovert.util.Slugify;

@Transactional
@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryDtoMapper categoryDtoMapper;
	private final FileService fileService;
	
	public CategoryService (CategoryRepository categoryRepository, CategoryDtoMapper categoryDtoMapper,
			FileService fileService) {
		this.categoryRepository = categoryRepository;
		this.categoryDtoMapper = categoryDtoMapper;
		this.fileService = fileService;
	}
	
	public CategoryDto createCategory(Category category) {
		Category dbCategory = categoryRepository.findByName(category.getName());
		
		if(dbCategory != null) {
			throw new ApiException("Category with the name '" + category.getName() + "' already exists.");
		}
		
		String slug = Slugify.toSlug(category.getName());
		category.setSlug(slug);
		
		dbCategory = categoryRepository.save(category);
		
		return categoryDtoMapper.apply(dbCategory);
	}
	
	public CategoryDto getCategory(Integer categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		return categoryDtoMapper.apply(category);
	}
	public CategoryDto getCategoryBySlug(String slug) {
		Category category = categoryRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));
		
		return categoryDtoMapper.apply(category);
	}
	
	public List<CategoryDto> getCategories(){
		
		List<Category> categories = categoryRepository.findAll();
		
//		if(categories.size() == 0) {
//			throw new ApiException("No category is created till now.");
//		}
		
		return categories.stream().map(categoryDtoMapper).collect(Collectors.toList());
	}
	
	public CategoryDto updateCategory(Category category, Integer categoryId) {
		Category dbCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		category.setId(dbCategory.getId());
		category.setImage(dbCategory.getImage());
		
		String slug = Slugify.toSlug(category.getName());
		category.setSlug(slug);
		
		Category savedCategory = categoryRepository.save(category);
		
		return categoryDtoMapper.apply(savedCategory);
	}
	
	public CategoryDto updateCategoryImage(Integer categoryId, MultipartFile image) throws IOException {
		Category dbCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		String fileName = fileService.uploadImage(image);
		
		fileService.deleteImage(dbCategory.getImage());
		
		dbCategory.setImage(fileName);
		
		Category savedCategory = categoryRepository.save(dbCategory);
		
		return categoryDtoMapper.apply(savedCategory);
	}
	
	public String deleteCategory(Integer categoryId) throws IOException {
		Category dbCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		categoryRepository.delete(dbCategory);
		
		fileService.deleteImage(dbCategory.getImage());
		
		return "Category with categoryId: " + categoryId + " deleted successfully.";
	}
	
}
