package server.Exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super("Bad request");
    }
}
