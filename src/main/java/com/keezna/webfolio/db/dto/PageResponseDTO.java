package com.keezna.webfolio.db.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

@Data
public class PageResponseDTO<E> {
	private List<E> dtoList;

	private List<Integer> pageNumList;

	private PageRequestDTO pageRequestDTO;

	private boolean prev, next;

	private int totalCount, prevPage, nextPage, totalPage, current;

	@Builder(builderMethodName = "withAll")
	public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO,
			long totalCount) {
		this.dtoList = dtoList;
		this.pageRequestDTO = pageRequestDTO;
		this.totalCount = (int) totalCount;

		int end = (int) (Math
				.ceil((totalCount / (double) pageRequestDTO.getSize())));
		int start = end - 9;

		this.pageNumList = IntStream.rangeClosed(start, end).boxed()
				.collect(Collectors.toList());

		if (prev) {
			this.prevPage = start - 1;
		}

		if (next) {
			this.nextPage = end + 1;
		}
		this.totalPage = this.pageNumList.size();
		this.current = pageRequestDTO.getPage();
	}

}
