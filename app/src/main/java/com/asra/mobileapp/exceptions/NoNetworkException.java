package com.asra.mobileapp.exceptions;

import java.io.IOException;

public class NoNetworkException extends IOException {

    public NoNetworkException(String error){
        super(error);
    }

    public NoNetworkException(){
    }
}
