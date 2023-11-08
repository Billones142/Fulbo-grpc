package fulbo.ucp;

public class Masajista extends IntegranteSeleccion{
    public Masajista(String pNom, String pApe, int pH, double pSB) {
        super(pNom, pApe, pH, pSB);
    }

    public Masajista(String pNom, String pApe, double pSB) {
        super(pNom, pApe, pSB);
    }
    
    String titulacion;

    /********************Comienzo encapsulacion********************/
    public void setTitulacion(String titulo) {
        this.titulacion = titulo;
    }

    public String getTitulacion() {
        return titulacion;
    }
    /**********************Fin encapsulacion**********************/

    public String rolEntrenamiento() {
        return "asistir en salud";
    }

    public String mostrarDatos() {
        return apeYnom() + " - Sueldo Básico: $" + getSueldoBasico() + (getHijos() > 0?" - Hijos: " + getHijos():"")+
        " - Masajista - Titulación: " + getTitulacion() + "\r\n";
    }
}