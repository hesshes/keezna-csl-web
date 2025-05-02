package com.keezna.webfolio.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.keezna.webfolio.db.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {

}
