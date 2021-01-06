package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {
	
	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemVendedor() {
		System.out.println("onMenuItemVendedor");
	}
	
	@FXML
	public void onMenuItemDepartamento() {
		System.out.println("onMenuItemDepartamento");
	}
	
	@FXML
	public void onMenuItemAbout() {
		System.out.println("onMenuItemAbout");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
