package com.grpcServicio.servicioFulbo.services;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

//import com.google.protobuf.Field;
//import com.google.protobuf.Descriptors.FieldDescriptor;

import io.grpc.stub.StreamObserver;

// clases de servidor
//import com.grpcServicio.servicioFulbo.MultiplesMensajesStream;
//import com.grpcServicio.servicioFulbo.MultiplesRespuestasStream;

// interfaces gRCP
import com.grpcInterfaces.fulbo.ServicioFulboGrpc.ServicioFulboImplBase;

//mensajes para comunicacion
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.DatosDePeticion;

//mensajes para objetos
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.Jugador_gRPC;
import com.grpcInterfaces.fulbo.Entrenador_gRPC;
import com.grpcInterfaces.fulbo.Masajista_gRPC;

// clases de Fulbo
import fulbo.ucp.SeleccionAFA;
import fulbo.ucp.Jugador;
import fulbo.ucp.Entrenador;
import fulbo.ucp.Masajista;

import fulbo.ucp.interfaces.IintegranteSeleccion;



@GRpcService
public class ServicioFulbo extends ServicioFulboImplBase {
    
    private SeleccionAFA seleccion;

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
    public void peticionDeIntegrantes(Peticion peticion, StreamObserver<IntegranteSeleccion_gRPC> responseObserver) {
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

    @Override
    public void peticionDeDatos(Peticion peticion, StreamObserver<DatosDePeticion> responseObserver) {
        if (peticion.getTo() != 1) {
            return;
        }

        String request= peticion.getMessage();

        DatosDePeticion.Builder datosAenviar= DatosDePeticion.newBuilder();

        if (request.startsWith("liquidar sueldos")) {
            String[] datos= seleccion.liquidarSueldos().split("\r\n");

            for (String linea : datos) {
                datosAenviar.addMessage(linea);                
            }

        }else if (request.startsWith("solicitar nomina")) {
            String[] datos= seleccion.mostrarNomina().split("\r\n");

            for (String linea : datos) {
                datosAenviar.addMessage(linea);                
            }

        }else if (request.startsWith("sueldo neto de jugador: ")) {
            int index= Integer.parseInt(request.split("jugador: ")[1]);
            IintegranteSeleccion integrante= seleccion.getSeleccionado().get(index);
            double sueldoNetoJugador= -1;
            if (integrante instanceof Jugador) {
                sueldoNetoJugador= ((Jugador)integrante).sueldoNeto();
            }else if (integrante instanceof Entrenador) {
                sueldoNetoJugador= ((Entrenador)integrante).sueldoNeto();
            }else if (integrante instanceof Masajista) {
                sueldoNetoJugador= ((Masajista)integrante).sueldoNeto();
            }
            String nombreYapellido= integrante.getNombre() + " " + integrante.getApellido();
            datosAenviar.addMessage(nombreYapellido + " tiene un sueldo neto de $" + sueldoNetoJugador);

        }else{
            datosAenviar.addMessage("Peticion no valida");
        }

        responseObserver.onNext(datosAenviar.build());

        responseObserver.onCompleted();
    }

    @Override
    public void enviarSeleccion(SeleccionAFA_gRPC seleccionRecibida, StreamObserver<RecibirMensaje> responseObserver){
        seleccion= protocoloAseleccion(seleccionRecibida);

        RecibirMensaje mensaje= RecibirMensaje.newBuilder()
                            .setMessage("Seleccion con el presidente " + seleccionRecibida.getPresidente() + " recibida")
                            .build();

        responseObserver.onNext(mensaje);

        responseObserver.onCompleted();
    }
    
    private SeleccionAFA_gRPC seleccionAProtocolo(SeleccionAFA seleccionAFA){
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

    private SeleccionAFA protocoloAseleccion(SeleccionAFA_gRPC seleccionProtocolo) {
        SeleccionAFA nuevaSeleccion= new SeleccionAFA(seleccionProtocolo.getPresidente());

        for (int i = 0; i < seleccionProtocolo.getSeleccionadoCount(); i++) {
            IintegranteSeleccion integranteAagregar= protocoloAintegrante(
                                    seleccionProtocolo.getSeleccionado(i));

            // no preguntar... cosas de java, o maven, no se, estoy confundido, pero funciona
            if (integranteAagregar instanceof Jugador) {
                nuevaSeleccion.agregarIntegrante((Jugador)integranteAagregar);
            }else if (integranteAagregar instanceof Entrenador) {
                nuevaSeleccion.agregarIntegrante((Entrenador)integranteAagregar);
            }else if (integranteAagregar instanceof Masajista) {
                nuevaSeleccion.agregarIntegrante((Masajista)integranteAagregar);
            }
            
        }

        return nuevaSeleccion;
    }

    /**
     * @param nuevoIntegrante
     * @return null si no es un integrante valido
     */
    public IintegranteSeleccion protocoloAintegrante(IntegranteSeleccion_gRPC nuevoIntegrante) {

        String nombre= nuevoIntegrante.getNombre();
        String apellido= nuevoIntegrante.getApellido();
        int hijos= nuevoIntegrante.getHijos();
        double sueldoBasico= nuevoIntegrante.getSueldoBasico();
        if(nuevoIntegrante.hasJugador()){
            return new Jugador(nombre,
                                    apellido,
                                    hijos,
                                    sueldoBasico,
                                    nuevoIntegrante.getJugador().getPosicionTactica(),
                                    nuevoIntegrante.getJugador().getPremio());
        }else if(nuevoIntegrante.hasEntrenador()){
            return new Entrenador(nombre,
                                        apellido,
                                        hijos,
                                        sueldoBasico,
                                        nuevoIntegrante.getEntrenador().getNacionalidad());
        }else if(nuevoIntegrante.hasMasajista()){
            return new Entrenador(nombre,
                                        apellido,
                                        hijos,
                                        sueldoBasico,
                                        nuevoIntegrante.getMasajista().getTitulacion());
        }

        return null;
    }

    /*private Class tipoDeIntegrante(IintegranteSeleccion integrante) { // funcion experimental para solucionar error
        
        if (integrante instanceof Jugador) { 
                return Jugador.class;
            }else if (integrante instanceof Entrenador) {
                return Entrenador.class;
            }else if (integrante instanceof Masajista) {
                return Masajista.class;
            }else{
                return IintegranteSeleccion.class;
            }
    }*/
}