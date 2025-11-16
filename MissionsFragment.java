package com.hacker.finalapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MissionsFragment {

    private TextView missionsOutput;
    private Button btnNewMission, btnCompleteMission, btnShowMissions;
    private List<Mission> missions;
    private Random random = new Random();
    private int missionId = 1;

    private class Mission {
        int id;
        String title;
        String description;
        int reward; // ✅ AHORA ES DINERO EN DÓLARES
        boolean completed;
        String difficulty;

        Mission(int id, String title, String description, int reward, String difficulty) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.reward = reward;
            this.completed = false;
            this.difficulty = difficulty;
        }
    }

    public void inicializar(View view) {
        missionsOutput = (TextView) view.findViewById(R.id.missionsOutput);
        btnNewMission = (Button) view.findViewById(R.id.btnNewMission);
        btnCompleteMission = (Button) view.findViewById(R.id.btnCompleteMission);
        btnShowMissions = (Button) view.findViewById(R.id.btnShowMissions);

        missions = new ArrayList<>();
        initializeMissions();
        setupListeners();
        
        missionsOutput.setText("📊 PANEL DE MISIONES ACTIVADO\n> Sistema de contratos cargado\n> Misiones disponibles: " + getActiveMissionsCount() + "\n");
    }

    private void initializeMissions() {
        // ✅ RECOMPENSAS ACTUALIZADAS A DÓLARES
        missions.add(new Mission(missionId++, "Infiltración Básica", 
            "Penetrar servidor corporativo nivel 1", 500, "Fácil")); // $500
        missions.add(new Mission(missionId++, "Robo de Datos", 
            "Extraer archivos confidenciales", 1000, "Media")); // $1,000
        missions.add(new Mission(missionId++, "Desactivar Firewall", 
            "Bypass sistema de seguridad", 1500, "Difícil")); // $1,500
    }

    private void setupListeners() {
        btnNewMission.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { generateNewMission(); }
        });
        btnCompleteMission.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { completeRandomMission(); }
        });
        btnShowMissions.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showAllMissions(); }
        });
    }

    private void generateNewMission() {
        String[] titles = {
            "Hackeo de Red", "Criptoanálisis", "Suplantación DNS",
            "Inyección SQL", "Ataque DDoS", "Phishing Avanzado"
        };
        
        String[] descriptions = {
            "Comprometer seguridad de red objetivo",
            "Descifrar comunicaciones encriptadas", 
            "Redirigir tráfico de dominio específico",
            "Extraer datos de base de datos vulnerable",
            "Sobrecargar servidor objetivo",
            "Crear campaña de phishing convincente"
        };
        
        String[] difficulties = {"Fácil", "Media", "Difícil", "Élite"};
        // ✅ RECOMPENSAS EN DÓLARES
        int[] rewards = {500, 1000, 2000, 5000}; // $500, $1,000, $2,000, $5,000
        
        int diffIndex = random.nextInt(difficulties.length);
        
        Mission newMission = new Mission(
            missionId++,
            titles[random.nextInt(titles.length)],
            descriptions[random.nextInt(descriptions.length)],
            rewards[diffIndex], // ✅ DINERO EN DÓLARES
            difficulties[diffIndex]
        );
        
        missions.add(newMission);
        missionsOutput.append("> 🎯 NUEVA MISIÓN DISPONIBLE!\n");
        missionsOutput.append(">   ID: " + newMission.id + " | " + newMission.difficulty + "\n");
        missionsOutput.append(">   " + newMission.title + "\n");
        // ✅ MOSTRAR RECOMPENSA EN DÓLARES
        missionsOutput.append(">   Recompensa: $" + newMission.reward + "\n");
        missionsOutput.append(">   Objetivo: " + newMission.description + "\n");
    }

    private void completeRandomMission() {
        List<Mission> activeMissions = getActiveMissionsList();
        if (activeMissions.isEmpty()) {
            missionsOutput.append("> ❌ No hay misiones activas para completar\n");
            return;
        }
        
        Mission mission = activeMissions.get(random.nextInt(activeMissions.size()));
        mission.completed = true;
        
        // ✅ OBTENER INSTANCIA DE PLAYERPROGRESS PARA AGREGAR DINERO
        PlayerProgress playerProgress = new PlayerProgress(missionsOutput.getContext());
        playerProgress.addDollars(mission.reward);
        
        missionsOutput.append("> ✅ MISIÓN COMPLETADA!\n");
        missionsOutput.append(">   ID: " + mission.id + " | " + mission.difficulty + "\n");
        missionsOutput.append(">   " + mission.title + "\n");
        // ✅ MOSTRAR GANANCIA EN DÓLARES
        missionsOutput.append(">   🎁 Recompensa obtenida: $" + mission.reward + "\n");
        missionsOutput.append(">   💰 Total actual: " + playerProgress.getMoneyFormatted() + "\n");
        missionsOutput.append(">   Misiones activas restantes: " + getActiveMissionsCount() + "\n");
    }

    private void showAllMissions() {
        missionsOutput.append("> 📋 LISTA DE MISIONES:\n");
        missionsOutput.append(">   TOTAL: " + missions.size() + " | ACTIVAS: " + getActiveMissionsCount() + "\n");
        
        for (Mission mission : missions) {
            String status = mission.completed ? "✅" : "🟡";
            // ✅ MOSTRAR RECOMPENSA EN DÓLARES
            missionsOutput.append(String.format(
                ">   %s ID: %d | %s | $%d\n", // ✅ CAMBIADO A $
                status, mission.id, mission.title, mission.reward
            ));
        }
    }

    private int getActiveMissionsCount() {
        int count = 0;
        for (Mission mission : missions) {
            if (!mission.completed) count++;
        }
        return count;
    }

    private List<Mission> getActiveMissionsList() {
        List<Mission> active = new ArrayList<>();
        for (Mission mission : missions) {
            if (!mission.completed) active.add(mission);
        }
        return active;
    }
}
