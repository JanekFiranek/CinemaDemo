package tech.firanek.cinemademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("tech.firanek.cinemademo.repository")
@EntityScan("tech.firanek.cinemademo.entity")
@ComponentScan("tech.firanek.cinemademo.*")
public class CinemaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaDemoApplication.class, args);
	}
}
