package com.keezna.webfolio.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.keezna.webfolio.db.dto.TodoDTO;
import com.keezna.webfolio.db.model.Todo;
import com.keezna.webfolio.db.repository.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class TodoRepositoryTests {

	@Autowired
	private TodoRepository todoRepository;

	@Test
	public void repoObjectNullCheck() {
		assertNotNull(todoRepository);
		log.info("todoRepository {}", todoRepository);
	}

	@Test
	public void testInsert() {
		for (int i = 1; i <= 100; i++) {
			Todo todo = Todo.builder().title("Title...." + i)
					.dueDate(LocalDate.of(2023, 12, 31)).writer("user00")
					.build();
			todoRepository.save(todo);
		}
	}

	@Test
	public void testModify() {
		Long bno = 33L;
		Optional<Todo> result = todoRepository.findById(bno);
		Todo todo = result.orElseThrow();
		todo.setTitle("Modified 33...");
		todo.setCompelete(true);
		todo.setDueDate(LocalDate.of(2023, 10, 10));
		todoRepository.save(todo);
	}

	@Test
	public void testDelete() {
		Long tno = 1L;
		todoRepository.deleteById(tno);
	}

	@Test
	public void testPaging() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
		Page<Todo> result = todoRepository.findAll(pageable);
		log.info("total {}", result.getTotalElements());
		result.getContent().stream().forEach(todo -> log.info("todo {}", todo));
	}

	@Test
	public void testGet() {
		Todo todo = todoRepository.findById(3L).orElseThrow(
				() -> new IllegalArgumentException("Value not found"));
		assertNotNull(todo);
		log.info("todo : {}", todo);
	}

}
