/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Arco;
import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaLista"
    private Button btnCreaLista; // Value injected by FXMLLoader

    @FXML // fx:id="btnMassimo"
    private Button btnMassimo; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCanzone"
    private ComboBox<Track> cmbCanzone; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<Genre> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtMemoria"
    private TextField txtMemoria; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void btnCreaLista(ActionEvent event) {

    	txtResult.clear();
    	
    	Track canzone = cmbCanzone.getValue();
    	Long bytes = null;
    	try {
    	bytes = Long.parseLong(txtMemoria.getText());
    	} catch(NumberFormatException nfe){
    		txtResult.setText("Errore: inserire valore numerico in byte per la memoria");
    		nfe.printStackTrace();
    		return;
    	}
    	if(bytes.equals(null)) {
    		txtResult.setText("Errore: inserire valore memoria");
    		return;
    	}
    	
    	if(canzone.equals(null)) {
    		txtResult.setText("Errore: Inserire canzone");
    		return;
    	}
    	
    	List<Track> canzoni = this.model.listaCanzoni(canzone, bytes);
    	
    	txtResult.appendText("Lista canzoni:\n");
    	for(Track t : canzoni) {
    		txtResult.appendText(t+"\n");
    	}
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	cmbCanzone.getItems().clear();
    	
    	Genre genere = cmbGenere.getValue();
    	if(genere == null) {
    		txtResult.appendText("Errore: Inserire genere");
    		return;
    	}
    	
    	this.model.creaGrafo(genere);
    	
    	cmbCanzone.getItems().addAll(this.model.getVertici());
    	int nVert = model.getNvert();
    	int nArc = model.getNArc();
    	
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("# Vertici: "+nVert+"\n# Archi: "+nArc);
    	
    	
    }

    @FXML
    void doDeltaMassimo(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Arco arcoMax = this.model.getArcoMax();
    	
    	txtResult.appendText("COPPIA CANZONE DELTA MASSIMO:\n");
    	txtResult.appendText(arcoMax.getT1() +" *** "+arcoMax.getT2()+" -> "+arcoMax.getPeso());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaLista != null : "fx:id=\"btnCreaLista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMassimo != null : "fx:id=\"btnMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCanzone != null : "fx:id=\"cmbCanzone\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMemoria != null : "fx:id=\"txtMemoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	cmbGenere.getItems().addAll(this.model.getAllGeneri());
    }

}
