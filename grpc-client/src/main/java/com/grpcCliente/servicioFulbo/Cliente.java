package com.grpcCliente.servicioFulbo;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.lognet.springboot.grpc.GRpcService;
//import org.springframework.context.annotation.Bean;

import com.grpcInterfaces.fulbo.Entrenador_gRPC;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.Jugador_gRPC;
import com.grpcInterfaces.fulbo.Masajista_gRPC;
import com.grpcInterfaces.fulbo.Peticion;
import com.grpcInterfaces.fulbo.RecibirMensaje;
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC;
import com.grpcInterfaces.fulbo.ServicioFulboGrpc;

import fulbo.ucp.Masajista;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;


@GRpcService
public class Cliente {
	private Channel channel;
    private ServicioFulboGrpc.ServicioFulboBlockingStub blockingStub;

	SeleccionAFA_gRPC.Builder seleccionAFA= null;

	/******************************Inicio de menus******************************/

	public boolean menuPrincipal() {
		return menuPrincipal(true, "");
	}

	public boolean menuPrincipal(String mensaje) {
		return menuPrincipal(true, mensaje);
	}

	public boolean menuPrincipal(boolean limpiarTerminal, String mensajeAmostrar) {

		if (limpiarTerminal) {
			borrarTerminal();
		}

		if (!mensajeAmostrar.isEmpty()) {
			System.out.println(mensajeAmostrar);
		}

		enviarMensaje("ping"); // ping para verificar la coneccion con el servidor

        System.out.println("Elija una opcion\r\n" + //
							"1. Enviar Ping\n\r" + //
                            "2. Crear nueva Seleccion AFA");
		if (seleccionAFA != null) {
			System.out.println("3. Enviar seleccion guardada\r\n" + //
								"4. Editar seleccion AFA (si ya existe)\r\n"+
								"5. Borrar datos de la seleccion\n\r" + //
								"6. Terminar comunicacion con el servidor"); //TODO: hacer peticion de datos
		}
		int eleccion= 0;
		try {
			eleccion= Integer.parseInt(System.console().readLine());
		} catch (NumberFormatException error) {
			System.out.println("No se ingreso un numero valido. error:" + error.getMessage());
		}

		switch (eleccion) {
			case 1: // Enviar Ping
				ping();
				return menuPrincipal();

			case 2: // Crear nueva Seleccion AFA
				return menuCrearSeleccion();

			case 3:
				enviarSeleccion();
				return menuPrincipal();

			case 4: 
				if (seleccionAFA != null) {
					menuEditarSeleccion();
				}else{
					eleccion= 0; // si no existe volver al menu
				}
				break;

			case 5:
				if (seleccionAFA != null) {
					seleccionAFA= null;
					return menuPrincipal();
				}else{ // si no existe volver al menu
					eleccion= 0;
				}
				break;

			case 6: // termina la comunicacion con el servidor ya que retorna false si no se hace nada
				break;

			default:
				return menuPrincipal("No se elijio una opcion valida");
		}

		return false; // si el programa llega a este punto se corta la comunicacion
    }
	
	
	public boolean menuEditarSeleccion(){
		System.out.println();
		return false;
	}

	public void menuEditarIntegrantes() { //TODO
		borrarTerminal();
		System.out.println("Elija una opcion:\r\n" + //
							"1. Volver" +
							"1. Agregar integrante");
		for (int i = 0; i < seleccionAFA.getSeleccionadoCount(); i++) {
			IntegranteSeleccion_gRPC integrante= seleccionAFA.getSeleccionado(i);

			String nombre= integrante.getNombre();
			String apellido= integrante.getApellido();
			int hijos= integrante.getHijos();
			double sueldoBasico= integrante.getSueldoBasico();

			System.out.println((i+2)+ ". " + 
							nombre + " " + apellido +
							", tiene " + hijos +
							" con un sueldo basico de $" + sueldoBasico);

			System.out.print("Es ");

			if (integrante.hasJugador()) {
				System.out.println("Jugador" + 
				", juega como " + integrante.getJugador().getPosicionTactica() + 
				(integrante.getJugador().getPremio()?" y tiene un premio":""));

			}else if (integrante.hasEntrenador()) {
				System.out.println("Entrenador, y es de " + integrante.getEntrenador().getNacionalidad());

			}else if (integrante.hasMasajista()) {
				System.out.println("Masajista, con un titulo como " + integrante.getMasajista());
			}

			int eleccionEdicionIntegrante= 1;
			try {
				eleccionEdicionIntegrante= Integer.parseInt(System.console().readLine());
			} catch (Exception e) {
				System.out.println("numero no valido");
			}

			if (eleccionEdicionIntegrante == 2) {
				menuAgregarIntegrante();
			}else if (eleccionEdicionIntegrante > 2) {
				menuEditarIntegrante(eleccionEdicionIntegrante-2);
			}else{
				System.out.println("eleccion no valida");
				menuEditarIntegrantes();
			}
		}
	}
	
	
	public boolean menuCrearSeleccion() { //TODO
		SeleccionAFA_gRPC.Builder nuevaSeleccion= SeleccionAFA_gRPC.newBuilder();
		
		System.out.println("Cual es el apellido de su presidente?");
		String apellidoPresidente= System.console().readLine();
		nuevaSeleccion.setPresidente(apellidoPresidente);
		
		System.out.println("quieres agregar un integrante? \r\n" + //
		"1. Si\r\n" + //
		"2. No");
		int eleccionAgregrarIntegrante= Integer.parseInt(System.console().readLine());
		if (eleccionAgregrarIntegrante == 1) {
			menuAgregarIntegrante();
		}
		
		System.out.println("termino de ingresar los datos para su nueva seleccion, quiere enviarla al servidor?\r\n" + //
		"1. Si\r\n" + //
		"2. No");
		int eleccionEnviarSeleccion= Integer.parseInt(System.console().readLine());
		
		
		if (eleccionEnviarSeleccion == 1) {
			seleccionAFA= nuevaSeleccion;
			String respuestaServer= blockingStub.enviarSeleccion(nuevaSeleccion.build()).getMessage();
			System.out.println("respuesta del servidor" + respuestaServer);
		}else if (eleccionEnviarSeleccion == 2) {
			System.out.println("los datos quedaran guardados, puede borrarlos si quiere");
		}
		
		return menuPrincipal();
	}

	private IntegranteSeleccion_gRPC.Builder menuCrearBorrarIntegrante() {
		borrarTerminal();
		IntegranteSeleccion_gRPC.Builder nuevoIntegrante= IntegranteSeleccion_gRPC.newBuilder();
	
		System.out.print("Que apellido tiene?: ");
		nuevoIntegrante.setApellido(System.console().readLine());
		System.out.println();
	
		System.out.print("Que nombre tiene?: ");
		nuevoIntegrante.setNombre(System.console().readLine());
		System.out.println();
	
		System.out.print("Cuantos hijos tiene?: ");
		nuevoIntegrante.setHijos(Integer.parseInt(System.console().readLine()));
		System.out.println();
	
		System.out.print("Que sueldo basico tiene?: ");
		nuevoIntegrante.setSueldoBasico(Double.parseDouble(System.console().readLine()));
		System.out.println();
	
		System.out.print("Que tipo de integrante sera?\r\n" + //
				"1. Jugador\r\n" + //
				"2. Entrenador\r\n" + //
				"3. Masajista");
	
		int eleccionTipoDeIntegrante= Integer.parseInt(System.console().readLine());
	
		switch (eleccionTipoDeIntegrante) {
			case 1:
				Jugador_gRPC.Builder jugador= Jugador_gRPC.newBuilder();
	
				System.out.print("Que posicion tactica tiene?: ");
				jugador.setPosicionTactica(System.console().readLine());
				System.out.println();
	
				System.out.print("Tiene premio?(1:si, otro:no): ");
				jugador.setPremio((Integer.parseInt(System.console().readLine()) == 1?true:false));
				System.out.println();
	
				nuevoIntegrante.setJugador(jugador);
				break;
			
			case 2:
				Entrenador_gRPC.Builder entrenador= Entrenador_gRPC.newBuilder();
	
				System.out.print("Que posicion tactica tiene?: ");
				entrenador.setNacionalidad(System.console().readLine());
				System.out.println();
	
				nuevoIntegrante.setEntrenador(entrenador);
				break;
	
			case 3:
				Masajista_gRPC.Builder masajista= Masajista_gRPC.newBuilder();
	
				System.out.print("Que posicion tactica tiene?: ");
				masajista.setTitulacion(System.console().readLine());
				System.out.println();
	
				nuevoIntegrante.setMasajista(masajista);
				break;
		
			default:
				break;
		}
	
		return nuevoIntegrante;
	}
	
	private boolean menuAgregarIntegrante(){
		seleccionAFA.addSeleccionado(menuCrearBorrarIntegrante());
		return menuPrincipal();
	}
	
		public boolean menuEditarIntegrante(int index) {
		seleccionAFA.setSeleccionado(index, menuCrearBorrarIntegrante());
		return menuPrincipal();
		}
	
	/******************************Fin de menus******************************/
	
    public Cliente(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ServicioFulboGrpc.newBlockingStub(channel);
	}
	
	private void ping() {
		System.out.println("enviando Ping...");
		RecibirMensaje respuestaPing= this.enviarMensaje("ping");
		System.out.println("respuesta del servidor: " + respuestaPing.getMessage());
	}
	
    private RecibirMensaje enviarMensaje(String message) {
		Peticion peticion = Peticion.newBuilder()
		.setTo(1)
		.setMessage(message)
		.build();
		
		return blockingStub.ping(peticion);
    }
	
	private RecibirMensaje enviarSeleccion() {
		return blockingStub.enviarSeleccion(seleccionAFA.build());
	}

	public static void borrarTerminal() {
		try {
				Thread.sleep(500);
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {}
	}
}