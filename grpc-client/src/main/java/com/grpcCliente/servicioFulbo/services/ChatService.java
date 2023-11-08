package com.grpcCliente.servicioFulbo.services;

import com.grpc.fulbo.ChatServiceGrpc.ChatServiceImplBase;
import com.grpcCliente.servicioFulbo.MultiplesMensajesStream;
import com.grpcCliente.servicioFulbo.MultiplesRespuestasStream;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;

import com.google.protobuf.Field;
import com.google.protobuf.Descriptors.FieldDescriptor;

import com.grpc.fulbo.RecibirMensaje;
import com.grpc.fulbo.EnviarMensaje;
import com.grpc.fulbo.IntegranteSeleccion_gRPC;

import io.grpc.stub.StreamObserver;



@GRpcService
public class ChatService extends ChatServiceImplBase {

    @Override
    public void enviarMensaje(EnviarMensaje request, StreamObserver<RecibirMensaje> responseObserver) {
        //Crea la respuesta
        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setFrom(1)
                                        .setMessage("Hello back!")
                                        .build();


        //Envia el mensaje
        responseObserver.onNext(response);

        //Cierra la conexión
        responseObserver.onCompleted();
    }

    @Override
    public void enviarIntegrante (IntegranteSeleccion_gRPC nuevoIntegrante, StreamObserver<RecibirMensaje> responseObserver) {
        // es capaz de recivir un integrante y devolver un mesaje de acuerdo los datos
        // TODO: que cree un objeto dentro del programa
        String nombre= nuevoIntegrante.getNombre();
        String apellido= nuevoIntegrante.getApellido();
        String rol= "";

        if(nuevoIntegrante.hasJugador()){
            rol= "Jugador";
        }else if(nuevoIntegrante.hasEntrenador()){
            rol= "Entrenador";
        }else if(nuevoIntegrante.hasMasajista()){
            rol= "Masajista";
        }

        String mensaje= "hola " + nombre + " " + apellido + ", " + rol;


        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setFrom(1)
                                        .setMessage(mensaje)
                                        .build();
        
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<EnviarMensaje> enviarMultiplesMensajes(StreamObserver<RecibirMensaje> responseObserver) {
        return new MultiplesMensajesStream(responseObserver);
        /*
         * Una solución alternativa es crear una clase anomina, esto es hacer el new StreamObserver e implementar los métodos necesarios. 
         */
        //return new StreamObserver<EnviarMensaje>(){} 
    }

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
    }
    
}