package server.Exceptions;

public class NoKeyException extends RuntimeException{
    public NoKeyException(){
        super("No such key");
    }
}
