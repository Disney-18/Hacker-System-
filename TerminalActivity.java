package com.hacker.finalapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

public class TerminalActivity extends Activity {
    
    private TextView terminalOutput;
    private EditText commandInput;
    private LinearLayout terminalContainer;
    private CommandProcessor commandProcessor;
    private PlayerProgress playerProgress;
    
    private StringBuilder commandHistory = new StringBuilder();
    private int historyIndex = -1;
    private String currentInput = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                           WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.activity_terminal);
        
        initializeComponents();
        initializeSystems();
        showWelcomeMessage();
    }
    
    private void initializeComponents() {
        terminalOutput = (TextView) findViewById(R.id.terminalOutput);
        commandInput = (EditText) findViewById(R.id.commandInput);
        terminalContainer = (LinearLayout) findViewById(R.id.terminalContainer);
        
        // Configurar el TextView para scroll
        terminalOutput.setMovementMethod(new ScrollingMovementMethod());
        
        // === LISTENER PARA TECLADO FÍSICO ===
        commandInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        executeCommand();
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        showPreviousCommand();
                        return true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        showNextCommand();
                        return true;
                    }
                }
                return false;
            }
        });
        
        // === LISTENER PARA TECLADO VIRTUAL (NUEVO) ===
        commandInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || 
                    actionId == android.view.inputmethod.EditorInfo.IME_ACTION_GO ||
                    actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                    executeCommand();
                    return true;
                }
                return false;
            }
        });
        
        // Configurar el EditText para enviar con ENTER
        commandInput.setSingleLine(true);
        commandInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
        
        // Focus automático en el input
        commandInput.requestFocus();
        
        // Mostrar teclado después de un breve delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, 500);
        
        setupAutoComplete();
    }
    
    private void initializeSystems() {
        playerProgress = new PlayerProgress(this);
        commandProcessor = new CommandProcessor(this, playerProgress);
    }
    
    private void showWelcomeMessage() {
        appendToTerminal("╔══════════════════════════════════════╗\n");
        appendToTerminal("║         🖥️ HACKER TERMINAL v2.0      ║\n");
        appendToTerminal("║           TERMUX STYLE EDITION       ║\n");
        appendToTerminal("╠══════════════════════════════════════╣\n");
        appendToTerminal("║  Sistema de hacking profesional      ║\n");
        appendToTerminal("║  Escribe 'help' para ver comandos    ║\n");
        appendToTerminal("║  Escribe 'tutorial' para aprender    ║\n");
        appendToTerminal("╚══════════════════════════════════════╝\n\n");
        
        appendToTerminal("📊 ESTADO DEL SISTEMA:\n");
        appendToTerminal("• Rango: " + playerProgress.getHackerRankName() + "\n");
        appendToTerminal("• Dinero: " + playerProgress.getMoneyFormatted() + "\n");
        appendToTerminal("• Valor total: $" + String.format("%,.0f", playerProgress.getTotalValue()) + "\n");
        
        if (!playerProgress.isTutorialCompleted()) {
            appendToTerminal("\n🎓 TUTORIAL DISPONIBLE: Escribe 'tutorial' para comenzar\n");
        }
        
        appendToTerminal("\nroot@hacker-system:~$ ");
    }
    
    private void executeCommand() {
        String command = commandInput.getText().toString().trim();
        
        if (!command.isEmpty()) {
            // Guardar en el historial
            saveToHistory(command);
            
            // Mostrar comando en terminal (solo si no es hack para mantener secreto)
            if (!command.toLowerCase().startsWith("hack")) {
                appendToTerminal(command + "\n");
            }
            
            // Procesar comando
            String result = commandProcessor.processCommand(command);
            
            // Mostrar resultado
            if (command.toLowerCase().startsWith("hack") && !command.equalsIgnoreCase("hack")) {
                appendToTerminal(result);
            } else {
                appendToTerminal(result);
            }
            
            // Limpiar input y mostrar nuevo prompt
            commandInput.setText("");
            
            // Solo mostrar prompt si no hay juego activo o es un comando normal
            if (!command.toLowerCase().equals("hack") && !command.isEmpty()) {
                appendToTerminal("\nroot@hacker-system:~$ ");
            }
            
            // Scroll al final
            scrollToBottom();
        }
        
        // Mantener focus en el input y mostrar teclado
        commandInput.requestFocus();
        showKeyboard();
    }
    
    private void saveToHistory(String command) {
        commandHistory.append(command).append("\n");
        historyIndex = -1;
        currentInput = "";
    }
    
    private void showPreviousCommand() {
        String[] history = commandHistory.toString().split("\n");
        if (history.length > 0) {
            if (historyIndex == -1) {
                currentInput = commandInput.getText().toString();
                historyIndex = history.length - 1;
            } else if (historyIndex > 0) {
                historyIndex--;
            }
            commandInput.setText(history[historyIndex]);
            commandInput.setSelection(commandInput.getText().length());
        }
    }
    
    private void showNextCommand() {
        String[] history = commandHistory.toString().split("\n");
        if (historyIndex != -1) {
            if (historyIndex < history.length - 1) {
                historyIndex++;
                commandInput.setText(history[historyIndex]);
            } else {
                historyIndex = -1;
                commandInput.setText(currentInput);
            }
            commandInput.setSelection(commandInput.getText().length());
        }
    }
    
    private void appendToTerminal(String text) {
        terminalOutput.append(text);
    }
    
    private void scrollToBottom() {
        terminalOutput.post(new Runnable() {
            @Override
            public void run() {
                int scrollAmount = terminalOutput.getLayout().getLineTop(terminalOutput.getLineCount()) 
                                 - terminalOutput.getHeight();
                if (scrollAmount > 0) {
                    terminalOutput.scrollTo(0, scrollAmount);
                } else {
                    terminalOutput.scrollTo(0, 0);
                }
            }
        });
    }
    
    // Métodos para los botones de acción rápida
    public void onHelpClick(View view) {
        appendToTerminal("help\n");
        String result = commandProcessor.processCommand("help");
        appendToTerminal(result);
        appendToTerminal("\nroot@hacker-system:~$ ");
        scrollToBottom();
        commandInput.requestFocus();
        showKeyboard();
    }

    public void onHackClick(View view) {
        appendToTerminal("hack\n");
        String result = commandProcessor.processCommand("hack");
        appendToTerminal(result);
        appendToTerminal("\nroot@hacker-system:~$ ");
        scrollToBottom();
        commandInput.requestFocus();
        showKeyboard();
    }

    public void onStatusClick(View view) {
        appendToTerminal("status\n");
        String result = commandProcessor.processCommand("status");
        appendToTerminal(result);
        appendToTerminal("\nroot@hacker-system:~$ ");
        scrollToBottom();
        commandInput.requestFocus();
        showKeyboard();
    }

    public void onToolsClick(View view) {
        appendToTerminal("tools\n");
        String result = commandProcessor.processCommand("tools");
        appendToTerminal(result);
        appendToTerminal("\nroot@hacker-system:~$ ");
        scrollToBottom();
        commandInput.requestFocus();
        showKeyboard();
    }

    public void onClearClick(View view) {
        terminalOutput.setText("");
        appendToTerminal("root@hacker-system:~$ ");
        scrollToBottom();
        commandInput.requestFocus();
        showKeyboard();
    }
    
    @Override
    public void onBackPressed() {
        appendToTerminal("\n⏹️  Usa 'exit' para salir o 'help' para ver comandos\n");
        appendToTerminal("root@hacker-system:~$ ");
        commandInput.requestFocus();
        showKeyboard();
    }
    
    // Método para manejar el botón físico ENTER del teclado
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && 
            event.getAction() == KeyEvent.ACTION_DOWN) {
            executeCommand();
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_TAB && 
            event.getAction() == KeyEvent.ACTION_DOWN) {
            autocompleteCommand();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    
    // Mostrar teclado
    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(commandInput, InputMethodManager.SHOW_IMPLICIT);
    }
    
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commandInput.getWindowToken(), 0);
    }

    // === MÉTODOS DE AUTOCOMPLETADO ===
    private void setupAutoComplete() {
        commandInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard();
                }
            }
        });
        
        commandInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                showCommandSuggestions(s.toString());
            }
        });
    }

    private void showCommandSuggestions(String input) {
        if (input.length() > 1) {
            String[] commands = {"help", "status", "hack", "tools", "scan", "crypto", 
                               "web", "traffic", "firewall", "files", "missions",
                               "market", "upgrade", "money", "rank", "skills",
                               "achievements", "event", "daily", "leaderboard", "stats"};
            
            for (String cmd : commands) {
                if (cmd.startsWith(input.toLowerCase())) {
                    appendToTerminal("\n💡 ¿Quisiste decir: '" + cmd + "'? (TAB para autocompletar)");
                    scrollToBottom();
                    break;
                }
            }
        }
    }

    private void autocompleteCommand() {
        String input = commandInput.getText().toString().toLowerCase();
        String[] commands = {"help", "status", "hack", "tools", "scan", "crypto", 
                           "web", "traffic", "firewall", "files", "missions",
                           "market", "upgrade", "money", "rank", "skills",
                           "achievements", "event", "daily", "leaderboard", "stats"};
        
        for (String cmd : commands) {
            if (cmd.startsWith(input) && !cmd.equals(input)) {
                commandInput.setText(cmd);
                commandInput.setSelection(cmd.length());
                break;
            }
        }
    }
}
