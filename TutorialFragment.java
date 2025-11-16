package com.hacker.finalapp;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TutorialFragment {

    // Variables para las vistas
    private LinearLayout pageWelcome, pageTerminal, pageEconomy, pageModules, pageProgress, pageFinal;
    private Button btnPrev, btnNext, btnSkip;
    private TextView pageIndicator;

    // Control de páginas
    private int currentPage = 0;
    private final int TOTAL_PAGES = 6;

    // Referencia al contexto para PlayerProgress
    private View rootView;

    public void inicializar(View view) {
        this.rootView = view;

        // Inicializar todas las vistas
        inicializarVistas(view);

        // Configurar listeners de botones
        configurarListeners();

        // Mostrar la primera página
        mostrarPagina(0);
    }

    private void inicializarVistas(View view) {
        // Obtener referencias a todas las páginas
        pageWelcome = (LinearLayout) view.findViewById(R.id.pageWelcome);
        pageTerminal = (LinearLayout) view.findViewById(R.id.pageTerminal);
        pageEconomy = (LinearLayout) view.findViewById(R.id.pageEconomy);
        pageModules = (LinearLayout) view.findViewById(R.id.pageModules);
        pageProgress = (LinearLayout) view.findViewById(R.id.pageProgress);
        pageFinal = (LinearLayout) view.findViewById(R.id.pageFinal);

        // Botones de navegación
        btnPrev = (Button) view.findViewById(R.id.btnPrev);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnSkip = (Button) view.findViewById(R.id.btnSkip);

        // Indicador de página
        pageIndicator = (TextView) view.findViewById(R.id.pageIndicator);
    }

    private void configurarListeners() {
        // Botón ANTERIOR
        btnPrev.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentPage > 0) {
						mostrarPagina(currentPage - 1);
					}
				}
			});

        // Botón SIGUIENTE
        btnNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (currentPage < TOTAL_PAGES - 1) {
						mostrarPagina(currentPage + 1);
					} else {
						// Última página - completar tutorial
						completarTutorial();
					}
				}
			});

        // Botón SALTAR
        btnSkip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					completarTutorial();
				}
			});
    }

    private void mostrarPagina(int pagina) {
        currentPage = pagina;

        // Ocultar todas las páginas primero
        ocultarTodasLasPaginas();

        // Mostrar la página actual
        switch (pagina) {
            case 0:
                pageWelcome.setVisibility(View.VISIBLE);
                break;
            case 1:
                pageTerminal.setVisibility(View.VISIBLE);
                break;
            case 2:
                pageEconomy.setVisibility(View.VISIBLE);
                break;
            case 3:
                pageModules.setVisibility(View.VISIBLE);
                break;
            case 4:
                pageProgress.setVisibility(View.VISIBLE);
                break;
            case 5:
                pageFinal.setVisibility(View.VISIBLE);
                break;
        }

        // Actualizar interfaz según la página
        actualizarInterfaz();

        // Actualizar indicador de página
        actualizarIndicadorPagina();
    }

    private void ocultarTodasLasPaginas() {
        pageWelcome.setVisibility(View.GONE);
        pageTerminal.setVisibility(View.GONE);
        pageEconomy.setVisibility(View.GONE);
        pageModules.setVisibility(View.GONE);
        pageProgress.setVisibility(View.GONE);
        pageFinal.setVisibility(View.GONE);
    }

    private void actualizarInterfaz() {
        // Configurar visibilidad de botones según la página

        // Botón ANTERIOR: visible excepto en primera página
        if (currentPage == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
        }

        // Botón SIGUIENTE: cambiar texto en última página
        if (currentPage == TOTAL_PAGES - 1) {
            btnNext.setText("🎉 COMENZAR");
            btnNext.setBackgroundColor(0xFFFFFF00); // Amarillo
        } else {
            btnNext.setText("SIGUIENTE ➡️");
            btnNext.setBackgroundColor(0xFF00FF00); // Verde
        }

        // Botón SALTAR: ocultar en última página
        if (currentPage == TOTAL_PAGES - 1) {
            btnSkip.setVisibility(View.GONE);
        } else {
            btnSkip.setVisibility(View.VISIBLE);
        }
    }

    private void actualizarIndicadorPagina() {
        String indicador = (currentPage + 1) + "/" + TOTAL_PAGES;
        pageIndicator.setText(indicador);
    }

    private void completarTutorial() {
        // Guardar estado de tutorial completado
        try {
            PlayerProgress playerProgress = new PlayerProgress(rootView.getContext());
            playerProgress.setTutorialCompleted();
        } catch (Exception e) {
            // Si hay error, continuar igual
        }

        // Regresar al terminal principal
        regresarAlTerminal();
    }

    private void regresarAlTerminal() {
        // Esta función se implementará en MainActivity
        // Por ahora solo mostramos un mensaje
        try {
            // Intentar obtener el contenedor principal y mostrar terminal
            if (rootView.getParent() != null) {
                // Buscar el MainActivity para cambiar de tab
                View mainView = rootView.getRootView();
                Button tabTerminal = (Button) mainView.findViewById(R.id.tabTerminal);
                if (tabTerminal != null) {
                    tabTerminal.performClick();
                }
            }
        } catch (Exception e) {
            // Fallback: mensaje de éxito
            if (pageIndicator != null) {
                pageIndicator.setText("✅ Tutorial completado");
            }
        }
    }

    // Método para reiniciar tutorial (opcional)
    public void reiniciarTutorial() {
        currentPage = 0;
        mostrarPagina(0);
    }

    // Método para saber si está en última página
    public boolean esUltimaPagina() {
        return currentPage == TOTAL_PAGES - 1;
    }

    // Método para obtener página actual
    public int getPaginaActual() {
        return currentPage;
    }
}
