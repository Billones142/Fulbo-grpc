package fulbo.ucp;

import java.text.DecimalFormat;

import fulbo.ucp.interfaces.IintegranteSeleccion;

abstract class IntegranteSeleccion implements IintegranteSeleccion{
    public IntegranteSeleccion(String pNom, String pApe, int pH, double pSB) {
        super();
        setNombre(pNom);
        setApellido(pApe);
        setSueldoBasico(pSB);
        setHijos(pH);
    }

    public IntegranteSeleccion(String pNom, String pApe, double pSB) {
        super();
        setNombre(pNom);
        setApellido(pApe);
        setSueldoBasico(pSB);
        setHijos(0);
    }
    
    private String nombre;
    private String apellido;
    private int hijos;
    private double sueldoBasico;

    /*********************Inicio De Encapsulacion*********************/
    protected void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido() {
        return apellido;
    }

    protected void setHijos(int hijos) {
        this.hijos = hijos;
    }

    public int getHijos() {
        return hijos;
    }

    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    protected void setSueldoBasico(double sueldoBasico) {
        this.sueldoBasico = sueldoBasico;
    }

    public double getSueldoBasico() {
        return sueldoBasico;
    }
    /*********************Fin De Encapsulacion*********************/

    public abstract String rolEntrenamiento();

    protected double adicionalHijos(){
        return getHijos()*5000;
    }

    protected double aportes(){
        return getSueldoBasico() * 0.925;
    }

    public double sueldoNeto(){
        return adicionalHijos() + aportes();
    }

    protected String apeYnom(){
        return getApellido()+ ", " + getNombre();
    }

    public String mostrarDatos(){
        return apeYnom() + " - Sueldo Básico: $" + format(getSueldoBasico()) + (getHijos() > 0?" - Hijos: " + getHijos():"");
    }

    public String format(Double val) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###.00");
        return decimalFormat.format(val);
    }
}