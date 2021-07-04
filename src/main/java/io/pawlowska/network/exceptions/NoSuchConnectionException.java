package io.pawlowska.network.exceptions;

public class NoSuchConnectionException extends RuntimeException {

    public NoSuchConnectionException(String msg) {
        super(msg);
    }
}