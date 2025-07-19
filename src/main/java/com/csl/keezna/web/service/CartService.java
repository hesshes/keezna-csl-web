package com.csl.keezna.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csl.keezna.web.db.dto.CartItemDTO;
import com.csl.keezna.web.db.dto.CartItemListDTO;
import com.csl.keezna.web.db.model.Cart;
import com.csl.keezna.web.db.model.CartItem;
import com.csl.keezna.web.db.model.Member;
import com.csl.keezna.web.db.model.Product;
import com.csl.keezna.web.db.repository.CartItemRepository;
import com.csl.keezna.web.db.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {
		String email = cartItemDTO.getEmail();

		Long pno = cartItemDTO.getPno();
		int qty = cartItemDTO.getQty();
		Long cino = cartItemDTO.getCino();
		if (cino != null) {
			Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);
			CartItem cartItem = cartItemResult.orElseThrow();
			cartItem.setQty(qty);
			cartItemRepository.save(cartItem);
			return getCartItems(email);
		}

		Cart cart = getCart(email);
		CartItem cartItem = null;

		cartItem = cartItemRepository.getItemOfPno(email, pno);

		if (cartItem == null) {
			Product product = Product.builder().pno(pno).build();
			cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
		} else {
			cartItem.setQty(qty);
		}

		cartItemRepository.save(cartItem);

		return getCartItems(email);

	}

	private Cart getCart(String email) {
		Cart cart = null;
		Optional<Cart> result = cartRepository.getCartOfMember(email);
		if (result.isEmpty()) {
			log.info("Cart of the member is not exist!!!!");
			Member member = Member.builder().email(email).build();
			Cart tempCart = Cart.builder().owner(member).build();
			cart = cartRepository.save(tempCart);
		} else {
			cart = result.get();
		}
		return cart;
	}

	public List<CartItemListDTO> getCartItems(String email) {
		return cartItemRepository.getItemOfCartDTOByEmail(email);
	}

	public List<CartItemListDTO> remove(Long cino) {
		Long cno = cartItemRepository.getCartFromItem(cino);
		log.info("Cart No : {}", cno);
		cartItemRepository.deleteById(cino);
		return cartItemRepository.getItemsOfCartDTOByCart(cno);
	}
}
