package com.grpcCliente.servicioFulbo;

import java.util.Iterator;

import com.grpcInterfaces.fulbo.DatosDePeticion;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC;
import com.grpcInterfaces.fulbo.ServicioFulboGrpc;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;

/**
     * clase abstracta que contiene todos los metodos necesarios para comunicarse con el servidor
     * para usarla solo hay que crear otra clase que herede esta
     */
public abstract class ClienteFunciones{
    private Channel channel;
    private ServicioFulboGrpc.ServicioFulboBlockingStub servidor;

    public ClienteFunciones(String host, int port) {
        super();
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.servidor = ServicioFulboGrpc.newBlockingStub(channel);
    }

    protected String ping() {
		Peticion peticion = Peticion.newBuilder()
		.setTo(1)
		.setMessage("ping")
		.build();

		RecibirMensaje respuestaPing= servidor.ping(peticion);

        return respuestaPing.getMessage();
    }

    protected String enviarSeleccion(SeleccionAFA_gRPC.Builder nuevaSeleccion) {
        return servidor.enviarSeleccion(nuevaSeleccion.build()).getMessage();
    }

    protected String solicitudDeNomina() {
		return enviarPeticion("solicitar nomina");
	}

    protected String solicitudLiquidacionDeSueldo() {
		return enviarPeticion("liquidar sueldos");
	}

    protected String solicitudSueldoNetoJugador(int index) {
		return enviarPeticion("sueldo neto de jugador: " + index);
	}
	
	protected String enviarPeticion(String peticion){
		DatosDePeticion datos= servidor.peticionDeDatos(Peticion.newBuilder()
									.setTo(1)
									.setMessage(peticion)
									.build());
		
		StringBuilder datosString= new StringBuilder();

		if (datos.getMessageCount() == 1) {
			datosString.append("No se recibio nada");
		}

		for (int i = 0; i < datos.getMessageCount(); i++) {
			datosString.append(datos.getMessage(i) + "\r\n");
		}

		return datosString.toString();
	}

    protected Iterator<IntegranteSeleccion_gRPC> peticionIntegrantes(){
        return servidor.peticionDeIntegrantes(Peticion.newBuilder().setTo(1).build());
    }
}
