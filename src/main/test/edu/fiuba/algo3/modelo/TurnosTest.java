package edu.fiuba.algo3.modelo;

import edu.fiuba.algo3.modelo.cartas.MazoDeCartasPais;
import edu.fiuba.algo3.modelo.tablero.JugadorNoTieneSuficientesFichasException;
import edu.fiuba.algo3.modelo.ataque.Dados;
import edu.fiuba.algo3.modelo.objetivos.ObjetivoTeg;
import edu.fiuba.algo3.modelo.tablero.Continente;
import edu.fiuba.algo3.modelo.tablero.Pais;
import edu.fiuba.algo3.modelo.tablero.Tablero;
import edu.fiuba.algo3.modelo.turnos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TurnosTest {

    ArrayList<Pais> paisesOceania= new ArrayList<>();
    ArrayList<Pais> paises= new ArrayList<>();
    ArrayList<Pais> paisesAsia= new ArrayList<>();
    ArrayList<Continente> continentes= new ArrayList<>();
    HashMap<String, Jugador> jugadores = new HashMap<>();
    Tablero tablero;
    ObjetivoTeg objetivoGana ;
    ObjetivoTeg objetivoPierde ;

    @BeforeEach
    void setUp() {
        paisesOceania.add(new Pais("Borneo", List.of("Australia","Malasia")));
        paisesOceania.add(new Pais("Sumatra", List.of("Australia","India")));
        paisesOceania.add(new Pais("Australia", List.of("Chile","Sumatra","Java","Borneo")));
        paisesOceania.add(new Pais("Java", List.of("Australia")));
        paisesAsia.add(new Pais("China", List.of("Australia","Malasia")));
        paisesAsia.add(new Pais("Japon", List.of("Australia","India")));
        paisesAsia.add(new Pais("Rusia", List.of("Chile","Sumatra","Java","Borneo")));
        continentes.add(new Continente("Oceania",2,paisesOceania));
        continentes.add(new Continente("Asia",2,paisesAsia));
        jugadores.put("Amarillo",new Jugador("Amarillo"));
        jugadores.put("Rojo",new Jugador("Rojo"));
        paises.addAll(paisesAsia);
        paises.addAll(paisesOceania);
        objetivoGana = Mockito.mock(ObjetivoTeg.class);
        objetivoPierde = Mockito.mock(ObjetivoTeg.class);
        when(objetivoGana.cumplioObjetivo(any(Teg.class))).thenReturn(true);
        when(objetivoPierde.cumplioObjetivo(any(Teg.class))).thenReturn(false);
    }

    @Test
    public void primeraRondaInicialCadaJugadorTieneCincoFichasParaPoner(){
        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));

        paisesAsia.get(0).asignarJugador(jugadores.get("Amarillo"));

        //La cantidad de fichas se calcula la primera vez que trata de poner fichas
        turnos.colocarEjercitos("China",0);
        assertEquals(5,jugadores.get("Amarillo").sacarFichas(0));
    }



    @Test
    public void segundaRondaInicialCadaJugadorTieneCincoFichasParaPoner(){
        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));


        paisesAsia.get(0).asignarJugador(jugadores.get("Amarillo"));
        paisesAsia.get(1).asignarJugador(jugadores.get("Rojo"));


        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("Japon",5);
        turnos.finEtapa();

        //La cantidad de fichas se calcula la primera vez que trata de poner fichas
        turnos.colocarEjercitos("China",0);
        assertEquals(3,jugadores.get("Amarillo").sacarFichas(0));
    }

    @Test
    public void jugadorNoPuedePonerSieteFichasEnLaPrimeraRondaInicial(){
        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));


        paisesAsia.get(0).asignarJugador(jugadores.get("Amarillo"));
        paisesAsia.get(1).asignarJugador(jugadores.get("Rojo"));


        assertThrows(JugadorNoTieneSuficientesFichasException.class,()-> turnos.colocarEjercitos("China",7));

    }

    @Test
    public void jugadorNoPuedePonerCuatrFichasEnLaSegundaRondaInicial(){
        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));

        paisesAsia.get(0).asignarJugador(jugadores.get("Amarillo"));
        paisesAsia.get(1).asignarJugador(jugadores.get("Rojo"));
        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("Japon",5);
        turnos.finEtapa();

        assertThrows(JugadorNoTieneSuficientesFichasException.class,()-> turnos.colocarEjercitos("China",4));
        assertThrows(JugadorSigueTeniendoFichasException.class, turnos::finEtapa);


    }

    @Test
    public void NoSePuedeComenzarJuegoCOn(){

        Turnos turnos = new Turnos();
        turnos.agregarJugador("Amarillo");
        assertThrows(NoHaySuficientesJugadoresException.class, turnos::comenzarJuego);
    }

    @Test
    public void NoSePuedeAgregarMasDeSeisJugadores(){

        Turnos turnos = new Turnos();
        turnos.agregarJugador("Amarillo");
        turnos.agregarJugador("Rojo");
        turnos.agregarJugador("Verde");
        turnos.agregarJugador("Magenta");
        turnos.agregarJugador("Negro");
        turnos.agregarJugador("Azul");
        assertThrows(LimiteDeJugadoresException.class, ()->  turnos.agregarJugador("Celeste"));
    }

    @Test
    public void PrimerasDosROndasEntreDosJugadoresPuedenColocarSusOchoFichasIniciales(){
        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));
        for (Pais pais : paisesAsia){
            pais.asignarJugador(jugadores.get("Amarillo"));
        }

        for (Pais pais : paisesOceania){
            pais.asignarJugador(jugadores.get("Rojo"));
        }

        assertEquals("Amarillo",turnos.getJugadorActual());
        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();

        assertEquals("Rojo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Borneo",5);
        turnos.finEtapa();

        assertEquals("Amarillo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Rusia",3);
        turnos.finEtapa();

        assertEquals("Rojo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Australia",3);
        turnos.finEtapa();


        assertTrue(turnos.devolverRondaActual() instanceof RondaAtaque);
        assertEquals(6,paisesAsia.get(0).perderFichas(0));
        assertEquals(4,paisesAsia.get(2).perderFichas(0));
        assertEquals(6,paisesOceania.get(0).perderFichas(0));
        assertEquals(4,paisesOceania.get(2).perderFichas(0));
    }

    @Test
    public void ColocarEjercitos2Paises3jugadores(){
        jugadores.put("Verde",new Jugador("Verde"));
        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo","Verde"));

        for (Pais pais : paisesAsia){
            pais.asignarJugador(jugadores.get("Rojo"));
        }

        (paisesOceania.get(0)).asignarJugador(jugadores.get("Amarillo"));
        (paisesOceania.get(1)).asignarJugador(jugadores.get("Amarillo"));
        (paisesOceania.get(2)).asignarJugador(jugadores.get("Verde"));
        (paisesOceania.get(3)).asignarJugador(jugadores.get("Verde"));

        turnos.colocarEjercitos("Borneo",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("Australia",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("Borneo",3);
        turnos.finEtapa();
        turnos.colocarEjercitos("Rusia",3);
        turnos.finEtapa();
        turnos.colocarEjercitos("Australia",3);
        turnos.finEtapa();

        assertTrue(turnos.devolverRondaActual() instanceof RondaAtaque);
        assertTrue(continentes.get(1).esDeJugador(jugadores.get("Rojo")));
    }

    @Test
    public void ColocarEjercitos2Paises2jugadoresAtque(){
        Pais paisAtacante = Mockito.mock(Pais.class);
        paises.set(5,paisAtacante);

        Dados dados = Mockito.mock(Dados.class);
        int[] conjunto = {0,3};
        when(dados.compararDados(any(Dados.class))).thenReturn(conjunto);
        when(paisAtacante.getNombre()).thenReturn("Australia");
        when(paisAtacante.tirarDados(3)).thenReturn(dados);
        when(paisAtacante.getJugador()).thenReturn(jugadores.get("Amarillo"));
        when(paisAtacante.esDeJugador(jugadores.get("Amarillo"))).thenReturn(true);
        when(paisAtacante.esAdyacente(paisesOceania.get(0))).thenReturn(true);
        when(paisAtacante.esAdyacente(paisesOceania.get(3))).thenReturn(true);

        tablero = new Tablero(continentes,paises);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));

        paisAtacante.asignarJugador(jugadores.get("Amarillo"));
        (paisesOceania.get(1)).asignarJugador(jugadores.get("Amarillo"));
        (paisesAsia.get(0)).asignarJugador(jugadores.get("Rojo"));
        (paisesOceania.get(0)).asignarJugador(jugadores.get("Rojo"));
        (paisesOceania.get(3)).asignarJugador(jugadores.get("Rojo"));

        turnos.colocarEjercitos("Sumatra",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();
        turnos.colocarEjercitos("Sumatra",3);
        turnos.finEtapa();
        turnos.colocarEjercitos("China",3);
        turnos.finEtapa();
        jugadores.get("Amarillo").agregarFichas(2);
        turnos.atacar("Australia","Borneo",3);
        turnos.atacar("Australia","Java",3);
        assertTrue((tablero.getPais("Borneo")).esDeJugador(jugadores.get("Amarillo")));
        assertTrue((tablero.getPais("Java")).esDeJugador(jugadores.get("Amarillo")));
    }

    @Test
    public void reagruparDespuesDeAtacar(){
        tablero = new Tablero(continentes,paises);
        jugadores.get("Amarillo").darObjetivo(objetivoPierde);
        jugadores.get("Rojo").darObjetivo(objetivoPierde);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));
        for (Pais pais : paisesAsia){
            pais.asignarJugador(jugadores.get("Amarillo"));
        }

        for (Pais pais : paisesOceania){
            pais.asignarJugador(jugadores.get("Rojo"));
        }

        assertEquals("Amarillo",turnos.getJugadorActual());
        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();

        assertEquals("Rojo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Borneo",5);
        turnos.finEtapa();

        assertEquals("Amarillo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Rusia",3);
        turnos.finEtapa();

        assertEquals("Rojo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Australia",3);
        turnos.finEtapa();


        assertTrue(turnos.devolverRondaActual() instanceof RondaAtaque);
        turnos.finEtapa();
        assertTrue(turnos.devolverRondaActual() instanceof RondaReagrupacion);
        turnos.finEtapa();
        assertTrue(turnos.devolverRondaActual() instanceof RondaAtaque);
        assertEquals("Rojo",turnos.getJugadorActual());

        assertTrue(turnos.devolverRondaActual() instanceof RondaAtaque);
        turnos.finEtapa();
        assertTrue(turnos.devolverRondaActual() instanceof RondaReagrupacion);
        turnos.finEtapa();
        assertTrue(turnos.devolverRondaActual() instanceof RondaColocacion);
    }


    @Test
    public void enRondaReagrupacionSePuedenPasarFichasCorrectamente(){
        tablero = new Tablero(continentes,paises);
        jugadores.get("Amarillo").darObjetivo(objetivoPierde);
        jugadores.get("Rojo").darObjetivo(objetivoPierde);
        Teg teg = new Teg(tablero,jugadores,new MazoDeCartasPais());
        Turnos turnos = new Turnos(teg,List.of("Amarillo","Rojo"));
        for (Pais pais : paisesAsia){
            pais.asignarJugador(jugadores.get("Rojo"));
        }

        for (Pais pais : paisesOceania){
            pais.asignarJugador(jugadores.get("Amarillo"));
        }

        assertEquals("Amarillo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Borneo",5);
        turnos.finEtapa();

        assertEquals("Rojo",turnos.getJugadorActual());
        turnos.colocarEjercitos("China",5);
        turnos.finEtapa();

        assertEquals("Amarillo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Australia",3);
        turnos.finEtapa();

        assertEquals("Rojo",turnos.getJugadorActual());
        turnos.colocarEjercitos("Rusia",3);
        turnos.finEtapa();


        assertTrue(turnos.devolverRondaActual() instanceof RondaAtaque);
        turnos.finEtapa();
        assertTrue(turnos.devolverRondaActual() instanceof RondaReagrupacion);
        turnos.pasarFichas("Borneo","Australia",3);
        assertEquals(3,paisesOceania.get(0).perderFichas(0));
        assertEquals(7,paisesOceania.get(2).perderFichas(0));
    }
}
