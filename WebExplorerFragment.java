package com.hacker.finalapp;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class WebExplorerFragment {

    private TextView webOutput;
    private Button btnStartBrowse, btnStopBrowse, btnClearLogs;
    private Handler webHandler = new Handler();
    private boolean browsing = false;
    private Random random = new Random();
    
    private String[] websites = {
        "https://darknet-market.org", "https://secret-forums.com", 
        "https://hidden-wiki.net", "https://bitcoin-mixer.io",
        "https://anonymous-chat.org", "https://encrypted-mail.com"
    };
    
    private String[] actions = {
        "Navegando anónimamente", "Descargando datos", "Analizando seguridad",
        "Bypasseando firewall", "Escaneando puertos", "Recolectando información"
    };

    public void inicializar(View view) {
        webOutput = (TextView) view.findViewById(R.id.webOutput);
        btnStartBrowse = (Button) view.findViewById(R.id.btnStartBrowse);
        btnStopBrowse = (Button) view.findViewById(R.id.btnStopBrowse);
        btnClearLogs = (Button) view.findViewById(R.id.btnClearLogs);

        setupListeners();
        
        webOutput.setText("🌐 EXPLORADOR WEB ANÓNIMO ACTIVADO\n> TOR Simulation: ACTIVADO\n> Proxy: 7 capas configurado\n> User Agent: Firefox/Linux\n");
    }

    private void setupListeners() {
        btnStartBrowse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startBrowsing(); }
        });
        btnStopBrowse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { stopBrowsing(); }
        });
        btnClearLogs.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { clearLogs(); }
        });
    }

    private void startBrowsing() {
        browsing = true;
        webOutput.append("> 🚀 INICIANDO NAVEGACIÓN ANÓNIMA...\n");
        
        webHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!browsing) return;
                
                String site = websites[random.nextInt(websites.length)];
                String action = actions[random.nextInt(actions.length)];
                int progress = random.nextInt(100);
                
                String log = String.format("> 🌐 %s\n> ⚡ %s (%d%%)\n> 📍 IP Enmascarada: 192.168.%d.%d\n", 
                    site, action, progress, random.nextInt(255), random.nextInt(255));
                
                webOutput.append(log);
                
                if (browsing) {
                    webHandler.postDelayed(this, 2000 + random.nextInt(3000));
                }
            }
        }, 1500);
    }

    private void stopBrowsing() {
        browsing = false;
        webHandler.removeCallbacksAndMessages(null);
        webOutput.append("> ⏹️ NAVEGACIÓN DETENIDA\n");
    }

    private void clearLogs() {
        webOutput.setText("🌐 EXPLORADOR WEB ANÓNIMO ACTIVADO\n> Logs limpiados\n");
    }
}
