package dev.trickster12.runnerz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
public class MainDatabaseConfig {

    @Bean(name = "mainDataSource")
    @Primary // Marks this data source as the default one
    public DataSource mainDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/runnerz") // Update with your main database
                .username("trickster12")
                .password("admin")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}