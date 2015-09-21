package ExceptionClass;

public class WrongIdException extends RuntimeException {
    
    public WrongIdException(String message){
	    super(message);
    }
    
    public WrongIdException(){
	    super();
    }
}
