package com.hacker.finalapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class PlayerProgress {
    private static final String PREFS_NAME = "player_progress";
    private SharedPreferences prefs;
    private Random random = new Random();

    // ===== CLAVES ACTUALIZADAS PARA CRYPTO =====
    private static final String KEY_DOLLARS = "dollars";
    private static final String KEY_BITCOIN = "bitcoin";
    private static final String KEY_ETHEREUM = "ethereum";
    private static final String KEY_LITECOIN = "litecoin";
    private static final String KEY_DARKCOIN = "darkcoin";
    private static final String KEY_TOTAL_EARNED = "total_earned";
    private static final String KEY_HACKER_RANK = "hacker_rank";
    private static final String KEY_UNLOCKED_TOOLS = "unlocked_tools";
    private static final String KEY_SKILL_HACKING = "skill_hacking";
    private static final String KEY_SKILL_STEALTH = "skill_stealth";
    private static final String KEY_SKILL_CRYPTO = "skill_crypto";
    private static final String KEY_GAMES_PLAYED = "games_played";
    private static final String KEY_TOTAL_HACKS = "total_hacks";
    private static final String KEY_BEST_COMBO = "best_combo";
    private static final String KEY_TOTAL_TIME = "total_time";
    private static final String KEY_TUTORIAL_COMPLETED = "tutorial_completed";
	
    // Tasas de conversión (pueden fluctuar)
    public static final double BTC_TO_USD = 10000000;  // 1 BTC = $10,000,000
    public static final double ETH_TO_USD = 1000000;   // 1 ETH = $1,000,000  
    public static final double LTC_TO_USD = 10000;     // 1 LTC = $10,000
    public static final double DRK_TO_USD = 1;         // 1 DRK = $1

    public PlayerProgress(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        initializeDefaultTools();
    }

    private void initializeDefaultTools() {
        Set<String> tools = getUnlockedTools();
        if (tools.isEmpty()) {
            tools.add("🔍 Scanner Básico");
            prefs.edit().putStringSet(KEY_UNLOCKED_TOOLS, tools).apply();
        }
    }

    // ===== GETTERS ACTUALIZADOS PARA CRYPTO =====
    public double getDollars() {
        return prefs.getFloat(KEY_DOLLARS, 15000.0f); // Empezar con $15,000
    }

    public double getBitcoin() {
        return prefs.getFloat(KEY_BITCOIN, 0.0f);
    }

    public double getEthereum() {
        return prefs.getFloat(KEY_ETHEREUM, 0.0f);
    }

    public double getLitecoin() {
        return prefs.getFloat(KEY_LITECOIN, 0.0f);
    }

    public double getDarkcoin() {
        return prefs.getFloat(KEY_DARKCOIN, 15000.0f); // Equivalente a $15,000 iniciales
    }

    public double getTotalValue() {
        return getDollars() + 
			(getBitcoin() * BTC_TO_USD) + 
			(getEthereum() * ETH_TO_USD) + 
			(getLitecoin() * LTC_TO_USD) + 
			(getDarkcoin() * DRK_TO_USD);
    }

    public int getTotalEarned() {
        return prefs.getInt(KEY_TOTAL_EARNED, 0);
    }

    public int getHackerRank() {
        return prefs.getInt(KEY_HACKER_RANK, 0);
    }

    public String getHackerRankName() {
        int rank = getHackerRank();
        String[] ranks = {"🟢 NOVATO", "🔵 APRENDIZ", "🟡 EXPERTO", "🟠 ÉLITE", "🔴 MAESTRO", "💀 LEYENDA"};
        return ranks[Math.min(rank, ranks.length - 1)];
    }

    // ===== MÉTODOS DE FORMATO ACTUALIZADOS =====
    public String getMoneyFormatted() {
        return String.format("$%,.0f", getDollars());
    }

    public String getEarnedFormatted() {
        int earned = getTotalEarned();
        return String.format("$%,d", earned);
    }

    public Set<String> getUnlockedTools() {
        return prefs.getStringSet(KEY_UNLOCKED_TOOLS, new HashSet<String>());
    }

    public int getSkillHacking() { return prefs.getInt(KEY_SKILL_HACKING, 1); }
    public int getSkillStealth() { return prefs.getInt(KEY_SKILL_STEALTH, 1); }
    public int getSkillCrypto() { return prefs.getInt(KEY_SKILL_CRYPTO, 1); }
    public int getGamesPlayed() { return prefs.getInt(KEY_GAMES_PLAYED, 0); }
    public int getTotalHacks() { return prefs.getInt(KEY_TOTAL_HACKS, 0); }
    public int getBestCombo() { return prefs.getInt(KEY_BEST_COMBO, 0); }
    public long getTotalTime() { return prefs.getLong(KEY_TOTAL_TIME, 0); }

    // ===== SETTERS ACTUALIZADOS PARA CRYPTO =====
    public void addDollars(double amount) {
        double current = getDollars();
        int totalEarned = getTotalEarned();
        prefs.edit()
            .putFloat(KEY_DOLLARS, (float)(current + amount))
            .putInt(KEY_TOTAL_EARNED, totalEarned + (int)amount)
            .apply();
        checkRankUpgrade();
    }

    public void addBitcoin(double amount) {
        double current = getBitcoin();
        prefs.edit().putFloat(KEY_BITCOIN, (float)(current + amount)).apply();
    }

    public void addEthereum(double amount) {
        double current = getEthereum();
        prefs.edit().putFloat(KEY_ETHEREUM, (float)(current + amount)).apply();
    }

    public void addLitecoin(double amount) {
        double current = getLitecoin();
        prefs.edit().putFloat(KEY_LITECOIN, (float)(current + amount)).apply();
    }

    public void addDarkcoin(double amount) {
        double current = getDarkcoin();
        prefs.edit().putFloat(KEY_DARKCOIN, (float)(current + amount)).apply();
    }

    public boolean spendMoney(double amount) {
        double current = getDollars();
        if (current >= amount) {
            prefs.edit().putFloat(KEY_DOLLARS, (float)(current - amount)).apply();
            return true;
        }
        return false;
    }

    public boolean spendMoney(int amount) {
        return spendMoney((double) amount);
    }

    // ===== SISTEMA DE RECOMPENSAS MIXTAS =====
    public void addMissionEarnings(int baseDollars, String missionType) {
        double dollarsEarned = baseDollars;
        double cryptoMultiplier = getCryptoMultiplier();

        switch(missionType) {
            case "HACKEO_BASICO":
                addDollars(dollarsEarned);
                // 25% chance de ganar BTC pequeño
                if (random.nextDouble() < 0.25) {
                    double btcBonus = 0.00005 * cryptoMultiplier * (random.nextDouble() + 0.5);
                    addBitcoin(btcBonus);
                }
                break;

            case "ROBO_DATOS":
                addDollars(dollarsEarned * 0.7);
                // Alta probabilidad de BTC
                double btcEarned = 0.0002 * cryptoMultiplier * (random.nextDouble() + 0.7);
                addBitcoin(btcEarned);
                break;

            case "EXTRACCION_CRYPTO":
                addDollars(dollarsEarned * 0.4);
                // Principalmente ETH y LTC
                double ethEarned = 0.001 * cryptoMultiplier * (random.nextDouble() + 0.6);
                double ltcEarned = 0.05 * cryptoMultiplier * (random.nextDouble() + 0.4);
                addEthereum(ethEarned);
                addLitecoin(ltcEarned);
                break;

            case "MINADO_PASIVO":
                // Principalmente DRK para minado
                double drkEarned = dollarsEarned * 1.5 * cryptoMultiplier;
                addDarkcoin(drkEarned);
                break;

            case "ATAQUE_ELITE":
                // Recompensas grandes en todas las divisas
                addDollars(dollarsEarned * 1.2);
                addBitcoin(0.0005 * cryptoMultiplier);
                addEthereum(0.003 * cryptoMultiplier);
                addLitecoin(0.1 * cryptoMultiplier);
                break;

            default:
                addDollars(dollarsEarned);
                break;
        }

        incrementTotalHacks();
    }

    private double getCryptoMultiplier() {
        return 1.0 + (getSkillCrypto() * 0.1); // +10% por nivel de crypto
    }

    // ===== MÉTODO PARA MOSTRAR GANANCIAS RECIENTES =====
    public String getRecentCryptoEarnings() {
        StringBuilder earnings = new StringBuilder();

        if (getBitcoin() > 0.00001) {
            earnings.append("₿ ").append(String.format("%.6f", getBitcoin())).append(" BTC ");
        }
        if (getEthereum() > 0.0001) {
            earnings.append("Ξ ").append(String.format("%.4f", getEthereum())).append(" ETH ");
        }
        if (getLitecoin() > 0.01) {
            earnings.append("💎 ").append(String.format("%.2f", getLitecoin())).append(" LTC ");
        }
        if (getDarkcoin() > 100) {
            earnings.append("🪙 ").append(String.format("%.0f", getDarkcoin())).append(" DRK");
        }

        return earnings.toString().trim();
    }

    public boolean hasCryptoEarnings() {
        return getBitcoin() > 0.00001 || getEthereum() > 0.0001 || 
			getLitecoin() > 0.01 || getDarkcoin() > 100;
    }

    // ===== MÉTODOS EXISTENTES ACTUALIZADOS =====
    public void incrementGamesPlayed() {
        prefs.edit().putInt(KEY_GAMES_PLAYED, getGamesPlayed() + 1).apply();
    }

    public void incrementTotalHacks() {
        prefs.edit().putInt(KEY_TOTAL_HACKS, getTotalHacks() + 1).apply();
    }

    public void updateBestCombo(int combo) {
        if (combo > getBestCombo()) {
            prefs.edit().putInt(KEY_BEST_COMBO, combo).apply();
        }
    }

    public void addPlayTime(long timeMillis) {
        long current = getTotalTime();
        prefs.edit().putLong(KEY_TOTAL_TIME, current + timeMillis).apply();
    }

    public void unlockTool(String toolName) {
        Set<String> tools = new HashSet<>(getUnlockedTools());
        tools.add(toolName);
        prefs.edit().putStringSet(KEY_UNLOCKED_TOOLS, tools).apply();
    }

    public boolean upgradeSkill(String skillType, int cost) {
        if (spendMoney(cost)) {
            switch (skillType) {
                case "hacking":
                    int hackLvl = getSkillHacking();
                    prefs.edit().putInt(KEY_SKILL_HACKING, hackLvl + 1).apply();
                    return true;
                case "stealth":
                    int stealthLvl = getSkillStealth();
                    prefs.edit().putInt(KEY_SKILL_STEALTH, stealthLvl + 1).apply();
                    return true;
                case "crypto":
                    int cryptoLvl = getSkillCrypto();
                    prefs.edit().putInt(KEY_SKILL_CRYPTO, cryptoLvl + 1).apply();
                    return true;
            }
        }
        return false;
    }

    private void checkRankUpgrade() {
        double totalValue = getTotalValue();
        int currentRank = getHackerRank();

        int[] rankThresholds = {0, 25000, 75000, 200000, 500000, 1000000}; // En valor total

        for (int i = rankThresholds.length - 1; i >= 0; i--) {
            if (totalValue >= rankThresholds[i] && currentRank < i) {
                prefs.edit().putInt(KEY_HACKER_RANK, i).apply();
                unlockRankTools(i);
                break;
            }
        }
    }

    private void unlockRankTools(int rank) {
        String[] toolsByRank = {
            "🔍 Scanner Básico",
            "🛡️ Firewall Personal", 
            "🔓 Cracker SSL",
            "🌐 Proxy Anónimo",
            "💾 Keylogger",
            "📡 Packet Sniffer Pro"
        };

        if (rank < toolsByRank.length) {
            unlockTool(toolsByRank[rank]);
        }
    }

    // ===== MÉTODOS DE EFECTOS EN EL JUEGO =====
    public int getTimeBonus() {
        return (getSkillHacking() - 1) * 5;
    }

    public int getComboMultiplier() {
        return getSkillStealth();
    }

    public int getMoneyMultiplier() {
        return getSkillCrypto();
    }

    public int getExtraAttempts() {
        return (getSkillHacking() - 1) / 2;
    }

    // ===== CARTILLERA / WALLET ACTUALIZADA =====
    public String getWalletInfo() {
        return "💼 CARTILLERA DIGITAL\n" +
			"────────────────────\n" +
			"💵 Dólares: " + String.format("$%,.0f", getDollars()) + "\n" +
			"₿ Bitcoin: " + String.format("%.6f", getBitcoin()) + " BTC\n" +
			"Ξ Ethereum: " + String.format("%.4f", getEthereum()) + " ETH\n" + 
			"💎 Litecoin: " + String.format("%.2f", getLitecoin()) + " LTC\n" +
			"🪙 DarkCoin: " + String.format("%,.0f", getDarkcoin()) + " DRK\n" +
			"────────────────────\n" +
			"💰 Valor total: " + String.format("$%,.0f", getTotalValue()) + "\n" +
			"🎯 Rango: " + getHackerRankName();
    }

    // ===== INFORMACIÓN DE MEJORAS ACTUALIZADA =====
    public String getSkillInfo(String skillType) {
        switch (skillType) {
            case "hacking":
                int hackCost = getSkillHacking() * 1000;
                return "Nvl " + getSkillHacking() + " → " + (getSkillHacking() + 1) + 
                    " | Costo: " + String.format("$%,d", hackCost) + "\n" +
                    "Efecto: +5 segundos y +1 intento cada 2 niveles";
            case "stealth":
                int stealthCost = getSkillStealth() * 1200;
                return "Nvl " + getSkillStealth() + " → " + (getSkillStealth() + 1) + 
                    " | Costo: " + String.format("$%,d", stealthCost) + "\n" +
                    "Efecto: Multiplicador de dinero x" + (getSkillStealth() + 1);
            case "crypto":
                int cryptoCost = getSkillCrypto() * 1500;
                return "Nvl " + getSkillCrypto() + " → " + (getSkillCrypto() + 1) + 
                    " | Costo: " + String.format("$%,d", cryptoCost) + "\n" +
                    "Efecto: Multiplicador crypto +10% y dinero x" + (getSkillCrypto() + 1);
            default:
                return "";
        }
    }

    // ===== MÉTODOS DE COMPATIBILIDAD (para no romper código existente) =====

    /**
     * Método de compatibilidad - usar addMissionEarnings en su lugar
     * @deprecated Usar addMissionEarnings en su lugar
     */
    public void addMoney(int money) {
        addDollars(money);
    }

    /**
     * Método de compatibilidad - mantener para código existente
     */
    public int getTotalMoney() {
        return (int) getDollars();
    }

    // ===== RESET =====
    public void resetProgress() {
        prefs.edit().clear().apply();
        initializeDefaultTools();
    }

// ===== LOGROS ACTUALIZADOS =====
public String getAchievements() {
    StringBuilder achievements = new StringBuilder();
    achievements.append("🏆 LOGROS DESBLOQUEADOS:\n");

    // ✅ ACTUALIZADO: Metas en DÓLARES en lugar de puntos
    if (getTotalValue() >= 1000) 
        achievements.append("✅ Primer millón ($1,000)\n");
    else
        achievements.append("🔒 Primer millón ($1,000)\n");

    if (getHackerRank() >= 2)
        achievements.append("✅ Hacker Intermedio (Rango 2)\n");
    else
        achievements.append("🔒 Hacker Intermedio (Rango 2)\n");

    if (getBestCombo() >= 5)
        achievements.append("✅ Combo Master (x5 combo)\n");
    else
        achievements.append("🔒 Combo Master (x5 combo)\n");

    if (getTotalHacks() >= 50)
        achievements.append("✅ Hackeo Persistente (50 hacks)\n");
    else
        achievements.append("🔒 Hackeo Persistente (50 hacks)\n");

    if (getBitcoin() >= 0.001)
        achievements.append("✅ Minero Bitcoin (0.001 BTC)\n");
    else
        achievements.append("🔒 Minero Bitcoin (0.001 BTC)\n");

    if (getEthereum() >= 0.01)
        achievements.append("✅ Trader Ethereum (0.01 ETH)\n");
    else
        achievements.append("🔒 Trader Ethereum (0.01 ETH)\n");

    if (getLitecoin() >= 1.0)
        achievements.append("✅ Especialista Litecoin (1 LTC)\n");
    else
        achievements.append("🔒 Especialista Litecoin (1 LTC)\n");

    if (getDarkcoin() >= 50000)
        achievements.append("✅ Rey del DarkCoin (50,000 DRK)\n");
    else
        achievements.append("🔒 Rey del DarkCoin (50,000 DRK)\n");

    // ✅ NUEVOS LOGROS EN DÓLARES
    if (getTotalValue() >= 100000)
        achievements.append("✅ Rico y Peligroso ($100,000)\n");
    else
        achievements.append("🔒 Rico y Peligroso ($100,000)\n");

    if (getTotalValue() >= 1000000)
        achievements.append("✅ Billonario Cibernético ($1,000,000)\n");
    else
        achievements.append("🔒 Billonario Cibernético ($1,000,000)\n");

    return achievements.toString();
}

    // ===== MÉTODOS ADICIONALES PARA EL SISTEMA DE CRYPTO =====

    /**
     * Convierte una cantidad de una criptomoneda a dólares
     */
    public double convertToDollars(double amount, String cryptoType) {
        switch (cryptoType.toUpperCase()) {
            case "BTC": return amount * BTC_TO_USD;
            case "ETH": return amount * ETH_TO_USD;
            case "LTC": return amount * LTC_TO_USD;
            case "DRK": return amount * DRK_TO_USD;
            default: return amount;
        }
    }

    /**
     * Obtiene el balance formateado de una criptomoneda específica
     */
    public String getCryptoBalance(String cryptoType) {
        switch (cryptoType.toUpperCase()) {
            case "BTC": return String.format("%.6f BTC", getBitcoin());
            case "ETH": return String.format("%.4f ETH", getEthereum());
            case "LTC": return String.format("%.2f LTC", getLitecoin());
            case "DRK": return String.format("%,.0f DRK", getDarkcoin());
            default: return "0";
        }
    }

    /**
     * Verifica si el jugador tiene suficiente de una criptomoneda específica
     */
    public boolean hasEnoughCrypto(double amount, String cryptoType) {
        switch (cryptoType.toUpperCase()) {
            case "BTC": return getBitcoin() >= amount;
            case "ETH": return getEthereum() >= amount;
            case "LTC": return getLitecoin() >= amount;
            case "DRK": return getDarkcoin() >= amount;
            default: return false;
        }
    }

    /**
     * Gasta una cantidad específica de criptomoneda
     */
    public boolean spendCrypto(double amount, String cryptoType) {
        switch (cryptoType.toUpperCase()) {
            case "BTC":
                if (getBitcoin() >= amount) {
                    addBitcoin(-amount);
                    return true;
                }
                break;
            case "ETH":
                if (getEthereum() >= amount) {
                    addEthereum(-amount);
                    return true;
                }
                break;
            case "LTC":
                if (getLitecoin() >= amount) {
                    addLitecoin(-amount);
                    return true;
                }
                break;
            case "DRK":
                if (getDarkcoin() >= amount) {
                    addDarkcoin(-amount);
                    return true;
                }
                break;
        }
        return false;
    }
	
	// ===== MÉTODOS PARA TUTORIAL =====
public boolean isTutorialCompleted() {
    return prefs.getBoolean(KEY_TUTORIAL_COMPLETED, false);
}

public void setTutorialCompleted() {
    prefs.edit().putBoolean(KEY_TUTORIAL_COMPLETED, true).apply();
}

public void resetTutorial() {
    prefs.edit().putBoolean(KEY_TUTORIAL_COMPLETED, false).apply();
}
}
