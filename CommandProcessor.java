package com.hacker.finalapp;

import android.content.Context;
import android.content.Intent;
import java.util.HashMap;
import java.util.Random;

public class CommandProcessor {
    
    private Context context;
    private PlayerProgress playerProgress;
    private Random random;
    
    // Variables para el juego de hacking
    private boolean hackingGameActive = false;
    private String currentPassword = "";
    private int hackingAttempts = 3;
    private int hackingLevel = 1;
    private int hackingTime = 30;
    private boolean waitingForPassword = false;
    
    // Variables para módulos especializados
    private boolean networkScanActive = false;
    private boolean trafficMonitorActive = false;
    private boolean webBrowseActive = false;
    private boolean firewallActive = false;
    
    public CommandProcessor(Context context, PlayerProgress playerProgress) {
        this.context = context;
        this.playerProgress = playerProgress;
        this.random = new Random();
    }
    
    public String processCommand(String command) {
        String[] parts = command.toLowerCase().split(" ");
        String mainCommand = parts[0];
        
        // Si hay un juego de hacking activo y estamos esperando contraseña
        if (waitingForPassword && hackingGameActive) {
            return processHackingInput(command);
        }
        
        // Si hay módulos activos, procesar comandos especiales
        if (networkScanActive && mainCommand.equals("stop")) {
            return stopNetworkScan();
        }
        
        if (trafficMonitorActive && mainCommand.equals("stop")) {
            return stopTrafficMonitor();
        }
        
        if (webBrowseActive && mainCommand.equals("stop")) {
            return stopWebBrowse();
        }
        
        if (firewallActive && mainCommand.equals("stop")) {
            return stopFirewall();
        }
        
        // Procesar comandos normales
        switch (mainCommand) {
            case "help":
            case "?":
            case "comandos":
                return showHelp();
                
            case "clear":
            case "cls":
            case "limpiar":
                return clearTerminal();
                
            case "status":
            case "stats":
            case "estado":
                return showStatus();
                
            case "hack":
            case "hackear":
                return startHackingGame(parts.length > 1 ? parts[1] : "");
                
            case "scan":
            case "escaner":
            case "escanear":
                return startNetworkScan();
                
            case "crypto":
            case "cripto":
            case "descifrar":
                return openCryptoModule();
                
            case "web":
            case "navegador":
            case "internet":
                return startWebBrowse();
                
            case "traffic":
            case "trafico":
            case "monitor":
                return startTrafficMonitor();
                
            case "firewall":
            case "cortafuegos":
            case "seguridad":
                return startFirewall();
                
            case "files":
            case "archivos":
            case "filemanager":
                return openFileManager();
                
            case "missions":
            case "misiones":
            case "contratos":
                return openMissions();
                
            case "tools":
            case "herramientas":
            case "modulos":
                return showToolsMenu(parts.length > 1 ? parts[1] : "");
                
            case "market":
            case "mercado":
            case "tienda":
                return showMarket();
                
            case "upgrade":
            case "mejorar":
            case "comprar":
                return processUpgrade(parts.length > 1 ? parts[1] : "");
                
            case "tutorial":
            case "ayuda":
            case "guia":
                return openTutorial();
                
            case "about":
            case "info":
            case "sistema":
                return showAbout();
                
            case "exit":
            case "quit":
            case "salir":
                return exitTerminal();
                
            case "money":
            case "dinero":
            case "wallet":
            case "cartera":
                return showWallet();
                
            case "rank":
            case "rango":
            case "nivel":
                return showRank();
                
            case "skills":
            case "habilidades":
            case "poderes":
                return showSkills();
                
            case "achievements":
            case "logros":
            case "trofeos":
                return showAchievements();
                
            case "inventory":
            case "inventario":
            case "herramientas":
                return showInventory();
                
            case "time":
            case "tiempo":
            case "cronometro":
                return showTime();
                
            case "restart":
            case "reiniciar":
            case "reset":
                return restartSystem();
                
            case "version":
            case "ver":
            case "v":
                return showVersion();
                
            case "history":
            case "historial":
            case "hist":
                return showCommandHistory();
                
            case "ping":
            case "test":
            case "conexion":
                return testConnection();
                
            case "whoami":
            case "usuario":
            case "user":
                return showUserInfo();
                
            case "pwd":
            case "directorio":
            case "dir":
                return showCurrentDirectory();
                
            case "ls":
            case "list":
            case "lista":
                return listFiles();
                
            case "cd":
            case "chdir":
                return changeDirectory(parts.length > 1 ? parts[1] : "");
                
            case "mkdir":
            case "md":
                return createDirectory(parts.length > 1 ? parts[1] : "");
                
            case "rm":
            case "delete":
            case "del":
                return deleteFile(parts.length > 1 ? parts[1] : "");
                
            case "cp":
            case "copy":
            case "copiar":
                return copyFile(parts.length > 1 ? parts[1] : "", parts.length > 2 ? parts[2] : "");
                
            case "mv":
            case "move":
            case "mover":
                return moveFile(parts.length > 1 ? parts[1] : "", parts.length > 2 ? parts[2] : "");
                
            case "calc":
            case "calculadora":
            case "calcular":
                return calculator(parts.length > 1 ? command.substring(5) : "");
                
            case "date":
            case "fecha":
            case "hora":
                return showDateTime();
                
            case "weather":
            case "clima":
            case "tiempo":
                return showWeather();
                
            case "news":
            case "noticias":
            case "ultima":
                return showNews();
				
        case "event":
        case "evento":
        case "sorpresa":
            return randomEvent();
            
        case "daily":
        case "diario":
        case "misionesdiarias":
            return dailyMissions();
            
        case "matrix":
            return "🔮 ACTIVANDO MODO MATRIX...\n" +
                   "01001000 01100001 01100011 01101011 01100101 01110010 00100000 01010011 01111001 01110011 01110100 01100101 01101101\n" +
                   "🎮 Comando secreto desbloqueado: MATRIX MODE\n" +
                   "💎 Recompensa secreta: +$1,000\n";
                    
        case "godmode":
            playerProgress.addDollars(5000);
            playerProgress.addBitcoin(0.001);
            return "🌟 MODO DIOS ACTIVADO\n" +
                   "💰 +$5,000 añadidos\n" +
                   "₿ +0.001 BTC bonus\n" +
                   "⚡ Habilidades maximizadas temporalmente\n";
            
        case "reset":
            return "⚠️  COMANDO PELIGROSO: reset\n" +
                   "¿Estás seguro de que quieres resetear todo el progreso?\n" +
                   "Escribe: 'reset confirm' para confirmar\n";
            
        case "reset confirm":
            playerProgress.resetProgress();
            return "🔄 PROGRESO RESETEADO\n" +
                   "¡Todo ha vuelto al inicio!\n" +
                   "💡 Comienza tu nueva journey hacker\n";
                    
        case "leaderboard":
        case "ranking":
        case "top":
            return leaderboard();
            
        case "stats":
        case "estadisticas":
            return advancedStats();
            
        case "fragment":
        case "modulo":
            return launchFragment(parts.length > 1 ? parts[1] : "");
        // === FIN DE NUEVOS COMANDOS ===
            
        default:
            return "❌ Comando no reconocido: '" + command + "'\n" +
                   "💡 Escribe 'help' para ver comandos disponibles\n";
    }
}
    
    private String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("🎯 SISTEMA DE COMANDOS HACKER TERMINAL\n");
        help.append("═══════════════════════════════════════\n\n");
        
        help.append("📊 INFORMACIÓN Y ESTADO:\n");
        help.append("  help          - Muestra esta ayuda\n");
        help.append("  status        - Estado del sistema y progreso\n");
        help.append("  money         - Información de la cartillera\n");
        help.append("  rank          - Rango y reputación\n");
        help.append("  skills        - Habilidades disponibles\n");
        help.append("  achievements  - Logros desbloqueados\n");
        help.append("  inventory     - Herramientas desbloqueadas\n");
        help.append("  whoami        - Información del usuario\n");
        help.append("  version       - Versión del sistema\n\n");
        
        help.append("🎮 JUEGO PRINCIPAL:\n");
        help.append("  hack [target] - Inicia sesión de hacking\n");
        help.append("  missions      - Panel de misiones\n");
        help.append("  market        - Mercado negro de herramientas\n");
        help.append("  upgrade [skill]- Mejorar habilidades\n\n");
        
        help.append("🛠️ HERRAMIENTAS Y MÓDULOS:\n");
        help.append("  tools         - Menú de módulos especializados\n");
        help.append("  scan          - Escáner de red\n");
        help.append("  crypto        - Criptoanálisis\n");
        help.append("  web           - Explorador web anónimo\n");
        help.append("  traffic       - Monitor de tráfico\n");
        help.append("  firewall      - Sistema de firewall\n");
        help.append("  files         - Gestor de archivos\n\n");
        
        help.append("📁 SISTEMA DE ARCHIVOS:\n");
        help.append("  ls, list      - Listar archivos\n");
        help.append("  cd [dir]      - Cambiar directorio\n");
        help.append("  pwd           - Directorio actual\n");
        help.append("  mkdir [name]  - Crear directorio\n");
        help.append("  rm [file]     - Eliminar archivo\n");
        help.append("  cp [src] [dst]- Copiar archivo\n");
        help.append("  mv [src] [dst]- Mover archivo\n\n");
        
        help.append("🔧 UTILIDADES DEL SISTEMA:\n");
        help.append("  clear         - Limpiar terminal\n");
        help.append("  history       - Historial de comandos\n");
        help.append("  date          - Fecha y hora actual\n");
        help.append("  calc [expr]   - Calculadora\n");
        help.append("  ping          - Test de conexión\n");
        help.append("  restart       - Reiniciar sistema\n\n");
        
        help.append("🎓 APRENDIZAJE:\n");
        help.append("  tutorial      - Tutorial del sistema\n");
        help.append("  about         - Información del sistema\n");
        help.append("  exit          - Salir (usar BACK)\n\n");
        
        help.append("💡 CONSEJOS:\n");
        help.append("• Usa TAB para autocompletar comandos\n");
        help.append("• Flechas ARRIBA/ABAJO para historial\n");
        help.append("• Los comandos son case-insensitive\n");
        help.append("• Puedes usar comandos en español/inglés\n");
        
        return help.toString();
    }
    
    private String showStatus() {
        StringBuilder status = new StringBuilder();
        status.append("📊 ESTADO DEL SISTEMA - HACKER TERMINAL\n");
        status.append("═══════════════════════════════════════\n\n");
        
        status.append("👤 INFORMACIÓN DEL USUARIO:\n");
        status.append("  🎯 Rango: ").append(playerProgress.getHackerRankName()).append("\n");
        status.append("  💰 Dinero: ").append(playerProgress.getMoneyFormatted()).append("\n");
        status.append("  💎 Valor total: $").append(String.format("%,.0f", playerProgress.getTotalValue())).append("\n");
        status.append("  📈 Total ganado: ").append(playerProgress.getEarnedFormatted()).append("\n\n");
        
        status.append("🔧 HABILIDADES PRINCIPALES:\n");
        status.append("  🔓 Hacking:   Nvl ").append(playerProgress.getSkillHacking())
             .append(" (+").append(playerProgress.getTimeBonus()).append("s, +").append(playerProgress.getExtraAttempts()).append(" intentos)\n");
        status.append("  🦉 Stealth:   Nvl ").append(playerProgress.getSkillStealth())
             .append(" (Dinero x").append(playerProgress.getComboMultiplier()).append(")\n");
        status.append("  🔐 Crypto:    Nvl ").append(playerProgress.getSkillCrypto())
             .append(" (Dinero x").append(playerProgress.getMoneyMultiplier()).append(")\n\n");
        
        status.append("📈 ESTADÍSTICAS DE JUEGO:\n");
        status.append("  🎮 Partidas jugadas: ").append(playerProgress.getGamesPlayed()).append("\n");
        status.append("  🔓 Hacks exitosos: ").append(playerProgress.getTotalHacks()).append("\n");
        status.append("  ⚡ Mejor combo: x").append(playerProgress.getBestCombo()).append("\n");
        status.append("  🛠️ Herramientas: ").append(playerProgress.getUnlockedTools().size()).append(" desbloqueadas\n");
        status.append("  ⏱️ Tiempo total: ").append(formatTime(playerProgress.getTotalTime())).append("\n\n");
        
        status.append("🎯 PROGRESO ACTUAL:\n");
        status.append("  ").append(getProgressBar()).append("\n");
        
        return status.toString();
    }
    
    private String startHackingGame(String target) {
        if (hackingGameActive) {
            return "❌ Ya hay una sesión de hacking activa\n" +
                   "💡 Termina el hack actual o escribe 'stop'\n";
        }
        
        hackingGameActive = true;
        waitingForPassword = true;
        hackingLevel = 1;
        hackingAttempts = 3 + playerProgress.getExtraAttempts();
        hackingTime = 30 + playerProgress.getTimeBonus();
        
        // Generar contraseña para hackear
        String[] passwords = {"ALFA", "BETA", "GAMMA", "DELTA", "OMEGA", "SIGMA", "ZULU", "CYBER", "ROGUE", "QUANTUM"};
        currentPassword = passwords[random.nextInt(passwords.length)];
        
        String pista = generarPista(currentPassword);
        String objetivo = target.isEmpty() ? "BANCO CORPORATIVO" : target.toUpperCase();
        
        return "🎮 MODO HACKING ACTIVADO\n" +
               "══════════════════════════\n\n" +
               "🔓 OBJETIVO: " + objetivo + "\n" +
               "📊 NIVEL: " + hackingLevel + "\n" +
               "⏰ Tiempo: " + hackingTime + " segundos\n" +
               "🎯 Intentos: " + hackingAttempts + "\n" +
               "💰 Recompensa base: $" + (200 + (hackingLevel * 50)) + "\n" +
               "🌟 Multiplicadores: Combo x" + playerProgress.getComboMultiplier() + 
               " | Dinero x" + playerProgress.getMoneyMultiplier() + "\n\n" +
               "🔍 PISTA: " + pista + "\n\n" +
               "💻 ESCRIBE LA CONTRASEÑA PARA HACKEAR:\n" +
               "> ";
    }
    
    private String processHackingInput(String input) {
        if (!hackingGameActive || !waitingForPassword) {
            return "❌ No hay sesión de hacking activa\n" +
                   "💡 Usa 'hack' para comenzar\n";
        }
        
        if (input.equalsIgnoreCase("stop") || input.equalsIgnoreCase("cancelar")) {
            hackingGameActive = false;
            waitingForPassword = false;
            return "⏹️ Sesión de hacking cancelada\n" +
                   "💡 Usa 'hack' para intentar de nuevo\n";
        }
        
        if (input.equalsIgnoreCase(currentPassword)) {
            // ¡Hack exitoso!
            hackingGameActive = false;
            waitingForPassword = false;
            
            // Calcular recompensa
            int baseReward = 200 + (hackingLevel * 50);
            int bonusCombo = hackingLevel * 10 * playerProgress.getComboMultiplier();
            int totalReward = (baseReward + bonusCombo) * playerProgress.getMoneyMultiplier();
            
            // Añadir recompensa
            playerProgress.addMissionEarnings(totalReward, getMissionTypeByLevel(hackingLevel));
            playerProgress.incrementTotalHacks();
            playerProgress.updateBestCombo(hackingLevel);
            
            StringBuilder result = new StringBuilder();
            result.append("✅ ¡HACK EXITOSO! 🔓\n");
            result.append("🎉 Has comprometido el sistema objetivo\n\n");
            result.append("💰 RECOMPENSAS OBTENIDAS:\n");
            result.append("  • Dinero: $").append(totalReward).append("\n");
            result.append("  • Combo: x").append(hackingLevel).append("\n");
            result.append("  • Experiencia: +").append(hackingLevel * 10).append(" XP\n\n");
            
            // Mostrar crypto ganada si la hay
            if (playerProgress.hasCryptoEarnings()) {
                result.append("🌟 BONUS CRYPTO:\n");
                result.append("  ").append(playerProgress.getRecentCryptoEarnings()).append("\n\n");
            }
            
            result.append("📊 ESTADO ACTUAL:\n");
            result.append("  • Dinero total: ").append(playerProgress.getMoneyFormatted()).append("\n");
            result.append("  • Valor total: $").append(String.format("%,.0f", playerProgress.getTotalValue())).append("\n");
            result.append("  • Hacks exitosos: ").append(playerProgress.getTotalHacks()).append("\n\n");
            
            result.append("💡 Usa 'hack' para otra sesión o 'missions' para contratos\n");
            
            return result.toString();
        } else {
            hackingAttempts--;
            
            if (hackingAttempts <= 0) {
                hackingGameActive = false;
                waitingForPassword = false;
                playerProgress.incrementGamesPlayed();
                
                return "❌ ¡HACK FALLIDO! 🚫\n" +
                       "💀 Has sido detectado por el sistema\n" +
                       "🔒 Conexión terminada - Intentos agotados\n\n" +
                       "📈 Lección aprendida: +10 XP\n" +
                       "💡 Usa 'hack' para intentar de nuevo\n" +
                       "🔧 Mejora tus habilidades con 'upgrade hacking'\n";
            } else {
                String pista = generarPista(currentPassword);
                return "❌ Contraseña incorrecta\n" +
                       "🎯 Intentos restantes: " + hackingAttempts + "\n" +
                       "🔍 PISTA MEJORADA: " + pista + "\n\n" +
                       "💻 INTENTA DE NUEVO:\n" +
                       "> ";
            }
        }
    }
    
    private String startNetworkScan() {
        if (networkScanActive) {
            return "🔍 Escáner de red ya está activo\n" +
                   "💡 Escribe 'stop' para detener el escaneo\n";
        }
        
        networkScanActive = true;
        
        // Simular escaneo de red
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Aquí iría la lógica de escaneo en tiempo real
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        return "🔍 INICIANDO ESCÁNER DE RED...\n" +
               "══════════════════════════════\n\n" +
               "📡 Configurando interfaz de red...\n" +
               "🌐 Protocolo: TCP/IP\n" +
               "🎯 Rango: 192.168.1.0/24\n" +
               "🔍 Escaneando dispositivos...\n\n" +
               "💡 Escribe 'stop' para detener el escaneo\n" +
               "📊 Dispositivos detectados aparecerán aquí:\n\n";
    }
    
    private String stopNetworkScan() {
        if (!networkScanActive) {
            return "❌ No hay escáner de red activo\n";
        }
        
        networkScanActive = false;
        return "⏹️ Escáner de red detenido\n" +
               "📊 Resumen: 8 dispositivos detectados\n" +
               "💡 Usa 'scan' para nuevo escaneo\n";
    }
    
    private String startTrafficMonitor() {
        if (trafficMonitorActive) {
            return "📡 Monitor de tráfico ya está activo\n" +
                   "💡 Escribe 'stop' para detener monitoreo\n";
        }
        
        trafficMonitorActive = true;
        
        return "📡 INICIANDO MONITOR DE TRÁFICO...\n" +
               "══════════════════════════════════\n\n" +
               "🔍 Capturando paquetes de red...\n" +
               "🌐 Interfaz: eth0 (promiscuo)\n" +
               "📊 Buffer: 64MB asignado\n" +
               "⚠️  Detección de intrusos: ACTIVADA\n\n" +
               "💡 Escribe 'stop' para detener monitoreo\n" +
               "📈 Paquetes capturados:\n\n" +
               "> 📦 [TCP] 192.168.1.15:54321 → 93.184.216.34:80 [SYN]\n" +
               "> 📦 [UDP] 192.168.1.23:12345 → 8.8.8.8:53 [DNS Query]\n" +
               "> 📦 [HTTP] 192.168.1.42 → facebook.com [GET /]\n";
    }
    
    private String stopTrafficMonitor() {
        if (!trafficMonitorActive) {
            return "❌ No hay monitor de tráfico activo\n";
        }
        
        trafficMonitorActive = false;
        return "⏹️ Monitor de tráfico detenido\n" +
               "📊 Resumen: 1,247 paquetes analizados\n" +
               "💡 Usa 'traffic' para nuevo monitoreo\n";
    }
    
    private String startWebBrowse() {
        if (webBrowseActive) {
            return "🌐 Navegador web ya está activo\n" +
                   "💡 Escribe 'stop' para detener navegación\n";
        }
        
        webBrowseActive = true;
        
        return "🌐 INICIANDO EXPLORADOR WEB ANÓNIMO...\n" +
               "══════════════════════════════════════\n\n" +
               "🕵️‍♂️ Configurando TOR... OK\n" +
               "🔒 Proxy: 7 capas configurado\n" +
               "🌍 User Agent: Firefox/Linux anónimo\n" +
               "📡 Conexión: Encriptada (AES-256)\n\n" +
               "🚀 Conectando a red TOR...\n" +
               "📍 IP enmascarada: 193.105.134." + random.nextInt(255) + "\n\n" +
               "💡 Escribe 'stop' para detener navegación\n" +
               "🌐 Sitios visitados:\n\n" +
               "> 🔗 darknet-market.org [CARGANDO...]\n" +
               "> 🔗 hidden-wiki.net [CONECTADO]\n" +
               "> 🔗 bitcoin-mixer.io [SEGURO]\n";
    }
    
    private String stopWebBrowse() {
        if (!webBrowseActive) {
            return "❌ No hay navegador web activo\n";
        }
        
        webBrowseActive = false;
        return "⏹️ Navegador web detenido\n" +
               "📊 Resumen: 15 sitios visitados anónimamente\n" +
               "💡 Usa 'web' para nueva sesión\n";
    }
    
    private String startFirewall() {
        if (firewallActive) {
            return "🛡️ Firewall ya está activo\n" +
                   "💡 Escribe 'stop' para desactivar\n";
        }
        
        firewallActive = true;
        
        return "🛡️ ACTIVANDO SISTEMA FIREWALL...\n" +
               "════════════════════════════════\n\n" +
               "🔒 Cargando reglas de seguridad... OK\n" +
               "📊 Reglas activas: 42\n" +
               "⚠️  Detección de intrusos: ACTIVADA\n" +
               "📡 Monitoreo en tiempo real: INICIADO\n\n" +
               "🛡️ ESTADO: PROTEGIDO\n" +
               "🎯 Ataques bloqueados: 0\n" +
               "📈 Paquetes analizados: 0\n\n" +
               "💡 Escribe 'stop' para desactivar firewall\n" +
               "🔧 Usa 'tools firewall' para más opciones\n";
    }
    
    private String stopFirewall() {
        if (!firewallActive) {
            return "❌ Firewall no está activo\n";
        }
        
        firewallActive = false;
        return "⏹️ Firewall desactivado\n" +
               "⚠️  ADVERTENCIA: Sistema desprotegido\n" +
               "📊 Resumen: 3 ataques bloqueados\n" +
               "💡 Usa 'firewall' para reactivar\n";
    }
    
    private String showToolsMenu(String subcommand) {
        if (subcommand.isEmpty()) {
            return "🛠️ MÓDULOS ESPECIALIZADOS - HACKER TOOLKIT\n" +
                   "═══════════════════════════════════════════\n\n" +
                   "🔍  scan     - Escáner de red y dispositivos\n" +
                   "🔓  crypto   - Sistema de criptoanálisis\n" +
                   "🌐  web      - Explorador web anónimo\n" +
                   "📡  traffic  - Monitor de tráfico de red\n" +
                   "🛡️  firewall - Sistema de protección\n" +
                   "💾  files    - Gestor de archivos\n" +
                   "📊  missions - Panel de misiones\n\n" +
                   "🎯 HERRAMIENTAS AVANZADAS:\n" +
                   "🔦  portscan - Escáner de puertos\n" +
                   "📡  packetsniffer - Analizador de paquetes\n" +
                   "🔐  passwordcracker - Crackeo de contraseñas\n" +
                   "🌐  webserver - Servidor web local\n\n" +
                   "💡 USO: tools [módulo] para acceder directamente\n" +
                   "📝 Ejemplo: 'tools scan' para escáner de red\n" +
                   "🛑 Escribe 'stop' en cualquier módulo para salir\n";
        }
        
        switch (subcommand) {
            case "scan":
                return startNetworkScan();
            case "crypto":
                return openCryptoModule();
            case "web":
                return startWebBrowse();
            case "traffic":
                return startTrafficMonitor();
            case "firewall":
                return startFirewall();
            case "files":
                return openFileManager();
            case "missions":
                return openMissions();
            case "portscan":
                return "🔦 ESCÁNER DE PUERTOS\nNo implementado aún\n";
            case "packetsniffer":
                return "📡 ANALIZADOR DE PAQUETES\nNo implementado aún\n";
            case "passwordcracker":
                return "🔐 CRACKEADOR DE CONTRASEÑAS\nNo implementado aún\n";
            case "webserver":
                return "🌐 SERVIDOR WEB LOCAL\nNo implementado aún\n";
            default:
                return "❌ Módulo no encontrado: '" + subcommand + "'\n" +
                       "💡 Escribe 'tools' para ver módulos disponibles\n";
        }
    }
    
    // Métodos auxiliares
    private String generarPista(String password) {
        if (password.length() <= 2) return password;
        StringBuilder pista = new StringBuilder();
        pista.append(password.charAt(0));
        for (int i = 1; i < password.length() - 1; i++) {
            pista.append('*');
        }
        pista.append(password.charAt(password.length() - 1));
        return pista.toString();
    }
    
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
    
    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    private String getProgressBar() {
        double totalValue = playerProgress.getTotalValue();
        int progress = (int) (totalValue / 1000000.0 * 20); // Basado en $1M para 100%
        progress = Math.min(progress, 20);
        
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < 20; i++) {
            if (i < progress) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("] ").append(progress * 5).append("%");
        
        return bar.toString();
    }
    
    private String suggestCommand(String command) {
        String[] suggestions = {
            "help", "status", "hack", "tools", "scan", "crypto", 
            "web", "traffic", "firewall", "files", "missions",
            "market", "upgrade", "money", "rank", "skills"
        };
        
        for (String suggestion : suggestions) {
            if (suggestion.startsWith(command.toLowerCase())) {
                return "'" + suggestion + "'";
            }
        }
        
        return "'help'";
    }
    
    // Los demás métodos se mantienen igual que en la versión anterior
    private String openCryptoModule() {
        return "🔓 SISTEMA DE CRIPTOANÁLISIS ACTIVADO\n" +
               "══════════════════════════════════════\n\n" +
               "📝 Cifrado César ROT13 disponible\n" +
               "🔐 Base64 encoding/decoding\n" +
               "🔎 Hash MD5 simulado\n" +
               "🔑 Algoritmos soportados:\n" +
               "  • ROT13, ROT47\n" +
               "  • Base64, Hex\n" +
               "  • MD5, SHA-1 (simulado)\n\n" +
               "💡 Usa este módulo para practicar criptografía\n" +
               "🔧 Funcionalidad completa en desarrollo\n";
    }
    
    private String openFileManager() {
        return "💾 GESTOR DE ARCHIVOS ACTIVADO\n" +
               "═══════════════════════════════\n\n" +
               "📁 Sistema de archivos: EXT4\n" +
               "💽 Espacio libre: 1.2 TB disponible\n" +
               "🔐 Cifrado AES-256: ACTIVO\n" +
               "📊 Archivos en sistema: 156\n\n" +
               "📂 DIRECTORIO ACTUAL: /root/\n" +
               "  📄 config.txt (2.5 KB)\n" +
               "  🔒 passwords.enc (1.8 KB)\n" +
               "  📁 logs/ (DIR)\n" +
               "  ⚡ system.dll (45.2 KB)\n" +
               "  📄 readme.md (5.1 KB)\n\n" +
               "💡 Gestión segura de archivos disponible\n" +
               "🔧 Usa comandos: ls, cd, mkdir, rm, cp, mv\n";
    }
    
    private String openMissions() {
        return "📊 PANEL DE MISIONES CARGADO\n" +
               "════════════════════════════\n\n" +
               "🎯 CONTRATOS DISPONIBLES:\n" +
               "  1. Infiltración Básica - $500 (Fácil)\n" +
               "  2. Robo de Datos - $1,000 (Media)\n" +
               "  3. Desactivar Firewall - $1,500 (Difícil)\n\n" +
               "💰 RECOMPENSAS ACTIVAS:\n" +
               "  • Hackeo exitoso: +$200-$500\n" +
               "  • Combo x5: Bonus +$100\n" +
               "  • Tiempo récord: Bonus +$50\n\n" +
               "💡 Usa 'hack' para comenzar misiones\n" +
               "📈 Sistema de reputación: ACTIVO\n";
    }
    
    private String showMarket() {
        return "🏴‍☠️ MERCADO NEGRO - HERRAMIENTAS ILEGALES\n" +
               "════════════════════════════════════════\n\n" +
               "🔧 HERRAMIENTAS BÁSICAS:\n" +
               "  🔍 Scanner Básico       - $1,000\n" +
               "  💾 Keylogger            - $2,500\n" +
               "  🌐 VPN Premium          - $5,000\n" +
               "  🛡️ Firewall Personal    - $7,500\n\n" +
               "⚡ HERRAMIENTAS AVANZADAS:\n" +
               "  🔓 Cracker SSL          - $15,000\n" +
               "  📡 Packet Sniffer       - $25,000\n" +
               "  🕵️‍♂️ Identity Spoofer     - $50,000\n" +
               "  🌐 DDoS Botnet          - $100,000\n\n" +
               "💎 HERRAMIENTAS ÉLITE:\n" +
               "  🔮 Zero-Day Exploit     - $250,000\n" +
               "  🧠 AI Hacking Assistant - $500,000\n" +
               "  🌍 Global Backdoor      - $1,000,000\n\n" +
               "💡 Usa: upgrade [habilidad] para mejorar\n" +
               "💰 Tu dinero: " + playerProgress.getMoneyFormatted() + "\n";
    }
    
    private String processUpgrade(String skill) {
        if (skill.isEmpty()) {
            return "❌ Especifica una habilidad para mejorar\n" +
                   "💡 Opciones: hacking, stealth, crypto\n" +
                   "📝 Ejemplo: upgrade hacking\n";
        }
        
        switch (skill) {
            case "hacking":
                int hackCost = playerProgress.getSkillHacking() * 1000;
                if (playerProgress.upgradeSkill("hacking", hackCost)) {
                    return "✅ HACKING MEJORADO! Nivel " + playerProgress.getSkillHacking() + "\n" +
                           "🎯 Efecto: +5 segundos y +1 intento cada 2 niveles\n" +
                           "💰 Costo: $" + hackCost + "\n" +
                           "💳 Dinero restante: " + playerProgress.getMoneyFormatted() + "\n";
                } else {
                    return "❌ Fondos insuficientes para mejorar Hacking\n" +
                           "💵 Necesitas: $" + hackCost + "\n" +
                           "💰 Tienes: " + playerProgress.getMoneyFormatted() + "\n";
                }
                
            case "stealth":
                int stealthCost = playerProgress.getSkillStealth() * 1200;
                if (playerProgress.upgradeSkill("stealth", stealthCost)) {
                    return "✅ STEALTH MEJORADO! Nivel " + playerProgress.getSkillStealth() + "\n" +
                           "🎯 Efecto: Multiplicador de dinero aumentado\n" +
                           "💰 Costo: $" + stealthCost + "\n" +
                           "💳 Dinero restante: " + playerProgress.getMoneyFormatted() + "\n";
                } else {
                    return "❌ Fondos insuficientes para mejorar Stealth\n" +
                           "💵 Necesitas: $" + stealthCost + "\n" +
                           "💰 Tienes: " + playerProgress.getMoneyFormatted() + "\n";
                }
                
            case "crypto":
                int cryptoCost = playerProgress.getSkillCrypto() * 1500;
                if (playerProgress.upgradeSkill("crypto", cryptoCost)) {
                    return "✅ CRYPTO MEJORADO! Nivel " + playerProgress.getSkillCrypto() + "\n" +
                           "🎯 Efecto: Multiplicador crypto +10% y dinero\n" +
                           "💰 Costo: $" + cryptoCost + "\n" +
                           "💳 Dinero restante: " + playerProgress.getMoneyFormatted() + "\n";
                } else {
                    return "❌ Fondos insuficientes para mejorar Crypto\n" +
                           "💵 Necesitas: $" + cryptoCost + "\n" +
                           "💰 Tienes: " + playerProgress.getMoneyFormatted() + "\n";
                }
                
            default:
                return "❌ Habilidad no reconocida: '" + skill + "'\n" +
                       "💡 Habilidades disponibles: hacking, stealth, crypto\n";
        }
    }
    
    private String openTutorial() {
        playerProgress.setTutorialCompleted();
        return "🎓 TUTORIAL HACKER TERMINAL\n" +
               "═══════════════════════════\n\n" +
               "👋 BIENVENIDO AL SISTEMA:\n" +
               "Este es un simulador de hacking profesional\n" +
               "con interfaz de terminal estilo Termux.\n\n" +
               "📋 COMANDOS BÁSICOS:\n" +
               "• 'help' - Ver todos los comandos\n" + 
               "• 'status' - Tu progreso y estadísticas\n" +
               "• 'hack' - Juego principal para ganar dinero\n" +
               "• 'tools' - Módulos especializados\n\n" +
               "💰 SISTEMA ECONÓMICO:\n" +
               "• Gana dinero hackeando sistemas\n" +
               "• Mejora habilidades con 'upgrade'\n" +
               "• Compra herramientas en 'market'\n" +
               "• Sube de rango con tu valor total\n\n" +
               "🎯 ESTRATEGIA RECOMENDADA:\n" +
               "1. Comienza con 'hack' para ganar dinero\n" +
               "2. Mejora 'hacking' para más intentos\n" +
               "3. Explora todos los 'tools'\n" +
               "4. Completa misiones para bonus\n\n" +
               "🔧 MÓDULOS DISPONIBLES:\n" +
               "• scan - Descubre dispositivos en red\n" +
               "• crypto - Sistema de cifrado/descifrado\n" +
               "• web - Navegación anónima\n" +
               "• traffic - Análisis de tráfico\n" +
               "• firewall - Protección del sistema\n\n" +
               "✅ Tutorial marcado como completado\n" +
               "🚀 ¡Que comience tu journey hacker!\n";
    }
    
    private String showAbout() {
        return "ℹ️  HACKER SYSTEM v2.0 - TERMINAL EDITION\n" +
               "══════════════════════════════════════════\n\n" +
               "👨‍💻 DESARROLLADO POR: Equipo Hacker Terminal Pro\n" +
               "🎯 LÍDER: [Tu Nombre Aquí]\n\n" +
               "🔧 CARACTERÍSTICAS TÉCNICAS:\n" +
               "• Terminal estilo Termux profesional\n" +
               "• 9 módulos especializados integrados\n" +
               "• Sistema económico con 5 divisas\n" +
               "• Progresión permanente con habilidades\n" +
               "• Interfaz optimizada para teclado físico\n" +
               "• Base de datos SQLite para persistencia\n\n" +
               "🎮 MÓDULOS IMPLEMENTADOS:\n" +
               "✅ Terminal de juego principal\n" +
               "✅ Escáner de red simulado\n" +
               "✅ Sistema de criptoanálisis\n" +
               "✅ Explorador web anónimo\n" +
               "✅ Monitor de tráfico\n" +
               "✅ Firewall de sistema\n" +
               "✅ Gestor de archivos\n" +
               "✅ Panel de misiones\n" +
               "✅ Sistema de tutorial\n\n" +
               "⚠️  AVISO LEGAL:\n" +
               "Este es un proyecto EDUCATIVO de simulación\n" +
               "Desarrollado con fines de aprendizaje en\n" +
               "ciberseguridad y programación Android.\n\n" +
               "🎯 OBJETIVO: Educación en ethical hacking\n";
    }
    
    private String showWallet() {
        return playerProgress.getWalletInfo();
    }
    
    private String showRank() {
        return "📊 SISTEMA DE RANGOS - HACKER PROGRESSION\n" +
               "══════════════════════════════════════════\n\n" +
               "🎯 TU RANGO ACTUAL: " + playerProgress.getHackerRankName() + "\n" +
               "💰 VALOR TOTAL: $" + String.format("%,.0f", playerProgress.getTotalValue()) + "\n\n" +
               "📈 PROGRESIÓN DE RANGOS:\n" +
               "🟢 NOVATO       - $0 a $24,999\n" +
               "🔵 APRENDIZ     - $25,000 a $74,999\n" +
               "🟡 EXPERTO      - $75,000 a $199,999\n" +
               "🟠 ÉLITE        - $200,000 a $499,999\n" +
               "🔴 MAESTRO      - $500,000 a $999,999\n" +
               "💀 LEYENDA      - $1,000,000+\n\n" +
               "🎁 RECOMPENSAS POR RANGO:\n" +
               "• Nuevas herramientas desbloqueadas\n" +
               "• Multiplicadores de dinero mejorados\n" +
               "• Acceso a contratos exclusivos\n" +
               "• Herramientas élite desbloqueadas\n\n" +
               "💡 Sigue hackeando para subir de rango!\n" +
               "🚀 Tu progreso: " + getProgressBar() + "\n";
    }
    
    private String showSkills() {
        return "🎯 SISTEMA DE HABILIDADES\n" +
               "═════════════════════════\n\n" +
               playerProgress.getSkillInfo("hacking") + "\n\n" +
               playerProgress.getSkillInfo("stealth") + "\n\n" +
               playerProgress.getSkillInfo("crypto") + "\n\n" +
               "💡 Usa: upgrade [habilidad] para mejorar\n" +
               "💰 Ejemplo: 'upgrade hacking'\n" +
               "🎯 Estrategia: Mejora HACKING primero\n";
    }
    
    private String showAchievements() {
        return playerProgress.getAchievements();
    }
    
    private String showInventory() {
        StringBuilder inventory = new StringBuilder();
        inventory.append("🎒 INVENTARIO - HERRAMIENTAS DESBLOQUEADAS\n");
        inventory.append("══════════════════════════════════════════\n\n");
        
        java.util.Set<String> tools = playerProgress.getUnlockedTools();
        if (tools.isEmpty()) {
            inventory.append("📭 No hay herramientas desbloqueadas\n");
            inventory.append("💡 Desbloquea herramientas subiendo de rango\n");
            inventory.append("🛒 O compra en el mercado con 'market'\n");
        } else {
            inventory.append("🔧 HERRAMIENTAS ACTIVAS:\n");
            for (String tool : tools) {
                inventory.append("  • ").append(tool).append("\n");
            }
            inventory.append("\n📊 Total: ").append(tools.size()).append(" herramientas\n");
        }
        
        inventory.append("\n💡 Usa 'market' para comprar más herramientas\n");
        inventory.append("🎯 Sube de rango para desbloquear herramientas élite\n");
        
        return inventory.toString();
    }
    
    // Métodos adicionales para comandos del sistema
    private String clearTerminal() {
        return "\033[H\033[2J"; // Códigos ANSI para limpiar pantalla
    }
    
    private String exitTerminal() {
        return "⏹️  Usa el botón BACK del dispositivo para salir\n" +
               "💾 Tu progreso se guarda automáticamente\n" +
               "🚪 O escribe 'restart' para reiniciar el sistema\n";
    }
    
    private String showTime() {
        return "⏰ SISTEMA DE TIEMPO\n" +
               "Tiempo de juego total: " + formatTime(playerProgress.getTotalTime()) + "\n" +
               "💡 El tiempo corre mientras juegas\n";
    }
    
    private String restartSystem() {
        return "🔄 REINICIANDO SISTEMA...\n" +
               "💾 Guardando progreso... OK\n" +
               "🔧 Recargando módulos... OK\n" +
               "🚀 Sistema reiniciado correctamente\n\n" +
               "root@hacker-system:~$ ";
    }
    
    private String showVersion() {
        return "🖥️ HACKER TERMINAL v2.0\n" +
               "Build: 2025.01.15.RELEASE\n" +
               "Platform: Android AIDE\n" +
               "Architecture: ARM/x86\n" +
               "Status: 🟢 OPERATIONAL\n";
    }
    
    private String showCommandHistory() {
        return "📜 HISTORIAL DE COMANDOS\n" +
               "Últimos 10 comandos:\n" +
               "1. help\n" +
               "2. status\n" +
               "3. hack\n" +
               "4. tools\n" +
               "5. market\n" +
               "6. upgrade hacking\n" +
               "7. money\n" +
               "8. rank\n" +
               "9. skills\n" +
               "10. achievements\n\n" +
               "💡 Usa flechas ARRIBA/ABAJO para navegar\n";
    }
    
    private String testConnection() {
        return "🌐 TEST DE CONEXIÓN\n" +
               "Pinging 8.8.8.8...\n" +
               "Reply from 8.8.8.8: time=32ms\n" +
               "Reply from 8.8.8.8: time=28ms\n" +
               "Reply from 8.8.8.8: time=35ms\n\n" +
               "📊 Estadísticas:\n" +
               "Paquetes: Enviados=3, Recibidos=3, Perdidos=0\n" +
               "Tiempo aprox: 32ms\n" +
               "Estado: 🟢 CONEXIÓN ESTABLE\n";
    }
    
    private String showUserInfo() {
        return "👤 INFORMACIÓN DE USUARIO\n" +
               "Usuario: root\n" +
               "Grupo: hacker\n" +
               "UID: 0\n" +
               "GID: 0\n" +
               "Shell: /system/bin/hacker-terminal\n" +
               "Home: /root\n" +
               "Login: " + java.time.LocalDateTime.now().toString() + "\n";
    }
    
    private String showCurrentDirectory() {
        return "📁 DIRECTORIO ACTUAL\n" +
               "/root/hacker-system/\n" +
               "💡 Usa 'ls' para listar archivos\n";
    }
    
    private String listFiles() {
        return "📊 CONTENIDO DE /root/hacker-system/\n\n" +
               "drwxr-xr-x root root 4.0K Jan 15 10:30 ./\n" +
               "drwxr-xr-x root root 4.0K Jan 15 10:30 ../\n" +
               "-rw-r--r-- root root 2.5K Jan 15 10:25 config.txt\n" +
               "-rw-r--r-- root root 1.8K Jan 15 10:25 passwords.enc\n" +
               "drwxr-xr-x root root 4.0K Jan 15 10:25 logs/\n" +
               "-rwxr-xr-x root root  45K Jan 15 10:25 system.dll*\n" +
               "-rw-r--r-- root root 5.1K Jan 15 10:25 readme.md\n" +
               "-rw-r--r-- root root 3.2K Jan 15 10:25 keys.pem\n" +
               "drwxr-xr-x root root 4.0K Jan 15 10:25 documents/\n" +
               "-rw-r--r-- root root 102K Jan 15 10:25 backup.zip\n\n" +
               "💡 Total: 9 elementos\n";
    }
    
    private String changeDirectory(String dir) {
        return "📁 Cambiando a directorio: " + (dir.isEmpty() ? "/" : dir) + "\n" +
               "💡 Directorio cambiado correctamente\n";
    }
    
    private String createDirectory(String name) {
        if (name.isEmpty()) {
            return "❌ Especifica un nombre para el directorio\n";
        }
        return "📁 Creando directorio: " + name + "\n" +
               "✅ Directorio creado correctamente\n";
    }
    
    private String deleteFile(String file) {
        if (file.isEmpty()) {
            return "❌ Especifica un archivo para eliminar\n";
        }
        return "🗑️ Eliminando archivo: " + file + "\n" +
               "✅ Archivo eliminado correctamente\n";
    }
    
    private String copyFile(String src, String dst) {
        if (src.isEmpty() || dst.isEmpty()) {
            return "❌ Especifica origen y destino\n";
        }
        return "📋 Copiando " + src + " → " + dst + "\n" +
               "✅ Archivo copiado correctamente\n";
    }
    
    private String moveFile(String src, String dst) {
        if (src.isEmpty() || dst.isEmpty()) {
            return "❌ Especifica origen y destino\n";
        }
        return "🚚 Moviendo " + src + " → " + dst + "\n" +
               "✅ Archivo movido correctamente\n";
    }
    
    private String calculator(String expression) {
        if (expression.isEmpty()) {
            return "🧮 CALCULADORA HACKER\n" +
                   "Ejemplos: 'calc 2+2', 'calc 100*1.5', 'calc 500-250'\n";
        }
        
        try {
            // Evaluación simple de expresiones
            double result = eval(expression);
            return "🧮 RESULTADO: " + expression + " = " + result + "\n";
        } catch (Exception e) {
            return "❌ Error en la expresión: " + expression + "\n" +
                   "💡 Usa: +, -, *, /, números y punto decimal\n";
        }
    }
    
    private double eval(final String expression) {
    // Hacemos la variable final y usamos una clase interna
    return new ExpressionEvaluator(expression).parse();
}

// Clase interna para evaluar expresiones
private class ExpressionEvaluator {
    private final String expression;
    private int pos = -1;
    private int ch;
    
    public ExpressionEvaluator(String expression) {
        this.expression = expression;
    }
    
    void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }
    
    boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }
    
    double parse() {
        nextChar();
        double x = parseExpression();
        if (pos < expression.length()) throw new RuntimeException("Carácter inesperado: " + (char)ch);
        return x;
    }
    
    double parseExpression() {
        double x = parseTerm();
        for (;;) {
            if      (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }
    
    double parseTerm() {
        double x = parseFactor();
        for (;;) {
            if      (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else return x;
        }
    }
    
    double parseFactor() {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();
        
        double x;
        int startPos = this.pos;
        if (eat('(')) {
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(expression.substring(startPos, this.pos));
        } else {
            throw new RuntimeException("Carácter inesperado: " + (char)ch);
        }
        
        return x;
    }
}
    
    private String showDateTime() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        return "📅 FECHA Y HORA DEL SISTEMA\n" +
               "Fecha: " + now.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
               "Hora: " + now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n" +
               "Zona: " + java.time.ZoneId.systemDefault() + "\n";
    }
    
    private String showWeather() {
        return "🌤️  INFORMACIÓN DEL CLIMA\n" +
               "Ubicación: Underground Data Center\n" +
               "Temperatura: 18°C (64°F)\n" +
               "Humedad: 45%\n" +
               "Condiciones: Climatizado artificialmente\n" +
               "💡 Servidor: Siempre fresco y seco\n";
    }
    
    private String showNews() {
        return "📰 ULTIMAS NOTICIAS HACKER\n\n" +
               "🔓 NUEVA VULNERABILIDAD DESCUBIERTA\n" +
               "Security researchers han encontrado...\n\n" +
               "💰 BITCOIN SUPERa los $100,000\n" +
               "El mercado crypto continúa su rally...\n\n" +
               "🌐 INTERNET CUMPLE 60 AÑOS\n" +
               "La red que cambió el mundo celebra...\n\n" +
               "💡 Fuente: Hacker News Network\n";
    }

private String checkAchievements() {
    StringBuilder achievements = new StringBuilder();
    achievements.append("🏆 LOGROS RECIENTES:\n\n");
    
    double totalValue = playerProgress.getTotalValue();
    
    if (totalValue >= 1000 && totalValue < 5000) {
        achievements.append("⭐ PRIMER MILLÓN\n   Has alcanzado $1,000\n   ¡Sigue así!\n\n");
    }
    
    if (playerProgress.getTotalHacks() >= 10) {
        achievements.append("🔓 HACKER NOVATO\n   10 hacks exitosos\n   ¡Vas mejorando!\n\n");
    }
    
    if (playerProgress.getSkillHacking() >= 3) {
        achievements.append("⚡ HACKING EXPERTO\n   Nivel 3 de Hacking\n   +15 segundos bonus\n\n");
    }
    
    if (achievements.toString().equals("🏆 LOGROS RECIENTES:\n\n")) {
        achievements.append("📝 Aún no hay logros nuevos\n");
        achievements.append("💡 Sigue hackeando para desbloquear logros\n");
    }
    
    return achievements.toString();
}

private String randomEvent() {
    int event = random.nextInt(10);
    
    switch(event) {
        case 0:
            int bonus = 100 + random.nextInt(400);
            playerProgress.addDollars(bonus);
            return "🎁 EVENTO ESPECIAL: Bono de dinero\n" +
                   "💰 Has recibido: $" + bonus + "\n" +
                   "💳 Total: " + playerProgress.getMoneyFormatted() + "\n";
                    
        case 1:
            double btcBonus = 0.0001 + (random.nextDouble() * 0.0004);
            playerProgress.addBitcoin(btcBonus);
            return "🎁 EVENTO ESPECIAL: Minería Bitcoin\n" +
                   "₿ Has minado: " + String.format("%.6f", btcBonus) + " BTC\n" +
                   "💎 Valor: $" + String.format("%.2f", btcBonus * PlayerProgress.BTC_TO_USD) + "\n";
                    
        case 2:
            return "🎁 EVENTO ESPECIAL: Boost temporal\n" +
                   "⚡ Todas las habilidades +1 nivel por 5 hacks\n" +
                   "🎯 ¡Aprovecha este bonus!\n";
                    
        default:
            return "📊 No hay eventos especiales en este momento\n" +
                   "💡 Los eventos ocurren aleatoriamente\n";
    }
}

private String dailyMissions() {
    return "📅 MISIONES DIARIAS - " + java.time.LocalDate.now() + "\n" +
           "════════════════════════════════\n\n" +
           "🎯 MISIONES ACTIVAS:\n" +
           "1. Completa 3 hacks exitosos - $300\n" +
           "2. Alcanza combo x5 - $150\n" + 
           "3. Mejora una habilidad - $200\n" +
           "4. Gana $1,000 total - $500\n\n" +
           "💰 RECOMPENSAS EXTRA:\n" +
           "• Completar todas: Bonus $200\n" +
           "• Tiempo récord: Bonus extra\n\n" +
           "💡 Usa 'hack' para comenzar\n";
}

private String leaderboard() {
    return "🏆 TABLA DE LÍDERES - HACKER RANKING\n" +
           "════════════════════════════════════\n\n" +
           "🥇 [ANÓNIMO] - $2,450,000\n" +
           "🥈 [GHOST] - $1,890,500\n" +
           "🥉 [CYPHER] - $1,235,750\n" +
           "4. [NEO] - $987,300\n" +
           "5. [TRINITY] - $756,800\n\n" +
           "🎯 TU POSICIÓN: #6 - $" + 
           String.format("%,.0f", playerProgress.getTotalValue()) + "\n" +
           "💡 Sube de posición hackeando más sistemas\n";
}

private String advancedStats() {
    return "📈 ESTADÍSTICAS AVANZADAS\n" +
           "════════════════════════\n\n" +
           "🎮 RENDIMIENTO DE HACKING:\n" +
           "• Precisión: " + (playerProgress.getTotalHacks() > 0 ? 
               (playerProgress.getTotalHacks() * 100 / (playerProgress.getTotalHacks() + playerProgress.getGamesPlayed())) : 0) + "%\n" +
           "• Velocidad promedio: " + (playerProgress.getTotalHacks() > 0 ? 
               (playerProgress.getTotalTime() / playerProgress.getTotalHacks() / 1000) : 0) + " seg/hack\n" +
           "• Eficiencia: " + (playerProgress.getTotalValue() > 0 ? 
               ((int)(playerProgress.getTotalValue() / playerProgress.getTotalHacks())) : 0) + "%\n\n" +
           "💰 ANÁLISIS FINANCIERO:\n" +
           "• Ingreso por hora: $" + (playerProgress.getTotalTime() > 0 ? 
               (int)(playerProgress.getTotalValue() / (playerProgress.getTotalTime() / 3600000.0)) : 0) + "\n" +
           "• ROI habilidades: " + (playerProgress.getTotalValue() > 0 ? 
               (int)((playerProgress.getTotalValue() - 15000) / 15000 * 100) : 0) + "%\n" +
           "• Crypto portfolio: " + getCryptoDistribution() + "\n\n" +
           "📊 TENDENCIAS:\n" +
           "• Mejor día: Hoy\n" +
           "• Racha actual: " + playerProgress.getBestCombo() + " hacks\n" +
           "• Predicción: " + getPrediction() + "\n";
}

private String getCryptoDistribution() {
    double total = playerProgress.getTotalValue();
    if (total == 0) return "Sin crypto";
    
    double btcPercent = (playerProgress.getBitcoin() * PlayerProgress.BTC_TO_USD) / total * 100;
    double ethPercent = (playerProgress.getEthereum() * PlayerProgress.ETH_TO_USD) / total * 100;
    
    return String.format("BTC: %.1f%%, ETH: %.1f%%", btcPercent, ethPercent);
}

private String getPrediction() {
    double hourlyIncome = playerProgress.getTotalTime() > 0 ? 
        playerProgress.getTotalValue() / (playerProgress.getTotalTime() / 3600000.0) : 100;
    
    double hoursToMillion = (1000000 - playerProgress.getTotalValue()) / hourlyIncome;
    
    if (hoursToMillion <= 0) {
        return "¡Ya eres millonario! 🎉";
    } else if (hoursToMillion < 24) {
        return "Millonario en " + (int)hoursToMillion + " horas 💰";
    } else {
        return "Millonario en " + (int)(hoursToMillion / 24) + " días 🚀";
    }
}

private String launchFragment(String fragmentName) {
    switch(fragmentName) {
        case "scanner":
        case "scan":
            return "🔍 Lanzando NetworkScannerFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "crypto":
            return "🔓 Lanzando CryptoFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "web":
            return "🌐 Lanzando WebExplorerFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "traffic":
            return "📡 Lanzando TrafficMonitorFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "firewall":
            return "🛡️ Lanzando FirewallFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "files":
            return "💾 Lanzando FileManagerFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "missions":
            return "📊 Lanzando MissionsFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "about":
            return "ℹ️ Lanzando AboutFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        case "tutorial":
            return "🎓 Lanzando TutorialFragment...\n" +
                   "💡 Usa 'back' para regresar a la terminal\n";
        default:
            return "❌ Fragment no encontrado: " + fragmentName + "\n" +
                   "💡 Fragmentos disponibles: scanner, crypto, web, traffic, firewall, files, missions, about, tutorial\n";
    }
} 
}
