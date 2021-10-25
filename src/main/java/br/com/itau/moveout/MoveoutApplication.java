package br.com.itau.moveout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class MoveoutApplication {

	private static final Logger log = LoggerFactory.getLogger(MoveoutApplication.class);

	private final JdbcTemplate jdbcTemplate;
	public MoveoutApplication(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(MoveoutApplication.class, args);
	}

}
