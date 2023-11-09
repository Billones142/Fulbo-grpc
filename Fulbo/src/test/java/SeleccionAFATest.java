import static org.junit.Assert.*;

import org.junit.Test;

//import fulbo.ucp.Entrenador;
import fulbo.ucp.Jugador;
//import fulbo.ucp.Masajista;
import fulbo.ucp.SeleccionAFA;


public class SeleccionAFATest {
    @Test
    public void liquidarSueldos_JugadorConPremioTest() {
        SeleccionAFA seleccionAFA= new SeleccionAFA("Escaloni");
        Jugador messi= new Jugador("Lionel", "Messi", 3, 200000, "Delantero", true);
        seleccionAFA.agregarIntegrante(messi);

        String nomina= "--------------------------------------------------------------------------------------\r\n" + //
                "Resumen de sueldos a pagar:\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Messi, Lionel - Sueldo Básico: $200.000,00 - Hijos: 3 - Jugador - Delantero (Premio habilitado)\r\n" + //
                "Sueldo Neto: $260.000,00\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Monto total a pagar en concepto de sueldos: $ 260.000,00\r\n" + //
                "--------------------------------------------------------------------------------------";

        String nominaCreada= seleccionAFA.liquidarSueldos();

        assertEquals(nomina, nominaCreada);
    }

    /*@Test
    public void liquidarSueldos_JugadorSinPremioTest() {
        SeleccionAFA seleccionAFA= new SeleccionAFA("Escaloni");
        Jugador dybala= new Jugador("Paulo", "Dybala", 80000, "Delantero", false);
        seleccionAFA.agregarIntegrante(dybala);

        String nomina= "--------------------------------------------------------------------------------------\r\n" + //
                "Resumen de sueldos a pagar:\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Dybala, Paulo - Sueldo Básico: $80000.0 - Jugador - Delantero\r\n" + //
                "Sueldo Neto: $74000.0\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Monto total a pagar en concepto de sueldos: $ 74000.0\r\n" + //
                "--------------------------------------------------------------------------------------";

        String nominaCreada= seleccionAFA.liquidarSueldos();

        assertEquals(nomina, nominaCreada);
    }*/

    /*@Test
    public void liquidarSueldos_EntrenadorTest() {
        SeleccionAFA seleccionAFA= new SeleccionAFA("Scaloni");
        Entrenador scaloni= new Entrenador("Lionel Sebastián", "Scaloni", 2, 150000, "Argentina");

        seleccionAFA.agregarIntegrante(scaloni);

        String nomina= "--------------------------------------------------------------------------------------\r\n" + //
                "Resumen de sueldos a pagar:\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Scaloni, Lionel Sebastián - Sueldo Básico: $150000.0 - Hijos: 2 - Entrenador - Nacionalidad: Argentina\r\n" + //
                "Sueldo Neto: $148750.0\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Monto total a pagar en concepto de sueldos: $ 148750.0\r\n" + //
                "--------------------------------------------------------------------------------------";

        String nominaCreada= seleccionAFA.liquidarSueldos();

        assertEquals(nomina, nominaCreada);
    }

    @Test
    public void liquidarSueldos_oMasajistaTest() {
        SeleccionAFA seleccionAFA= new SeleccionAFA("Escaloni");
        Masajista masajista= new Masajista("Roberto" ,"Rodriguez", 50000);
        masajista.setTitulacion("Fisioterapeuta");

        seleccionAFA.agregarIntegrante(masajista);

        String nomina= "--------------------------------------------------------------------------------------\r\n" + //
                "Resumen de sueldos a pagar:\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Rodriguez, Roberto - Sueldo Básico: $50000.0 - Masajista - Titulación: Fisioterapeuta\r\n" + //
                "Sueldo Neto: $46250.0\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Monto total a pagar en concepto de sueldos: $ 46250.0\r\n" + //
                "--------------------------------------------------------------------------------------";

        String nominaCreada= seleccionAFA.liquidarSueldos();

        assertEquals(nomina, nominaCreada);
    }

    @Test
    public void liquidarSueldos_FinalTest() {
        SeleccionAFA seleccionAFA= new SeleccionAFA("Scaloni");

        Entrenador scaloni= new Entrenador("Lionel Sebastián", "Scaloni", 2, 150000, "Argentina");
        Jugador messi= new Jugador("Lionel", "Messi", 3, 200000, "Delantero", true);
        Jugador dybala= new Jugador("Paulo", "Dybala", 80000, "Delantero", false);
        Masajista masajista= new Masajista("Roberto" ,"Rodriguez", 50000);
        masajista.setTitulacion("Fisioterapeuta");

        seleccionAFA.agregarIntegrante(scaloni);
        seleccionAFA.agregarIntegrante(messi);
        seleccionAFA.agregarIntegrante(dybala);
        seleccionAFA.agregarIntegrante(masajista);

        String nomina= "--------------------------------------------------------------------------------------\r\n" + //
                "Resumen de sueldos a pagar:\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Scaloni, Lionel Sebastián - Sueldo Básico: $150000.0 - Hijos: 2 - Entrenador - Nacionalidad: Argentina\r\n" + //
                "Sueldo Neto: $148750.0\r\n" + //
                "Messi, Lionel - Sueldo Básico: $200000.0 - Hijos: 3 - Jugador - Delantero (Premio habilitado)\r\n" + //
                "Sueldo Neto: $260000.0\r\n" + //
                "Dybala, Paulo - Sueldo Básico: $80000.0 - Jugador - Delantero\r\n" + //
                "Sueldo Neto: $74000.0\r\n" + //
                "Rodriguez, Roberto - Sueldo Básico: $50000.0 - Masajista - Titulación: Fisioterapeuta\r\n" + //
                "Sueldo Neto: $46250.0\r\n" + //
                "--------------------------------------------------------------------------------------\r\n" + //
                "Monto total a pagar en concepto de sueldos: $ 529000.0\r\n" + //
                "--------------------------------------------------------------------------------------";

        String nominaCreada= seleccionAFA.liquidarSueldos();

        assertEquals(nomina, nominaCreada);
    }*/
}