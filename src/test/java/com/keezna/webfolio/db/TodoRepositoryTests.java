package com.keezna.webfolio.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
            // log.info("saveTodo: {}", todoRepository.save(todo));
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

}
