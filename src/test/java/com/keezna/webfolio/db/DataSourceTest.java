package com.keezna.webfolio.db;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testDataSourceConnection() {
        assertNotNull(dataSource, "DataSource should not be null");

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            System.out.println("Successfully connected to the database!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

}
