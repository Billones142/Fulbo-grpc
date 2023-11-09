package com.grpcCliente.servicioFulbo.services;

import com.grpcInterfaces.fulbo.ChatServiceGrpc.ChatServiceImplBase;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;

import com.google.protobuf.Field;
import com.google.protobuf.Descriptors.FieldDescriptor;

import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;

import io.grpc.stub.StreamObserver;



@GRpcService
public class ChatService extends ChatServiceImplBase {

    @Override
    public void ping(Peticion request, StreamObserver<RecibirMensaje> responseObserver) {
        //Crea la respuesta
        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setMessage("Hello back!")
                                        .build();


        //Envia el mensaje
        responseObserver.onNext(response);

        //Cierra la conexión
        responseObserver.onCompleted();
    }
}