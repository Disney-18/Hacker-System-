package com.hacker.finalapp;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class TrafficMonitorFragment {

    private TextView trafficOutput;
    private Button btnStartMonitor, btnStopMonitor, btnAnalyze;
    private Handler trafficHandler = new Handler();
    private boolean monitoring = false;
    private Random random = new Random();
    private int packetCount = 0;

    public void inicializar(View view) {
        trafficOutput = (TextView) view.findViewById(R.id.trafficOutput);
        btnStartMonitor = (Button) view.findViewById(R.id.btnStartMonitor);
        btnStopMonitor = (Button) view.findViewById(R.id.btnStopMonitor);
        btnAnalyze = (Button) view.findViewById(R.id.btnAnalyze);

        setupListeners();
        
        trafficOutput.setText("📡 MONITOR DE TRÁFICO ACTIVADO\n> Capturando paquetes...\n> Análisis en tiempo real\n");
    }

    private void setupListeners() {
        btnStartMonitor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startMonitoring(); }
        });
        btnStopMonitor.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { stopMonitoring(); }
        });
        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { analyzeTraffic(); }
        });
    }

    private void startMonitoring() {
        monitoring = true;
        packetCount = 0;
        trafficOutput.append("> 🚀 INICIANDO CAPTURA DE PAQUETES...\n");
        
        trafficHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!monitoring) return;
                
                packetCount++;
                String[] protocols = {"TCP", "UDP", "HTTP", "HTTPS", "SSH", "FTP"};
                String[] sources = {"192.168.1.", "10.0.0.", "172.16.0.", "External"};
                String[] destinations = {"Google", "Facebook", "LocalHost", "Unknown"};
                
                String protocol = protocols[random.nextInt(protocols.length)];
                String source = sources[random.nextInt(sources.length)] + random.nextInt(255);
                String dest = destinations[random.nextInt(destinations.length)];
                int size = 64 + random.nextInt(1500);
                int port = 1024 + random.nextInt(64511);
                
                String log = String.format("> 📦 Paquete #%d | %s | %s:%d → %s | %d bytes\n", 
                    packetCount, protocol, source, port, dest, size);
                
                trafficOutput.append(log);
                
                if (monitoring) {
                    trafficHandler.postDelayed(this, 500 + random.nextInt(1000));
                }
            }
        }, 1000);
    }

    private void stopMonitoring() {
        monitoring = false;
        trafficHandler.removeCallbacksAndMessages(null);
        trafficOutput.append("> ⏹️ MONITOREO DETENIDO\n");
        trafficOutput.append("> 📊 TOTAL PAQUETES CAPTURADOS: " + packetCount + "\n");
    }

    private void analyzeTraffic() {
        trafficOutput.append("> 🔍 ANALIZANDO TRÁFICO CAPTURADO...\n");
        trafficOutput.append("> 📈 Estadísticas:\n");
        trafficOutput.append(">    • Paquetes TCP: " + (packetCount * 0.4) + "\n");
        trafficOutput.append(">    • Paquetes UDP: " + (packetCount * 0.3) + "\n");
        trafficOutput.append(">    • Tráfico HTTP: " + (packetCount * 0.2) + "\n");
        trafficOutput.append(">    • Tráfico Encriptado: " + (packetCount * 0.1) + "\n");
        trafficOutput.append("> ⚠️  Posible actividad sospechosa detectada\n");
    }
}
