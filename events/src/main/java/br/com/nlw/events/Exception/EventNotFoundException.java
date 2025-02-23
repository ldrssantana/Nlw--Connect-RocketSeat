package br.com.nlw.events.Exception;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(String msg){
        super(msg);
    }
}
