package com.keezna.webfolio.service;

import com.keezna.webfolio.db.dto.PageRequestDTO;
import com.keezna.webfolio.db.dto.PageResponseDTO;
import com.keezna.webfolio.db.dto.ProductDTO;

public interface ProductService {
	public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

	public Long register(ProductDTO productDTO);

	public ProductDTO get(Long pno);

	public void modify(ProductDTO productDTO);

	public void remove(Long pno);

}
