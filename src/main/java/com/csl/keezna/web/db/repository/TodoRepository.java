package com.csl.keezna.web.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csl.keezna.web.db.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
