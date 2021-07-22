package edu.fiuba.algo3.modelo;

import edu.fiuba.algo3.excepciones.JugadorNoPoseePaisException;

public class CartaPais {
    private final String nombrePais;
    private final String simbolo;
    private Pais pais;

    CartaPais(String nombrePais,String simbolo){
        this.nombrePais = nombrePais;
        this.simbolo = simbolo;
    }

    CartaPais(String nombrePais,String simbolo,Pais unPais){
        this.nombrePais = nombrePais;
        this.simbolo = simbolo;
        this.pais = unPais;
    }

    public void asignarPaisA(Jugador unJugador){
        this.pais.asignarJugador(unJugador);
    }

    public void setPais(Tablero tablero) {
        this.pais = tablero.getPais(this.nombrePais);
    }

    public Pais getPais() {
        return this.pais;
    }

    public String getNombrePais() {
        return this.nombrePais;
    }

    public String getSimbolo() {
        return this.simbolo;
    }

    public boolean esSimbolo(String simbolo) {
        return this.simbolo.equalsIgnoreCase(simbolo);
    }

    public void activarCarta(Jugador unJugador){
        try {
            this.pais.agregarFichas(2,unJugador);
        }catch (JugadorNoPoseePaisException e){}
    }
}
