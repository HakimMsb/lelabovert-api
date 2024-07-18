package com.hakmesb.lelabovert.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.config.AppConstants;
import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.payload.OrderDto;
import com.hakmesb.lelabovert.payload.OrderResponse;
import com.hakmesb.lelabovert.payload.OrderStatus;
import com.hakmesb.lelabovert.service.OrderService;

@RequestMapping("/api/v1")
@RestController
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/admin/orders")
	public ResponseEntity<OrderResponse> getAllOrders(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
		OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
	}

	@PostMapping("/user/cart/{cartId}/order")
	public ResponseEntity<OrderDto> placeOrderWithAccount(@PathVariable Integer cartId,
			Authentication authentication) {
		Account account = (Account) authentication.getPrincipal();
		
		OrderDto orderDto = orderService.placeOrderWithAccount(account, cartId);
		
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/user/orders")
	public ResponseEntity<List<OrderDto>> getOrdersByAccount(Authentication authentication){
		Account account = (Account) authentication.getPrincipal();
		
		List<OrderDto> orderDtos = orderService.getOrdersByAccount(account);
		
		return new ResponseEntity<List<OrderDto>>(orderDtos, HttpStatus.FOUND);
	}
	
	@GetMapping("/user/order/{orderId}")
	public ResponseEntity<OrderDto> getOrderWithUser(@PathVariable Integer orderId,
			Authentication authentication){
		Account account = (Account) authentication.getPrincipal();
		
		OrderDto orderDto = orderService.getOrder(account, orderId);
		
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/admin/order/{orderId}")
	public ResponseEntity<OrderDto> getOrderWithAdmin(@PathVariable Integer orderId){
		OrderDto orderDto = orderService.getOrder(orderId);
		
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.FOUND);
	}
	
	@PutMapping("/admin/order/{orderId}/status")
	public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Integer orderId,
			@RequestBody OrderStatus orderStatus){
		OrderDto orderDto = orderService.updateOrderStatus(orderId,
				orderStatus.isConfirmed(), orderStatus.isDispatched());
		
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/order/{orderId}")
	public ResponseEntity<OrderDto> cancelOrder(@PathVariable Integer orderId){
		OrderDto orderDto = orderService.cancelOrder(orderId);
		
		return new ResponseEntity<OrderDto>(orderDto, HttpStatus.OK);
	}

}
