import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class UserDb {
	private List<User> utenti;
	public UserDb(){
		this.utenti = new ArrayList<User>();
	}
	
	public boolean auth(String name, int password){
		for(User u: utenti){
			if(u.name().equals(name) && u.checkPsw(password))
				return true;
		}
		return false;
	}
	
	public boolean add(String name, int password){
		for(User u : utenti){
			if(u.name().equals(name))
				//Esiste già l'utente
				return false;
		}
		User nuovoUtente = new User(name, password);
		utenti.add(nuovoUtente);
		return true;
	}
	
	public boolean remove(String nome){
		Iterator<User> i = utenti.iterator();
		//Per ogni utente
		while(i.hasNext()){
			//Se si chiama come l'utente da dover rimuovere
			if(i.next().name().equals(nome)){
				i.remove();
				return true;
			}
		}
		return false;
	}
	
	public boolean userExists(String nome){
		for(User u: utenti){
			if(u.name().equals(nome))
				return true;
		}
		return false;
	}
	
	public User getUser(String nome){
		for(User u: utenti)
			if(u.name().equals(nome))
				return u;
		return null;
	}
}
