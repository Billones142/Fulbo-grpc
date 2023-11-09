package com.grpcServicio.servicioFulbo;

import java.util.Random;

import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;

import io.grpc.stub.StreamObserver;

public class MultiplesMensajesStream implements StreamObserver<Peticion>{
    private StreamObserver<RecibirMensaje> responseObserver;
    private Random random = new Random();

    //Necesitamos inyectar el stream observer para poder responder al cliente el mensaje
    public MultiplesMensajesStream(StreamObserver<RecibirMensaje> responseObserver){
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(Peticion value) {
        System.out.println("MultiplesMensajesStream - Nueva peticion recibida: "+value.getMessage());
        
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("MultiplesMensajesStream - Ocurrio un error: "+t.getMessage());
        
    }

    @Override
    public void onCompleted() {
        //Los streams unidireccionales de cliente a servidor, solo requieren una respuesta del servidor.
        RecibirMensaje respuesta = RecibirMensaje.newBuilder()
                                    .setMessage("Hola mundo desde el servidor: "+ "1")
                                    .build();

        this.responseObserver.onNext(respuesta);
        this.responseObserver.onCompleted();
    }
    
}
