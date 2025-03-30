package dev.trickster12.runnerz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Configuration
@Profile("test") // Makes this configuration active only for the "test" profile
public class TestDatabaseConfig {

    @Bean(name = "testDataSource")
    public DataSource testDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5433/runnerz_test")
                .username("trickster12")
                .password("admin")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}