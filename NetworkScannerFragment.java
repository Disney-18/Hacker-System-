package com.hacker.finalapp;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class NetworkScannerFragment {
    
    private TextView scannerOutput;
    private Button btnStartScan, btnStopScan;
    private Handler scannerHandler = new Handler();
    private boolean scanning = false;
    private Random random = new Random();
    private int deviceCount = 0;

    public void inicializar(View view) {
        scannerOutput = (TextView) view.findViewById(R.id.scannerOutput);
        btnStartScan = (Button) view.findViewById(R.id.btnStartScan);
        btnStopScan = (Button) view.findViewById(R.id.btnStopScan);
        
        btnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNetworkScan();
            }
        });
        
        btnStopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopNetworkScan();
            }
        });
        
        scannerOutput.setText("🔍 SISTEMA DE ESCANEO DE RED\n> Listo para escanear...\n");
    }

    private void startNetworkScan() {
        scanning = true;
        deviceCount = 0;
        scannerOutput.setText("🔍 INICIANDO ESCANEO DE RED...\n");
        
        scannerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!scanning) return;
                
                deviceCount++;
                String[] deviceTypes = {"Router", "PC", "Smartphone", "Server", "IoT Device"};
                String[] osTypes = {"Windows", "Linux", "Android", "iOS", "RouterOS"};
                
                String device = deviceTypes[random.nextInt(deviceTypes.length)];
                String os = osTypes[random.nextInt(osTypes.length)];
                String ip = "192.168.1." + (random.nextInt(254) + 1);
                int ports = random.nextInt(10) + 1;
                
                String log = String.format("> 📱 %s | %s | %s | %d puertos\n", 
                    ip, device, os, ports);
                
                scannerOutput.append(log);
                
                if (scanning) {
                    scannerHandler.postDelayed(this, 1000 + random.nextInt(2000));
                }
            }
        }, 1000);
    }

    private void stopNetworkScan() {
        scanning = false;
        scannerHandler.removeCallbacksAndMessages(null);
        scannerOutput.append("\n> ⏹️ ESCANEO DETENIDO\n");
        scannerOutput.append("> 📊 Total dispositivos: " + deviceCount + "\n");
    }
}
