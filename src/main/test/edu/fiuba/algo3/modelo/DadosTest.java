package edu.fiuba.algo3.modelo;

import edu.fiuba.algo3.excepciones.NoSePuedenCrearCeroDados;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class DadosTest {

    @Test
    public void dadosSeCreaConUnDado(){
        Dados dados = new Dados(1);
        assertEquals(1,dados.cantidadDados());
    }

    @Test
    public void dadosSeCreaConTresDados(){
        Dados dados = new Dados(3);
        assertEquals(3,dados.cantidadDados());
    }

    @Test
    public void crearDadosConCeroDadosTiraExcepcionNoSePuedenCrearCeroDados(){

        assertThrows(NoSePuedenCrearCeroDados.class, () -> new Dados(0));
    }

    @Test
    public void comparacionDeDadosAtacantePierdeTresFichas(){
        Dados dados1 = new Dados(3);
        Dados dados2 = Mockito.mock(Dados.class);

        when(dados2.obtenerDado(0)).thenReturn(6);
        when(dados2.obtenerDado(1)).thenReturn(6);
        when(dados2.obtenerDado(2)).thenReturn(6);
        when(dados2.cantidadDados()).thenReturn(3);

        int[] fichasPerdidads = dados1.comparadorDeDados(dados2);


        assertEquals(3,fichasPerdidads[0]);
        assertEquals(0,fichasPerdidads[1]);
    }

    @Test
    public void comparacionDeDadosDefensorPierdeTresFichas(){
        Dados dados1 = new Dados(3);
        Dados dados2 = Mockito.mock(Dados.class);

        when(dados2.obtenerDado(0)).thenReturn(0);
        when(dados2.obtenerDado(1)).thenReturn(0);
        when(dados2.obtenerDado(2)).thenReturn(0);
        when(dados2.cantidadDados()).thenReturn(3);

        int[] fichasPerdidads = dados1.comparadorDeDados(dados2);


        assertEquals(0,fichasPerdidads[0]);
        assertEquals(3,fichasPerdidads[1]);
    }

    @Test
    public void comparacionDeDadosSoloSeComparaUnDado(){
        Dados dados1 = new Dados(3);
        Dados dados2 = Mockito.mock(Dados.class);

        when(dados2.obtenerDado(0)).thenReturn(0);
        when(dados2.cantidadDados()).thenReturn(1);

        int[] fichasPerdidads = dados1.comparadorDeDados(dados2);


        assertEquals(0,fichasPerdidads[0]);
        assertEquals(1,fichasPerdidads[1]);
    }
}