package com.hakmesb.lelabovert.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Category;
import com.hakmesb.lelabovert.model.Product;
import com.hakmesb.lelabovert.payload.ProductDto;
import com.hakmesb.lelabovert.payload.ProductResponse;
import com.hakmesb.lelabovert.payload.mapper.ProductDtoMapper;
import com.hakmesb.lelabovert.repository.CategoryRepository;
import com.hakmesb.lelabovert.repository.ProductRepository;
import com.hakmesb.lelabovert.util.Slugify;

@Transactional
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductDtoMapper productDtoMapper;
	private final FileService fileService;
	private final CartService cartService;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository,
			ProductDtoMapper productDtoMapper, FileService fileService, CartService cartService) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productDtoMapper = productDtoMapper;
		this.fileService = fileService;
		this.cartService = cartService;
	}

	public ProductDto addProduct(Integer categoryId, Product product){
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		boolean isProductNotPresent = true;

		List<Product> products = category.getProducts();

		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getName().equals(product.getName())
					&& products.get(i).getDescription().equals(product.getDescription())) {
				isProductNotPresent = false;
				break;
			}
		}
		
		if(isProductNotPresent) {
			product.setCategory(category);
			
			String slug = Slugify.toSlug(product.getName());
			product.setSlug(slug);
			
			Product dbProduct = productRepository.save(product);
			
			return productDtoMapper.apply(dbProduct);
		}else {
			throw new ApiException("Product already exists.");
		}
	}
	
	public ProductResponse getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Pageable pageDetails = pageDetails(pageNumber, pageSize, sortBy, sortOrder);
		
		Page<Product> productsPage = productRepository.findAll(pageDetails);
		
		List<Product> products = productsPage.getContent();
		
//		if (products.size() == 0) {
//			throw new ApiException("There are no products yet.");
//		}
		
		List<ProductDto> productDtos = products.stream().map(productDtoMapper).collect(Collectors.toList());
		
		return new ProductResponse(productDtos, productsPage.getNumber(), productsPage.getSize(), productsPage.getTotalElements(),
				productsPage.getTotalPages(), productsPage.isLast());
	}
	
	public ProductDto getProduct(Integer productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		return productDtoMapper.apply(product);
	}
	
	public ProductDto getProductBySlug(String slug) {
		Product product = productRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
		
		return productDtoMapper.apply(product);
	}
	
	public ProductResponse getProductsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		Pageable pageDetails = pageDetails(pageNumber, pageSize, sortBy, sortOrder);
		
		Page<Product> productsPage = productRepository.findByCategoryId(category.getId(), pageDetails);
		
		List<Product> products = productsPage.getContent();
		
//		if (products.size() == 0) {
//			throw new ApiException(category.getName() + " category doesn't contain any products.");
//		}
		
		List<ProductDto> productDtos = products.stream().map(productDtoMapper).collect(Collectors.toList());
		
		return new ProductResponse(productDtos, productsPage.getNumber(), productsPage.getSize(), productsPage.getTotalElements(),
				productsPage.getTotalPages(), productsPage.isLast());
	}
	
	public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Pageable pageDetails = pageDetails(pageNumber, pageSize, sortBy, sortOrder);
		
		Page<Product> productsPage = productRepository.findByNameLike("%"+ keyword + "%", pageDetails);
		
		List<Product> products = productsPage.getContent();
		
		if (products.size() == 0) {
			throw new ApiException("Products not found with keyword: " + keyword);
		}
		
		List<ProductDto> productDtos = products.stream().map(productDtoMapper).collect(Collectors.toList());
		
		return new ProductResponse(productDtos, productsPage.getNumber(), productsPage.getSize(), productsPage.getTotalElements(),
				productsPage.getTotalPages(), productsPage.isLast());
	}
	
	public ProductDto updateProduct(Product product, Integer productId) {
		Product dbProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		Float oldPrice = dbProduct.getPrice();
		
		product.setId(dbProduct.getId());
		product.setCategory(dbProduct.getCategory());
		product.setImage(dbProduct.getImage());
		
		String slug = Slugify.toSlug(product.getName());
		product.setSlug(slug);
		
		Product savedProduct = productRepository.save(product);
		
		cartService.updateProductInCarts(productId, oldPrice, product.getPrice());
		
		return productDtoMapper.apply(savedProduct);
	}
	
	public ProductDto updateProductImage(Integer productId, MultipartFile image) throws IOException{
		Product dbProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		String fileName = fileService.uploadImage(image);
		
		if(dbProduct.getImage() != null) {
			fileService.deleteImage(dbProduct.getImage());
		}
		
		dbProduct.setImage(fileName);
		
		Product savedProduct = productRepository.save(dbProduct);
		
		return productDtoMapper.apply(savedProduct);
	}
	
	public String deleteProduct(Integer productId) throws IOException {
		Product dbProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		productRepository.delete(dbProduct);
		
		fileService.deleteImage(dbProduct.getImage());
		
		return "Product with productId: " + productId + " deleted successfully.";
	}
	
	private Pageable pageDetails(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		return PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	}

}
