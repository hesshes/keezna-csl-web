package com.keezna.webfolio.service;

import org.springframework.stereotype.Service;

import com.keezna.webfolio.db.dto.TodoDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TodoService {

    public Long register(TodoDTO todoDTO) {

        log.info("................");
        return null;
    }

}
