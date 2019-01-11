package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import gui.listeners.DatachangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationException;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	
	private DepartmentService service;
	
	private List<DatachangeListener> datachangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtId;
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if(entity == null) throw new IllegalStateException("entity was null");
		if(service == null) throw new IllegalStateException("service was null");
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDatachangeListeners();
			Utils.currentStage(event).close();
			
		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		}

	}
	private Department getFormData() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) exception.addError("name", "Field can't be empty");
		
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size() > 0) throw exception;
		
		return obj;
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("department was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	public void setDepartment(Department e) {
		entity = e;
	}
	public void setDepartmentService(DepartmentService s) {
		service = s;
	}
	public void subscribeDataChangeListener(DatachangeListener listener) {
		datachangeListeners.add(listener);
	}
	private void notifyDatachangeListeners() {
		for (DatachangeListener listener : datachangeListeners) {
			listener.onDatachanged();
		}
	}
	private void setErrorMessage(Map<String,String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("name")) labelErrorName.setText(errors.get("name"));
	}
}
