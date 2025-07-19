package com.csl.keezna.web.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

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
@SpringBootTest
@ActiveProfiles("dev")
public class CartRepositoryTests {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Test
	@Commit
	@Transactional
	public void testInsertByProduct() {
		log.info("test1--------------------------------");
		String email = "user1@aaa.com";
		Long pno = 5L;
		int qty = 1;

		CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

		if (cartItem != null) {
			cartItem.setQty(qty);
			cartItemRepository.save(cartItem);

			return;
		}

		Optional<Cart> result = cartRepository.getCartOfMember(email);
		Cart cart = null;

		if (result.isEmpty()) {
			log.info("MemberCart is not exist!!");
			Member member = Member.builder().email(email).build();
			Cart tempCart = Cart.builder().owner(member).build();
			cart = cartRepository.save(tempCart);
		} else {
			cart = result.get();
		}

		log.info("----- cart : {}", cart);

		if (cartItem == null) {
			Product product = Product.builder().pno(pno).build();
			cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
		}

		cartItemRepository.save(cartItem);
	}

	@Test
	@Commit
	public void testUpdateByCino() {
		Long cino = 1L;
		int qty = 4;

		Optional<CartItem> result = cartItemRepository.findById(cino);
		CartItem cartItem = result.orElseThrow();
		cartItem.setQty(qty);
		cartItemRepository.save(cartItem);
	}

	@Test
	public void testListOfMember() {
		String email = "user1@aaa.com";
		List<CartItemListDTO> cartItemList = cartItemRepository.getItemOfCartDTOByEmail(email);

		for (CartItemListDTO dto : cartItemList) {
			log.info("dto : {}", dto);
		}
	}
}
