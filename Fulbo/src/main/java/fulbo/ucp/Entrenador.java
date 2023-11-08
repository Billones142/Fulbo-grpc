package fulbo.ucp;

public class Entrenador 
    extends IntegranteSeleccion{
    public Entrenador( String pNom, String pApe, int pH, double pSB, String pNacio){
        super(pNom, pApe, pH, pSB);
        setNacionalidad(pNacio);
    }

    public Entrenador( String pNom, String pApe, double pSB, String pNacio){
        super(pNom,pApe,pSB);
        setNacionalidad(pNacio);
    }

    private String nacionalidad;

    /*********************Inicio De Encapsulacion*********************/
    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /*********************Fin De Encapsulacion*********************/

    @Override
    public double sueldoNeto(){
        return aportes() + adicionalHijos() + (!getNacionalidad().contains("Argentina")?10000:0);
    }

    public String rolEntrenamiento(){
        return "dirigir";
    }

    @Override
    public String mostrarDatos(){
        return apeYnom() + " - Sueldo Básico: $" + Format.format(getSueldoBasico()) + (getHijos() > 0?" - Hijos: " + getHijos():"")+
        " - Entrenador - Nacionalidad: " + getNacionalidad() + "\r\n";
    }
}