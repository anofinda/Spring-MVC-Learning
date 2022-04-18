package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author dongyudeng
 */
@Component
public class DatabaseInitializer {
    @Autowired
    DataSource dataSource;
    @PostConstruct
    public void init() throws SQLException {
        try(var connection=dataSource.getConnection()){
            try(var statement=connection.createStatement()){
                statement.executeUpdate("DROP TABLE IF EXISTS users");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                        + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, "
                        + "username VARCHAR(100) NOT NULL, "
                        + "password VARCHAR(100) NOT NULL, "
                        + "email VARCHAR(100) NOT NULL,"
                        + "gender VARCHAR(100) NOT NULL, "
                        + "UNIQUE (username))");
            }
        }
    }
}
