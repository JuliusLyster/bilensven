package dk.bilensven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BilensvenApplication {

	public static void main(String[] args) {
		SpringApplication.run(BilensvenApplication.class, args);
	}
}
