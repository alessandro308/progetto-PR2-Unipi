import java.util.List;
import java.util.ArrayList;

public class DigitalDoc{
	private String name;
	private String owner;
	private List<String> friends =  new ArrayList<String>();
	
	public DigitalDoc(String name, String user){
		this.name = name;
		this.owner = user;
	}
	
	public boolean isOwner(String user){
		return this.owner == user;
	}
	
	//Verifica che l'utente user possa accedere al documento
	//Ritorna true se user ha accesso
	//false altrimenti
	public boolean canSee(String user){
		return this.friends.contains(user) || isOwner(user);
	}
	
	//Aggiunge l'utente user alla lista degli utenti che possono accedere del documento
	public void addFriend(String user){
		this.friends.add(user);
	}
	
	// Rimuove l'utente user da chi ha accesso al documento
	// Il proprietario non può essere rimosso
	// Ritorna il numero di proprietari aggiornato
	public boolean removeFriend(String user){
		if(this.owner==user)
			return false;
		friends.remove(user);
		return true;
	}
	
	public String name(){
		return this.name;
	}
	
}
