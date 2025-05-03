package com.keezna.webfolio.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
