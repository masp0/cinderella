package application;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;


public class ResourceController implements Initializable {

	private int changeCount = 0;
	private static int updatestatus = 0;
	private String configFile= "Resources/SQLConnection.properties";
	private DBConnect dbc;
	private ObservableList<Resource>data, changedData, unchangedData;
	
	@FXML //  fx:id="tableview"
	private TableView<Resource> tableview;
	@FXML
	private TableColumn<Resource, String> resCol,stockCol,mstockCol,noteCol;
	@FXML //  fx:id="CBox1"
	private ComboBox<String> CBox1;
	@FXML //  fx:id="TF1"
	private TextField TF1;
	@FXML
	private Button saveButton, searchButton, deleteButton, addButton;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	
		
		/* ComboBox CBox1 */
		CBox1.getItems().addAll("Resource_ID","Stock","Min_Stock","Note");
		CBox1.getSelectionModel().select("Resource_ID");
		
		/*Tabelle initialisieren*/
		tableview.setEditable(true);
		initTable();
		initSQL();
		
		
		/*Funktionalitaet - addButton*/
		addButton.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		        	Resource nres = new Resource("","","","");
		        	data.add(nres);
		        	tableview.setItems(data);
		        	updatestatus = 1;     
		 }
    });
		
		/*Funktionalitaet - deleteButton*/
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		        	Resource selectedItem = tableview.getSelectionModel().getSelectedItem();
		        	String res_id = selectedItem.Res_ID.get();
		            tableview.getItems().remove(selectedItem);
		            setData("DELETE FROM Resources WHERE resid = " + res_id);  
		            getData("SELECT resid, stock, minstock, note FROM Resources");
		 }
    });
		
		/*Funktionalitaet - saveButton*/
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		        	Alert alert = new Alert(AlertType.CONFIRMATION);
		        	alert.setTitle("Bestätigung benötigt!");
		        	alert.setHeaderText("Es wurden " + changeCount + " Veränderungen auf dem lokalen Datenbestand festgestellt!");
		        	
		        	alert.getDialogPane().getButtonTypes().clear();
		        	ButtonType saveButtonType = new ButtonType("Änderungen übernehmen");
					ButtonType abortButtonType = new ButtonType("Änderungen verwerfen");
					alert.getDialogPane().getButtonTypes().addAll(saveButtonType, abortButtonType);

		        	Label label = new Label("Folgende Datensätze haben sich geändert:");
		        	String output = "";
		        	for (Resource res : changedData){
		        		output += res.toString() + "\n";
		        	}
		        	TextArea textArea = new TextArea(output);
		        	textArea.setEditable(false);

		        	textArea.setMaxWidth(Double.MAX_VALUE);
		        	textArea.setMaxHeight(Double.MAX_VALUE);
		        	GridPane.setVgrow(textArea, Priority.ALWAYS);
		        	GridPane.setHgrow(textArea, Priority.ALWAYS);

		        	GridPane expContent = new GridPane();
		        	expContent.setMaxWidth(Double.MAX_VALUE);
		        	expContent.add(label, 0, 0);
		        	expContent.add(textArea, 0, 1);
		        	
		        	// Set expandable Exception into the dialog pane.
		        	alert.getDialogPane().setContent(expContent);
		        	Optional<ButtonType> result = alert.showAndWait();
		        
		        	
		        	// IF zur Unterscheidung von Update oder Abortbutton
		        	if (result.get() == saveButtonType){
		        	    /*Lade lokalen Datensatz in die Datenbank*/
		        		int rows = 0;	        	 
		        	    		if (updatestatus == 0){
		        	    		for (Resource i : changedData){
		        	    			setData("UPDATE resources "
		        	    				 	 + "SET stock='" + i.getStock() + "'"+
		        	    					 ", minstock='" + i.getMinStock() + "'" +
		        	    				 	 ", note='" + i.getNote() + "'" +
		        	    				 	 " "+ " WHERE resid='"+i.getRes_ID() + "'");		        	    				        	    			
		        	    			rows++;
		        	    			System.out.println("UPDATE resources "
		        	    				 	 + "SET stock='" + i.getStock() + "'"+
		        	    					 ", minstock='" + i.getMinStock() + "'" +
		        	    				 	 ", note='" + i.getNote() + "'" +
		        	    				 	 " "+ " WHERE resid='"+i.getRes_ID() + "';");
		        	    		}
		        	    		}else{
		        	    			for (Resource i : changedData){
			        	    			setData("INSERT INTO resources VALUES("+
			        	    					"'" + i.getRes_ID()+"',"
			        	    				 	 + "'" + i.getStock() + "'"+
			        	    					 ",'" + i.getMinStock() + "'" +
			        	    				 	 ",'" + i.getNote() + "'" +
			        	    				 	 ")");
			        	    				 	 
			        	    			
			        	    			System.out.println("INSERT INTO resources VALUES("+
			        	    					"'" + i.getRes_ID()+"',"
			        	    				 	 + "'" + i.getStock() + "'"+
			        	    					 ",'" + i.getMinStock() + "'" +
			        	    				 	 ",'" + i.getNote() + "'" +
			        	    				 	 ")");
			        	    			rows++;
			        	    			updatestatus = 0;
		        	    			
		        	    		}
		        	    		}
		        	    		// ENDE IF ELSE UPDATE / INSERT
		        	    		Alert updated = new Alert(AlertType.INFORMATION);
		        	    		updated.setTitle("Update Vorgang erfolgreich!");
		        	    		updated.setHeaderText("Update Vorgang wurde erfolgreich ausgeführt!");
		        	    		updated.setContentText("Es wurden: " + rows + " Datensätze geändert.");

		        	    		updated.showAndWait();
//		        	    	}
//		        	    }
		        	} 
		        	else if (result.get() == abortButtonType) 
		        		data.clear();
		        	backup();
		        }
		    });
		
			/*Funktionalitaet - searchButton*/
            searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	String choice = "Res_ID"; 
            	switch(CBox1.getValue()) {
               		case "Res_ID": choice = "resid";  break;
               		case "Stock": choice = "stock"; break;
               		case "Min_Stock": choice = "minstock"; break;
               		case "Note": choice = "note";   break;
            	}
            	String[] param = {TF1.getText()};
            	String params = TF1.getText();
           
            	if(params != null && !params.isEmpty()){	
            	
            	if(checkSQLInject(param)) {
            		getData("SELECT resid, stock, minstock, note FROM resources WHERE " + choice + " LIKE '" +"%" + TF1.getText()+ "%" + "';");
            		System.out.println("SELECT resid, stock, minstock, note FROM resources WHERE " + choice + " = '" + TF1.getText()+"';");
            		tableview.setItems(data);
            	}
            	else {
            		showError("Bitte geben sie keine Sonderzeichen ein,\nder Suche-Vorgang wurde abgebrochen.", "Verdacht auf SQL Injection!");
    	    		data.clear();
    	    		backup();
            	}
            	}
            	else {
            		getData("SELECT resid, stock, minstock, note FROM resources");
            	}	
            }
		    });
	}
	
	public void initTable() {
		/*Populating Tableview*/
		 tableview.setStyle("-fx-focus-color: transparent;");
		 tableview.setEditable(true);
	    resCol.setCellValueFactory(new PropertyValueFactory<Resource,String>("Res_ID"));
	    resCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    resCol.setOnEditCommit(
	    	    new EventHandler<CellEditEvent<Resource, String>>() {
	    	        @Override
	    	        public void handle(CellEditEvent<Resource, String> t) {
	    	            t.getTableView().getItems().get(t.getTablePosition().getRow()).setRes_ID(t.getNewValue());
	    	            changeCount += 1;
	    	            //Add Item to list with changes
	    	            if ( !changedData.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())) )	
	    	            		changedData.add( t.getTableView().getItems().get(t.getTablePosition().getRow()) );
	    	        }
	    	    }
	    	);
	    stockCol.setCellValueFactory(new PropertyValueFactory<Resource,String>("Stock"));
	    stockCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    stockCol.setOnEditCommit(
	    	    new EventHandler<CellEditEvent<Resource, String>>() {
	    	        @Override
	    	        public void handle(CellEditEvent<Resource, String> t) {
	    	            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setStock(t.getNewValue());
	    	            	changeCount += 1;
	    	            	if ( !changedData.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())) )	
	    	            		changedData.add( t.getTableView().getItems().get(t.getTablePosition().getRow()) );
	    	        }
	    	    }
	    	);
	    mstockCol.setCellValueFactory(new PropertyValueFactory<Resource,String>("MinStock"));  
	    mstockCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    mstockCol.setOnEditCommit(
	    	    new EventHandler<CellEditEvent<Resource, String>>() {
	    	        @Override
	    	        public void handle(CellEditEvent<Resource, String> t) {
	    	            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setMinStock(t.getNewValue());
	    	            	changeCount += 1;
	    	            	if ( !changedData.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())) )	
	    	            		changedData.add( t.getTableView().getItems().get(t.getTablePosition().getRow()) );
	    	        }
	    	    }
	    	);
	    noteCol.setCellValueFactory(new PropertyValueFactory<Resource,String>("Note"));  
	    noteCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    noteCol.setOnEditCommit(
	    	    new EventHandler<CellEditEvent<Resource, String>>() {
	    	        @Override
	    	        public void handle(CellEditEvent<Resource, String> t) {
	    	            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setNote(t.getNewValue());
	    	            	changeCount += 1;
	    	            	if ( !changedData.contains(t.getTableView().getItems().get(t.getTablePosition().getRow())) )	
	    	            		changedData.add( t.getTableView().getItems().get(t.getTablePosition().getRow()) );
	    	        }
	    	    }
	    	);
	    
	}
	
	
	
	public void setData(String query){
		DBConnect dbc = new DBConnect(configFile);
		String res ="";
		try {
			res = dbc.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public void insertData(){
//		
//	}
	
	public void getData(String query) {
		dbc = new DBConnect(configFile);
		data = FXCollections.observableArrayList();
		changedData   = FXCollections.observableArrayList();
		unchangedData = FXCollections.observableArrayList();

		int rowCount = 0;
		ResultSet rs = null;
		try {
			rs = dbc.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			while(rs.next() == true){
				rowCount++;
				try {
						Resource nres = new Resource(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
						System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3) + rs.getString(4));
						data.add(nres);
				} 
				catch(Exception e) {
					System.out.println("Reading SQL result failed. " + e.getMessage());
					System.out.println(e.getMessage());
				}
			}
		} 
		catch (SQLException e) {
				e.printStackTrace();
		}
		if(rowCount < 1)
			showError("Die Suche war leider ergebnislos.", "Keine Übereinstimmungen.");

		backup();	
		tableview.setItems(data);
		dbc.disconnect();
	}
	

	private void initSQL(){	
		
			getData("SELECT resid, stock, minstock, note FROM resources");			
	}
	
	//necessary for listmanagement e.g. backup
	public void backup(){
	    unchangedData.clear();
	    unchangedData = FXCollections.observableArrayList(data);
	    changedData.clear();
	    changeCount = 0;
	}	
	

	/*Function to open opoup Error Dialog with given Errormessage and description*/
	public void showError(String err, String desc){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Fehler!");
		alert.setHeaderText(desc);
		alert.setContentText(err);
		alert.showAndWait();
	}
	
	/*Checks the SQL Get Query Parameter for SQL Injection attempts
	 *  returns true on legitimate Strings*/
	public boolean checkSQLInject(String[] params){
		String patterns[] = {"\"", "*", ";", "'", "\\"};
		for (String s: params){
			for (int i = 0; i < patterns.length; i++){
				if (s.contains(patterns[i]))
					return false;
			}
		}
		return true;
	}
	
}
