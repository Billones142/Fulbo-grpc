package com.grpcServicio.servicioFulbo.services;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;

import com.google.protobuf.Field;
import com.google.protobuf.Descriptors.FieldDescriptor;

import io.grpc.stub.StreamObserver;

// clases de servidor
import com.grpcServicio.servicioFulbo.MultiplesMensajesStream;
import com.grpcServicio.servicioFulbo.MultiplesRespuestasStream;

// interfaces gRCP
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.EnviarMensaje;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.ChatServiceGrpc.ChatServiceImplBase;

// clases de Fulbo
import fulbo.ucp.*;
import fulbo.ucp.interfaces.*;



@GRpcService
public class ChatService extends ChatServiceImplBase {

    @Override
    public void ping(EnviarMensaje request, StreamObserver<RecibirMensaje> responseObserver) {
        //Crea la respuesta
        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setFrom(1)
                                        .setMessage("pong")
                                        .build();


        //Envia el mensaje
        if (request.getTo() == 1) {
            responseObserver.onNext(response);
        }
        

        //Cierra la conexión
        responseObserver.onCompleted();
    }

    @Override
    public void enviarIntegrante (IntegranteSeleccion_gRPC nuevoIntegrante, StreamObserver<RecibirMensaje> responseObserver) {
        // es capaz de recivir un integrante y devolver un mesaje de acuerdo los datos
        // TODO: que cree un objeto dentro del programa

        IintegranteSeleccion integrante;
        
        String rol= "";
        if(nuevoIntegrante.hasJugador()){
            rol= "Jugador";
            integrante= new Jugador(nuevoIntegrante.getNombre(),
                                    nuevoIntegrante.getApellido(),
                                    nuevoIntegrante.getHijos(),
                                    nuevoIntegrante.getSueldoBasico(),
                                    nuevoIntegrante.getJugador().getPosicionTactica(),
                                    nuevoIntegrante.getJugador().getPremio());
        }else if(nuevoIntegrante.hasEntrenador()){
            rol= "Entrenador";
        }else if(nuevoIntegrante.hasMasajista()){
            rol= "Masajista";
        }

        String mensaje= "nuevo Integrante: " + nuevoIntegrante.getNombre() + " " + nuevoIntegrante.getApellido() + ", " + rol;


        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setFrom(1)
                                        .setMessage(mensaje)
                                        .build();
        
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    /*@Override
    public StreamObserver<EnviarMensaje> enviarMultiplesMensajes(StreamObserver<RecibirMensaje> responseObserver) {
        return new MultiplesMensajesStream(responseObserver);
        /*
         * Una solución alternativa es crear una clase anomina, esto es hacer el new StreamObserver e implementar los métodos necesarios. 
         */
        //return new StreamObserver<EnviarMensaje>(){} 
    /*}

    @Override
    public StreamObserver<EnviarMensaje> enviarRecibirMultiplesMensajes(StreamObserver<RecibirMensaje> responseObserver) {
        return new MultiplesRespuestasStream(responseObserver);
    }

    @Override
    public void recibirMultiplesRespuestas(EnviarMensaje request, StreamObserver<RecibirMensaje> responseObserver) {
        Random random = new Random();
        RecibirMensaje mensajeRespuesta = null;
        for(int i=0;i<10;i++){
            mensajeRespuesta = RecibirMensaje.newBuilder()
                                .setFrom(random.nextInt())
                                .setMessage("Hola stream desde servidor "+random.nextInt())
                                .build();
            responseObserver.onNext(mensajeRespuesta);
        }

        responseObserver.onCompleted();
    }*/
}