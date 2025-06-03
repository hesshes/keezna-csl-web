package com.keezna.webfolio.service;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.keezna.webfolio.db.dto.PageRequestDTO;
import com.keezna.webfolio.db.dto.PageResponseDTO;
import com.keezna.webfolio.db.dto.ProductDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Test
	public void testList() {

		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

		PageResponseDTO<ProductDTO> result = productService
				.getList(pageRequestDTO);

		result.getDtoList().forEach(dto -> log.info("dto : {}", dto));

	}

	@Test
	public void testRegister() {
		ProductDTO productDTO = ProductDTO.builder().pname("새로운 상품")
				.pdesc("신규 추가 등록 테스트").price(3000).build();

		productDTO.setUploadFileNames(
				List.of(UUID.randomUUID() + "_" + "Test1.jpg",
						UUID.randomUUID() + "_" + "Test2.jpg"));
		
		productService.register(productDTO);
	}
	
	@Test
	public void testRead() {
		Long pno = 12L;
		
		ProductDTO productDTO = productService.get(pno);
		
		log.info("productDTO : {}", productDTO);
		log.info("productDTO.getUploadFileNames() : {}", productDTO.getUploadFileNames());
	}
}
