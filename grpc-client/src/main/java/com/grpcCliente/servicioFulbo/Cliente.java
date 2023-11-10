package com.grpcCliente.servicioFulbo;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;

import com.grpcInterfaces.fulbo.ChatServiceGrpc;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC;
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC.Builder;

import fulbo.ucp.SeleccionAFA;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;


@SpringBootApplication
public class Cliente {
	private Channel channel;
    private ChatServiceGrpc.ChatServiceBlockingStub blockingStub;

	Builder seleccionAFA;

	/******************************Inicio de menus******************************/

	public void menuPrincipal() {
		// TODO: agregar que resetee la terminal
		
		enviarMensaje("ping"); // ping para verificar la coneccion con el servidor

        System.out.println("Elija una opcion\r\n" + //
                            "1. editar seleccion AFA (si ya existe)\r\n" + //
                            "2. crear nueva Seleccion AFA\r\n" + //
                            "3. enviar Ping\n\r" + //
							"4. terminar comunicacion con el servidor");
        int eleccion= Integer.parseInt(System.console().readLine());

		switch (eleccion) {
			case 1:
				menuEditarSeleccion();
				break;

			case 2:
				menuCrearSeleccion();
				break;

			case 3:
				ping();
				menuPrincipal();
				break;

			case 4:
				break;
		
			default:
				System.out.println("No selecciono una opcion valida");
				menuPrincipal();
				break;
		}
    }

	public void menuEditarSeleccion(){
		System.out.println();
	}

	public void menuCrearSeleccion() { //TODO
		Builder nuevaSeleccion= SeleccionAFA_gRPC.newBuilder();

		System.out.println("Cual es el apellido de su presidente?");
		String apellidoPresidente= System.console().readLine();
		nuevaSeleccion.setPresidente(apellidoPresidente);

		System.out.println("quieres agregar un integrante? \r\n" + //
							"1. Si\r\n" + //
							"2. No");
		int eleccionAgregrarIntegrante= Integer.parseInt(System.console().readLine());
		if (eleccionAgregrarIntegrante == 1) {
			//menuAgregarIntegrante();
		}

		System.out.println("termino de ingresar los datos para su nueva seleccion, quiere enviarla al servidor?\r\n" + //
							"1. Si\r\n" + //
							"2. No");
		int eleccionEnviarSeleccion= Integer.parseInt(System.console().readLine());


		if (eleccionEnviarSeleccion == 1) {
			seleccionAFA= nuevaSeleccion;
			blockingStub.enviarSeleccion(nuevaSeleccion.build());
		}
	}

	/******************************Fin de menus******************************/

    public Cliente(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ChatServiceGrpc.newBlockingStub(channel);
	}

	public void ping() {
		System.out.println("enviando Ping...");
		RecibirMensaje respuestaPing= this.enviarMensaje("ping");
		System.out.println("respuesta del servidor: " + respuestaPing.getMessage());
	}

    public RecibirMensaje enviarMensaje(String message) {
        Peticion peticion = Peticion.newBuilder()
		.setTo(1)
		.setMessage(message)
		.build();

		return blockingStub.ping(peticion);
    }
}