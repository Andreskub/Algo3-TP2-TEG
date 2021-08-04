package edu.fiuba.algo3.controller;

import edu.fiuba.algo3.modelo.Turnos;
import edu.fiuba.algo3.vistas.CargadorDeEscena;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GetCartaController implements Initializable {
    @FXML
    public Label carta;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carta.setText("Carta pais");
    }

    public void volverAlTablero(){
        CargadorDeEscena.cargarEscena("/vistas/tablero.fxml");
    }
}
