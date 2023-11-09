package com.grpcCliente.servicioFulbo;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;

import com.grpcInterfaces.fulbo.ChatServiceGrpc;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;


@SpringBootApplication
public class Cliente {

	private Channel channel;
    private ChatServiceGrpc.ChatServiceBlockingStub blockingStub;

    public Cliente(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ChatServiceGrpc.newBlockingStub(channel);
	}

	public void ping() {
		RecibirMensaje respuestaPing= this.sendMessage("ping");
		System.out.println(respuestaPing.getMessage());
	}

    public RecibirMensaje sendMessage(String message) {
        Peticion peticion = Peticion.newBuilder()
		.setTo(1)
		.setMessage(message)
		.build();

        RecibirMensaje respuesta = blockingStub.ping(peticion);
		System.out.println(respuesta.getMessage());
		return respuesta;
    }


	public void limpiarTerminal() {
		String osName = System.getProperty("os.name");
		try {
			if (osName.contains("Windows")) {
		Runtime.getRuntime().exec("cls");
		} else {
		Runtime.getRuntime().exec("clear");
		}
		} catch (Exception e) {}
	}
}