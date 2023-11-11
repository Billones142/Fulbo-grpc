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
import com.grpcInterfaces.fulbo.Entrenador_gRPC;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.Jugador_gRPC;
import com.grpcInterfaces.fulbo.Masajista_gRPC;

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

    
    @Override
    public void peticionDeIntegrantes(Peticion peticion, StreamObserver<IntegranteSeleccion_gRPC> responseObserver) { //TODO
        if (peticion.getTo() != 1) {
            return;
        }
        IntegranteSeleccion_gRPC jugador= null;
        for (int i = 0; i < seleccion.getSeleccionado().size(); i++) {
            jugador= jugadorAprotocolo(seleccion.getSeleccionado().get(i));
            responseObserver.onNext(jugador);
        }
        
        responseObserver.onCompleted();
    }
    
    private SeleccionAFA_gRPC seleccionAFAProtocolo(SeleccionAFA seleccionAFA){
        SeleccionAFA_gRPC.Builder seleccionAenviar= SeleccionAFA_gRPC.newBuilder();

        seleccionAenviar.setPresidente(seleccionAFA.getPresidente());
        
        for (int i = 0; i < seleccionAFA.getSeleccionado().size(); i++) {
            seleccionAenviar.addSeleccionado(jugadorAprotocolo(seleccionAFA.getSeleccionado().get(i)));
        }

        return seleccionAenviar.build();
    }

    private IntegranteSeleccion_gRPC jugadorAprotocolo(IintegranteSeleccion integranteSeleccion) {
        IntegranteSeleccion_gRPC.Builder integranteAenviar= IntegranteSeleccion_gRPC.newBuilder();

        integranteAenviar.setNombre(integranteSeleccion.getNombre());
        integranteAenviar.setApellido(integranteSeleccion.getApellido());
        integranteAenviar.setHijos(integranteSeleccion.getHijos());
        integranteAenviar.setSueldoBasico(integranteSeleccion.getSueldoBasico());
        
        if (integranteSeleccion instanceof Jugador) { // si es jugador
            Jugador jugador= (Jugador)integranteSeleccion;
            Jugador_gRPC.Builder jugadorAenviar= Jugador_gRPC.newBuilder();

            jugadorAenviar.setPremio(jugador.getPremio());
            jugadorAenviar.setPosicionTactica(jugador.getPosicionTactica());
            
            integranteAenviar.setJugador(jugadorAenviar);
        }else if (integranteSeleccion instanceof Entrenador) { // si es entrenador
            Entrenador entrenador= (Entrenador)integranteSeleccion;
            Entrenador_gRPC.Builder entrenadorAenviar= Entrenador_gRPC.newBuilder();
            
            entrenadorAenviar.setNacionalidad(entrenador.getNacionalidad());
            
            integranteAenviar.setEntrenador(entrenadorAenviar);
        }else if (integranteSeleccion instanceof Masajista) { // si es masajista
            Masajista masajista= (Masajista)integranteSeleccion;
            Masajista_gRPC.Builder masajistaAenviar= Masajista_gRPC.newBuilder();
            
            masajistaAenviar.setTitulacion(masajista.getTitulacion());
            
            integranteAenviar.setMasajista(masajistaAenviar);
        }


        return integranteAenviar.build();
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

    private SeleccionAFA crearSeleccion(SeleccionAFA_gRPC seleccionProtocolo) { //TODO
        SeleccionAFA nuevaSeleccion= new SeleccionAFA(seleccionProtocolo.getPresidente());
        for (int i = 0; i < seleccionProtocolo.getSeleccionadoCount(); i++) {
            nuevaSeleccion.agregarIntegrante(
                                crearIntegrante(
                                    seleccionProtocolo.getSeleccionado(i))
            );
        }

        for (int i = 0; i < seleccionProtocolo.getSeleccionadoCount(); i++) {
            nuevaSeleccion.agregarIntegrante(crearIntegrante(seleccionProtocolo.getSeleccionado(i)));
        }

        return nuevaSeleccion;
    }
}