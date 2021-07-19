package edu.fiuba.algo3.modelo;

import edu.fiuba.algo3.excepciones.JugadorNoPoseePaisException;
import edu.fiuba.algo3.excepciones.PaisNoEsLimitrofe;

import java.util.List;

public class Pais {
    private String nombre;
    private Ejercito ejercito;
    private Jugador jugador;
    private List<String> paisesLimitrofes;

    public Pais(String nombrePais, List<String>paisesLimitrofes){
        this.ejercito = new Ejercito();
        this.nombre = nombrePais;
        this.paisesLimitrofes = paisesLimitrofes;
        this.jugador = null;
    }

    public String getNombre() {return this.nombre;}
    public Jugador getJugador(){
        return this.jugador;
    }

    public boolean esAdyacente(Pais unPais){
        //Puede dar errores si los limitrofes o nombres tienen mal los cases
        return this.paisesLimitrofes.contains(unPais.getNombre());
    }

    public void agregarFichas(int cantidadFichas, Jugador unJugador) throws JugadorNoPoseePaisException {
        if(this.jugador == null) { this.jugador = unJugador; }
        if( jugador.devolverFichas()== 0){return;}

        if (this.esDeJugador(unJugador)) {
            this.ejercito.agregarFichas(cantidadFichas);
        } else {
            throw new JugadorNoPoseePaisException();
        }
    }

    /*
    * Le resta cantidadFichas a las fichas del ejercito
    * Devuelve la cantidad de fichas restantes.
    * */
    public int perderFichas(int cantidadFichas){
        return this.ejercito.perderFichas(cantidadFichas);
    }

    /*
    * Cambia el dueño del pais actual con el de unPais
    * y le pide que le pase una ficha
    */
    private void actualizarDuenio(Pais unPais){
        this.jugador = unPais.getJugador();

        unPais.pasarFichasA(this,1);
    }

    /*
    * Resta fichas al ejercito del pais, si el ejercito queda con 0 fichas
    * el pais se actualiza de dueño con el de pais atacante,se le pasa una ficha
    * y devuelve true
    * En caso contrario(no se conquistoel pais), devuelve false
    */
    public boolean perderFichas(int cantidadFichas, Pais paisAtacante){
        if(this.ejercito.perderFichas(cantidadFichas) > 0){
            return false;
        }
        this.actualizarDuenio(paisAtacante);
        return true;
    }


    public void pasarFichasA(Pais unPais, int cantidadFichas){
        if(!this.esAdyacente(unPais)){
            throw new PaisNoEsLimitrofe();
        }

        this.ejercito.pasarFichasADe(unPais,this.jugador,cantidadFichas);
    }

    public boolean esDeJugador(Jugador jugador) {
        return jugador.esElMismoJugador(this.jugador);
    }

    /*
    * Tira la misma cantidad de dados que fichas en el pais, hasta 3
    * Devuelve el conjunto de Dados
    */
    public Dados tirarDados(){
        return this.ejercito.tirarDados();
    }

    /*
    * Tira la uno dado menos que fichas en el pais, hasta 3
    * Devuelve el conjunto de Dados
    */
    public Dados tirarDados(int cantidadDeDados){
        return this.ejercito.tirarDados(cantidadDeDados);
    }

}
