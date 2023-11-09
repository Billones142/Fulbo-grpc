package com.grpcCliente.servicioFulbo.services;

import com.grpcInterfaces.fulbo.ChatServiceGrpc.ChatServiceImplBase;
import com.grpcCliente.servicioFulbo.MultiplesMensajesStream;
import com.grpcCliente.servicioFulbo.MultiplesRespuestasStream;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;

import com.google.protobuf.Field;
import com.google.protobuf.Descriptors.FieldDescriptor;

import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.EnviarMensaje;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;

import io.grpc.stub.StreamObserver;



@GRpcService
public class ChatService extends ChatServiceImplBase {

    @Override
    public void ping(EnviarMensaje request, StreamObserver<RecibirMensaje> responseObserver) {
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
}