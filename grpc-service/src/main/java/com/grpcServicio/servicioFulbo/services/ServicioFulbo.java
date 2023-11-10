package com.grpcServicio.servicioFulbo.services;

import org.lognet.springboot.grpc.GRpcService;

//import com.google.protobuf.Field;
//import com.google.protobuf.Descriptors.FieldDescriptor;

import io.grpc.stub.StreamObserver;

// clases de servidor
//import com.grpcServicio.servicioFulbo.MultiplesMensajesStream;
//import com.grpcServicio.servicioFulbo.MultiplesRespuestasStream;

// interfaces gRCP
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.ServicioFulboGrpc.ServicioFulboImplBase;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;


// clases de Fulbo
import fulbo.ucp.*;
import fulbo.ucp.interfaces.*;



@GRpcService
public class ServicioFulbo extends ServicioFulboImplBase {

    SeleccionAFA seleccion;

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

    public SeleccionAFA crearSeleccion(SeleccionAFA_gRPC seleccionMensaje) { //TODO
        SeleccionAFA nuevaSeleccion= new SeleccionAFA(seleccionMensaje.getPresidente());
        for (int i = 0; i < seleccionMensaje.getSeleccionadoCount(); i++) {
            nuevaSeleccion.agregarIntegrante(
                                crearIntegrante(
                                    seleccionMensaje.getSeleccionado(i))
            );
        }


        return nuevaSeleccion;
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