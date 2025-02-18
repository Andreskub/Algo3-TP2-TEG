package edu.fiuba.algo3.modelo.cartas;


import edu.fiuba.algo3.modelo.Jugador;
import edu.fiuba.algo3.modelo.excepciones.MazoNoTieneSuficientesCartasException;

import java.util.*;

public class MazoDeCartasPais implements CartasPaisTeg,CartasPaisJugador{
    private final List<CartaPais> cartasPais;
    private Canje numeroDecanje = new PrimerosCanjes();

    public MazoDeCartasPais(List<CartaPais> cartaPais) {
        this.cartasPais = cartaPais;
    }

    public MazoDeCartasPais() {
        this.cartasPais = new ArrayList<>();
    }

    public int cantidadDeCartas() {
       return cartasPais.size();
    }

    public void agregarCartaPais(CartaPais cartaPais) {
        this.cartasPais.add(cartaPais);
    }

    public void agregarCartasPais(List<CartaPais> cartas) {
        this.cartasPais.addAll(cartas);
    }

    public void asignarPaises(List<Jugador> jugadores) {
        Collections.shuffle(this.cartasPais);
        ListIterator<CartaPais> cartas = this.cartasPais.listIterator();
        int i = 0;
        while (cartas.hasNext()) {
            cartas.next().asignarPaisA(jugadores.get(i % jugadores.size()));
            i++;
        }
    }


    private void pasarCartasCanje(List<CartaPais> cartas, CartasPaisTeg cartasPais){
        this.cartasPais.removeAll(cartas);
        cartasPais.agregarCartasPais(cartas);
    }


    private List<CartaPais> listaDecartasIguales(String simbolo){
        Iterator<CartaPais> iterator = this.cartasPais.listIterator();
        List<CartaPais> cartas = new ArrayList<>();

        while (iterator.hasNext() && cartas.size() < 3) {
            CartaPais carta = iterator.next();
            if (carta.esSimbolo(simbolo)) {
                cartas.add(carta);
            }
        }
        return cartas;
    }

    private boolean canjearTresCartasIgules(List<String> simbolos,CartasPaisTeg cartasPais){
        boolean pasoCanje = false;

        for(String simbolo: simbolos) {
            List<CartaPais> cartas = this.listaDecartasIguales(simbolo);
            if (cartas.size() > 2) {
                pasoCanje = true;
                pasarCartasCanje(cartas,cartasPais);
                break;
            }
        }
        return pasoCanje;
    }

    private boolean canjearTresCartasDistintas(List<String> simbolos,CartasPaisTeg cartasPais){
        List<CartaPais> cartas = new ArrayList<>();
        CartaPais carta;
        for(String simbolo: simbolos) {
            carta = this.cartasPais.stream().filter(cartaPais -> cartaPais.esSimbolo(simbolo))
                    .findFirst().orElse(null);

            if(carta != null){
                cartas.add(carta);
                this.cartasPais.remove(carta);
            }
        }

        this.cartasPais.addAll(cartas);

        if(cartas.size() > 2){
            pasarCartasCanje(cartas,cartasPais);
            return true;
        }
        return false;
    }

    public void activarCartas(Jugador unJugador){
        for(CartaPais carta: this.cartasPais){
            carta.activarCarta(unJugador);
        }
    }

    public boolean canjeDeCartas(Jugador unJugador,CartasPaisTeg cartasPais){
        if(this.cartasPais.size() < 3){
            return false;
        }
        List<String> simbolos = List.of("Globo","Barco","Cañon");

        if (canjearTresCartasIgules(simbolos,cartasPais) || canjearTresCartasDistintas(simbolos,cartasPais)){
            this.numeroDecanje = this.numeroDecanje.hacerCanje(unJugador);
            return true;
        }
        return false;
    }

    public boolean darCartaA(Jugador jugador) {
        if(cartasPais.isEmpty()){
            throw new MazoNoTieneSuficientesCartasException();
        }

        if(jugador.darCartaPais(cartasPais.get(0))) {
            cartasPais.remove(0);
            return true;
        }
        return false;
    }

    public List<String> getCartas(){
        List<String> listaNombresCartas = new ArrayList<>();
        for(CartaPais cartaPais: cartasPais){
            listaNombresCartas.add(cartaPais.getNombrePais()+" - "+cartaPais.getSimbolo());
        }
        return listaNombresCartas;
    }
}
