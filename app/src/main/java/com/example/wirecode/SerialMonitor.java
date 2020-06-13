package com.example.wirecode;

import com.example.wirecode.database.Mapping;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SerialMonitor extends AppCompatActivity {
    Button btOpen, btClose, btWrite;
    EditText etWrite;
    TextView tvRead;
    Spinner spBaud;
    CheckBox cbAutoscroll;

    Physicaloid mPhysicaloid; // initialising library
    ArrayList<String> pinmapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));

        setContentView(R.layout.activity_serial);

        TextView title = (TextView) findViewById(R.id.text_title);
        Toast.makeText(this, "Opening serial port", Toast.LENGTH_LONG);



        Intent intent = getIntent();
        pinmapping = intent.getStringArrayListExtra(MainActivity.EXTRA_SERIAL_MAPPING);
        String partno = intent.getStringExtra(MainActivity.EXTRA_SERIAL_PART);
        title.setText(partno);

        TextView textContent = (TextView) findViewById(R.id.text_content);// Android TextView

    }

    private void serial_usbserial () {
        UsbService usbService;

        usbService.changeBaudRate(9600);

    }

    private void serial_physicaloid (TextView textContent) {
        mPhysicaloid = new Physicaloid(this);

        mPhysicaloid.setBaudrate(9600);

        if(mPhysicaloid.open()) {
            byte[] buf = new byte[256];

            mPhysicaloid.read(buf, buf.length);
            String str = new String(buf);
            textContent.append(str);
            Toast.makeText(this, str, Toast.LENGTH_LONG);
            mPhysicaloid.close();
        } else {
            Toast.makeText(this, "Can't Open", Toast.LENGTH_LONG);

        }
    }
}