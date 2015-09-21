import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import ExceptionClass.*;
import java.util.Iterator;

public class ShareDocImpl implements ShareDoc {
	/*
	 * -----------------------------------
	 * funzione di astrazione:
	 	utenti: lista degli utenti che possono accedere al sistema, ogni utente è identificato in maniera univoca da un nome
	 	sharedList: lista, per ogni utente, delle notifiche di documento con loro condivisi
	 	docs: lista dei documenti caricati nel sistema, ogni doc è identificato in maniera univoca da un nome
	 *------------------------------------
	 * Invariante di rappresentazione
	 	utente[i].name() != utente[j].name() \forall i != j
	 	docs[i].name() != docs[j].name() \forall i != j
	 *------------------------------------
	 */
	 	
	private UserDb utenti = new UserDb();
	private Map<User, Library> sharedList = new HashMap<User, Library>();
	private Library docs = new Library();
	
	// Req: name != utente_i.name() \forall utente_i in utenti
	// Mod: Aggiunge l'utente name alla lista di utenti
	// Ret: -
	// Aggiunge l'utente solo se non è già presente nel sistema. Invariante preservato.
	public boolean addUser(String name, int password){
		return utenti.add(name, password);
	}

	// Req: name è un utente esistente
	// Mod: Rimuove l'utente name dalla lista di utenti
	// Ret: -
	// Preserva l'invariante perchè rimuovere l'utente solo se esiste.
	public void removeUser(String name){
		utenti.remove(name);
	}

	// Req: doc != doc_i.name() \forall doc_i in docs
	// Mod: Aggiunge doc alla lista docs
	// Ret: True se l'inserimento ha successo, false altrimenti
	// Aggiunge un documento solo se non esiste un documento già esistente con nome doc. Invariante preservato.
	public boolean addDoc(String user, String doc, int code){
		if(utenti.auth(user, code) == false) 
			//Password errata, fallita autenticazione
			return false;
		DigitalDoc document = new DigitalDoc(doc, user);
		return docs.add(document);
	}
	
	// Req: -
	// Mod: Rimuove dal sistema il documento digitale identificato dal nome. 
	// Ret: true se l’operazione ha successo,
	// 	false se fallisce perche' non esiste un documento con quel nome. 
	// Rimuove un documento solo se il documento esiste. Invariante preservato.
	public boolean removeDoc(String user, String doc, int code){
		if(utenti.auth(user, code) == false){
			System.out.println("Password errata\n");
			return false;
		}
		if(docs.docExists(doc)==false)
			//Non esiste documento con quel nome
			return false;
		if(docs.getFromName(doc).isOwner(user) == true){
			//Questo documento è di proprietà dell'utente
			docs.remove(doc);
			return true;
		}
		//L'utente non ha diritto di rimozione
		return false;
	}

	// Req: doc è un documento in docs, user è un utente del sistema
	// Effect: Legge il documento digitale identificato dal nome.
	// Exc: WrongIdException se user non e' il nome di un utente registrato 
	// 		o se non esiste un documento con quel nome
	// 		o se il codice non e’ corretto
	// Ret: -
	// Legge il documento solo se il documento esiste. Inviante preservato.
	public void readDoc(String user, String doc, int code){
		if(utenti.userExists(user) == false || 
			docs.docExists(doc) == false ||
			utenti.auth(user, code) == false ||
			docs.getFromName(doc).canSee(user) == false){
			throw new WrongIdException();
		}
		
		System.out.println("Aperto documento: " + doc);
	}

	// Req: doc è un documento in docs, user è un utente del sistema
	// Effect: Notifica una condivisione di documento
	// Exc: Lancia un'eccezione WrongIdException se fromName o toName non sono nomi 
	// 		di utenti registrati o se non esiste un documento con quel nome
	//		 o se il codice non e’ corretto
	// Ret: -
	// Permette all'utente to si accedere al documento dell'utente from. Non aggiunge documenti, l'invariante è preservato.
	public void shareDoc(String fromName, String toName, String doc, int code) throws WrongIdException{
		if(utenti.userExists(fromName) == false || utenti.userExists(toName) == false 
				|| docs.docExists(doc) == false || utenti.auth(fromName, code) == false)
			throw new WrongIdException();
		if(docs.getFromName(doc).canSee(fromName)){
            		//È autorizzato a vedere il documento e quindi a condividerlo
            		docs.getFromName(doc).addFriend(toName);
            		DigitalDoc sharedDoc = docs.getFromName(doc);
            		if(sharedList.containsKey(toName)==false){
	            		Library sharedDocs = new Library();
	            		sharedList.put(utenti.getUser(toName), sharedDocs);
            		}
			sharedList.get(utenti.getUser(toName)).add(sharedDoc);
		}
		else{
			//L'utente non ha accesso al documento
			throw new WrongIdException();
		}
	}

	// Req: user è un utente del sistema
	// Mod: cancella il nome dell'ultimo documento condivido dalla coda delle notifiche di condivisione.
	// Exc: mptyQueueException se non ci sono notifiche,
	// 		o WrongIdException se user non e' il nome di un utente registrato
	// 		o se il codice non e’ corretto
	// Ret: il nome del documento condiviso 
	// Non modifica alcun documento o utente. Preserva l'invariante.
	public String getNext(String user, int code) throws EmptQueueException, WrongIdException{
		if(utenti.userExists(user) == false || utenti.auth(user,code) == false)
            		throw new WrongIdException("Errore con l'utente");
        	if(sharedList.get(utenti.getUser(user)).size() == 0)
            		throw new EmptQueueException("Non ci sono notifiche per" + user);
        	return sharedList.get(utenti.getUser(user)).pop().name();
	}

}
