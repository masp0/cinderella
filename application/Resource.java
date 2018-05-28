package application;
import javafx.beans.property.SimpleStringProperty;
public class Resource {
	
	public SimpleStringProperty Res_ID = new SimpleStringProperty();
	public SimpleStringProperty Stock = new SimpleStringProperty();
	public SimpleStringProperty MinStock = new SimpleStringProperty();
	public SimpleStringProperty Note = new SimpleStringProperty();
	
	
	public Resource(String Res_ID, String Stock, String MinStock, String Note) {
		this.Res_ID.set(Res_ID);
		this.Stock.set(Stock);
		this.MinStock.set(MinStock);
		this.Note.set(Note);
	}
	
	public Resource(Resource res){
		this.Res_ID.set(res.getRes_ID());
		this.Stock.set(res.getStock());
		this.MinStock.set(res.getMinStock());
		this.Note.set(res.getNote());

	}
	@Override
	public String toString(){
		return "Res_ID: " + Res_ID.get() + "; Stock: " + Stock.get() + "; MinStock: " + MinStock.get() + "; Note: " + Note.get();
	}

	public String getRes_ID() {
		return Res_ID.get();
	}

	public void setRes_ID(String Res_ID) {
		this.Res_ID.set(Res_ID);
	}

	public String getStock() {
		return Stock.get();
	}

	public void setStock(String Stock) {
		this.Stock.set(Stock);
	}
	
	public String getMinStock() {
		return this.MinStock.get();
	}

	public void setMinStock(String MinStock) {
		this.MinStock.set(MinStock);
	}

	public String getNote() {
		return Note.get();
	}

	public void setNote(String Note) {
		this.Note.set(Note);
	}
	
}

