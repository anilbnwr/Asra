package com.asra.mobileapp.util;

import android.content.Context;

import com.asra.mobileapp.common.MessageProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import androidx.core.content.ContextCompat;

public class ResourceFetcher {

    private Context context;

    @Inject
    public ResourceFetcher(Context context){
        this.context = context.getApplicationContext();

    }

    public String getString(int resourceId){
        return context.getString(resourceId);
    }

    public int getColor(int resourceId){
        return ContextCompat.getColor(context, resourceId);
    }

    public String getConfigString(String key){
        return MessageProvider.getInstance().getText(key);
    }

    public String readFileFromRawDirectory(int resourceId){
        InputStream iStream = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = null;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream = new ByteArrayOutputStream();
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream == null? "" : byteStream.toString();
    }
}
