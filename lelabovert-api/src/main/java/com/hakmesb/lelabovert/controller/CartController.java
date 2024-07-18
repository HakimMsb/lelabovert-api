package com.hakmesb.lelabovert.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.payload.CartDto;
import com.hakmesb.lelabovert.service.CartService;

@RequestMapping("/api/v1")
@RestController
public class CartController {
	
	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@GetMapping("/user/cart")
	public ResponseEntity<CartDto> getCart(Authentication authentication){
		Account account = (Account) authentication.getPrincipal();
		
		CartDto cartDto = cartService.getCart(account.getId());
		
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

	@PostMapping("/user/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDto> addProductToCart(@PathVariable Integer cartId,
			@PathVariable Integer productId, @PathVariable Integer quantity){
		CartDto cartDto =  cartService.addProductToCart(cartId, productId, quantity);
		
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.CREATED);
	}
	
	@PutMapping("/user/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDto> updateProductQuantity(@PathVariable Integer cartId,
			@PathVariable Integer productId, @PathVariable Integer quantity){
		CartDto cartDto = cartService.updateProductQuantityInCart(cartId, productId, quantity);
		
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/user/carts/{cartId}/product/{productId}")
	public ResponseEntity<CartDto> deleteProductFromCart(@PathVariable Integer cartId,
			@PathVariable Integer productId){
		CartDto cartDto = cartService.deleteProductFromCart(cartId, productId);
		
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
	
}
