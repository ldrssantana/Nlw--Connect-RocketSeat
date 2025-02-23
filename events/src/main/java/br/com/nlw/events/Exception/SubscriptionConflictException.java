package br.com.nlw.events.Exception;

public class SubscriptionConflictException extends RuntimeException {

    public SubscriptionConflictException( String msg) {
        super(msg);
    }
}
