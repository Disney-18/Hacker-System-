package com.hacker.finalapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutFragment {

    private TextView aboutOutput;
    private Button btnShowCredits, btnShowVersion, btnShowFeatures;

    public void inicializar(View view) {
        aboutOutput = (TextView) view.findViewById(R.id.aboutOutput);
        btnShowCredits = (Button) view.findViewById(R.id.btnShowCredits);
        btnShowVersion = (Button) view.findViewById(R.id.btnShowVersion);
        btnShowFeatures = (Button) view.findViewById(R.id.btnShowFeatures);

        setupListeners();
        
        showWelcomeMessage();
    }

    private void setupListeners() {
        btnShowCredits.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showCredits(); }
        });
        btnShowVersion.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showVersionInfo(); }
        });
        btnShowFeatures.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { showFeatures(); }
        });
    }

    private void showWelcomeMessage() {
        aboutOutput.setText("╔══════════════════════════════════╗\n");
        aboutOutput.append("║        🖥️ HACKER SYSTEM 🖥️        ║\n");
        aboutOutput.append("║                v8.0        ║\n");
        aboutOutput.append("╠══════════════════════════════════╣\n");
        aboutOutput.append("║    SISTEMA DE SIMULACIÓN DE      ║\n");
        aboutOutput.append("║      CIBERSEGURIDAD AVANZADA     ║\n");
        aboutOutput.append("╚══════════════════════════════════╝\n\n");
        aboutOutput.append("> Bienvenido al sistema terminal más avanzado\n");
        aboutOutput.append("> Desarrollado para entrenamiento ético en hacking\n");
        aboutOutput.append("> 9 módulos especializados integrados\n");
        aboutOutput.append("> Interfaz de terminal profesional\n\n");
        aboutOutput.append("> Usa los botones para explorar la información del sistema\n");
    }

    private void showCredits() {
        aboutOutput.setText("╔══════════════════════════════════╗\n");
        aboutOutput.append("║           👨‍💻 CRÉDITOS                   ║\n");
        aboutOutput.append("╠══════════════════════════════════╣\n");
        aboutOutput.append("║ DESARROLLADO POR:                ║\n");
        aboutOutput.append("║   Equipo VoxApp Media S.L.       ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ LÍDER DE PROYECTO:               ║\n");
        aboutOutput.append("║   [Disney Gutiérrez Guevara]     ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ DESARROLLADORES:                 ║\n");
        aboutOutput.append("║   • Programador Principal        ║\n");
        aboutOutput.append("║   • Diseñador de UI/UX           ║\n");
        aboutOutput.append("║   • Especialista en Seguridad    ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ AGRADECIMIENTOS:                 ║\n");
        aboutOutput.append("║   • Comunidad de Ethical Hacking ║\n");
        aboutOutput.append("║   • Proyectos Open Source        ║\n");
        aboutOutput.append("║   • Beta Testers                 ║\n");
        aboutOutput.append("╚══════════════════════════════════╝\n\n");
        aboutOutput.append("> Este proyecto es educativo y tiene como fin\n");
        aboutOutput.append("> promover el aprendizaje de ciberseguridad\n");
        aboutOutput.append("> de manera ética y responsable.\n");
    }

    private void showVersionInfo() {
        aboutOutput.setText("╔══════════════════════════════════╗\n");
        aboutOutput.append("║         ℹ️ INFORMACIÓN           ║\n");
        aboutOutput.append("║            DEL SISTEMA           ║\n");
        aboutOutput.append("╠══════════════════════════════════╣\n");
        aboutOutput.append("║ VERSIÓN: 8.0.1 Professional      ║\n");
        aboutOutput.append("║ BUILD: 2025.01.15.RELEASE        ║\n");
        aboutOutput.append("║ PLATAFORMA: Android AIDE         ║\n");
        aboutOutput.append("║ ARQUITECTURA: ARM/x86            ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ MÓDULOS ACTIVOS:                 ║\n");
        aboutOutput.append("║   • Terminal de Juego (100%)     ║\n");
        aboutOutput.append("║   • Escáner de Red (100%)        ║\n");
        aboutOutput.append("║   • Criptoanálisis (100%)        ║\n");
        aboutOutput.append("║   • Explorador Web (100%)        ║\n");
        aboutOutput.append("║   • Monitor de Tráfico (100%)    ║\n");
        aboutOutput.append("║   • Firewall (100%)              ║\n");
        aboutOutput.append("║   • Gestor de Archivos (100%)    ║\n");
        aboutOutput.append("║   • Panel de Misiones (100%)     ║\n");
        aboutOutput.append("║   • Acerca del Sistema (100%)    ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ ESTADO: 🟢 OPERATIVO             ║\n");
        aboutOutput.append("║ LICENCIA: Educativa              ║\n");
        aboutOutput.append("╚══════════════════════════════════╝\n\n");
        aboutOutput.append("> Sistema optimizado para Android\n");
        aboutOutput.append("> Compatible con AIDE y Android Studio\n");
        aboutOutput.append("> Tiempo de desarrollo: 2 meses\n");
        aboutOutput.append("> Líneas de código: 5,000+ aprox.\n");
    }

    private void showFeatures() {
        aboutOutput.setText("╔══════════════════════════════════╗\n");
        aboutOutput.append("║         🚀 CARACTERÍSTICAS        ║\n");
        aboutOutput.append("╠══════════════════════════════════╣\n");
        aboutOutput.append("║ ✅ 9 PESTAÑAS ESPECIALIZADAS      ║\n");
        aboutOutput.append("║ ✅ INTERFAZ TERMINAL PROFESIONAL  ║\n");
        aboutOutput.append("║ ✅ SISTEMA DE HABILIDADES         ║\n");
        aboutOutput.append("║ ✅ MERCADO NEGRO VIRTUAL          ║\n");
        aboutOutput.append("║ ✅ SISTEMA DE REPUTACIÓN          ║\n");
        aboutOutput.append("║ ✅ EVENTOS ALEATORIOS             ║\n");
        aboutOutput.append("║ ✅ POWER-UOS Y HERRAMIENTAS       ║\n");
        aboutOutput.append("║ ✅ BASE DE DATOS SQLITE           ║\n");
        aboutOutput.append("║ ✅ SONIDOS Y EFECTOS VISUALES     ║\n");
        aboutOutput.append("║ ✅ GUARDADO AUTOMÁTICO            ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ 🎮 MODOS DE JUEGO:               ║\n");
        aboutOutput.append("║   • Modo Carrera Progresiva      ║\n");
        aboutOutput.append("║   • Modo Sistema Avanzado        ║\n");
        aboutOutput.append("║   • Múltiples tipos de desafíos  ║\n");
        aboutOutput.append("║                                  ║\n");
        aboutOutput.append("║ 🔧 HERRAMIENTAS INCLUIDAS:       ║\n");
        aboutOutput.append("║   • Scanner de Red Simulado      ║\n");
        aboutOutput.append("║   • Sistema de Cifrado ROT13     ║\n");
        aboutOutput.append("║   • Firewall con Reglas          ║\n");
        aboutOutput.append("║   • Monitor de Tráfico           ║\n");
        aboutOutput.append("║   • Explorador Web Anónimo       ║\n");
        aboutOutput.append("║   • Gestor de Archivos           ║\n");
        aboutOutput.append("╚══════════════════════════════════╝\n\n");
        aboutOutput.append("> Características técnicas:\n");
        aboutOutput.append("> • Diseño responsive para móviles\n");
        aboutOutput.append("> • Optimizado para rendimiento\n");
        aboutOutput.append("> • Código modular y escalable\n");
        aboutOutput.append("> • Fácil de extender y modificar\n");
    }
}
