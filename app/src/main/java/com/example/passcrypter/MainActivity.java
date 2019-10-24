package com.example.passcrypter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText text, password;
    TextView result;
    Button start, end;
    String input, output, pass, AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        result = (TextView) findViewById(R.id.resultado);
        start = (Button) findViewById(R.id.startProcess);
        end = (Button) findViewById(R.id.endProcess);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    output = Encryption(text.getText().toString(),password.getText().toString());
                    result.setText(output);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    output = Decryption(output,password.getText().toString());
                    result.setText(output);
                } catch (Exception e) {
                    Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String Decryption(String text, String password) throws Exception {
        SecretKeySpec key = generateKeySpec(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decVal = Base64.decode(text,Base64.DEFAULT);
        byte[] finalVal = c.doFinal(decVal);
        String result = new String(finalVal);
        return result;
    }

    private String Encryption(String text, String password) throws Exception {
        SecretKeySpec key = generateKeySpec(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = cipher.doFinal(text.getBytes());
        String result = Base64.encodeToString(encVal,Base64.DEFAULT);
        return result;
    }

    private SecretKeySpec generateKeySpec(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
}
