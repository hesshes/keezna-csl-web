package com.keezna.webfolio.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.keezna.webfolio.db.dto.TodoDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("dev")
@SpringBootTest
public class TodoServiceTest {

	@Autowired
	private TodoService todoService;

	@Test
	public void regTest() {
		TodoDTO todoDTO = TodoDTO.builder().title("ServiceTest")
				.writer("tester").dueDate(LocalDate.of(2023, 10, 10)).build();
		Long tno = todoService.register(todoDTO);
		assertNotNull(tno);
	}

	@Test
	public void getTest() {
		Long tno = 10L;
		TodoDTO todoDto = todoService.getTodo(tno);
		log.info("todoDTO : {}", todoDto);
	}

}
