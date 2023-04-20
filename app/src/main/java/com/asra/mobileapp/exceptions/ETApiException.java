package com.asra.mobileapp.exceptions;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.network.retrofit.response.ETResponse;
import com.asra.mobileapp.util.StringUtils;

import java.io.IOException;

public class ETApiException extends IOException {

    ETResponse apiResponse;
    public int errorCode;

    public ETApiException(String error){

        super(error);
        apiResponse = null;
    }

    public ETApiException(ETResponse response){

        super(StringUtils.isEmpty(response.message) ? MessageProvider.getInstance()
                .getText(MessageProvider.error_generic_message) : response.message);
        apiResponse = response;
        errorCode = response.errorCode;
    }

    public interface ApiErrorCode{
        int BILLING_ADDRESS = 1;
        int EMERGENCY_CONTACT = 2;
    }
}
