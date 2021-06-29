package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entidade;
	private SellerService service;
	private DepartmentService departmentService;
	private List<DataChangeListener> list = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	private ObservableList<Department> obsList;

	public void setSeller(Seller entidade) {
		this.entidade = entidade;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
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
		} catch (ValidationException e) {
			setErrorMessages(e.getError());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar Departamento", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : list) {
			listener.onDataChanged();
		}
	}

	private Seller getFormData() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Erro de validação");

		obj.setId(Utils.tryparsetoInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "O campo não pode ser vazio");
		}
		obj.setName(txtNome.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "O campo não pode ser vazio");
		}
		obj.setEmail(txtEmail.getText());

		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "O campo não pode ser vazio");
		} else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}

		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "O campo não pode ser vazio");
		}
		obj.setBaseSalary(Utils.tryparsetoDouble(txtBaseSalary.getText()));

		obj.setDepartment(comboBoxDepartment.getValue());
		
		if (exception.getError().size() > 0) {
			throw exception;
		}

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
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}

		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText((entidade.getName()));
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entidade.getBaseSalary()));
		if (entidade.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entidade.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}

		if (entidade.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entidade.getDepartment());
		}
	}

	public void loadAssociateObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentServiço nulo, nao foi injetado");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorNome.setText(fields.contains("nome") ? errors.get("nome") : "");
		labelErrorEmail.setText(fields.contains("email") ? errors.get("email") : "");
		labelErrorBaseSalary.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");
		labelErrorBirthDate.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");

	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
