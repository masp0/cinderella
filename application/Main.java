package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

 
public class Main extends Application{
	
	public static void main(String[] args){
		launch(args);
		System.out.println("Hellu");
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("MainView.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.setTitle("Ressourcenverwaltung");
		stage.show();
	}
}