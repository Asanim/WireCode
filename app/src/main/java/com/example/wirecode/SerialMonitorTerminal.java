package com.example.wirecode;

import android.content.Intent;
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

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import java.util.ArrayList;

public class SerialMonitorTerminal extends AppCompatActivity {
    Button btOpen, btClose,  btWrite;
    EditText etWrite;
    TextView tvRead;
    Spinner spBaud;
    CheckBox cbAutoscroll;

    Physicaloid mPhysicaloid; // initialising library
    ArrayList<String> pinmapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_monitor);

        TextView title = (TextView) findViewById(R.id.text_title);
        btOpen  = (Button) findViewById(R.id.btOpen);
        btClose = (Button) findViewById(R.id.btClose);
        btWrite = (Button) findViewById(R.id.btWrite);
        etWrite = (EditText) findViewById(R.id.etWrite);
        tvRead  = (TextView) findViewById(R.id.tvRead);
        spBaud = (Spinner) findViewById(R.id.spinner);
        cbAutoscroll = (CheckBox)findViewById(R.id.autoscroll);
        mPhysicaloid = new Physicaloid(this);

        Intent intent = getIntent();

        pinmapping = intent.getStringArrayListExtra(MainActivity.EXTRA_SERIAL_MAPPING);
        String partno = intent.getStringExtra(MainActivity.EXTRA_SERIAL_PART);
        title.setText(partno);
    }

    public void onClickOpen(View v) {
        String baudtext = spBaud.getSelectedItem().toString();
        switch (baudtext) {
            case "300 baud":
                mPhysicaloid.setBaudrate(300);
                break;
            case "1200 baud":
                mPhysicaloid.setBaudrate(1200);
                break;
            case "2400 baud":
                mPhysicaloid.setBaudrate(2400);
                break;
            case "4800 baud":
                mPhysicaloid.setBaudrate(4800);
                break;
            case "9600 baud":
                mPhysicaloid.setBaudrate(9600);
                break;
            case "19200 baud":
                mPhysicaloid.setBaudrate(19200);
                break;
            case "38400 baud":
                mPhysicaloid.setBaudrate(38400);
                break;
            case "576600 baud":
                mPhysicaloid.setBaudrate(576600);
                break;
            case "744880 baud":
                mPhysicaloid.setBaudrate(744880);
                break;
            case "115200 baud":
                mPhysicaloid.setBaudrate(115200);
                break;
            case "230400 baud":
                mPhysicaloid.setBaudrate(230400);
                break;
            case "250000 baud":
                mPhysicaloid.setBaudrate(250000);
                break;
            default:
                mPhysicaloid.setBaudrate(9600);
        }

        if(mPhysicaloid.open()) {
            setEnabledUi(true);

            if(cbAutoscroll.isChecked())
            {
                tvRead.setMovementMethod(new ScrollingMovementMethod());
            }
            mPhysicaloid.addReadListener(new ReadLisener() {
                @Override
                public void onRead(int size) {
                    byte[] buf = new byte[size];
                    mPhysicaloid.read(buf, size);
                    String mapping = pin_mapping(buf);

                    tvAppend(tvRead, Html.fromHtml("<font color=blue>" + "pin: "+new String(buf)+"Mapping: "+mapping + "</font>"));
                }
            });
        } else {
            Toast.makeText(this, "Cannot open", Toast.LENGTH_LONG).show();
        }
    }

    public String pin_mapping (byte[] buffer) {
         String num_string = buffer.toString();
         int pin_value = Integer.parseInt(num_string);

        return pinmapping.get(pin_value);

    }

    public void onClickClose(View v) {
        if(mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
            setEnabledUi(false);
        }
    }

    public void onClickWrite(View v) {
        String str = etWrite.getText().toString()+"\r\n";
        if(str.length()>0) {
            byte[] buf = str.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            spBaud.setEnabled(false);
            cbAutoscroll.setEnabled(false);
            btClose.setEnabled(true);
            btWrite.setEnabled(true);
            etWrite.setEnabled(true);
        } else {
            btOpen.setEnabled(true);
            spBaud.setEnabled(true);
            cbAutoscroll.setEnabled(true);
            btClose.setEnabled(false);
            btWrite.setEnabled(false);
            etWrite.setEnabled(false);
        }
    }

    Handler mHandler = new Handler();
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }
}