package com.grpcCliente.servicioFulbo;

public class MainCliente {

    public static void main(String[] args) {
        String host= "localhost";
        int port= 6999;

        System.out.println("Usar ip y puerto por defecto?(1: no, otro: si)");
        int eleccionIp= 0;
        try {
            eleccionIp= Integer.parseInt(System.console().readLine());
        } catch (Exception e) {}

        if (eleccionIp == 1) {
            System.out.print("ip: ");
            host= System.console().readLine();
            System.out.print("puerto: ");
            port= Integer.parseInt(System.console().readLine());
            System.out.println();
        }

        boolean seguirEjecutando= true;
        while (seguirEjecutando) {
            try {
                Cliente client = new Cliente(host, port);
                seguirEjecutando= client.menuPrincipal();
            } catch (Exception e) {
                System.out.print("no se pudo establecer la conexion con el servidor\r\n" + //
                "reintentado en 3 segundos");
                try {
                    for (int i = 0; i < 3; i++) {
                        System.out.print(".");
                        Thread.sleep(1000);
                    }
                    } catch (InterruptedException a) {
                    Thread.currentThread().interrupt();
                    }
                    System.out.println();
            }
        }
    }
}