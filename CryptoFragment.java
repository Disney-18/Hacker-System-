package com.hacker.finalapp;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;

public class CryptoFragment {

    private TextView cryptoOutput;
    private EditText inputText;
    private Button btnEncrypt, btnDecrypt, btnClear;
    private HashMap<Character, Character> cipherMap;
    private HashMap<Character, Character> decipherMap;

    public void inicializar(View view) {
        cryptoOutput = (TextView) view.findViewById(R.id.cryptoOutput);
        inputText = (EditText) view.findViewById(R.id.inputText);
        btnEncrypt = (Button) view.findViewById(R.id.btnEncrypt);
        btnDecrypt = (Button) view.findViewById(R.id.btnDecrypt);
        btnClear = (Button) view.findViewById(R.id.btnClear);

        initializeCipher();
        setupListeners();

        cryptoOutput.setText("🔓 SISTEMA DE CRIPTOANÁLISIS ACTIVADO\n> Cifrado César (ROT13) listo\n> Base64 disponible\n> Hash MD5 simulado\n");
    }

    private void initializeCipher() {
        cipherMap = new HashMap<>();
        decipherMap = new HashMap<>();
        
        String original = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String shifted = "NOPQRSTUVWXYZABCDEFGHIJKLMnopqrstuvwxyzabcdefghijklm5678901234";
        
        for (int i = 0; i < original.length(); i++) {
            cipherMap.put(original.charAt(i), shifted.charAt(i));
            decipherMap.put(shifted.charAt(i), original.charAt(i));
        }
    }

    private void setupListeners() {
        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { encryptText(); }
        });
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { decryptText(); }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { clearText(); }
        });
    }

    private void encryptText() {
        String text = inputText.getText().toString().trim();
        if (text.isEmpty()) {
            cryptoOutput.append("> ❌ Texto vacío\n");
            return;
        }

        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            encrypted.append(cipherMap.getOrDefault(c, c));
        }

        String result = "🔒 TEXTO CIFRADO: " + encrypted.toString();
        cryptoOutput.append("> " + result + "\n");
        
        String fakeHash = generateFakeMD5(text);
        cryptoOutput.append("> 📋 HASH MD5: " + fakeHash + "\n");
    }

    private void decryptText() {
        String text = inputText.getText().toString().trim();
        if (text.isEmpty()) {
            cryptoOutput.append("> ❌ Texto vacío\n");
            return;
        }

        StringBuilder decrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            decrypted.append(decipherMap.getOrDefault(c, c));
        }

        String result = "🔓 TEXTO DESCIFRADO: " + decrypted.toString();
        cryptoOutput.append("> " + result + "\n");
    }

    private void clearText() {
        inputText.setText("");
        cryptoOutput.setText("🔓 SISTEMA DE CRIPTOANÁLISIS ACTIVADO\n> Buffer limpiado\n");
    }

    private String generateFakeMD5(String text) {
        int fakeHash = text.hashCode() & 0xfffffff;
        return String.format("%08x", fakeHash).toUpperCase();
    }
}
