import ExceptionClass.*;

public interface ShareDoc {

	// Aggiunge l’utente con la relativa password alla repository.
	// Restituisce true se l'inserimento ha successo,
	// false se fallisce perche' esiste gia' un utente con il medesimo nome. 
	public boolean addUser(String name, int password);
	
	// Elimina l’utente e tutti i suoi documenti digitali 
	public void removeUser(String name);
	
	// Aggiunge al sistema il documento digitale identificato dal nome. 
	// Restituisce true se l'inserimento ha successo,
	// false se fallisce perche' esiste gia' un documento con quel nome. 
	public boolean addDoc(String user, String doc, int code);
	
	// Rimuove dal sistema il documento digitale identificato dal nome. 
	// Restituisce true se l’operazione ha successo,
	// false se fallisce perche' non esiste un documento con quel nome. 
	public boolean removeDoc(String user, String doc, int code);
	
	// Legge il documento digitale identificato dal nome.
	// Lancia WrongIdException se user non e' il nome di un utente registrato 
	// o se non esiste un documento con quel nome
	// o se il codice non e’ corretto
	public void readDoc(String user, String doc, int code);
	
	// Notifica una condivisione di documento
	// Lancia un'eccezione WrongIdException se fromName o toName non sono nomi 
	// di utenti registrati o se non esiste un documento con quel nome
	// o se il codice non e’ corretto
	public void shareDoc(String fromName, String toName, String doc, int code) throws WrongIdException;
	
	// Restituisce il nome del documento condiviso cancellandolo dalla coda 
	// delle notifiche di condivisione.
	// Lancia un'eccezione EmptyQueueException se non ci sono notifiche,
	// o WrongIdException se user non e' il nome di un utente registrato
	// o se il codice non e’ corretto
	public String getNext(String user, int code) throws EmptQueueException, WrongIdException;

}