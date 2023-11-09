package com.grpcServicio.servicioFulbo.services;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;

import com.google.protobuf.Field;
import com.google.protobuf.Descriptors.FieldDescriptor;

import io.grpc.stub.StreamObserver;

// clases de servidor
//import com.grpcServicio.servicioFulbo.MultiplesMensajesStream;
//import com.grpcServicio.servicioFulbo.MultiplesRespuestasStream;

// interfaces gRCP
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.ChatServiceGrpc.ChatServiceImplBase;

// clases de Fulbo
import fulbo.ucp.*;
import fulbo.ucp.interfaces.*;



@GRpcService
public class ChatService extends ChatServiceImplBase {

    @Override
    public void ping(Peticion request, StreamObserver<RecibirMensaje> responseObserver) {
        //Crea la respuesta
        
        String text= "";
        
        //Envia el mensaje
        if ((request.getTo() == 1) && (request.getMessage().contains("ping"))) {
            text= "pong";
        }
        
        RecibirMensaje response = RecibirMensaje.newBuilder()
        .setMessage(text)
        .build();
        
        responseObserver.onNext(response);

        //Cierra la conexión
        responseObserver.onCompleted();
    }

    @Override
    public void enviarIntegrante (IntegranteSeleccion_gRPC nuevoIntegrante, StreamObserver<RecibirMensaje> responseObserver) {
        // es capaz de recivir un integrante y devolver un mesaje de acuerdo los datos

        IintegranteSeleccion integrante= crearIntegrante(nuevoIntegrante);
        
        String rol= "";
        if(nuevoIntegrante.hasJugador()){
            rol= "Jugador";
        }else if(nuevoIntegrante.hasEntrenador()){
            rol= "Entrenador";
        }else if(nuevoIntegrante.hasMasajista()){
            rol= "Masajista";
        }

        String mensaje= "nuevo Integrante: " + nuevoIntegrante.getNombre() + " " + nuevoIntegrante.getApellido() + ", " + rol;


        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setMessage(mensaje)
                                        .build();
        
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    public IintegranteSeleccion crearIntegrante(IntegranteSeleccion_gRPC nuevoIntegrante) {
        IintegranteSeleccion integrante= null;

        String nombre= nuevoIntegrante.getNombre();
        String apellido= nuevoIntegrante.getApellido();
        int hijos= nuevoIntegrante.getHijos();
        double sueldoBasico= nuevoIntegrante.getSueldoBasico();
        if(nuevoIntegrante.hasJugador()){
            integrante= new Jugador(nombre,
                                    apellido,
                                    hijos,
                                    sueldoBasico,
                                    nuevoIntegrante.getJugador().getPosicionTactica(),
                                    nuevoIntegrante.getJugador().getPremio());
        }else if(nuevoIntegrante.hasEntrenador()){
            integrante=  new Entrenador(nombre,
                                        apellido,
                                        hijos,
                                        sueldoBasico,
                                        nuevoIntegrante.getEntrenador().getNacionalidad());
        }else if(nuevoIntegrante.hasMasajista()){
            integrante=  new Entrenador(nombre,
                                        apellido,
                                        hijos,
                                        sueldoBasico,
                                        nuevoIntegrante.getMasajista().getTitulacion());
        }

        return integrante;
    }
}