package com.grpcCliente.servicioFulbo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;


@GRpcService
public class Cliente {
	private Channel channel;
    private ServicioFulboGrpc.ServicioFulboBlockingStub blockingStub;

	SeleccionAFA_gRPC.Builder seleccionAFA= null;
	ConsoleEraser eraser;
	String respuestaDelServidor= null;

	/******************************Inicio de menus******************************/

	public boolean menuPrincipal() {
		return menuPrincipal(true, "");
	}

	public boolean menuPrincipal(String mensaje) {
		return menuPrincipal(true, mensaje);
	}

	public boolean menuPrincipal(boolean limpiarTerminal, String mensajeAmostrar) { // TODO: ejecutar en un while en vez de ser recursivo

		if (limpiarTerminal) {
			borrarTerminal();
		}

		if (!mensajeAmostrar.isEmpty()) {
			System.out.println(mensajeAmostrar);
		}

		if (respuestaDelServidor != null) {
			System.out.println( horaFormateada() + "  Respuesta del servidor: " );
			System.out.println(respuestaDelServidor);
			System.out.println();
			respuestaDelServidor= null;
		}

        System.out.println("Elija una opcion\r\n" + //
							"1. Enviar Ping\n\r" + //
                            "2. Crear nueva Seleccion FIFA\n\r" + 
							"3. Hacer peticion al servidor");
		if (seleccionAFA != null) {
			System.out.println("3. Enviar seleccion guardada\r\n" + //
								"4. Editar seleccion FIFA\r\n"+
								"5. Borrar datos de la seleccion\n\r" + //
								"6. Terminar comunicacion con el servidor");
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

			case 4:
				menuHacerPeticion();
				break;

			case 5:
				enviarSeleccion();
				return menuPrincipal();

			case 6: 
				if (seleccionAFA != null) {
					menuEditarSeleccion();
				}else{
					eleccion= 0; // si no existe volver al menu
				}
				break;

			case 7:
				if (seleccionAFA != null) {
					seleccionAFA= null;
					return menuPrincipal();
				}else{ // si no existe volver al menu
					eleccion= 0;
				}
				break;

			case 8: // termina la comunicacion con el servidor ya que retorna false si no se hace nada
				break;

			default:
				return menuPrincipal("No se elijio una opcion valida");
		}

		return false; // si el programa llega a este punto se corta la comunicacion
    }


	private boolean menuHacerPeticion() { //TODO: ver peticiones
		borrarTerminal();
		System.out.println("Que peticion quiere hacer al servidor?\r\n" + //
							"1. liquidar sueldos\r\n" + //
							"2. solicitar nomina\r\n" + //
							"3. sueldo neto de jugador");

		int eleccionPeticion;
		try {
			eleccionPeticion= Integer.parseInt(System.console().readLine());
		} catch (Exception e) {
			eleccionPeticion= 0;
		}

		switch (eleccionPeticion) {
			case 1:
				solicitudLiquidacionDeSueldo();
				break;

			case 2:
				solicitudDeSueldos();
				break;

			case 3:
				menuSolicitudSueldoNetoJugador();
				break;

			default:
				break;
		}

		return menuPrincipal();
	}
	
	private void solicitudDeSueldos() { //TODO
		
	}

	private void menuSolicitudSueldoNetoJugador() { //TODO
		borrarTerminal();
		System.out.println("De que jugador quiere solicitar el sueldo");
		//solicitudDeJugadores();
	}
	
	public void menuEditarSeleccion(){ //TODO
		borrarTerminal();
		System.out.println("");
	}

	public void menuEditarIntegrantes() {
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
	
	public boolean menuCrearSeleccion() {
		borrarTerminal();
		SeleccionAFA_gRPC.Builder nuevaSeleccion= SeleccionAFA_gRPC.newBuilder();

		System.out.println("Cual es el apellido de su presidente?");
		String apellidoPresidente= System.console().readLine();
		nuevaSeleccion.setPresidente(apellidoPresidente);

		boolean seguirAgregandoIntegrantes= true;
		while (seguirAgregandoIntegrantes) {
			System.out.println("quieres agregar un integrante? \r\n" + //
			"1. Si\r\n" + //
			"2. No");
			int eleccionAgregrarIntegrante= Integer.parseInt(System.console().readLine());
			if (eleccionAgregrarIntegrante == 1) {
				menuAgregarIntegrante();
			}else{
				seguirAgregandoIntegrantes= false;
			}
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

	private IntegranteSeleccion_gRPC.Builder menuCrearEditarIntegrante() {
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

		System.out.println("Que tipo de integrante sera?\r\n" + //
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

				System.out.print("Que titulacion tiene?: ");
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
		seleccionAFA.addSeleccionado(menuCrearEditarIntegrante());
		return menuPrincipal();
	}

	public boolean menuEditarIntegrante(int index) {
		seleccionAFA.setSeleccionado(index, menuCrearEditarIntegrante());
		return menuPrincipal();
	}
	
	/******************************Fin de menus******************************/
	
    public Cliente(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.blockingStub = ServicioFulboGrpc.newBlockingStub(channel);
		ping(); // ping para verificar la coneccion con el servidor

		eraser = new ConsoleEraser();
		eraser.addIgnoreClass(System.class);
		eraser.start();
	}

	private void ping() {
		borrarTerminal();

		System.out.println("enviando Ping...");

		Peticion peticion = Peticion.newBuilder()
		.setTo(1)
		.setMessage("ping")
		.build();

		RecibirMensaje respuestaPing= blockingStub.ping(peticion);


		respuestaDelServidor= respuestaPing.getMessage();
	}

	private void solicitudLiquidacionDeSueldo() { //TODO
		
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

	private String horaFormateada(){
		Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		
		return localDateTime.getHour()+":" + localDateTime.getMinute() + ":" + localDateTime.getSecond();
	}
}