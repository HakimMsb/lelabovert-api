package com.hakmesb.lelabovert.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.config.AppConstants;
import com.hakmesb.lelabovert.payload.SalesResponse;
import com.hakmesb.lelabovert.payload.StocksResponse;
import com.hakmesb.lelabovert.service.StockService;

@RequestMapping("/api/v1")
@RestController
public class StockController {
	
	private final StockService stockService;
	
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}
	
	@GetMapping("/admin/stocks")
	public ResponseEntity<StocksResponse> getStocks(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		StocksResponse stocksResponse = stockService.getStocks(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<StocksResponse>(stocksResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("/admin/sales")
	public ResponseEntity<SalesResponse> getSales(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
		SalesResponse salesResponse = stockService.getSales(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<SalesResponse>(salesResponse, HttpStatus.FOUND);
	}
	
	@PostMapping("/admin/product/{productId}/stock/{quantity}")
	public ResponseEntity<Void> addProductStock(@PathVariable Integer productId, @PathVariable Integer quantity) {
		stockService.addProductStock(productId, quantity);
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/admin/product/{productId}/stock/{quantity}")
	public ResponseEntity<Void> removeProductStock(@PathVariable Integer productId, @PathVariable Integer quantity) {
		stockService.removeProductStock(productId, quantity);
		
		return ResponseEntity.ok().build();
	}

}
