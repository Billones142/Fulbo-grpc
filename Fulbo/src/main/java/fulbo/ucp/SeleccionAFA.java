package fulbo.ucp;

import java.util.ArrayList;

public class SeleccionAFA {
    public SeleccionAFA(String pPresidente) {
        super();
        setPresidente(pPresidente);
    }

    private ArrayList<IntegranteSeleccion> seleccionado= new ArrayList<>();
    private String presidente;

    /********************Comienzo encapsulacion********************/
    private void setPresidente(String presidente) {
        this.presidente = presidente;
    }

    public String getPresidente() {
        return presidente;
    }

    public void setSeleccionado(ArrayList<IntegranteSeleccion> seleccionado) {
        this.seleccionado = seleccionado;
    }

    public ArrayList<IntegranteSeleccion> getSeleccionado() {
        return seleccionado;
    }
    /**********************Fin encapsulacion**********************/

    public void agregarIntegrante(IntegranteSeleccion pIntegrante) {
        getSeleccionado().add(pIntegrante);
    }
    
    public void quitarIntegrante(IntegranteSeleccion pIntegrante) {
        for (int i = 0; i < getSeleccionado().size(); i++) {
            if (getSeleccionado().get(i) == pIntegrante) {
                getSeleccionado().remove(i);
                break;
            }
        }
    }

    public String liquidarSueldos() {
        StringBuilder sueldosCompletos= new StringBuilder("");
        double montoTotal= 0;

        sueldosCompletos.append("--------------------------------------------------------------------------------------\r\n" + //
                            "Resumen de sueldos a pagar:\r\n" + //
                            "--------------------------------------------------------------------------------------\r\n");

        for (int i = 0; i < getSeleccionado().size(); i++) {
            IntegranteSeleccion integrante= getSeleccionado().get(i);

            sueldosCompletos.append(integrante.mostrarDatos());

            sueldosCompletos.append("Sueldo Neto: $" + Format.format(integrante.sueldoNeto()) + "\r\n");
            montoTotal+= integrante.sueldoNeto();
        }

        sueldosCompletos.append("--------------------------------------------------------------------------------------\r\n" +
                            "Monto total a pagar en concepto de sueldos: $ " + Format.format(montoTotal) + "\r\n" +
                            "--------------------------------------------------------------------------------------");

        return sueldosCompletos.toString();
    }

    private int encontrarApellido(String apellido) {
        int index= -1;
        for (int i = 0; i < getSeleccionado().size(); i++) {
            if (getSeleccionado().get(i).getApellido().contains(apellido)) {
                index= i;
            }
        }
        return index;
    }
    
    public String conocerRol(String pApellido) {
        int indiceDeJugador= encontrarApellido(pApellido); 
        if (indiceDeJugador == -1) {
            return "El nombre ingresado no pertenece a un integrante de la selección.";
        }else{
            IntegranteSeleccion integrante= getSeleccionado().get(indiceDeJugador);
            // “Integrante: Apellido, Nombre - Rol que cumple: rol.”
            return "Integrante: " + integrante.getApellido() + ", " + 
            integrante.getNombre() + " - Rol que cumple: " + integrante.rolEntrenamiento() + ".";
        }
    }

    public String mostrarNomina() {
        StringBuilder nominaCompleta= new StringBuilder();

        nominaCompleta.append("Asociación de Futbol Argentina - AFA\r\n" + //
                "Presidente: Tapia, Claudio\r\n" + //
                "Integrantes del Seleccionado Argentino de Futbol:\r\n" + //
                "-------------------------------------------------------------------\r\n");

        for (int i = 0; i < getSeleccionado().size(); i++) {
            nominaCompleta.append(getSeleccionado().get(i).mostrarDatos() + "\r\n");
        }

        nominaCompleta.append("-------------------------------------------------------------------");

        return nominaCompleta.toString();
    }
}