package com.hakmesb.lelabovert.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hakmesb.lelabovert.model.Category;
import com.hakmesb.lelabovert.payload.CategoryDto;
import com.hakmesb.lelabovert.service.CategoryService;

@RequestMapping("/api/v1")
@RestController
public class CategoryController {

	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping("/admin/category")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody Category category){
		CategoryDto dbCategoryDto = categoryService.createCategory(category);
		
		return new ResponseEntity<CategoryDto>(dbCategoryDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/public/category/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId){
		CategoryDto categoryDto = categoryService.getCategory(categoryId);
		
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	@GetMapping("/public/category/slug/{slug}")
	public ResponseEntity<CategoryDto> getCategoryBySlug(@PathVariable String slug){
		CategoryDto categoryDto = categoryService.getCategoryBySlug(slug);
		
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	@GetMapping("/public/categories")
	public ResponseEntity<List<CategoryDto>> getCategories(){
		List<CategoryDto> categories = categoryService.getCategories();
		
		return new ResponseEntity<List<CategoryDto>>(categories, HttpStatus.OK);
	}
	
	@PutMapping("/admin/category/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody Category category, @PathVariable Integer categoryId){
		CategoryDto categoryDto = categoryService.updateCategory(category, categoryId);
		
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	@PutMapping("/admin/categories/{categoryId}/image")
	public ResponseEntity<CategoryDto> updateCategoryImage(@PathVariable Integer categoryId,
			@RequestParam MultipartFile image) throws IOException{
		CategoryDto categoryDto = categoryService.updateCategoryImage(categoryId, image);
		
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/category/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId) throws IOException{
		String status = categoryService.deleteCategory(categoryId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
}
