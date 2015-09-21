import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Library{
	private LinkedList<DigitalDoc> libreria;
	
	public Library(){
		libreria = new LinkedList<DigitalDoc>();
	}
	
	//Ritorna l'oggetto DigitalDoc avente nome nome.
	//Ritorna null se non esiste alcun DigitalDoc con nome nome
	public DigitalDoc getFromName(String nome){
		for(DigitalDoc d: libreria){
			if(d.name().equals(nome))
				return d;
		}
		return null;
	}
	
	public boolean docExists(String nome){
		for(DigitalDoc d: libreria){
			if(d.name().equals(nome))
				return true;
		}
		return false;
	}
	
	public boolean add(DigitalDoc doc){
		if(this.getFromName(doc.name()) == null){
			libreria.add(doc);
			return true;
		}
		return false;
	}
	
	public void remove(String doc){
		//Costruisci l'iteratore
		Iterator<DigitalDoc> i = libreria.iterator();

		while(i.hasNext()){
			//Controlla che il nome del documento sia uguale a doc
			if(i.next().name().equals(doc))
				i.remove();
		} 
	}
	
	public int size(){
		return libreria.size();
	}
	
	public DigitalDoc pop(){
		return libreria.pop();
	}
	
}
