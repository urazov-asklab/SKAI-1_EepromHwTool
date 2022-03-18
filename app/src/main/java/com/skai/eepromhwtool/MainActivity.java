package com.skai.eepromhwtool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.lang.System;

public class MainActivity extends AppCompatActivity {
    private Button buttonRead;
    private Button buttonWrite;

    private EditText editTextOffset;
    private EditText editTextSize;
    private EditText editTextRW;

    private String pathFile = "/sys/bus/nvmem/devices/6-00570/nvmem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRead     = (Button)findViewById(R.id.buttonRead);
        buttonWrite    = (Button)findViewById(R.id.buttonWrite);
        editTextOffset = (EditText)findViewById(R.id.editTextOffset);
        editTextSize   = (EditText)findViewById(R.id.editTextSize);
        editTextRW     = (EditText)findViewById(R.id.editTextRW);

        View.OnClickListener clickButtonWrite = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offset   = Integer.parseInt(editTextOffset.getText().toString());
                byte[] bytes = editTextRW.getText().toString().getBytes(StandardCharsets.UTF_8);
                try {
                    RandomAccessFile file = new RandomAccessFile(pathFile, "rw");
                    file.read(bytes, offset, bytes.length);
                    file.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        View.OnClickListener clickButtonRead = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int offset   = Integer.parseInt(editTextOffset.getText().toString());
                int size     = Integer.parseInt(editTextSize.getText().toString());
                byte[] bytes = new byte[size];
                int sizeRead = 10;
                byte[] bufferRead = new byte[sizeRead];
                try {
                    RandomAccessFile file = new RandomAccessFile(pathFile, "r");
                    int counter = offset;
                    while (counter < offset + size) {
                        if (counter + 10 >= offset + size)
                            sizeRead = offset + size - counter;
                        file.read(bufferRead, counter, sizeRead);
                        System.arraycopy(bufferRead,0, bytes, counter - offset, sizeRead);
                        counter += sizeRead;
                    };
                    file.close();
                    String str = new String(bytes, StandardCharsets.UTF_8);
                    editTextRW.setText(str);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        buttonRead.setOnClickListener(clickButtonRead);
        buttonWrite.setOnClickListener(clickButtonWrite);
    }
}
