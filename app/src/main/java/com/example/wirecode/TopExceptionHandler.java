package com.example.wirecode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Activity app = null;

    public TopExceptionHandler(Activity app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;
    }

    public void uncaughtException(Thread t, Throwable e) {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i=0; i<arr.length; i++) {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";

        // If the exception was thrown in a background thread inside
        // AsyncTask, then the actual exception can be found with getCause

        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++) {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";
        Toast.makeText(
                app,
                report,
                Toast.LENGTH_LONG).show();
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);

        String uriText = "mailto:" + Uri.encode("samjt775@gmail.com") +
                "?subject=" + Uri.encode("the subject") +
                "&body=" + Uri.encode(report);
        Uri uri = Uri.parse(uriText);

        sendIntent.setData(uri);
        app.startActivity(Intent.createChooser(sendIntent, "Send mail..."));


        try {
            FileOutputStream trace = app.openFileOutput("stack.trace",
                    Context.MODE_PRIVATE);
            trace.write(report.getBytes());
            trace.close();
        } catch(IOException ioe) {
            Toast.makeText(app, "cannot save stack trace", Toast.LENGTH_LONG).show();
        }

        defaultUEH.uncaughtException(t, e);
    }
}
