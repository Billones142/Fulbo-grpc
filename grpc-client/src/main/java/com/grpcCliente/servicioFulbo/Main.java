package com.grpcCliente.servicioFulbo;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Cliente client = new Cliente("localhost", 6999);
        System.out.println("haciendo ping...");
        client.ping();
		System.console().readLine();
    }
}