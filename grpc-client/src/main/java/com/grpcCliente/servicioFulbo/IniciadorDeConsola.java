package com.grpcCliente.servicioFulbo;

import java.io.File;

@Deprecated
public class IniciadorDeConsola {
    public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");
        String jarFileName = null;
        String claseMain= "com.grpcCliente.servicioFulbo";

        for (String pathEntry : classpath.split(File.pathSeparator)) {
            if (pathEntry.endsWith(".jar")) {
                jarFileName = pathEntry;
                break;
            }
        }

        
        String comando= "java -jar "+ jarFileName + claseMain;

        try {
            new ProcessBuilder(comando).inheritIO().start().waitFor();
        } catch (Exception e) {}
    }
}
