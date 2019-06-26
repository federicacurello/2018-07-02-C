/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="compagnieMinimo"
    private TextField compagnieMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoDestinazione"
    private ComboBox<Airport> cmbBoxAeroportoDestinazione; // Value injected by FXMLLoader

    @FXML // fx:id="numeroTratteTxtInput"
    private TextField numeroTratteTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	txtResult.clear();
    	try {
    		int compagnie= Integer.parseInt(compagnieMinimo.getText());
    		if(compagnie<1) {
    			txtResult.setText("Inserire un numero intero maggiore di 0!");
    			return;
    		}
    		 model.creaGrafo(compagnie);
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero maggiore di 0!");
    		return;
    	}
    	setCmbBoxAeroportoPartenza();
   
    }

    private void setCmbBoxAeroportoPartenza() {
    	cmbBoxAeroportoPartenza.getItems().addAll(model.getGrafo().vertexSet());
		
	}

	@FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
		txtResult.clear();
		if(cmbBoxAeroportoPartenza.getValue()==null) {
			txtResult.setText("Selezionare un aereoporto di partenza!");
			return;
		}
		Airport scelto= cmbBoxAeroportoPartenza.getValue();
		txtResult.appendText(model.getConnessi(scelto));
		setCmbBoxAeroportoDestinazione();
    }

    private void setCmbBoxAeroportoDestinazione() {
    	cmbBoxAeroportoDestinazione.getItems().addAll(model.getGrafo().vertexSet());
    	cmbBoxAeroportoDestinazione.getItems().remove(cmbBoxAeroportoPartenza.getValue());
		
	}

	@FXML
    void doCercaItinerario(ActionEvent event) {
		txtResult.clear();
		if(cmbBoxAeroportoDestinazione.getValue()==null) {
			txtResult.setText("Selezionare un aereoporto di destinazione!");
			return;
		}
		Airport arrivo= cmbBoxAeroportoDestinazione.getValue();
		try {
    		int maxTratte= Integer.parseInt(numeroTratteTxtInput.getText());
    		if(maxTratte<1) {
    			txtResult.setText("Inserire un numero intero maggiore di 0!");
    			return;
    		}
    		 txtResult.appendText(model.cercaItinerario(cmbBoxAeroportoPartenza.getValue(), arrivo, maxTratte) + " totale voli: "+model.pesoMax(model.cercaItinerario(cmbBoxAeroportoPartenza.getValue(), arrivo, maxTratte)));
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero maggiore di 0!");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert compagnieMinimo != null : "fx:id=\"compagnieMinimo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoDestinazione != null : "fx:id=\"cmbBoxAeroportoDestinazione\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroTratteTxtInput != null : "fx:id=\"numeroTratteTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    
    public void setModel(Model model) {
  		this.model = model;
  		
  	}
}
