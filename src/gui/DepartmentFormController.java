package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	private Department entidade;

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setDepartment(Department entidade) {
		this.entidade = entidade;
	}
	
	@FXML
	public void onButtonSaveAction() {
		System.out.println("onButtonSaveAction");
	}
	
	@FXML
	public void onButtonCancelAction() {
		System.out.println("onButtonCancelAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {	
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}
		
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText((entidade.getName()));
	}

}
