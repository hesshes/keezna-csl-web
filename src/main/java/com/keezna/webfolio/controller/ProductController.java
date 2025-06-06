package com.keezna.webfolio.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keezna.webfolio.db.dto.PageRequestDTO;
import com.keezna.webfolio.db.dto.PageResponseDTO;
import com.keezna.webfolio.db.dto.ProductDTO;
import com.keezna.webfolio.service.ProductService;
import com.keezna.webfolio.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomFileUtil fileUtil;

	@PostMapping("/")
	public Map<String, Long> register(ProductDTO producDTO) {

		List<MultipartFile> files = producDTO.getFiles();

		List<String> uploadFileNames = fileUtil.saveFiles(files);

		producDTO.setUploadFileNames(uploadFileNames);

		log.info("register - uploadFileNames: {}", uploadFileNames);

		Long pno = productService.register(producDTO);
		return Map.of("RESULT", pno);
	}

	@GetMapping("/list")
	public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
		return productService.getList(pageRequestDTO);
	}

	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGet(
			@PathVariable("fileName") String fileName) {
		log.info(fileName);
		ResponseEntity<Resource> res = fileUtil.getFile(fileName);
		return res;
	}

	@GetMapping("/{pno}")
	public ProductDTO read(@PathVariable(name = "pno") Long pno) {
		return productService.get(pno);
	}

	@PutMapping("/{pno}")
	public Map<String, String> modify(@PathVariable(name = "pno") Long pno,
			ProductDTO productDTO) {
		productDTO.setPno(pno);
		ProductDTO oldProductDTO = productService.get(pno);

		List<String> oldFileNames = oldProductDTO.getUploadFileNames();

		List<MultipartFile> files = productDTO.getFiles();

		List<String> currentUploadFileNames = fileUtil.saveFiles(files);

		List<String> uploadFileNames = productDTO.getUploadFileNames();

		if (currentUploadFileNames != null
				&& currentUploadFileNames.size() > 0) {
			uploadFileNames.addAll(currentUploadFileNames);
		}

		productService.modify(oldProductDTO);

		if (oldFileNames != null && oldFileNames.size() > 0) {
			List<String> removeFiles = oldFileNames.stream()
					.filter(fileName -> uploadFileNames.indexOf(fileName) == -1)
					.collect(Collectors.toList());
			fileUtil.deleteFiles(removeFiles);
		}
		return Map.of("RESULT", "SUCCESS");
	}

	@DeleteMapping("/{pno}")
	public Map<String, String> remove(@PathVariable(name = "pno") Long pno) {
		List<String> oldFileNames = productService.get(pno)
				.getUploadFileNames();
		productService.remove(pno);

		fileUtil.deleteFiles(oldFileNames);

		return Map.of("RESULT", "SUCCESS");
	}
}
