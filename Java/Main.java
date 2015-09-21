import ExceptionClass.*;

public class Main{
	public static void main(String args[]){
		ShareDoc x = new ShareDocImpl();
		//Visione Operatore
		//Aggiungi un utente
		if(x.addUser("Pippo", 0000) == false)
			System.out.println("Errore1");
		if(x.addUser("Pluto", 1234) == false)
			System.out.println("Errore2");
		if(x.addUser("Topolino", 5678) == false)
			System.out.println("Errore3");
		if(x.addUser("Qui", 1235) == false)
			System.out.println("Errore4");
			
		//Aggiungo utente con nome già occupato
		if(x.addUser("Pippo", 1234) == true)
			System.out.println("Errore5");
		
		//Rimuovo Utente
		x.removeUser("Qui");
		
		//Visione Utente
		//Aggiungo documenti
		if(x.addDoc("Pippo", "DocumentoPippo", 0000)==false)
			System.out.println("Errore6");
		if(x.addDoc("Pluto", "DocumentoPluto", 1234)==false)
			System.out.println("Errore7");
		if(x.addDoc("Pippo", "DocumentoPippo1", 0000)==false)
			System.out.println("Errore8");
			
		//Aggiungo documento che già esiste
		if(x.addDoc("Pippo", "DocumentoPippo", 0000)==true)
			System.out.println("Errore9");
			
		//Rimuovo Documento
		if(x.removeDoc("Pippo", "DocumentoPippo", 0000) == false)
			System.out.println("Errore10");
		//Rimuovo Documento Inesistente
		if(x.removeDoc("Pippo", "DocumentoInesistente", 0000) == true)
			System.out.println("Errore11");
		
		//Leggo i documenti
		x.readDoc("Pluto", "DocumentoPluto", 1234);
		//Cerco di leggere documento inesistente
		try{
			x.readDoc("Pluto", "DocumentoInesistente", 1234);
		}
		catch(WrongIdException e){
			System.out.println("Eccezione Catturata1 " + e);
		}
		
		x.addDoc("Topolino", "DocumentoTopolino", 5678);
		
		try{
			x.shareDoc("Topolino", "Pluto", "DocumentoTopolino", 5678);
		}
		catch(WrongIdException e){
			System.out.println("Eccezione catturata" + e);
		}
		
		try{
			x.shareDoc("Pippo", "Pluto", "DocumentoTopolino", 0000);
		}
		catch(WrongIdException e){
			System.out.println("Eccezione Catturata2 " + e);
		}
		
		try{
			String s = x.getNext("Pluto", 1234);
			System.out.println("Notifica per Pluto: " + s);
		}
		catch(EmptQueueException e){
			System.out.println("Eccezione Catturata " + e);
		}
		
		try{
			x.getNext("Pluto", 1234);
		}
		catch(EmptQueueException e){
			System.out.println("Eccezione Catturata3 " + e);
		}
		
	}
}
