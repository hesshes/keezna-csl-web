package com.csl.keezna.web.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csl.keezna.web.db.dto.PageRequestDTO;
import com.csl.keezna.web.db.dto.PageResponseDTO;
import com.csl.keezna.web.db.dto.ProductDTO;
import com.csl.keezna.web.db.model.Product;
import com.csl.keezna.web.db.model.ProductImage;
import com.csl.keezna.web.db.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
				Sort.by("pno").descending());

		Page<Object[]> result = productRepository.selectList(pageable);

		List<ProductDTO> dtoList = result.get().map(arr -> {
			Product product = (Product) arr[0];
			ProductImage productImage = (ProductImage) arr[1];

			ProductDTO productDTO = ProductDTO.builder().pno(product.getPno())
					.pname(product.getPname()).pdesc(product.getPdesc()).price(product.getPrice())
					.build();
			String imageStr = productImage.getFileName();
			productDTO.setUploadFileNames(List.of(imageStr));

			return productDTO;
		}).collect(Collectors.toList());

		long totalCount = result.getTotalElements();

		return PageResponseDTO.<ProductDTO>withAll().dtoList(dtoList).totalCount(totalCount)
				.pageRequestDTO(pageRequestDTO).build();
	}

	public Long register(ProductDTO productDTO) {
		Product product = dtoToEntity(productDTO);
		Product result = productRepository.save(product);

		return result.getPno();
	}

	private Product dtoToEntity(ProductDTO productDTO) {

		Product product = Product.builder().pno(productDTO.getPno()).pname(productDTO.getPname())
				.pdesc(productDTO.getPdesc()).price(productDTO.getPrice()).build();

		List<String> uploadFileNames = productDTO.getUploadFileNames();

		if (uploadFileNames == null) {
			return product;
		}

		uploadFileNames.stream().forEach(uploadName -> {
			product.addImageString(uploadName);
		});

		return product;
	}

	public ProductDTO get(Long pno) {
		Optional<Product> result = productRepository.selectOne(pno);

		Product product = result.orElseThrow();

		ProductDTO productDTO = entityToDTO(product);

		return productDTO;

	}

	private ProductDTO entityToDTO(Product product) {

		ProductDTO productDTO = ProductDTO.builder().pno(product.getPno()).pdesc(product.getPdesc())
				.pname(product.getPname()).price(product.getPrice()).build();

		List<ProductImage> imageList = product.getImageList();

		if (imageList == null || imageList.size() == 0) {
			return productDTO;
		}

		List<String> fileNameList = imageList.stream()
				.map(productImage -> productImage.getFileName()).toList();

		productDTO.setUploadFileNames(fileNameList);

		return productDTO;
	}

	public void modify(ProductDTO productDTO) {
		Optional<Product> result = productRepository.findById(productDTO.getPno());

		Product product = result.orElseThrow();

		product.changeName(productDTO.getPname());
		product.changeDesc(productDTO.getPdesc());
		product.changePrice(productDTO.getPrice());

		product.clearList();

		List<String> uploadFileNames = productDTO.getUploadFileNames();

		if (uploadFileNames != null && uploadFileNames.size() > 0) {
			uploadFileNames.stream().forEach(uplaodName -> {
				product.addImageString(uplaodName);
			});
		}

		productRepository.save(product);
	}

	public void remove(Long pno) {
		productRepository.updateToDelete(pno, true);

	}

}
