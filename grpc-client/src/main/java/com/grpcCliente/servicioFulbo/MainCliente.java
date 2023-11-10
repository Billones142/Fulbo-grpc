package com.grpcCliente.servicioFulbo;

public class MainCliente {

    public static void main(String[] args) {
        while (true) {
            try {
                Cliente client = new Cliente("localhost", 6999);
                client.menuPrincipal();
            } catch (Exception e) {
                System.out.print("no se pudo establecer la coneccion con el servidor\r\n" + //
                "reintentado en 3 segundos");
                try {
                    for (int i = 0; i < 3; i++) {
                        System.out.print(".");
                        Thread.sleep(1000);
                    }
                    } catch (InterruptedException a) {
                    Thread.currentThread().interrupt();
                    }
                    System.out.println("\r\n");
            }
        }
    }
}