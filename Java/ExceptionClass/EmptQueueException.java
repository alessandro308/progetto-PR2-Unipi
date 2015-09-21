package ExceptionClass;

public class EmptQueueException extends RuntimeException{
	
	public EmptQueueException(String message){
		super(message);
	}
	
	public EmptQueueException(){
		super();
	}
}
