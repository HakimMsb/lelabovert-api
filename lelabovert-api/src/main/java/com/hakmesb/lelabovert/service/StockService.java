package com.hakmesb.lelabovert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Order;
import com.hakmesb.lelabovert.model.Product;
import com.hakmesb.lelabovert.model.Sale;
import com.hakmesb.lelabovert.model.Stock;
import com.hakmesb.lelabovert.payload.SaleDto;
import com.hakmesb.lelabovert.payload.SalesResponse;
import com.hakmesb.lelabovert.payload.StockDto;
import com.hakmesb.lelabovert.payload.StocksResponse;
import com.hakmesb.lelabovert.payload.mapper.SaleDtoMapper;
import com.hakmesb.lelabovert.payload.mapper.StockDtoMapper;
import com.hakmesb.lelabovert.repository.ProductRepository;
import com.hakmesb.lelabovert.repository.SaleRepository;
import com.hakmesb.lelabovert.repository.StockRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StockService {

	private final StockRepository stockRepository;
	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;
	private final StockDtoMapper stockDtoMapper;
	private final SaleDtoMapper saleDtoMapper;
	
	public StockService (StockRepository stockRepository, SaleRepository saleRepository,
			ProductRepository productRepository, StockDtoMapper stockDtoMapper, SaleDtoMapper saleDtoMapper) {
		this.stockRepository = stockRepository;
		this.saleRepository = saleRepository;
		this.productRepository = productRepository;
		this.stockDtoMapper = stockDtoMapper;
		this.saleDtoMapper = saleDtoMapper;
	}
	
	public StocksResponse getStocks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Stock> stocksPage = stockRepository.findAll(pageDetails);
		
		List<Stock> stocks = stocksPage.getContent();
		
		if(stocks.size() == 0) {
			throw new ApiException("There are no stocks yet.");
		}
		
		List<StockDto> stockDtos = stocks.stream().map(stockDtoMapper).collect(Collectors.toList());
		
		return new StocksResponse(stockDtos, stocksPage.getNumber(), stocksPage.getSize(), stocksPage.getTotalElements(),
				stocksPage.getTotalPages(), stocksPage.isLast());
	}
	
	public SalesResponse getSales(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Sale> salesPage = saleRepository.findAll(pageDetails);
		
		List<Sale> sales = salesPage.getContent();
		
		if(sales.size() == 0) {
			throw new ApiException("There are no sales yet.");
		}
		
		List<SaleDto> saleDtos = sales.stream().map(saleDtoMapper).collect(Collectors.toList());
		
		return new SalesResponse(saleDtos, salesPage.getNumber(), salesPage.getSize(), salesPage.getTotalElements(),
				salesPage.getTotalPages(), salesPage.isLast());
	}
	
	public void addProductStock(Integer productId, Integer quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		Stock stock = new Stock();
		stock.setProduct(product);
		stock.setQuantity(quantity);
		stockRepository.save(stock);
	}
	
	public void removeProductStock(Integer productId, Integer quantity) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		Stock stock = new Stock();
		stock.setProduct(product);
		stock.setQuantity(-quantity);
		stockRepository.save(stock);
	}
	
	public void addSale(Product product, Order order, Integer quantity, Float amount) {
		Sale sale = new Sale();
		sale.setProduct(product);
		sale.setOrder(order);
		sale.setQuantity(quantity);
		sale.setAmount(amount);
		saleRepository.save(sale);
	}
	
	public void cancelSales(Integer orderId) {
		saleRepository.deleteByOrderId(orderId);
	}
	
}
