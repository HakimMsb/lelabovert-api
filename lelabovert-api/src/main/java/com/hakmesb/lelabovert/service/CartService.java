package com.hakmesb.lelabovert.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Cart;
import com.hakmesb.lelabovert.model.CartDetail;
import com.hakmesb.lelabovert.model.Product;
import com.hakmesb.lelabovert.payload.CartDto;
import com.hakmesb.lelabovert.payload.mapper.CartDtoMapper;
import com.hakmesb.lelabovert.repository.CartDetailRepository;
import com.hakmesb.lelabovert.repository.CartRepository;
import com.hakmesb.lelabovert.repository.ProductRepository;

@Transactional
@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartDetailRepository cartDetailRepository;
	private final CartDtoMapper cartDtoMapper;
	private final ProductRepository productRepository;

	public CartService(CartRepository cartRepository, CartDetailRepository cartDetailRepository,
			CartDtoMapper cartDtoMapper, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.cartDetailRepository = cartDetailRepository;
		this.cartDtoMapper = cartDtoMapper;
		this.productRepository = productRepository;
	}

	public CartDto getCart(Integer accountId) {
		Cart cart = cartRepository.findByAccountId(accountId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "accountId", accountId);
		}

		CartDto cartDto = cartDtoMapper.apply(cart);

		return cartDto;
	}

	public CartDto addProductToCart(Integer cartId, Integer productId, Integer quantity) {
		if(!(quantity >= 0)) {
			throw new ApiException("Product quantity must be more than zero.");
		}
		
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		CartDetail cartDetail = cartDetailRepository.findByProductIdAndCartId(productId, cartId);

		if (cartDetail != null) {
			throw new ApiException("Product " + product.getName() + " already exists in the cart.");
		}

		if (product.getQuantity() == 0) {
			throw new ApiException(product.getName() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new ApiException("Please, make an order of the " + product.getName()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		cartDetail = new CartDetail();
		cartDetail.setCart(cart);
		cartDetail.setProduct(product);
		cartDetail.setQuantity(quantity);
		cartDetail.setAmount(product.getPrice() * quantity);

		cartDetailRepository.save(cartDetail);

		List<CartDetail> cartDetails = cartDetailRepository.findByCartId(cartId);

		Float totalAmount = 0.0f;
		for (CartDetail c : cartDetails) {
			totalAmount += c.getAmount();
		}
		cart.setTotalAmount(totalAmount);
		cart = cartRepository.save(cart);

		CartDto cartDto = cartDtoMapper.apply(cart);

		return cartDto;
	}

	public void updateProductInCarts(Integer productId, Float oldPrice, Float newPrice) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		List<Cart> carts = cartRepository.findByProductId(product.getId());
		
		for(Cart cart : carts) {
			CartDetail cartDetail = cartDetailRepository.findByProductIdAndCartId(productId, cart.getId());
			cartDetail.setAmount(cartDetail.getQuantity() * newPrice);
			cartDetailRepository.save(cartDetail);
			Float totalAmount = cart.getTotalAmount() + cartDetail.getQuantity() * (newPrice - oldPrice);
			cart.setTotalAmount(totalAmount);
			cartRepository.save(cart);
		}
	}
	
	public CartDto updateProductQuantityInCart(Integer cartId, Integer productId, Integer quantity) {
		if(!(quantity >= 0)) {
			throw new ApiException("Product quantity must be more than zero.");
		}
		
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
		
		if (product.getQuantity() == 0) {
			throw new ApiException(product.getName() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new ApiException("Please, make an order of the " + product.getName()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}
			
		CartDetail cartDetail = cartDetailRepository.findByProductIdAndCartId(productId, cartId);
		
		if (cartDetail == null) {
			throw new ApiException("Product " + product.getName() + " not available in the cart.");
		}
		
		Float totalAmount = cart.getTotalAmount() - product.getPrice() * cartDetail.getQuantity();
		
		cartDetail.setQuantity(quantity);
		cartDetail.setAmount(quantity * product.getPrice());
		cartDetail = cartDetailRepository.save(cartDetail);
		
		cart.setTotalAmount(totalAmount + product.getPrice() * cartDetail.getQuantity());
		cart = cartRepository.save(cart);
		
		return cartDtoMapper.apply(cart);
	}
	
	public CartDto deleteProductFromCart(Integer cartId, Integer productId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		CartDetail cartDetail = cartDetailRepository.findByProductIdAndCartId(productId, cartId);
		
		if (cartDetail == null) {
			throw new ResourceNotFoundException("CartDetail", "productId", productId);
		}
		
		cart.setTotalAmount(cart.getTotalAmount() - cartDetail.getQuantity() * product.getPrice());
		cart = cartRepository.save(cart);
		cartDetailRepository.deleteByProductIdAndCartId(productId, cartId);
		
		return cartDtoMapper.apply(cart);
	}

}
