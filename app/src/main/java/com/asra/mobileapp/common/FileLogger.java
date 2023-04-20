package com.asra.mobileapp.common;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class FileLogger {

    private static FileLogger mFileLogger;
    private File logFile;
    private File miscFile;
    private Context context;

    private FileLogger(Context context) {
        this.context = context.getApplicationContext();
        createLogFile();
    }

    public static FileLogger getInstance(Context context) {
        if (mFileLogger == null) {
            mFileLogger = new FileLogger(context);
        }
        return mFileLogger;
    }

    /**
     * Appends to a log file created for the current journey
     * @param message
     * @param args
     */
    public void logToCurrentTripFile(String message, Object... args) {
        if(PermissionUtils.hasAllPermissions(context, PermissionUtils.getStoragePermission())) {
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            message = currentDateTimeString + " " + message;

            if(logFile == null){
                createLogFile();
            }
            appendLog(logFile, message, args);
            //Timber.d(String.format(message, args));
            //Timber.d(message, args);
        }
    }

    private synchronized void appendLog(File file, String message, Object... args) {
        if(PermissionUtils.hasAllPermissions(context, PermissionUtils.getStoragePermission())) {
            String logText = args.length == 0 ? message : String.format(message, args);


            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                buf.append(logText);
                buf.newLine();
                buf.close();
            } catch (IOException e) {
                Timber.e(e, "Logging to file failed. %s", e.getLocalizedMessage());
               // Timber.e(e, "Logging to file failed.", e.getLocalizedMessage());
            }
        }
    }

    public void closeFile() {
        if (logFile != null) {
            logFile = null;
        }
        if(miscFile != null){
            miscFile = null;
        }
    }

    /**
     * Logs to /miscLog.txt
     * @param message
     */
    public synchronized void logToFile(String message, Object... args){
        if(PermissionUtils.hasAllPermissions(context, PermissionUtils.getStoragePermission())) {
            if (miscFile == null) {
                createMiscFile();
            }

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            message = currentDateTimeString + " " + message;
           // Timber.d(message, args);
            appendLog(miscFile, message, args);
        }
    }

    private void createLogFile() {
        if(PermissionUtils.hasAllPermissions(context, PermissionUtils.getStoragePermission())) {

            String currentDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            File directory = new File(Environment.getExternalStorageDirectory(), "EvolveGT/" + currentDateString + "/");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String currentTimeString = new SimpleDateFormat("HH_mm_ss", Locale.US).format(new Date());
            logFile = new File(directory, currentTimeString + ".txt");
            Timber.d("File created at :%s", logFile.getAbsolutePath());
        }
    }

    private void createMiscFile(){
        if(PermissionUtils.hasAllPermissions(context, PermissionUtils.getStoragePermission())) {
            String currentDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            File directory = new File(Environment.getExternalStorageDirectory(), "EvolveGT/" + currentDateString + "/");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            miscFile = new File(directory, "miscLog.txt");
            Timber.d("File created at :%s", miscFile.getAbsolutePath());
        }
    }
}