package com.keezna.webfolio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.keezna.webfolio.db.dto.PageRequestDTO;
import com.keezna.webfolio.db.dto.PageResponseDTO;
import com.keezna.webfolio.db.dto.TodoDTO;
import com.keezna.webfolio.db.model.Todo;
import com.keezna.webfolio.db.repository.TodoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TodoService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TodoRepository todoRepo;

	public Long register(TodoDTO todoDTO) {

		Todo todo = modelMapper.map(todoDTO, Todo.class);

		Todo savedTodo = todoRepo.save(todo);

		return savedTodo.getTno();
	}

	public TodoDTO getTodo(Long tno) {
		Todo todo = todoRepo.findById(tno)
				.orElseThrow(() -> new IllegalArgumentException("ERORR"));
		TodoDTO todoDTO = modelMapper.map(todo, TodoDTO.class);
		return todoDTO;
	}

	public void modify(TodoDTO todoDTO) {
		Todo todo = todoRepo.findById(todoDTO.getTno()).orElseThrow();
		todo.setTitle(todo.getTitle());
		todo.setDueDate(todoDTO.getDueDate());
		todo.setCompelete(todoDTO.isComplete());
		todoRepo.save(todo);
	}

	public void remove(Long tno) {
		todoRepo.deleteById(tno);
	}

	public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {
		Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
				pageRequestDTO.getSize(), Sort.by("tno").descending());
		Page<Todo> result = todoRepo.findAll(pageable);
		List<TodoDTO> dtoList = result.getContent().stream()
				.map(todo -> modelMapper.map(todo, TodoDTO.class))
				.collect(Collectors.toList());

		long totalCount = result.getTotalElements();
		PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO
				.<TodoDTO>withAll().dtoList(dtoList)
				.pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

		return responseDTO;

	}
}
