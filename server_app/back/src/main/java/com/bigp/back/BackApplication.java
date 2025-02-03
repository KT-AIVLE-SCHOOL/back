package com.bigp.back;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackApplication {

    // @PostConstruct
    // public void started() {
    //     TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    // }
	
	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("SELECT 1");
                System.out.println("Successfully connected to the database!");
            } catch (Exception e) {
                System.err.println("Failed to connect to the database: " + e.getMessage());
            }
        };
    }
}
