package com.hacker.finalapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {

    // Variables del juego - ACTUALIZADAS
    private TextView terminalOutput, levelText, scoreText, attemptsText;
    private TextView timerText, comboText, bonusText, desafioEspecial;
    private TextView progressText, estadoJuego, horaActual, comandoDisplay;
    private LinearLayout tecladoContainer, panelComandos;
    private Button powerupTiempo, powerupPista, powerupIntento;
    private Button btnSistema, btnPausa, btnSonido, btnReiniciar;
    private ScrollView terminalScroll;

    private int nivelActual = 1;
    private int dineroPartida = 0;
    private int intentosRestantes = 3;
    private int comboActual = 0;
    private int bonusNivel = 0;
    private int tiempoRestante = 30;
    private boolean juegoActivo = true;
    private boolean juegoPausado = false;
    private boolean modoSistema = false;
    private String modoActual = "CONTRASEÑA";
    private String respuestaCorrecta = "";
    private StringBuilder comandoActual = new StringBuilder();

    private boolean sonidoActivado = true;
    private int totalAciertos = 0;
    private int totalFallos = 0;
    private int mejorDineroPartida = 0;
    private int mejorCombo = 0;
    private int juegosCompletados = 0;
    private long tiempoInicioSesion;
    private long tiempoTotalJugado = 0;
    private boolean powerupPistaDisponible = false;
    private boolean powerupIntentoDisponible = false;
    private int powerupsUsadosEstaPartida = 0;
    private boolean fuePausadoPorSistema = false;
    private long tiempoCuandoSePauso = 0;

    // NUEVAS VARIABLES PARA SISTEMA PROFESIONAL
    private AnalyticsManager analytics;
    private UserPreferencesManager preferences;
    private PlayerProgress playerProgress;

    // Sistema de guardado
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "HackerGamePrefs";

    // Arrays de datos - ACTUALIZADOS CON DINERO
    private String[] contraseñasBase = {
        "ALFA", "BETA", "GAMMA", "DELTA", "OMEGA", "SIGMA", "ZULU", "CYBER", "ROGUE", "QUANTUM"
    };
    private String[] mensajesExito = {
        "> ✅ ¡TRANSFERENCIA EXITOSA!", "> 🔓 ¡FONDOS SECUESTRADOS!", "> 💾 ¡DINERO EXTRACTADO!"
    };
    private String[] mensajesError = {
        "> ❌ ¡TRANSACCIÓN BLOQUEADA!", "> 🚫 ¡CUENTA CONGELADA!", "> ⚠️  ¡ALERTA BANCARIA!"
    };

    private Random random = new Random();
    private CountDownTimer temporizador;
    private Handler handler = new Handler();
    private Handler horaHandler = new Handler();

    // SISTEMA DE PESTAÑAS
    private LinearLayout tabContainer;
    private String currentTab = "terminal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // INICIALIZAR SISTEMAS PROFESIONALES
        analytics = AnalyticsManager.getInstance(this);
        preferences = UserPreferencesManager.getInstance(this);
        playerProgress = new PlayerProgress(this);

        analytics.logEvent(AnalyticsManager.EVENT_APP_OPEN);

        inicializarSistemaTabs();

        cargarDatosGuardados();

        verificarEstadoPrevio();

        // ✅ VERIFICAR Y MOSTRAR TUTORIAL SI ES PRIMERA VEZ
        verificarYMostrarTutorial();

        tiempoInicioSesion = System.currentTimeMillis();
    }

    // ✅ NUEVO MÉTODO: Verificar y mostrar tutorial
    private void verificarYMostrarTutorial() {
        if (playerProgress != null && !playerProgress.isTutorialCompleted()) {
            // Es primera vez - mostrar tutorial automáticamente
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mostrarTabTutorial();
                }
            }, 1000);
        }
    }

    // ===== NUEVO MÉTODO PARA TIPOS DE MISIÓN =====
    private String getMissionTypeByLevel(int nivel) {
        switch(nivel) {
            case 1: return "HACKEO_BASICO";
            case 2: return "ROBO_DATOS"; 
            case 3: return "EXTRACCION_CRYPTO";
            case 4: return "MINADO_PASIVO";
            case 5: return "ATAQUE_ELITE";
            default: return "HACKEO_BASICO";
        }
    }

    private void inicializarSistemaTabs() {
        setContentView(R.layout.activity_main_tabs);

        tabContainer = (LinearLayout) findViewById(R.id.tabContainer);

        // Configurar listeners de los tabs - ACTUALIZADO CON TUTORIAL
        findViewById(R.id.tabTerminal).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabTerminal(); }
        });
        findViewById(R.id.tabScanner).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabScanner(); }
        });
        findViewById(R.id.tabCrypto).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabCrypto(); }
        });
        findViewById(R.id.tabWeb).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabWeb(); }
        });
        findViewById(R.id.tabTraffic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabTraffic(); }
        });
        findViewById(R.id.tabFirewall).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabFirewall(); }
        });
        findViewById(R.id.tabFiles).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabFiles(); }
        });
        findViewById(R.id.tabMissions).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabMissions(); }
        });
        // ✅ NUEVO LISTENER PARA TUTORIAL
        findViewById(R.id.tabTutorial).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabTutorial(); }
        });
        findViewById(R.id.tabAbout).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { mostrarTabAbout(); }
        });

        // Mostrar terminal por defecto
        mostrarTabTerminal();
    }

    private void mostrarTabTerminal() {
        currentTab = "terminal";
        tabContainer.removeAllViews();
        View terminalView = getLayoutInflater().inflate(R.layout.fragment_terminal, tabContainer, false);
        tabContainer.addView(terminalView);

        // Analytics
        if (analytics != null) {
            analytics.logModuleOpened("terminal");
        }

        // Animación de transición
        if (preferences != null && preferences.areAnimationsEnabled()) {
            AnimationHelper.slideInFromRight(terminalView);
        }

        // Re-inicializar el juego en el terminal
        inicializarVistasDesdeLayout(terminalView);
        inicializarSonidos();
        configurarJuego();
        iniciarNivel();
        iniciarActualizacionHora();
        actualizarTabActivo();
    }

    private void mostrarTabScanner() {
        currentTab = "scanner";
        tabContainer.removeAllViews();
        View scannerView = getLayoutInflater().inflate(R.layout.fragment_network_scanner, tabContainer, false);
        tabContainer.addView(scannerView);

        if (analytics != null) {
            analytics.logModuleOpened("network_scanner");
        }
        actualizarTabActivo();

        NetworkScannerFragment scanner = new NetworkScannerFragment();
        scanner.inicializar(scannerView);
    }

    private void mostrarTabCrypto() {
        currentTab = "crypto";
        tabContainer.removeAllViews();
        View cryptoView = getLayoutInflater().inflate(R.layout.fragment_crypto, tabContainer, false);
        tabContainer.addView(cryptoView);

        if (analytics != null) {
            analytics.logModuleOpened("crypto");
        }
        actualizarTabActivo();

        CryptoFragment crypto = new CryptoFragment();
        crypto.inicializar(cryptoView);
    }

    private void mostrarTabWeb() {
        currentTab = "web";
        tabContainer.removeAllViews();
        View webView = getLayoutInflater().inflate(R.layout.fragment_web, tabContainer, false);
        tabContainer.addView(webView);

        if (analytics != null) {
            analytics.logModuleOpened("web_explorer");
        }
        actualizarTabActivo();

        WebExplorerFragment web = new WebExplorerFragment();
        web.inicializar(webView);
    }

    private void mostrarTabTraffic() {
        currentTab = "traffic";
        tabContainer.removeAllViews();
        View trafficView = getLayoutInflater().inflate(R.layout.fragment_traffic, tabContainer, false);
        tabContainer.addView(trafficView);

        if (analytics != null) {
            analytics.logModuleOpened("traffic_monitor");
        }
        actualizarTabActivo();

        TrafficMonitorFragment traffic = new TrafficMonitorFragment();
        traffic.inicializar(trafficView);
    }

    private void mostrarTabFirewall() {
        currentTab = "firewall";
        tabContainer.removeAllViews();
        View firewallView = getLayoutInflater().inflate(R.layout.fragment_firewall, tabContainer, false);
        tabContainer.addView(firewallView);

        if (analytics != null) {
            analytics.logModuleOpened("firewall");
        }
        actualizarTabActivo();

        FirewallFragment firewall = new FirewallFragment();
        firewall.inicializar(firewallView);
    }

    private void mostrarTabFiles() {
        currentTab = "files";
        tabContainer.removeAllViews();
        View filesView = getLayoutInflater().inflate(R.layout.fragment_files, tabContainer, false);
        tabContainer.addView(filesView);

        if (analytics != null) {
            analytics.logModuleOpened("file_manager");
        }
        actualizarTabActivo();

        FileManagerFragment files = new FileManagerFragment();
        files.inicializar(filesView);
    }

    private void mostrarTabMissions() {
        currentTab = "missions";
        tabContainer.removeAllViews();
        View missionsView = getLayoutInflater().inflate(R.layout.fragment_missions, tabContainer, false);
        tabContainer.addView(missionsView);

        if (analytics != null) {
            analytics.logModuleOpened("missions");
        }
        actualizarTabActivo();

        MissionsFragment missions = new MissionsFragment();
        missions.inicializar(missionsView);
    }

    // ✅ NUEVO MÉTODO: Mostrar pestaña Tutorial
    private void mostrarTabTutorial() {
        currentTab = "tutorial";
        tabContainer.removeAllViews();
        View tutorialView = getLayoutInflater().inflate(R.layout.fragment_tutorial, tabContainer, false);
        tabContainer.addView(tutorialView);

        if (analytics != null) {
            analytics.logModuleOpened("tutorial");
        }

        // Animación de transición
        if (preferences != null && preferences.areAnimationsEnabled()) {
            AnimationHelper.slideInFromRight(tutorialView);
        }

        // Inicializar el fragment de tutorial
        TutorialFragment tutorial = new TutorialFragment();
        tutorial.inicializar(tutorialView);
        
        actualizarTabActivo();
    }

    private void mostrarTabAbout() {
        currentTab = "about";
        tabContainer.removeAllViews();
        View aboutView = getLayoutInflater().inflate(R.layout.fragment_about, tabContainer, false);
        tabContainer.addView(aboutView);

        if (analytics != null) {
            analytics.logModuleOpened("about");
        }
        actualizarTabActivo();

        AboutFragment about = new AboutFragment();
        about.inicializar(aboutView);
    }

    // ✅ MÉTODO ACTUALIZADO CON NUEVO TAB
    private void actualizarTabActivo() {
        int[] tabIds = {R.id.tabTerminal, R.id.tabScanner, R.id.tabCrypto, R.id.tabWeb, 
            R.id.tabTraffic, R.id.tabFirewall, R.id.tabFiles, R.id.tabMissions, 
            R.id.tabTutorial, R.id.tabAbout}; // ✅ AÑADIDO tabTutorial

        for (int tabId : tabIds) {
            findViewById(tabId).setBackgroundColor(0xFF001100);
        }

        int activeColor = 0xFF00AA00;
        switch (currentTab) {
            case "terminal": findViewById(R.id.tabTerminal).setBackgroundColor(activeColor); break;
            case "scanner": findViewById(R.id.tabScanner).setBackgroundColor(activeColor); break;
            case "crypto": findViewById(R.id.tabCrypto).setBackgroundColor(activeColor); break;
            case "web": findViewById(R.id.tabWeb).setBackgroundColor(activeColor); break;
            case "traffic": findViewById(R.id.tabTraffic).setBackgroundColor(activeColor); break;
            case "firewall": findViewById(R.id.tabFirewall).setBackgroundColor(activeColor); break;
            case "files": findViewById(R.id.tabFiles).setBackgroundColor(activeColor); break;
            case "missions": findViewById(R.id.tabMissions).setBackgroundColor(activeColor); break;
            case "tutorial": findViewById(R.id.tabTutorial).setBackgroundColor(activeColor); break; // ✅ NUEVO
            case "about": findViewById(R.id.tabAbout).setBackgroundColor(activeColor); break;
        }
    }

    // Método para inicializar vistas desde un layout inflado
    private void inicializarVistasDesdeLayout(View layout) {
        terminalOutput = (TextView) layout.findViewById(R.id.terminalOutput);
        levelText = (TextView) layout.findViewById(R.id.levelText);
        scoreText = (TextView) layout.findViewById(R.id.scoreText);
        attemptsText = (TextView) layout.findViewById(R.id.attemptsText);
        timerText = (TextView) layout.findViewById(R.id.timerText);
        comboText = (TextView) layout.findViewById(R.id.comboText);
        bonusText = (TextView) layout.findViewById(R.id.bonusText);
        desafioEspecial = (TextView) layout.findViewById(R.id.desafioEspecial);
        progressText = (TextView) layout.findViewById(R.id.progressText);
        estadoJuego = (TextView) layout.findViewById(R.id.estadoJuego);
        horaActual = (TextView) layout.findViewById(R.id.horaActual);
        comandoDisplay = (TextView) layout.findViewById(R.id.comandoDisplay);
        tecladoContainer = (LinearLayout) layout.findViewById(R.id.tecladoContainer);
        panelComandos = (LinearLayout) layout.findViewById(R.id.panelComandos);
        terminalScroll = (ScrollView) layout.findViewById(R.id.terminalScroll);

        powerupTiempo = (Button) layout.findViewById(R.id.powerupTiempo);
        powerupPista = (Button) layout.findViewById(R.id.powerupPista);
        powerupIntento = (Button) layout.findViewById(R.id.powerupIntento);

        btnSistema = (Button) layout.findViewById(R.id.btnSistema);
        btnPausa = (Button) layout.findViewById(R.id.btnPausa);
        btnSonido = (Button) layout.findViewById(R.id.btnSonido);
        btnReiniciar = (Button) layout.findViewById(R.id.btnReiniciar);

        configurarListeners();
    }

    private void configurarListeners() {
        btnSistema.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { toggleModoSistema(v); }
        });
        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { togglePausa(v); }
        });
        btnSonido.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { toggleSonido(v); }
        });
        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { reiniciarJuegoDesdeTeclado(v); }
        });

        powerupTiempo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { 
                if (!juegoPausado && juegoActivo) {
                    powerupsUsadosEstaPartida++;
                    usarPowerupTiempo();
                }
            }
        });

        powerupPista.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { 
                if (!juegoPausado && juegoActivo) {
                    powerupsUsadosEstaPartida++;
                    usarPowerupPista();
                }
            }
        });

        powerupIntento.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { 
                if (!juegoPausado && juegoActivo) {
                    powerupsUsadosEstaPartida++;
                    usarPowerupIntento();
                }
            }
        });
    }

    // MÉTODOS DEL JUEGO
    private void cargarDatosGuardados() {
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mejorDineroPartida = prefs.getInt("mejor_dinero_partida", 0);
        totalAciertos = prefs.getInt("total_aciertos", 0);
        totalFallos = prefs.getInt("total_fallos", 0);
        juegosCompletados = prefs.getInt("juegos_completados", 0);

        // Intentar cargar estado actual si existe
        nivelActual = prefs.getInt("nivel_actual", 1);
        dineroPartida = prefs.getInt("dinero_actual", 0);
        intentosRestantes = prefs.getInt("intentos_actual", 3);
        tiempoRestante = prefs.getInt("tiempo_actual", 30);
    }

    private void inicializarSonidos() {
        try {
            sonidoActivado = preferences.isSoundEnabled();
        } catch (Exception e) {
            // Manejar error silenciosamente
        }
    }

    private void configurarJuego() {
        dineroPartida = 0;
        nivelActual = 1;
        intentosRestantes = 3 + playerProgress.getExtraAttempts();
        comboActual = 0;
        juegoActivo = true;
        juegoPausado = false;
        modoSistema = false;
        comandoActual.setLength(0);
        actualizarDisplay();
        actualizarInterfaz();
        ocultarPowerups();
        
        // Mensaje de bienvenida con dinero actual - ACTUALIZADO
        terminalOutput.append("\n> 🎯 SESIÓN DE HACKEO INICIADA");
        terminalOutput.append("\n> 💰 FONDOS DISPONIBLES: " + playerProgress.getMoneyFormatted());
        terminalOutput.append("\n> 🌐 VALOR TOTAL: " + String.format("$%,.0f", playerProgress.getTotalValue()));
        if (playerProgress.getExtraAttempts() > 0) {
            terminalOutput.append("\n> 🎯 BONUS HABILIDADES: +" + playerProgress.getExtraAttempts() + " intentos iniciales");
        }
    }

    private void iniciarNivel() {
        if (analytics != null) {
            analytics.logLevelStart(nivelActual, modoActual);
        }

        if (nivelActual > 5) {
            juegoGanado();
            return;
        }

        if (preferences != null && preferences.areAnimationsEnabled()) {
            AnimationHelper.pulseEffect(progressText);
        }

        actualizarProgressText();
        modoActual = "CONTRASEÑA";
        iniciarModoContrasena();
        desafioEspecial.setText("MODO: " + modoActual + " | NIVEL: " + nivelActual + " | " + playerProgress.getHackerRankName());
        tiempoRestante = 30 + playerProgress.getTimeBonus();
        intentosRestantes = 3 + playerProgress.getExtraAttempts();
        bonusNivel = 0;
        actualizarInterfaz();
        iniciarTemporizador();
        generarPowerups();
        scrollAlFinal();
    }

    private void iniciarModoContrasena() {
        respuestaCorrecta = contraseñasBase[random.nextInt(contraseñasBase.length)];
        String mensaje = "> 🎯 OBJETIVO: BANCO CORPORATIVO\n";
        mensaje += "> NIVEL: " + nivelActual + "\n";
        mensaje += "> RANGO: " + playerProgress.getHackerRankName() + "\n";
        mensaje += "> FONDOS: " + playerProgress.getMoneyFormatted() + "\n";
        mensaje += "> VALOR TOTAL: " + String.format("$%,.0f", playerProgress.getTotalValue()) + "\n";
        mensaje += "> PISTA: " + generarPistaParaContraseña(respuestaCorrecta) + "\n";
        mensaje += "> ESCRIBE LA CONTRASEÑA:";
        terminalOutput.setText(mensaje);
    }

    private String generarPistaParaContraseña(String contraseña) {
        if (contraseña.length() <= 2) return contraseña;
        StringBuilder pista = new StringBuilder();
        pista.append(contraseña.charAt(0));
        for (int i = 1; i < contraseña.length() - 1; i++) {
            pista.append('*');
        }
        pista.append(contraseña.charAt(contraseña.length() - 1));
        return pista.toString();
    }

    private void iniciarTemporizador() {
        if (temporizador != null) temporizador.cancel();

        temporizador = new CountDownTimer(tiempoRestante * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (juegoPausado) {
                    cancel();
                    return;
                }
                tiempoRestante = (int) (millisUntilFinished / 1000);
                timerText.setText("⏳ " + tiempoRestante + "s");
                if (tiempoRestante <= 5) {
                    timerText.setTextColor(0xFFFF0000);
                    estadoJuego.setText("🔴 URGENTE!");
                } else if (tiempoRestante <= 10) {
                    timerText.setTextColor(0xFFFFFF00);
                    estadoJuego.setText("🟡 ATENCIÓN");
                } else {
                    timerText.setTextColor(0xFF00FF00);
                    estadoJuego.setText("🟢 CONECTADO");
                }
            }
            public void onFinish() {
                if (juegoPausado) return;
                tiempoRestante = 0;
                timerText.setText("⏳ 0s");
                timerText.setTextColor(0xFFFF0000);
                estadoJuego.setText("🔴 DESCONECTADO");
                terminalOutput.append("\n> ⏰ ¡TIEMPO AGOTADO!");
                manejarFallo();
            }
        }.start();
    }

    private void iniciarActualizacionHora() {
        actualizarHora();
        horaHandler.postDelayed(actualizarHoraRunnable, 1000);
    }

    private Runnable actualizarHoraRunnable = new Runnable() {
        @Override
        public void run() {
            actualizarHora();
            horaHandler.postDelayed(this, 1000);
        }
    };

    private void actualizarHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String hora = sdf.format(new Date());
        if (horaActual != null) {
            horaActual.setText(hora);
        }
    }

    // MÉTODOS DE TECLADO Y JUEGO
    public void toggleModoSistema(View view) {
        if (modoSistema) {
            panelComandos.setVisibility(View.GONE);
            modoSistema = false;
            btnSistema.setText("🏠 SIST");
            btnSistema.setBackgroundColor(0xFF00AAFF);
            if (juegoPausado) {
                reanudarJuego();
            }
            terminalOutput.append("\n> 🔄 Modo Juego activado");
        } else {
            panelComandos.setVisibility(View.VISIBLE);
            modoSistema = true;
            btnSistema.setText("🎮 JUEGO");
            btnSistema.setBackgroundColor(0xFFFFFF00);
            if (juegoActivo && !juegoPausado) {
                pausarJuego();
            }
            terminalOutput.append("\n> ⚙️ Modo Sistema activado");
        }
        scrollAlFinal();
    }

    public void togglePausa(View view) {
        if (juegoPausado) {
            reanudarJuego();
        } else {
            pausarJuego();
        }
    }

    public void toggleSonido(View view) {
        boolean newState = !preferences.isSoundEnabled();
        preferences.setSoundEnabled(newState);

        if (newState) {
            btnSonido.setText("🔊");
            btnSonido.setBackgroundColor(0xFF003300);
            AnimationHelper.pulseEffect(btnSonido);
        } else {
            btnSonido.setText("🔇");
            btnSonido.setBackgroundColor(0xFF660000);
        }

        preferences.triggerVibration();
    }

    private void pausarJuego() {
        juegoPausado = true;
        tiempoCuandoSePauso = System.currentTimeMillis();

        if (temporizador != null) {
            temporizador.cancel();
            temporizador = null;
        }

        btnPausa.setText("▶️ REANUDAR");
        btnPausa.setBackgroundColor(0xFF00FF00);
        estadoJuego.setText("⏸️ PAUSADO");
        estadoJuego.setTextColor(0xFFFFFF00);
        terminalOutput.append("\n> ⏸️ JUEGO EN PAUSA - Sistema persistente activado");
        scrollAlFinal();

        guardarEstadoCritico();
        guardarDatos();

        if (analytics != null) {
            analytics.logEvent("game_paused");
        }
    }

    private void reanudarJuego() {
        juegoPausado = false;
        btnPausa.setText("⏸️ PAUSA");
        btnPausa.setBackgroundColor(0xFFFF4444);
        estadoJuego.setText("🟢 CONECTADO");
        estadoJuego.setTextColor(0xFF00FF00);

        if (fuePausadoPorSistema) {
            long tiempoPausado = System.currentTimeMillis() - tiempoCuandoSePauso;
            terminalOutput.append("\n> 🔄 SISTEMA RECUPERADO - Pausado por " + (tiempoPausado / 1000) + " segundos");
            fuePausadoPorSistema = false;
        } else {
            terminalOutput.append("\n> ▶️ JUEGO REANUDADO");
        }

        if (juegoActivo && tiempoRestante > 0) {
            iniciarTemporizador();
            scrollAlFinal();

            if (analytics != null) {
                analytics.logEvent("game_resumed");
            }
        }
        scrollAlFinal();
    }

    // Métodos de teclado (se mantienen igual)
    public void letraQ(View view) { agregarLetra("Q"); }
    public void letraW(View view) { agregarLetra("W"); }
    public void letraE(View view) { agregarLetra("E"); }
    public void letraR(View view) { agregarLetra("R"); }
    public void letraT(View view) { agregarLetra("T"); }
    public void letraY(View view) { agregarLetra("Y"); }
    public void letraU(View view) { agregarLetra("U"); }
    public void letraI(View view) { agregarLetra("I"); }
    public void letraO(View view) { agregarLetra("O"); }
    public void letraP(View view) { agregarLetra("P"); }
    public void letraA(View view) { agregarLetra("A"); }
    public void letraS(View view) { agregarLetra("S"); }
    public void letraD(View view) { agregarLetra("D"); }
    public void letraF(View view) { agregarLetra("F"); }
    public void letraG(View view) { agregarLetra("G"); }
    public void letraH(View view) { agregarLetra("H"); }
    public void letraJ(View view) { agregarLetra("J"); }
    public void letraK(View view) { agregarLetra("K"); }
    public void letraL(View view) { agregarLetra("L"); }
    public void letraZ(View view) { agregarLetra("Z"); }
    public void letraX(View view) { agregarLetra("X"); }
    public void letraC(View view) { agregarLetra("C"); }
    public void letraV(View view) { agregarLetra("V"); }
    public void letraB(View view) { agregarLetra("B"); }
    public void letraN(View view) { agregarLetra("N"); }
    public void letraM(View view) { agregarLetra("M"); }
    public void numero0(View view) { agregarLetra("0"); }
    public void numero1(View view) { agregarLetra("1"); }
    public void numero2(View view) { agregarLetra("2"); }
    public void numero3(View view) { agregarLetra("3"); }
    public void numero4(View view) { agregarLetra("4"); }
    public void numero5(View view) { agregarLetra("5"); }
    public void numero6(View view) { agregarLetra("6"); }
    public void numero7(View view) { agregarLetra("7"); }
    public void numero8(View view) { agregarLetra("8"); }
    public void numero9(View view) { agregarLetra("9"); }

    public void borrarTexto(View view) {
        if (comandoActual.length() > 0) {
            comandoActual.deleteCharAt(comandoActual.length() - 1);
            actualizarDisplay();
        }
    }

    public void ejecutarComando(View view) {
        if (comandoActual.length() > 0) {
            if (validarComando(comandoActual.toString())) {
                procesarComando(comandoActual.toString());
            }
            comandoActual.setLength(0);
            actualizarDisplay();
        }
    }

    public void reiniciarJuegoDesdeTeclado(View view) {
        reiniciarJuego();
    }

    private void agregarLetra(String letra) {
        comandoActual.append(letra);
        actualizarDisplay();
    }

    private void actualizarDisplay() {
        if (comandoDisplay != null) {
            comandoDisplay.setText("> " + comandoActual.toString() + "_");
        }
    }

    private boolean validarComando(String comando) {
        if (comando == null || comando.trim().isEmpty()) {
            terminalOutput.append("\n> ❌ Comando vacío");
            scrollAlFinal();
            return false;
        }
        return true;
    }

    private void procesarComando(String comando) {
        terminalOutput.append("\n> " + comando);
        scrollAlFinal();

        if (modoSistema || juegoPausado) {
            terminalOutput.append("\n> ⚠️ El juego está en pausa. Usa 🎮 JUEGO para continuar");
            return;
        }

        if (comando.equalsIgnoreCase(respuestaCorrecta)) {
            totalAciertos++;
            manejarExito();
        } else {
            totalFallos++;
            manejarFallo();
        }
    }

    // MÉTODO ACTUALIZADO CON SISTEMA DE CRYPTO
    private void manejarExito() {
        if (temporizador != null) temporizador.cancel();

        // CALCULAR GANANCIAS EN DÓLARES
        int bonusTiempo = tiempoRestante * 20;
        int dineroBase = 200 + (nivelActual * 50);
        comboActual++;
        
        // MULTIPLICADORES DE HABILIDADES
        int multiplierCombo = playerProgress.getComboMultiplier();
        int multiplierDinero = playerProgress.getMoneyMultiplier();
        
        int bonusCombo = comboActual * 10 * multiplierCombo;
        int dineroNivel = (dineroBase + bonusTiempo + bonusCombo) * multiplierDinero;

        dineroPartida += dineroNivel;
        bonusNivel = dineroNivel - dineroBase;
        
        // ACTUALIZAR DINERO PERMANENTE CON CRYPTO - CAMBIO CLAVE
        playerProgress.addMissionEarnings(dineroNivel, getMissionTypeByLevel(nivelActual));
        playerProgress.incrementTotalHacks();
        playerProgress.updateBestCombo(comboActual);
        
        if (dineroPartida > mejorDineroPartida) mejorDineroPartida = dineroPartida;

        // Analytics
        if (analytics != null) {
            analytics.logLevelComplete(nivelActual, dineroNivel, tiempoRestante, comboActual);
        }

        // Animación de éxito
        if (preferences != null && preferences.areAnimationsEnabled()) {
            AnimationHelper.hackSuccessAnimation(terminalOutput);
            preferences.triggerVibration();
        }

        String mensajeExito = mensajesExito[random.nextInt(mensajesExito.length)];
        terminalOutput.append("\n" + mensajeExito);
        terminalOutput.append("\n> 💰 GANANCIA: " + String.format("$%,d", dineroNivel) + " (+" + String.format("$%,d", bonusNivel) + " bonus)");
        terminalOutput.append("\n> 🎯 HABILIDADES: Combo x" + multiplierCombo + " | Dinero x" + multiplierDinero);

        // MOSTRAR CRYPTO GANADA SI LA HAY - NUEVO
        if (playerProgress.hasCryptoEarnings()) {
            terminalOutput.append("\n> 🌟 BONUS CRYPTO: " + playerProgress.getRecentCryptoEarnings());
        }

        terminalOutput.append("\n> 💳 CARTILLERA: " + playerProgress.getMoneyFormatted());
        scrollAlFinal();

        nivelActual++;
        if (nivelActual <= 5) {
            terminalOutput.append("\n> 🔄 CONECTANDO AL SIGUIENTE BANCO...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iniciarNivel();
                }
            }, 2000);
        } else {
            playerProgress.incrementGamesPlayed();
            juegoGanado();
        }
        actualizarInterfaz();
    }

    private void manejarFallo() {
        comboActual = 0;
        intentosRestantes--;

        if (preferences != null && preferences.areAnimationsEnabled()) {
            AnimationHelper.errorShakeAnimation(terminalOutput);
            preferences.triggerVibration();
        }

        String mensajeError = mensajesError[random.nextInt(mensajesError.length)];
        terminalOutput.append("\n" + mensajeError);
        scrollAlFinal();
        if (intentosRestantes == 0) generarPowerups();
        if (intentosRestantes > 0) {
            terminalOutput.append("\n> 🔑 Intentos restantes: " + intentosRestantes);
            terminalOutput.append("\n> ESCRIBE LA RESPUESTA:");
            scrollAlFinal();
        } else {
            terminalOutput.append("\n> 🚨 ¡CUENTA BLOQUEADA!");
            terminalOutput.append("\n> 💀 OPERACIÓN TERMINADA");
            terminalOutput.append("\n> 💰 GANANCIA ESTA PARTIDA: " + String.format("$%,d", dineroPartida));
            playerProgress.incrementGamesPlayed();
            juegoActivo = false;
            if (temporizador != null) temporizador.cancel();
            guardarDatos();
        }
        actualizarInterfaz();
    }

    private void juegoGanado() {
        juegoActivo = false;
        if (temporizador != null) temporizador.cancel();
        ocultarPowerups();
        juegosCompletados++;
        guardarDatos();
        
        // MENSAJE DE VICTORIA ACTUALIZADO
        terminalOutput.append("\n\n╔══════════════════════╗");
        terminalOutput.append("\n║    🏆 VICTORIA!     ║");
        terminalOutput.append("\n║  FONDOS SECUESTRADOS║");
        terminalOutput.append("\n║   ESTA PARTIDA: " + String.format("$%,6d", dineroPartida) + " ║");
        terminalOutput.append("\n╠══════════════════════╣");
        terminalOutput.append("\n║ " + playerProgress.getHackerRankName() + " ║");
        terminalOutput.append("\n║ TOTAL: " + String.format("$%,.0f", playerProgress.getTotalValue()) + " ║");
        terminalOutput.append("\n╚══════════════════════╝");
        terminalOutput.append("\n> ¡FELICIDADES, HACKER FINANCIERO!");
        scrollAlFinal();
    }

    // MÉTODOS AUXILIARES
    private void generarPowerups() {
        ocultarPowerups();
        if (random.nextDouble() < 0.4) {
            powerupTiempo.setVisibility(View.VISIBLE);
            if (preferences != null && preferences.areAnimationsEnabled()) {
                AnimationHelper.powerUpAppearAnimation(powerupTiempo);
            }
        }
        if (random.nextDouble() < 0.3) {
            powerupPista.setVisibility(View.VISIBLE);
            powerupPistaDisponible = true;
            if (preferences != null && preferences.areAnimationsEnabled()) {
                AnimationHelper.powerUpAppearAnimation(powerupPista);
            }
        }
        if (intentosRestantes == 1 && random.nextDouble() < 0.5) {
            powerupIntento.setVisibility(View.VISIBLE);
            powerupIntentoDisponible = true;
            if (preferences != null && preferences.areAnimationsEnabled()) {
                AnimationHelper.powerUpAppearAnimation(powerupIntento);
            }
        }
    }

    private void ocultarPowerups() {
        powerupTiempo.setVisibility(View.GONE);
        powerupPista.setVisibility(View.GONE);
        powerupIntento.setVisibility(View.GONE);
        powerupPistaDisponible = false;
        powerupIntentoDisponible = false;
    }

    private void usarPowerupTiempo() {
        tiempoRestante += 15;
        timerText.setText("⏳ " + tiempoRestante + "s");
        timerText.setTextColor(0xFF00FF00);
        estadoJuego.setText("🟢 +15s");
        powerupTiempo.setVisibility(View.GONE);

        if (analytics != null) {
            analytics.logPowerUpUsed("time_extension", nivelActual);
        }

        if (preferences != null && preferences.areAnimationsEnabled()) {
            AnimationHelper.pulseEffect(timerText);
            preferences.triggerVibration();
        }

        terminalOutput.append("\n> ⏰ +15 SEGUNDOS AÑADIDOS!");
        scrollAlFinal();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                estadoJuego.setText("🟢 CONECTADO");
            }
        }, 2000);
    }

    private void usarPowerupPista() {
        if (modoActual.equals("CONTRASEÑA") && powerupPistaDisponible) {
            String pistaMejorada = mejorarPista(respuestaCorrecta);
            terminalOutput.append("\n> 🔍 PISTA MEJORADA: " + pistaMejorada);
            powerupPista.setVisibility(View.GONE);
            powerupPistaDisponible = false;

            if (analytics != null) {
                analytics.logPowerUpUsed("hint", nivelActual);
            }

            scrollAlFinal();
        }
    }

    private void usarPowerupIntento() {
        if (powerupIntentoDisponible) {
            intentosRestantes++;
            powerupIntento.setVisibility(View.GONE);
            powerupIntentoDisponible = false;

            if (analytics != null) {
                analytics.logPowerUpUsed("extra_life", nivelActual);
            }

            terminalOutput.append("\n> 💝 +1 INTENTO CONCEDIDO!");
            actualizarInterfaz();
            scrollAlFinal();
        }
    }

    private String mejorarPista(String palabra) {
        if (palabra.length() >= 4) {
            return palabra.charAt(0) + "" + palabra.charAt(1) + "**" + palabra.charAt(palabra.length()-1);
        }
        return generarPistaParaContraseña(palabra);
    }

    private void scrollAlFinal() {
        if (terminalScroll != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    terminalScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 100);
        }
    }

    private void actualizarProgressText() {
        int porcentaje = (nivelActual * 100) / 5;
        int barras = (porcentaje * 10) / 100;
        StringBuilder progressBar = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            if (i < barras) progressBar.append("■");
            else progressBar.append(" ");
        }
        progressBar.append("] ").append(porcentaje).append("%");
        progressText.setText(progressBar.toString());
    }

    // INTERFAZ ACTUALIZADA CON VALOR TOTAL
    private void actualizarInterfaz() {
        levelText.setText("NIVEL: " + nivelActual + "/5");
        scoreText.setText("DÓLARES: " + String.format("$%,d", dineroPartida));
        attemptsText.setText("INTENTOS: " + intentosRestantes);
        comboText.setText("COMBO: x" + comboActual);
        bonusText.setText("VALOR TOTAL: $" + String.format("%,.0f", playerProgress.getTotalValue()));
        actualizarProgressText();
    }

    private void reiniciarJuego() {
        if (temporizador != null) temporizador.cancel();
        if (modoSistema) {
            panelComandos.setVisibility(View.GONE);
            modoSistema = false;
            btnSistema.setText("🏠 SIST");
            btnSistema.setBackgroundColor(0xFF00AAFF);
        }
        configurarJuego();
        iniciarNivel();
        terminalOutput.setText("> SISTEMA REINICIADO...\n> INICIANDO NUEVA SESIÓN DE HACKEO...");
        scrollAlFinal();
    }

    private void guardarDatos() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("mejor_dinero_partida", mejorDineroPartida);
        editor.putInt("total_aciertos", totalAciertos);
        editor.putInt("total_fallos", totalFallos);
        editor.putInt("juegos_completados", juegosCompletados);

        // Guardar estado actual también para recuperación
        editor.putInt("nivel_actual", nivelActual);
        editor.putInt("dinero_actual", dineroPartida);
        editor.putInt("intentos_actual", intentosRestantes);
        editor.putInt("tiempo_actual", tiempoRestante);

        editor.apply();
    }

    // =============================================
    // MÉTODOS DE PERSISTENCIA 
    // =============================================

    private void guardarEstadoCritico() {
        SharedPreferences estadoCritico = getSharedPreferences("estado_critico", MODE_PRIVATE);
        SharedPreferences.Editor editor = estadoCritico.edit();

        editor.putBoolean("juegoPausado", juegoPausado);
        editor.putInt("nivelActual", nivelActual);
        editor.putInt("dineroPartida", dineroPartida);
        editor.putInt("tiempoRestante", tiempoRestante);
        editor.putInt("intentosRestantes", intentosRestantes);
        editor.putString("respuestaCorrecta", respuestaCorrecta);
        editor.putString("modoActual", modoActual);
        editor.putLong("tiempoPausa", System.currentTimeMillis());

        editor.apply();
        Log.i("HackerSystem", "Estado crítico guardado");
    }

    private void recuperarEstadoCritico() {
        SharedPreferences estadoCritico = getSharedPreferences("estado_critico", MODE_PRIVATE);

        juegoPausado = estadoCritico.getBoolean("juegoPausado", false);
        nivelActual = estadoCritico.getInt("nivelActual", 1);
        dineroPartida = estadoCritico.getInt("dineroPartida", 0);
        tiempoRestante = estadoCritico.getInt("tiempoRestante", 30);
        intentosRestantes = estadoCritico.getInt("intentosRestantes", 3);
        respuestaCorrecta = estadoCritico.getString("respuestaCorrecta", "");
        modoActual = estadoCritico.getString("modoActual", "CONTRASEÑA");

        actualizarInterfaz();
        desafioEspecial.setText("MODO: " + modoActual + " | NIVEL: " + nivelActual);

        Log.i("HackerSystem", "Estado crítico recuperado");
    }

    private void verificarEstadoPrevio() {
        SharedPreferences estadoCritico = getSharedPreferences("estado_critico", MODE_PRIVATE);
        boolean estabaPausado = estadoCritico.getBoolean("juegoPausado", false);

        if (estabaPausado) {
            juegoPausado = true;
            recuperarEstadoCritico();

            SharedPreferences.Editor editor = estadoCritico.edit();
            editor.clear();
            editor.apply();

            if (terminalOutput != null) {
                terminalOutput.append("\n> 🔄 JUEGO RECUPERADO - Estaba en pausa");
                scrollAlFinal();
            }
            Log.i("HackerSystem", "Estado previo recuperado y limpiado");
        }
    }

    // =============================================
    // MÉTODOS FUNCIONALES ACTUALIZADOS
    // =============================================

    public void comandoStats(View view) {
        if (modoSistema) {
            terminalOutput.append("\n> 📊 ESTADÍSTICAS DEL SISTEMA");
            terminalOutput.append("\n>   Aciertos: " + totalAciertos);
            terminalOutput.append("\n>   Fallos: " + totalFallos);
            terminalOutput.append("\n>   Mejor partida: " + String.format("$%,d", mejorDineroPartida));
            terminalOutput.append("\n>   Juegos completados: " + juegosCompletados);
            terminalOutput.append("\n>   Powerups usados: " + powerupsUsadosEstaPartida);
            terminalOutput.append("\n>   Dinero actual: " + playerProgress.getMoneyFormatted());
            terminalOutput.append("\n>   Valor total: " + String.format("$%,.0f", playerProgress.getTotalValue()));
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    // CARTILLERA ACTUALIZADA CON CRYPTO
    public void comandoCartillera(View view) {
        if (modoSistema) {
            terminalOutput.append("\n" + playerProgress.getWalletInfo());
            
            // Añadir fluctuaciones del mercado
            terminalOutput.append("\n\n> 📈 MERCADO ACTUAL:");
            terminalOutput.append("\n>   ₿ BTC: $" + String.format("%,.0f", PlayerProgress.BTC_TO_USD));
            terminalOutput.append("\n>   Ξ ETH: $" + String.format("%,.0f", PlayerProgress.ETH_TO_USD)); 
            terminalOutput.append("\n>   💎 LTC: $" + String.format("%,.0f", PlayerProgress.LTC_TO_USD));
            
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    public void comandoProgreso(View view) {
        if (modoSistema) {
            terminalOutput.append("\n> 📊 PROGRESO PERMANENTE");
            terminalOutput.append("\n>   Rango: " + playerProgress.getHackerRankName());
            terminalOutput.append("\n>   Dinero Total: " + playerProgress.getMoneyFormatted());
            terminalOutput.append("\n>   Valor Total: " + String.format("$%,.0f", playerProgress.getTotalValue()));
            terminalOutput.append("\n>   Total Ganado: " + playerProgress.getEarnedFormatted());
            terminalOutput.append("\n>   Habilidades:");
            terminalOutput.append("\n>     🔓 Hacking: Nvl " + playerProgress.getSkillHacking() + " (+" + playerProgress.getTimeBonus() + "s)");
            terminalOutput.append("\n>     🦉 Stealth: Nvl " + playerProgress.getSkillStealth() + " (Dinero x" + playerProgress.getComboMultiplier() + ")");
            terminalOutput.append("\n>     🔐 Crypto: Nvl " + playerProgress.getSkillCrypto() + " (Dinero x" + playerProgress.getMoneyMultiplier() + ")");
            terminalOutput.append("\n>   Estadísticas:");
            terminalOutput.append("\n>     Partidas: " + playerProgress.getGamesPlayed());
            terminalOutput.append("\n>     Hacks exitosos: " + playerProgress.getTotalHacks());
            terminalOutput.append("\n>     Mejor combo: x" + playerProgress.getBestCombo());
            terminalOutput.append("\n>   Herramientas: " + playerProgress.getUnlockedTools().size() + " desbloqueadas");
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    public void comandoMejoras(View view) {
        if (modoSistema) {
            terminalOutput.append("\n> 🎯 MEJORAR HABILIDADES");
            terminalOutput.append("\n>   " + playerProgress.getSkillInfo("hacking"));
            terminalOutput.append("\n>   " + playerProgress.getSkillInfo("stealth"));
            terminalOutput.append("\n>   " + playerProgress.getSkillInfo("crypto"));
            terminalOutput.append("\n>   Usa MEJORAR [habilidad] para mejorar");
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    public void comandoLogros(View view) {
        if (modoSistema) {
            terminalOutput.append("\n" + playerProgress.getAchievements());
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    public void comandoHabilidades(View view) {
        if (modoSistema) {
            terminalOutput.append("\n> 🎯 HABILIDADES DISPONIBLES");
            terminalOutput.append("\n>   COMP 1: +5 segundos ($1,000)");
            terminalOutput.append("\n>   COMP 2: Pista mejorada ($1,500)");
            terminalOutput.append("\n>   COMP 3: +1 intento ($2,000)");
            terminalOutput.append("\n>   COMP 4: Combo x2 ($3,000)");
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    // MERCADO NEGRO ACTUALIZADO A DÓLARES
    public void comandoMercado(View view) {
        if (modoSistema) {
            terminalOutput.append("\n> 🏴‍☠️ MERCADO NEGRO");
            terminalOutput.append("\n>   🔍 Scanner Básico: $1,000");
            terminalOutput.append("\n>   💾 Keylogger: $2,500");
            terminalOutput.append("\n>   🌐 VPN Premium: $5,000");
            terminalOutput.append("\n>   🛡️ Firewall Personal: $7,500");
            terminalOutput.append("\n>   🔓 Cracker SSL: $15,000");
            terminalOutput.append("\n>   📡 Packet Sniffer: $25,000");
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    // =============================================
    // MÉTODO NUEVO: COMANDO REPUTACIÓN
    // =============================================

    public void comandoReputacion(View view) {
        if (modoSistema) {
            try {
                int reputacion = juegosCompletados * 10 + nivelActual * 5;
                String nivelReputacion;
                
                // Determinar nivel de reputación
                if (reputacion >= 100) {
                    nivelReputacion = "💀 Leyenda del Underground";
                } else if (reputacion >= 75) {
                    nivelReputacion = "🔴 Maestro Criminal";
                } else if (reputacion >= 50) {
                    nivelReputacion = "🟠 Hacker Élite";
                } else if (reputacion >= 25) {
                    nivelReputacion = "🟡 Operador Avanzado";
                } else {
                    nivelReputacion = "🟢 Novato Sospechoso";
                }
                
                // Calcular porcentaje de confiabilidad (evitar división por cero)
                int porcentajeConfiabilidad = 0;
                int totalOperaciones = totalAciertos + totalFallos;
                if (totalOperaciones > 0) {
                    porcentajeConfiabilidad = (totalAciertos * 100) / totalOperaciones;
                }
                
                // Mostrar información de reputación
                terminalOutput.append("\n> 🌐 REPUTACIÓN EN LA DARK WEB");
                terminalOutput.append("\n>   Nivel: " + nivelReputacion);
                terminalOutput.append("\n>   Puntos reputación: " + reputacion);
                terminalOutput.append("\n>   Misiones completadas: " + juegosCompletados);
                terminalOutput.append("\n>   Hackeos exitosos: " + totalAciertos);
                terminalOutput.append("\n>   Operaciones fallidas: " + totalFallos);
                terminalOutput.append("\n>   Confiabilidad: " + porcentajeConfiabilidad + "%");
                terminalOutput.append("\n>   Rango actual: " + playerProgress.getHackerRankName());
                
                // Mostrar recomendaciones basadas en reputación
                if (reputacion < 25) {
                    terminalOutput.append("\n> 💡 Consejo: Completa más misiones para aumentar tu reputación");
                } else if (reputacion >= 75) {
                    terminalOutput.append("\n> ⚡ Estatus: Eres conocido en todos los foros underground");
                }
                
                scrollAlFinal();
                cerrarPanelComandos();
                
            } catch (Exception e) {
                // Manejar cualquier error silenciosamente
                if (terminalOutput != null) {
                    terminalOutput.append("\n> ⚠️ Error al cargar reputación");
                    scrollAlFinal();
                }
            }
        }
    }
    
    // =============================================
    // SISTEMA DE COMPRAS ACTUALIZADO A DÓLARES
    // =============================================

    public void comprar1(View view) { 
        if (modoSistema && playerProgress.upgradeSkill("hacking", 1000)) {
            terminalOutput.append("\n> ✅ HACKING MEJORADO! Nivel " + playerProgress.getSkillHacking());
            terminalOutput.append("\n> Efecto: +5 segundos por nivel");
            terminalOutput.append("\n> 💰 Dinero restante: " + playerProgress.getMoneyFormatted());
            actualizarInterfaz();
        } else if (modoSistema) {
            terminalOutput.append("\n> ❌ Fondos insuficientes para mejorar Hacking");
            terminalOutput.append("\n> 💰 Necesitas: $1,000 | Tienes: " + playerProgress.getMoneyFormatted());
        }
    }

    public void comprar2(View view) { 
        if (modoSistema && playerProgress.upgradeSkill("stealth", 1200)) {
            terminalOutput.append("\n> ✅ STEALTH MEJORADO! Nivel " + playerProgress.getSkillStealth());
            terminalOutput.append("\n> Efecto: Multiplicador de dinero aumentado");
            terminalOutput.append("\n> 💰 Dinero restante: " + playerProgress.getMoneyFormatted());
            actualizarInterfaz();
        } else if (modoSistema) {
            terminalOutput.append("\n> ❌ Fondos insuficientes para mejorar Stealth");
            terminalOutput.append("\n> 💰 Necesitas: $1,200 | Tienes: " + playerProgress.getMoneyFormatted());
        }
    }

    public void comprar3(View view) { 
        if (modoSistema && playerProgress.upgradeSkill("crypto", 1500)) {
            terminalOutput.append("\n> ✅ CRYPTO MEJORADO! Nivel " + playerProgress.getSkillCrypto());
            terminalOutput.append("\n> Efecto: Multiplicador de dinero aumentado");
            terminalOutput.append("\n> 💰 Dinero restante: " + playerProgress.getMoneyFormatted());
            actualizarInterfaz();
        } else if (modoSistema) {
            terminalOutput.append("\n> ❌ Fondos insuficientes para mejorar Crypto");
            terminalOutput.append("\n> 💰 Necesitas: $1,500 | Tienes: " + playerProgress.getMoneyFormatted());
        }
    }

    public void comprar4(View view) { 
        if (modoSistema && comprarHabilidad(2000, "Combo x2")) {
            comboActual *= 2;
            terminalOutput.append("\n> ⚡ Combo multiplicado x2!");
            actualizarInterfaz();
        }
    }

    public void comprar5(View view) { comprarHabilidad(3000, "Habilidad especial"); }
    public void comprar6(View view) { comprarHabilidad(5000, "Habilidad avanzada"); }

    // COMPRAS DE HERRAMIENTAS ACTUALIZADAS
    public void comprarH1(View view) { 
        if (modoSistema && playerProgress.spendMoney(1000)) {
            playerProgress.unlockTool("🔍 Scanner Básico");
            terminalOutput.append("\n> 🛒 'Scanner Básico' comprado!");
            terminalOutput.append("\n> -$1,000 | Total: " + playerProgress.getMoneyFormatted());
            scrollAlFinal();
            cerrarPanelComandos();
        } else if (modoSistema) {
            terminalOutput.append("\n> ❌ Fondos insuficientes para Scanner Básico");
        }
    }

    public void comprarH2(View view) { 
        if (modoSistema && playerProgress.spendMoney(2500)) {
            playerProgress.unlockTool("💾 Keylogger");
            terminalOutput.append("\n> 🛒 'Keylogger' comprado!");
            terminalOutput.append("\n> -$2,500 | Total: " + playerProgress.getMoneyFormatted());
            scrollAlFinal();
            cerrarPanelComandos();
        } else if (modoSistema) {
            terminalOutput.append("\n> ❌ Fondos insuficientes para Keylogger");
        }
    }

    public void comprarH3(View view) { comprarHerramienta(5000, "VPN Premium"); }
    public void comprarH4(View view) { comprarHerramienta(7500, "Firewall Personal"); }
    public void comprarH5(View view) { comprarHerramienta(15000, "Cracker SSL"); }
    public void comprarH6(View view) { comprarHerramienta(25000, "Packet Sniffer"); }

    // USAR HERRAMIENTAS
    public void usar1(View view) { usarHerramienta("Scanner Básico"); }
    public void usar2(View view) { usarHerramienta("Keylogger"); }
    public void usar3(View view) { usarHerramienta("VPN Premium"); }
    public void usar4(View view) { usarHerramienta("Firewall Personal"); }
    public void usar5(View view) { usarHerramienta("Cracker SSL"); }
    public void usar6(View view) { usarHerramienta("Packet Sniffer"); }

    // =============================================
    // MÉTODOS AUXILIARES
    // =============================================

    private boolean comprarHabilidad(int costo, String nombre) {
        if (playerProgress.spendMoney(costo)) {
            terminalOutput.append("\n> ✅ '" + nombre + "' adquirida!");
            terminalOutput.append("\n> -" + String.format("$%,d", costo) + " | Total: " + playerProgress.getMoneyFormatted());
            scrollAlFinal();
            cerrarPanelComandos();
            return true;
        } else {
            terminalOutput.append("\n> ❌ Fondos insuficientes. Necesitas " + String.format("$%,d", costo));
            scrollAlFinal();
            return false;
        }
    }

    private void comprarHerramienta(int costo, String nombre) {
        if (playerProgress.spendMoney(costo)) {
            playerProgress.unlockTool(nombre);
            terminalOutput.append("\n> 🛒 '" + nombre + "' comprada y desbloqueada!");
            terminalOutput.append("\n> -" + String.format("$%,d", costo) + " | Total: " + playerProgress.getMoneyFormatted());
            scrollAlFinal();
            cerrarPanelComandos();
        } else {
            terminalOutput.append("\n> ❌ Fondos insuficientes. Necesitas " + String.format("$%,d", costo));
            scrollAlFinal();
        }
    }

    private void usarHerramienta(String herramienta) {
        if (modoSistema) {
            terminalOutput.append("\n> 🛠️ Usando: " + herramienta);
            terminalOutput.append("\n> Efecto aplicado por 3 niveles");

            // Efectos reales en el juego
            switch(herramienta) {
                case "Scanner Básico":
                    tiempoRestante += 10;
                    terminalOutput.append("\n> ⏰ +10 segundos añadidos!");
                    break;
                case "Keylogger":
                    intentosRestantes++;
                    terminalOutput.append("\n> 💝 +1 intento adicional!");
                    break;
                case "VPN Premium":
                    comboActual += 2;
                    terminalOutput.append("\n> ⚡ +2 al combo actual!");
                    break;
            }

            actualizarInterfaz();
            scrollAlFinal();
            cerrarPanelComandos();
        }
    }

    private void cerrarPanelComandos() {
        panelComandos.setVisibility(View.GONE);
        modoSistema = false;
        btnSistema.setText("🏠 SIST");
        btnSistema.setBackgroundColor(0xFF00AAFF);

        if (juegoPausado) {
            reanudarJuego();
        }

        terminalOutput.append("\n> 🔄 Panel de comandos cerrado");
        scrollAlFinal();
    }
}
