package com.grpcCliente.servicioFulbo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.Iterator;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.lognet.springboot.grpc.GRpcService;
//import org.springframework.context.annotation.Bean;

import com.grpcInterfaces.fulbo.Entrenador_gRPC;
import com.grpcInterfaces.fulbo.IntegranteSeleccion_gRPC;
import com.grpcInterfaces.fulbo.Jugador_gRPC;
import com.grpcInterfaces.fulbo.Masajista_gRPC;
import com.grpcInterfaces.fulbo.SeleccionAFA_gRPC;


@GRpcService
public class MenuCliente extends ClienteFunciones{

	SeleccionAFA_gRPC.Builder seleccionAFA= null;
	ConsoleEraser eraser;
	private String respuestaDelServidor= null;

	/******************************Inicio de menus******************************/

	public boolean menuPrincipal() {

		String mensajeError= "";

		boolean seguirEjecutando= true;
		while (seguirEjecutando) {
			borrarTerminal();

			if (!mensajeError.isEmpty()) {
				println("Error: " + mensajeError);
			}

			if (respuestaDelServidor != null) {
				println( horaFormateada() + "  Respuesta del servidor: " );
				println(respuestaDelServidor);
				println();
				respuestaDelServidor= null;
			}

			println("Elija una opcion\r\n" + //
								"1. Enviar Ping\n\r" + //
								"2. Crear nueva Seleccion FIFA\n\r" + 
								"3. Solicitar al servidor");
			if (seleccionAFA != null) {
				println("4. Enviar seleccion guardada\r\n" + //
									"5. Editar seleccion FIFA guardada localmente\r\n"+
									"6. Borrar datos de la seleccion locales");
								}
			println("7. Terminar comunicacion con el servidor");
			int eleccion= 0;

			boolean numeroNoValido= true;

			do {
				try {
					eleccion= consoleInInt();
					boolean estaEntre3y7= (eleccion > 3) && (eleccion < 7);
					if ((seleccionAFA == null) && !estaEntre3y7) {
						numeroNoValido= false;
					}
				} catch (NumberFormatException error) {
					println("No se ingreso un numero valido. error:" + error.getMessage());
				}
			} while (numeroNoValido);

			switch (eleccion) {
				case 1: // Enviar Ping
					borrarTerminal();

					println("enviando Ping...");
					respuestaDelServidor= ping();
					menuPrincipal();
					break;

				case 2: // Crear nueva Seleccion AFA
					menuCrearSeleccion();
					break;

				case 3: //Solicitar al servidor
					menuHacerPeticion();
					break;

				case 4: //Enviar seleccion guardada
					enviarSeleccion(seleccionAFA);
					break;

				case 5: //Editar seleccion FIFA guardada localmente
					menuEditarSeleccion();
					break;

				case 6: //Borrar datos de la seleccion locales
					if (seleccionAFA != null) {
						seleccionAFA= null;
					}else{ // si no existe volver al menu
						eleccion= 0;
					}
					break;

				case 7: // termina la comunicacion con el servidor
					seguirEjecutando= false;
					break;

				default:
					mensajeError= "No se elijio una opcion valida";
			}
		}


		return false; // si el programa llega a este punto se corta la comunicacion
    }


	private void menuHacerPeticion() {
		borrarTerminal();
		println("Que solicitud quiere hacer al servidor?\r\n" + //
							"1. liquidar sueldos\r\n" + //
							"2. solicitar nomina\r\n" + //
							"3. sueldo neto de jugador");

		int eleccionPeticion;
		try {
			eleccionPeticion= consoleInInt();
		} catch (Exception e) {
			eleccionPeticion= 0;
		}

		switch (eleccionPeticion) {
			case 1:
				respuestaDelServidor= solicitudLiquidacionDeSueldo();
				break;

			case 2:
				respuestaDelServidor= solicitudDeNomina();
				break;

			case 3:
				menuSolicitudSueldoNetoJugador();
				break;

			default:
				break;
		}
	}

	private void menuSolicitudSueldoNetoJugador() {
		borrarTerminal();
		Iterator<IntegranteSeleccion_gRPC> jugadores= peticionIntegrantes();
		println("De que jugador quiere solicitar el sueldo?: " + 
				"1. volver");
		
		int contador= 0;
		while (jugadores.hasNext()) {
			contador++;
			IntegranteSeleccion_gRPC jugador= jugadores.next();
			print((contador+2) + ". " + jugador.getApellido() + jugador.getNombre() +
						", hijos: " + jugador.getHijos() +
						", sueldo basico: " + jugador.getSueldoBasico());

			if (jugador.hasJugador()) {
				println(", Jugador");
			}else if (jugador.hasEntrenador()) {
				println(", Entrenador");
			}else if (jugador.hasMasajista()) {
				println(", Masajista");
			}
		}

		int eleccionSueldoNetoJugador= consoleInInt();

		if (eleccionSueldoNetoJugador > 1) {
			respuestaDelServidor= solicitudSueldoNetoJugador(eleccionSueldoNetoJugador);
		}
	}

	private void menuEditarSeleccion(){
		borrarTerminal();
		println("El presidente actual de la seleccion es " + seleccionAFA.getPresidente() + "\n\r" +
		"quiere modificarlo?(1:si, otro:no): ");
		int eleccionModificarPresidente= consoleInInt();

		if (eleccionModificarPresidente == 1) {
			seleccionAFA.setPresidente(consoleIn());
			println();
		}

		print("Quiere editar los integrantes?(1:si, otro,no): ");
		int eleccionEdicionIntegrantes= consoleInInt();

		if (eleccionEdicionIntegrantes == 1) {
			menuEditarIntegrantes();
		}
	}

	private void menuEditarIntegrantes() {
		borrarTerminal();
		println("Elija una opcion:\r\n" + //
							"1. Volver\r\n" +
							"2. Agregar integrante");
		for (int i = 0; i < seleccionAFA.getSeleccionadoCount(); i++) {
			IntegranteSeleccion_gRPC integrante= seleccionAFA.getSeleccionado(i);
			
			String nombre= integrante.getNombre();
			String apellido= integrante.getApellido();
			int hijos= integrante.getHijos();
			double sueldoBasico= integrante.getSueldoBasico();
			
			println((i+3)+ ". " + 
			nombre + " " + apellido +
							", tiene " + hijos +
							" con un sueldo basico de $" + sueldoBasico);

			print("Es ");

			if (integrante.hasJugador()) {
				println("Jugador" + 
				", juega como " + integrante.getJugador().getPosicionTactica() + 
				(integrante.getJugador().getPremio()?" y tiene un premio":""));

			}else if (integrante.hasEntrenador()) {
				println("Entrenador, y es de " + integrante.getEntrenador().getNacionalidad());

			}else if (integrante.hasMasajista()) {
				println("Masajista, con un titulo como " + integrante.getMasajista());
			}

			int eleccionEdicionIntegrante= 1;
			try {
				eleccionEdicionIntegrante= consoleInInt();
			} catch (Exception e) {
				println("numero no valido");
			}

			if (eleccionEdicionIntegrante == 2) {
				menuAgregarIntegrante();
			}else if (eleccionEdicionIntegrante > 2) {
				menuAgregarIntegrante();
			}else if (eleccionEdicionIntegrante > 2) {
				menuEditarIntegrante(eleccionEdicionIntegrante-2);
			}else{
				println("eleccion no valida");
				menuEditarIntegrantes();
			}
		}
	}
	
	public void menuCrearSeleccion() {
		borrarTerminal();
		SeleccionAFA_gRPC.Builder nuevaSeleccion= SeleccionAFA_gRPC.newBuilder();

		println("Cual es el apellido de su presidente?");

		nuevaSeleccion.setPresidente(consoleIn());
		println();

		seleccionAFA= nuevaSeleccion;

		boolean seguirAgregandoIntegrantes= true;
		while (seguirAgregandoIntegrantes) {
			borrarTerminal();
			println("Quiere agregar"+ (nuevaSeleccion.getSeleccionadoCount() == 0?" un":" otro") + " integrante? \r\n" + //
			"1. Si\r\n" + //
			"2. No");
			int eleccionAgregrarIntegrante= consoleInInt();
			if (eleccionAgregrarIntegrante == 1) {
				menuAgregarIntegrante();
			}else{
				seguirAgregandoIntegrantes= false;
			}
		}
		println();


		println("termino de ingresar los datos para su nueva seleccion, quiere enviarla al servidor?\r\n" + //
		"1. Si\r\n" + //
		"2. No");
		int eleccionEnviarSeleccion= consoleInInt();


		if (eleccionEnviarSeleccion == 1) {
			seleccionAFA= nuevaSeleccion;
			String respuestaServer= enviarSeleccion(nuevaSeleccion);
			println("respuesta del servidor" + respuestaServer);
		}else if (eleccionEnviarSeleccion == 2) {
			println("los datos quedaran guardados, puede borrarlos si quiere");
		}
	}

	private IntegranteSeleccion_gRPC.Builder menuCrearEditarIntegrante() {
		borrarTerminal();
		IntegranteSeleccion_gRPC.Builder nuevoIntegrante= IntegranteSeleccion_gRPC.newBuilder();
		
		print("Que nombre tiene?: ");
		nuevoIntegrante.setNombre(consoleIn());
		println();

		print("Que apellido tiene?: ");
		nuevoIntegrante.setApellido(consoleIn());
		println();

		print("Cuantos hijos tiene?: ");
		nuevoIntegrante.setHijos(Integer.parseInt(consoleIn()));
		println();

		print("Que sueldo basico tiene?: ");
		nuevoIntegrante.setSueldoBasico(consoleInDouble());
		println();

		println("Que tipo de integrante sera?\r\n" + //
				"1. Jugador\r\n" + //
				"2. Entrenador\r\n" + //
				"3. Masajista");

		int eleccionTipoDeIntegrante= consoleInInt();

		switch (eleccionTipoDeIntegrante) {
			case 1:
				Jugador_gRPC.Builder jugador= Jugador_gRPC.newBuilder();

				print("Que posicion tactica tiene?: ");
				jugador.setPosicionTactica(consoleIn());
				println();
				
				print("Tiene premio?(1:si, otro:no): ");
				try {
					jugador.setPremio((Integer.parseInt(consoleIn()) == 1?true:false));
				} catch (Exception e) {
					jugador.setPremio(false);
				}

				println();

				nuevoIntegrante.setJugador(jugador);
				break;
				
			case 2:
				Entrenador_gRPC.Builder entrenador= Entrenador_gRPC.newBuilder();
				
				print("De que nacionalida es?: ");
				entrenador.setNacionalidad(consoleIn());
				println();
				
				nuevoIntegrante.setEntrenador(entrenador);
				break;

			case 3:
				Masajista_gRPC.Builder masajista= Masajista_gRPC.newBuilder();

				print("Que titulacion tiene?: ");
				masajista.setTitulacion(consoleIn());
				println();

				nuevoIntegrante.setMasajista(masajista);
				break;

				default:
				break;
		}
	
		return nuevoIntegrante;
	}

	private void menuAgregarIntegrante(){
		seleccionAFA.addSeleccionado(menuCrearEditarIntegrante());
	}

	public void menuEditarIntegrante(int index) {
		seleccionAFA.setSeleccionado(index, menuCrearEditarIntegrante());
		
	}
	
    public MenuCliente(String host, int port) {
		super(host, port);
		ping(); // ping para verificar la coneccion con el servidor

		eraser = new ConsoleEraser();
		eraser.addIgnoreClass(System.class);
		eraser.start();
	}
	
	/******************************Fin de menus******************************/

	

	private String horaFormateada(){
		Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

		int hora= localDateTime.getHour();
		int minuto= localDateTime.getMinute();
		int segundo= localDateTime.getSecond();

		String horaConFormato= (hora >= 10? ""+hora : "0"+hora);
		String minutoConFormato= (minuto >= 10? ""+minuto:"0"+minuto);
		String segundoConFormato= (segundo >= 10 ? ""+segundo:"0"+segundo);

		return horaConFormato+":" + minutoConFormato + ":" + segundoConFormato;
	}

	//**************************Consola**************************//

	protected void println() {
		System.out.println();
	}

	protected void println(String mensaje) {
		System.out.println(mensaje);
	}

	protected void print(String mensaje) {
		System.out.print(mensaje);
	}

	protected String consoleIn(){
		return System.console().readLine();
	}

	protected int consoleInInt(){
		try {
			return Integer.parseInt(consoleIn());
		} catch (Exception e) {
			return 0;
		}
	}

	protected double consoleInDouble(){
		try {
			return Double.parseDouble(consoleIn());
		} catch (Exception e) {
			return 0;
		}
	}

    public static void borrarTerminal() {
		try {
				Thread.sleep(500);
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {}
	}
}