package gui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.service.DepartmentService;

public class MainViewController implements Initializable {
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartament;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onmenuItemSellerAction() {
		System.out.println("seller");
	}

	@FXML
	public void onmenuItemDepartament() {
		loadView("/gui/Department.fxml",(DepartmentController controller)->{
			controller.setService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onmenuItemAbout() {
		loadView("/gui/About.fxml",x -> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> init) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbox.getChildren());
			
			T controller = loader.getController();
			init.accept(controller);
		} catch (Exception e) {
			Alerts.showAlert("IO Exception", "Error loanding view", e.getMessage(),AlertType.ERROR);
		}
		
		
	}

}
