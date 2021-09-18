package server.main;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import server.rules.GameConstants;

@SpringBootApplication
@EnableScheduling
@Configuration
public class MainServer {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MainServer.class);

		app.setDefaultProperties(Collections.singletonMap("server.port", GameConstants.DEFAULT_PORT));
		app.run(args);
	}
}
