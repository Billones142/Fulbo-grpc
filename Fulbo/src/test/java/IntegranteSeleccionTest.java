
import static org.junit.Assert.*;

import org.junit.Test;

import fulbo.ucp.*;

public class IntegranteSeleccionTest {
    @Test
    public void sueldo_JugadorTest() {
        Jugador messi= new Jugador("Leo", "Messsi", 3, 200000, "Delantero", true);

        assertEquals(260000, (int)messi.sueldoNeto());
    }

    @Test
    public void sueldo_EntrenadorTest() {
        Entrenador entrenador= new Entrenador("stiven", "monsalvo", 2, 150000, "Argentina");

        assertEquals((int)((150000 * 0.925)+(2*5000)), (int)entrenador.sueldoNeto());
    }

    @Test
    public void sueldo_MasajistaTest() {
        Masajista masajista= new Masajista("Luciano", "Torreon", 4, 50000);

        assertEquals((int)((50000*0.925)+(4*5000)), (int)masajista.sueldoNeto());
    }
}