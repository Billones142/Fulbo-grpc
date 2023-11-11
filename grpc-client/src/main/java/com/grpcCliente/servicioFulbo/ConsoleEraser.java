package com.grpcCliente.servicioFulbo;

import java.util.ArrayList;
import java.util.List;

public class ConsoleEraser extends Thread {

    private List<Class<?>> ignoreList = new ArrayList<>();

    public void addIgnoreClass(Class<?> classToIgnore) {
        ignoreList.add(classToIgnore);
    }

    public boolean isClassIgnored(Class<?> classToCheck) {
        return ignoreList.contains(classToCheck);
    }

    @Override
    public void run() {
        while (true) {
            // Check the console for new output
            int consola;

            try {
                consola= System.in.available();
            } catch (Exception e) {
                consola= 0;
            }

            if (consola > 0) {
                // Get the class that wrote the output
                Class<?> callingClass = Thread.currentThread().getStackTrace()[2].getClass();

                // Check if the class is in the ignore list
                if (!isClassIgnored(callingClass)) {
                    // Erase the console
                    borrarTerminal();
                }
            }

            // Wait for a short time before checking the console again
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Ignore the interruption
            }
        }
    }


    public void borrarTerminal() {
		try {
				Thread.sleep(500);
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {}
	}
}