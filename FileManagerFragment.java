package com.hacker.finalapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FileManagerFragment {

    private TextView fileOutput;
    private Button btnScanFiles, btnDeleteFile, btnEncryptFile, btnShowFiles;
    private List<String> fileSystem;
    private Random random = new Random();

    public void inicializar(View view) {
        fileOutput = (TextView) view.findViewById(R.id.fileOutput);
        btnScanFiles = (Button) view.findViewById(R.id.btnScanFiles);
        btnDeleteFile = (Button) view.findViewById(R.id.btnDeleteFile);
        btnEncryptFile = (Button) view.findViewById(R.id.btnEncryptFile);
        btnShowFiles = (Button) view.findViewById(R.id.btnShowFiles);

        fileSystem = new ArrayList<>();
        initializeFileSystem();
        setupListeners();
        
        fileOutput.setText("💾 GESTOR DE ARCHIVOS DEL SISTEMA\n> Sistema de archivos: EXT4\n> Espacio libre: 1.2 TB\n> Archivos detectados: " + fileSystem.size() + "\n");
    }

    private void initializeFileSystem() {
        fileSystem.add("📄 config.txt - 2.5 KB - " + getCurrentTime());
        fileSystem.add("🔒 passwords.enc - 1.8 KB - " + getCurrentTime());
        fileSystem.add("📁 logs/ - DIR - " + getCurrentTime());
        fileSystem.add("⚡ system.dll - 45.2 KB - " + getCurrentTime());
        fileSystem.add("📄 readme.md - 5.1 KB - " + getCurrentTime());
        fileSystem.add("🔑 keys.pem - 3.2 KB - " + getCurrentTime());
        fileSystem.add("📁 documents/ - DIR - " + getCurrentTime());
        fileSystem.add("📄 backup.zip - 102.4 KB - " + getCurrentTime());
    }

    private void setupListeners() {
        btnScanFiles.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { scanFiles(); }
        });
        btnDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { deleteFile(); }
        });
        btnEncryptFile.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { encryptFile(); }
        });
        btnShowFiles.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showFiles(); }
        });
    }

    private void scanFiles() {
        fileOutput.append("> 🔍 ESCANEANDO SISTEMA DE ARCHIVOS...\n");
        
        String[] newFiles = {
            "📄 temp_cache.tmp - 0.5 KB - " + getCurrentTime(),
            "📁 downloads/ - DIR - " + getCurrentTime(), 
            "🔒 secret_data.enc - 8.7 KB - " + getCurrentTime(),
            "📄 system_log.log - 12.3 KB - " + getCurrentTime()
        };
        
        for (String file : newFiles) {
            if (random.nextDouble() < 0.6) {
                fileSystem.add(file);
                fileOutput.append("> 📁 ENCONTRADO: " + file + "\n");
            }
        }
        
        fileOutput.append("> ✅ ESCANEO COMPLETADO\n");
        fileOutput.append("> 📊 Total archivos: " + fileSystem.size() + "\n");
    }

    private void deleteFile() {
        if (fileSystem.isEmpty()) {
            fileOutput.append("> ❌ No hay archivos para eliminar\n");
            return;
        }
        
        int index = random.nextInt(fileSystem.size());
        String deletedFile = fileSystem.remove(index);
        fileOutput.append("> 🗑️ ELIMINADO: " + deletedFile + "\n");
        fileOutput.append("> 📊 Archivos restantes: " + fileSystem.size() + "\n");
    }

    private void encryptFile() {
        if (fileSystem.isEmpty()) {
            fileOutput.append("> ❌ No hay archivos para cifrar\n");
            return;
        }
        
        int index = random.nextInt(fileSystem.size());
        String file = fileSystem.get(index);
        
        if (file.contains("🔒")) {
            fileOutput.append("> ⚠️  Archivo ya está cifrado: " + file + "\n");
        } else {
            String encryptedFile = file.replace("📄", "🔒").replace(".txt", ".enc");
            fileSystem.set(index, encryptedFile);
            fileOutput.append("> 🔒 CIFRADO: " + encryptedFile + "\n");
        }
    }

    private void showFiles() {
        fileOutput.append("> 📋 LISTA DE ARCHIVOS (" + fileSystem.size() + "):\n");
        for (int i = 0; i < fileSystem.size(); i++) {
            fileOutput.append(">   " + (i + 1) + ". " + fileSystem.get(i) + "\n");
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
