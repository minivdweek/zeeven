package nl.mini.zeeven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ZeevenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeevenApplication.class, args);
	}
}
