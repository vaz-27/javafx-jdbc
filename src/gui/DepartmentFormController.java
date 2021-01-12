package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entidade;
	private DepartmentService service;
	private List<DataChangeListener> list = new ArrayList<>();

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
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		list.add(listener);
	}
	
	@FXML
	public void onButtonSaveAction(ActionEvent event) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}
		if (service == null) {
			throw new IllegalStateException("Servico nulo");
		}
		
		try {
			entidade = getFormData();
			service.saveOrUpdate(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch(DbException e) {
			Alerts.showAlert("Erro ao salvar Departamento", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : list) {
			listener.onDataChanged();
		}		
	}

	private Department getFormData() {
		Department obj = new Department();
		
		obj.setId(Utils.tryparsetoInt(txtId.getText()));
		obj.setName(txtNome.getText());
		
		return obj;
	}

	@FXML
	public void onButtonCancelAction(ActionEvent event) {
		System.out.println("onButtonCancelAction");
		Utils.currentStage(event).close();
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
