package com.keezna.webfolio.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.keezna.webfolio.db.model.Product;
import com.keezna.webfolio.db.repository.ProductRepository;

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

	@Commit
	@Transactional
	@Test
	public void testDelete() {
		Long pno = 2L;

		productRepository.updateToDelete(pno, true);

	}

	@Test
	public void testUpdate() {
		Long pno = 10L;

		Product product = productRepository.selectOne(pno).get();

		product.changeName("10번 상품");
		product.changeDesc("10번 상품 설명입니다.");
		product.changePrice(5000);

		product.clearList();

		product.addImageString(UUID.randomUUID().toString() + "_" + "NJEWIMAGE1.jpg");
		product.addImageString(UUID.randomUUID().toString() + "_" + "NJEWIMAGE2.jpg");
		product.addImageString(UUID.randomUUID().toString() + "_" + "NJEWIMAGE3.jpg");

		productRepository.save(product);

	}

}
