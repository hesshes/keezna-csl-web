package com.keezna.stdfolio.db;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseConntectionTest {

	@Autowired
	private DataSource dataSource;

	@Test
	void testDatabaseConnection() throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			assertThat(connection).isNotNull(); // 연결 확인
			System.out.println("Database connection successful: " + connection.getMetaData().getURL());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
