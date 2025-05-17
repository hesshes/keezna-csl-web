package com.keezna.webfolio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keezna.webfolio.db.dto.ProductDTO;
import com.keezna.webfolio.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private CustomFileUtil fileUtil;

	@PostMapping("/")
	public Map<String, String> register(ProductDTO producDTO) {

		log.info("register - productDto: {}", producDTO);

		List<MultipartFile> files = producDTO.getFiles();

		List<String> uploadFileNames = fileUtil.saveFiles(files);

		producDTO.setUploadFileNames(uploadFileNames);

		log.info("register - uploadFileNames: {}", uploadFileNames);

		return Map.of("RESULT", "SUCCESS");
	}

}
