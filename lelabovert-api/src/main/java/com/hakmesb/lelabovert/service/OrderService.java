package com.hakmesb.lelabovert.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.model.Cart;
import com.hakmesb.lelabovert.model.CartDetail;
import com.hakmesb.lelabovert.model.Order;
import com.hakmesb.lelabovert.model.OrderItem;
import com.hakmesb.lelabovert.payload.OrderDto;
import com.hakmesb.lelabovert.payload.OrderResponse;
import com.hakmesb.lelabovert.payload.mapper.OrderDtoMapper;
import com.hakmesb.lelabovert.repository.CartDetailRepository;
import com.hakmesb.lelabovert.repository.CartRepository;
import com.hakmesb.lelabovert.repository.OrderRepository;

@Transactional
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderDtoMapper orderDtoMapper;
	private final CartRepository cartRepository;
	private final StockService stockService;
	private final CartDetailRepository cartDetailRepository;

	public OrderService(OrderRepository orderRepository, OrderDtoMapper orderDtoMapper,
			CartRepository cartRepository, StockService stockService,
			CartDetailRepository cartDetailRepository) {
		this.orderRepository = orderRepository;
		this.orderDtoMapper = orderDtoMapper;
		this.cartRepository = cartRepository;
		this.stockService = stockService;
		this.cartDetailRepository = cartDetailRepository;
	}

	public OrderDto placeOrderWithAccount(Account account, Integer cartId) {
		Cart cart = cartRepository.findByAccountIdAndId(account.getId(), cartId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		List<CartDetail> cartDetails = cartDetailRepository.findByCartId(cartId);

		if (cartDetails.size() == 0) {
			throw new ApiException("Cart is empty");
		}

		Order order = new Order();
		order.setTotalAmount(cart.getTotalAmount());
		order.setCustomer(account.getCustomer());
		order.setConfirmed(false);
		order.setDispatched(false);

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartDetail cartDetail : cartDetails) {
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(cartDetail.getQuantity());
			orderItem.setAmount(cartDetail.getAmount());
			orderItem.setProduct(cartDetail.getProduct());
			orderItem.setOrder(order);

			orderItems.add(orderItem);
		}

		order.setOrderItems(orderItems);
		Order savedOrder = orderRepository.save(order);

		for (CartDetail cartDetail : cartDetails) {
			stockService.addSale(cartDetail.getProduct(), savedOrder, cartDetail.getQuantity(), cartDetail.getAmount());
		}

		cart.setTotalAmount(0);
		cartRepository.save(cart);
		cartDetailRepository.deleteAll(cartDetails);

		return orderDtoMapper.apply(savedOrder);
	}

	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Order> ordersPage = orderRepository.findAll(pageDetails);

		List<Order> orders = ordersPage.getContent();

		if (orders.size() == 0) {
			throw new ApiException("There are no orders yet.");
		}

		List<OrderDto> orderDtos = orders.stream().map(orderDtoMapper).collect(Collectors.toList());

		return new OrderResponse(orderDtos, ordersPage.getNumber(), ordersPage.getSize(), ordersPage.getTotalElements(),
				ordersPage.getTotalPages(), ordersPage.isLast());
	}

	public List<OrderDto> getOrdersByAccount(Account account) {
		List<Order> orders = orderRepository.findByAccountId(account.getId());

		if (orders.size() == 0) {
			throw new ApiException("No orders placed yet by the account with email: " + account.getEmail());
		}

		List<OrderDto> orderDtos = orders.stream().map(orderDtoMapper).collect(Collectors.toList());

		return orderDtos;
	}
	
	public OrderDto getOrder(Account account, Integer orderId) {
		Order order = orderRepository.findByIdAndAccountId(orderId, account.getId());
		
		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return orderDtoMapper.apply(order);
	}
	
	public OrderDto getOrder(Integer orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

		return orderDtoMapper.apply(order);
	}
	
	public OrderDto updateOrderStatus(Integer orderId, Boolean isConfirmed, Boolean isDispatched) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));
		
		order.setConfirmed(isConfirmed);
		order.setDispatched(isDispatched);
		Order savedOrder = orderRepository.save(order);
		
		return orderDtoMapper.apply(savedOrder);
	}
	
	public OrderDto cancelOrder(Integer orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));
		
		if(order.getDeletedAt() != null) {
			throw new ApiException("Order already cancelled.");
		}
		
		order.setDeletedAt(new Date());
		Order savedOrder = orderRepository.save(order);
		
		stockService.cancelSales(orderId);
		
		return orderDtoMapper.apply(savedOrder);
	}

}
