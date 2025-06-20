package com.csl.keezna.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csl.keezna.web.db.dto.PageRequestDTO;
import com.csl.keezna.web.db.dto.PageResponseDTO;
import com.csl.keezna.web.db.dto.TodoDTO;
import com.csl.keezna.web.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/todo")
public class TodoController {

	@Autowired
	private TodoService todoService;

	@GetMapping("/{tno}")
	public TodoDTO get(@PathVariable(name = "tno") Long tno) {
		return todoService.getTodo(tno);
	}

	@GetMapping("/list")
	public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {
		log.info("pageRequestDTO : {}", pageRequestDTO);
		return todoService.getList(pageRequestDTO);
	}

	@PostMapping("/")
	public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
		log.info("todoDTO: {}", todoDTO);
		Long tno = todoService.register(todoDTO);
		return Map.of("TNO", tno);
	}

	@PutMapping("/{tno}")
	public Map<String, String> modify(@PathVariable(name = "tno") Long tno,
			@RequestBody TodoDTO todoDTO) {
		todoDTO.setTno(tno);
		log.info("Modify : {}", todoDTO);
		todoService.modify(todoDTO);
		return Map.of("RESULT", "SUCCESS");
	}

	@DeleteMapping("/{tno}")
	public Map<String, String> remove(@PathVariable(name = "tno") Long tno) {
		log.info("Remove : {}", tno);
		todoService.remove(tno);
		return Map.of("RESULT", "SUCCESS");
	}

}
