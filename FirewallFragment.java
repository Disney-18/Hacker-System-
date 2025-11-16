package com.hacker.finalapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirewallFragment {

    private TextView firewallOutput;
    private Button btnStartFirewall, btnStopFirewall, btnAddRule, btnShowRules;
    private List<String> firewallRules;
    private boolean firewallActive = false;
    private Random random = new Random();
    private int attackCount = 0;

    public void inicializar(View view) {
        firewallOutput = (TextView) view.findViewById(R.id.firewallOutput);
        btnStartFirewall = (Button) view.findViewById(R.id.btnStartFirewall);
        btnStopFirewall = (Button) view.findViewById(R.id.btnStopFirewall);
        btnAddRule = (Button) view.findViewById(R.id.btnAddRule);
        btnShowRules = (Button) view.findViewById(R.id.btnShowRules);

        firewallRules = new ArrayList<>();
        initializeDefaultRules();
        setupListeners();
        
        firewallOutput.setText("🛡️ SISTEMA DE FIREWALL ACTIVADO\n> Reglas cargadas: " + firewallRules.size() + "\n> Estado: INACTIVO\n");
    }

    private void initializeDefaultRules() {
        firewallRules.add("🔒 BLOQUEAR Puerto 23 (Telnet)");
        firewallRules.add("🔒 BLOQUEAR Puerto 21 (FTP)");
        firewallRules.add("🔒 BLOQUEAR IP 192.168.1.100");
        firewallRules.add("🟢 PERMITIR Puerto 80 (HTTP)");
        firewallRules.add("🟢 PERMITIR Puerto 443 (HTTPS)");
        firewallRules.add("🔒 BLOQUEAR Puerto 3389 (RDP)");
    }

    private void setupListeners() {
        btnStartFirewall.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startFirewall(); }
        });
        btnStopFirewall.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { stopFirewall(); }
        });
        btnAddRule.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { addCustomRule(); }
        });
        btnShowRules.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showRules(); }
        });
    }

    private void startFirewall() {
        firewallActive = true;
        firewallOutput.append("> 🚀 FIREWALL ACTIVADO\n");
        firewallOutput.append("> 🔍 Monitoreando tráfico...\n");
        
        simulateAttacks();
    }

    private void stopFirewall() {
        firewallActive = false;
        firewallOutput.append("> ⏹️ FIREWALL DESACTIVADO\n");
    }

    private void addCustomRule() {
        String[] newRules = {
            "🔒 BLOQUEAR Puerto 25 (SMTP)",
            "🟢 PERMITIR Puerto 22 (SSH)", 
            "🔒 BLOQUEAR IP 10.0.0.50",
            "🟢 PERMITIR Puerto 53 (DNS)",
            "🔒 BLOQUEAR Puerto 135 (RPC)"
        };
        
        String newRule = newRules[random.nextInt(newRules.length)];
        firewallRules.add(newRule);
        firewallOutput.append("> ✅ REGLA AGREGADA: " + newRule + "\n");
    }

    private void showRules() {
        firewallOutput.append("> 📋 REGLAS ACTIVAS (" + firewallRules.size() + "):\n");
        for (String rule : firewallRules) {
            firewallOutput.append(">   " + rule + "\n");
        }
    }

    private void simulateAttacks() {
        if (!firewallActive) return;
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (firewallActive) {
                    try {
                        Thread.sleep(2000 + random.nextInt(3000));
                        
                        attackCount++;
                        String[] attackTypes = {
                            "Port Scan", "DDoS Attempt", "SQL Injection", 
                            "Brute Force", "Malware", "Phishing"
                        };
                        String[] sourceIPs = {
                            "203.0.113." + random.nextInt(255),
                            "198.51.100." + random.nextInt(255), 
                            "192.0.2." + random.nextInt(255)
                        };
                        
                        final String attackType = attackTypes[random.nextInt(attackTypes.length)];
                        final String sourceIP = sourceIPs[random.nextInt(sourceIPs.length)];
                        final int port = random.nextInt(65535);
                        
                        // Actualizar UI en el hilo principal
                        if (firewallOutput != null) {
                            firewallOutput.post(new Runnable() {
                                @Override
                                public void run() {
                                    firewallOutput.append(String.format(
                                        "> 🚨 ATAQUE #%d: %s desde %s:%d\n> 🛡️ BLOQUEADO por Firewall\n", 
                                        attackCount, attackType, sourceIP, port
                                    ));
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();
    }
}
