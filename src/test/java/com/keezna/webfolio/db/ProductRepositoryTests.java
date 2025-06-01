package com.keezna.webfolio.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.keezna.webfolio.db.model.Product;
import com.keezna.webfolio.db.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void dependencyProductTest() {
		log.info("productRepository : {}" + productRepository);
		assertNotNull(productRepository);
	}

	@Test
	public void testInsert() {
		for (int i = 0; i < 10; i++) {
			Product product = Product.builder().pname("상품" + i).price(100 * i).pdesc("상품설명 " + i)
					.build();
			product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE1.jpg");
			product.addImageString(UUID.randomUUID().toString() + "_" + "IMAGE2.jpg");

			productRepository.save(product);

			log.info("------------------------------");
		}
	}

	@Transactional
	@Test
	public void testRead() {

		Long pno = 1L;

		Optional<Product> result = productRepository.findById(pno);

		Product product = result.orElseThrow();
		
		log.info("product : {}", product);

		log.info("product.getImageList() : {}", product.getImageList());

	}

	@Test
	public void testRead2() {
		Long pno = 3L;
		
		Optional<Product> result = productRepository.selectOne(pno);
		
		Product product = result.orElseThrow();
		
		log.info("product : {}", product);
		log.info("product.getImageList() : {}", product.getImageList());
		
	}
}
