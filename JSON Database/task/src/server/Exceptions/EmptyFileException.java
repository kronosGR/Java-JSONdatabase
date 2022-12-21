package server.Exceptions;

public class EmptyFileException extends RuntimeException{
    public EmptyFileException(){
        super("Empty file");
    }
}
