package server.Exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(){
        super("No such file");
    }
}
