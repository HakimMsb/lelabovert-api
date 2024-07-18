package com.hakmesb.lelabovert.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.hakmesb.lelabovert.config.AppConstants;
import com.hakmesb.lelabovert.model.Product;
import com.hakmesb.lelabovert.payload.ProductDto;
import com.hakmesb.lelabovert.payload.ProductResponse;
import com.hakmesb.lelabovert.service.FileService;
import com.hakmesb.lelabovert.service.ProductService;

@CrossOrigin
@RequestMapping("/api/v1")
@RestController
public class ProductController {

	private final ProductService productService;
	private final FileService fileService;
	
	public ProductController(ProductService productService, FileService fileService) {
		this.productService = productService;
		this.fileService = fileService;
	}
	
	@PostMapping("/admin/category/{categoryId}/product")
	public ResponseEntity<ProductDto> addProduct(@RequestBody Product product, @PathVariable Integer categoryId){
		ProductDto dbProductDto = productService.addProduct(categoryId, product);
		
		return new ResponseEntity<ProductDto>(dbProductDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getProducts(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		ProductResponse productResponse = productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);	
		
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
	}
	
	@GetMapping("/public/product/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable Integer productId){
		ProductDto productDto = productService.getProduct(productId);
		
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
	}
	
	@GetMapping("/public/product/slug/{slug}")
	public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug){
		ProductDto productDto = productService.getProductBySlug(slug);
		
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
	}
	
	@GetMapping("/public/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Integer categoryId,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		ProductResponse productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
	}
	
	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		ProductResponse productResponse = productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
	}
	
	@PutMapping("/admin/products/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@RequestBody Product product, @PathVariable Integer productId){
		ProductDto productDto = productService.updateProduct(product, productId);
		
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
	}
	
	@PutMapping("/admin/products/{productId}/image")
	public ResponseEntity<ProductDto> updateProductImage(@PathVariable Integer productId,
			@RequestParam MultipartFile image) throws IOException{
		ProductDto productDto = productService.updateProductImage(productId, image);
		
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/products/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) throws IOException {
		String status = productService.deleteProduct(productId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

    @GetMapping("public/product/image/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(fileService.getImagesDirectory()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
	
}
