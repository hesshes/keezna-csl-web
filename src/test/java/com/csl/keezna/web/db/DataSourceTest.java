package com.csl.keezna.web.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class DataSourceTest {

	@Autowired
	private DataSource dataSource;

	@Test
	void testDataSourceConnection() {
		assertNotNull(dataSource);
		log.info("DataSource: {}", dataSource);
		try (Connection connection = dataSource.getConnection()) {
			assertNotNull(connection);
			log.info("connection : {}", connection);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to connect to the database", e);
		}
	}

}
