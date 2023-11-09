package com.grpcServicio.servicioFulbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fulbo.ucp.SeleccionAFA;

@SpringBootApplication
public class Servidor {
	SeleccionAFA seleccion;

	public static void main(String[] args) {
		SpringApplication.run(Servidor.class, args);
	}
}
