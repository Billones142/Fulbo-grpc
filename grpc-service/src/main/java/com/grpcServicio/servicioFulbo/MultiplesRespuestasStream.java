package com.grpcServicio.servicioFulbo;

import java.util.Random;

import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;

import io.grpc.stub.StreamObserver;

public class MultiplesRespuestasStream implements StreamObserver<Peticion>{
    private StreamObserver<RecibirMensaje> responseObserver;
    private Random random = new Random();

    //Necesitamos inyectar el stream observer para poder responder al cliente el mensaje
    public MultiplesRespuestasStream(StreamObserver<RecibirMensaje> responseObserver){
        this.responseObserver = responseObserver;
    }


    @Override
    public void onNext(Peticion value) {
        System.out.println("MultiplesMensajesStream - Nuevo mensaje recibido: "+value);

        //En cuanto recibimos un mensaje, podemos enviar muchos mensajes de regreso al cliente
        RecibirMensaje mensajeRespuesta = null;
        for(int i=0;i<10;i++){
            mensajeRespuesta = RecibirMensaje.newBuilder()
                                .setMessage("Hola stream bidireccional desde servidor "+random.nextInt())
                                .build();
            this.responseObserver.onNext(mensajeRespuesta);
        }
        //Tambien puedes cerrar el stream ejecutando el onCompleted();
        //this.responseObserver.onCompleted();
        
    }

    @Override
    public void onError(Throwable t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCompleted() {
        RecibirMensaje mensajeRespuesta = null;
        for(int i=0;i<10;i++){
            mensajeRespuesta = RecibirMensaje.newBuilder()
                                .setMessage("Hola stream bidireccional desde servidor al completar "+random.nextInt())
                                .build();
            this.responseObserver.onNext(mensajeRespuesta);
        }
        this.responseObserver.onCompleted();
    }
    
}
