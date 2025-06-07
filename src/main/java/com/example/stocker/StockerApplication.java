package com.example.stocker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StockerApplication.class);

		// Detectar puerto desde variables de entorno o usar valor por defecto
		String port = System.getenv("PORT");

		// Usar valor del sistema o fallback al .env cargado por Spring
		if (port != null && !port.isEmpty()) {
			Map<String, Object> props = new HashMap<>();
			props.put("server.port", port);
			app.setDefaultProperties(props);
		}

		app.run(args);
	}
}

