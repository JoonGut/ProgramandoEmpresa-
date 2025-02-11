package com.example.game_exides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

/**
 * Actividad principal del juego que sirve como punto de entrada y menú principal.
 * Gestiona la navegación, selección de temas/dificultad, música de fondo y
 * muestra la información del jugador actual.
 */
public class MainActivity extends AppCompatActivity {

    // Variables para los botones principales
    private Button startButton;      // Botón para comenzar el juego
    private Button playerButton;     // Botón para gestión de jugador

    // Variables para la gestión del juego
    private String playerName;                           // Nombre del jugador actual
    private String selectedTema = "Historia";           // Tema seleccionado (por defecto)
    private String selectedDificultad = "Fácil";        // Dificultad seleccionada (por defecto)

    // Variables para animaciones y posicionamiento
    private float dX, dY;                               // Coordenadas para animaciones
    private class TextPosition {
        float x = 0;
        float y = 100;                                  // Posición vertical desde arriba
    }

    // Elementos de la interfaz
    private CustomTextView puestoTextView;              // Muestra el ranking del jugador
    private CustomTextView playerNameTextView;          // Muestra el nombre del jugador
    private TextoFlotante tvWelcome;                    // Texto de bienvenida animado

    // Variables para la gestión de audio
    private MediaPlayer mediaPlayer;                    // Reproductor de música legacy
    private AudioManagerPropio audioManagerPropio;      // Gestor de audio personalizado
    private static final String MUSIC_FILE = "pantallaprincipal.mp3";
    private Handler musicCheckHandler = new Handler();   // Manejador para verificar música
    private Runnable musicCheckRunnable;                // Tarea para verificar música

    // Variables para el sistema de ranking
    private String temaSeleccionado = "";               // Tema actual para ranking
    private String dificultadSeleccionada = "";         // Dificultad actual para ranking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el gestor de audio
        audioManagerPropio = AudioManagerPropio.getInstance(this);
        
        // Reproducir música de fondo con delay para evitar lag
        new Handler().postDelayed(() -> {
            audioManagerPropio.playMusic(R.raw.pantallaprincipal);
        }, 100);

        // Configurar la barra de acción con título y color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("EXIDES GAME");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#87CEEB")));
        }

        // Inicializar vistas y configurar la interfaz
        initializeViews();

        // Verificar si hay un jugador activo, si no, ir a registro
        if (!SessionManager.getInstance().hasCurrentPlayer()) {
            Intent intent = new Intent(MainActivity.this, RegisterPlayerActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Mostrar información del jugador actual
        String currentPlayer = SessionManager.getInstance().getCurrentPlayer();
        playerNameTextView.setText("Jugador: " + currentPlayer);
        
        // Recuperar preferencias guardadas
        SharedPreferences prefs = getSharedPreferences("GamePreferences", MODE_PRIVATE);
        selectedTema = prefs.getString("selectedTema", "Historia");
        selectedDificultad = prefs.getString("selectedDificultad", "Fácil");
        
        // Actualizar variables de ranking
        temaSeleccionado = selectedTema;
        dificultadSeleccionada = selectedDificultad;
        
        // Actualizar el ranking del jugador
        actualizarPuestoJugador();

        // Configurar listeners de botones
        setupButtonListeners();
        
        // Animar texto de bienvenida si está disponible
        if (tvWelcome != null) {
            animateWelcomeText();
        } else {
            Log.e("MainActivity", "tvWelcome es null después de findViewById");
        }

        // Iniciar música de fondo
        audioManagerPropio.playMusic(R.raw.pantallaprincipal);

        // Configurar verificación periódica de música
        musicCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (!audioManagerPropio.isMusicPlaying()) {
                    audioManagerPropio.playMusic(R.raw.pantallaprincipal);
                }
                musicCheckHandler.postDelayed(this, 500);
            }
        };
    }

    /**
     * Inicializa todas las vistas de la actividad.
     * Conecta las variables con sus correspondientes elementos en el layout.
     */
    private void initializeViews() {
        startButton = findViewById(R.id.btComenzar);
        playerButton = findViewById(R.id.btJugador);
        tvWelcome = findViewById(R.id.tvWelcome);
        playerNameTextView = findViewById(R.id.tvNombreJugador);
        puestoTextView = findViewById(R.id.tvPuesto);
    }

    /**
     * Configura los listeners para los botones principales.
     * Define el comportamiento al hacer clic en cada botón.
     */
    private void setupButtonListeners() {
        // Configurar el botón de inicio del juego
        startButton.setOnClickListener(v -> {
            startQuestionActivity(selectedTema, selectedDificultad);
        });

        // Configurar el botón de gestión de jugador
        playerButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Intent registerIntent = new Intent(MainActivity.this, RegisterPlayerActivity.class);
            startActivity(registerIntent);
        });
    }

    /**
     * Crea el menú de opciones en la barra de acción.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Maneja las selecciones en el menú de opciones.
     * Gestiona la selección de temas y dificultades, guardando las preferencias.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        // Guardar selecciones en SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("GamePreferences", MODE_PRIVATE).edit();
        
        // Manejar selección de tema
        if (id == R.id.tema_historia) {
            selectedTema = "Historia";
            editor.putString("selectedTema", selectedTema);
            onTemaSeleccionado("Historia");
            editor.apply();
            return true;
        } else if (id == R.id.tema_ciencia) {
            selectedTema = "Ciencia";
            editor.putString("selectedTema", selectedTema);
            onTemaSeleccionado("Ciencia");
            editor.apply();
            return true;
        } else if (id == R.id.tema_geografia) {
            selectedTema = "Geografia";
            onTemaSeleccionado("Geografia");
            return true;
        } else if (id == R.id.tema_cine) {
            selectedTema = "Cine";
            onTemaSeleccionado("Cine");
            return true;
        } else if (id == R.id.tema_leyenda) {
            selectedTema = "Leyenda";
            onTemaSeleccionado("Leyenda");
            return true;
        }
        
        // Manejar selección de dificultad
        else if (id == R.id.dificultad_facil) {
            selectedDificultad = "Fácil";
            editor.putString("selectedDificultad", selectedDificultad);
            onDificultadSeleccionada("Fácil");
            editor.apply();
            return true;
        } else if (id == R.id.dificultad_medio) {
            selectedDificultad = "Medio";
            onDificultadSeleccionada("Medio");
            return true;
        } else if (id == R.id.dificultad_dificil) {
            selectedDificultad = "Difícil";
            onDificultadSeleccionada("Difícil");
            return true;
        }

        // Opción para cerrar la aplicación
        if (item.getItemId() == R.id.menu_cerrar) {
            finishAffinity(); // Cierra la aplicación completamente
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Inicia la actividad de preguntas con los parámetros seleccionados.
     * @param tema Tema seleccionado para las preguntas
     * @param dificultad Nivel de dificultad seleccionado
     */
    private void startQuestionActivity(String tema, String dificultad) {
        // Detener la música antes de cambiar de actividad
        audioManagerPropio.stopMusic();
        
        // Preparar y lanzar la actividad de preguntas
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("PLAYER_NAME", playerName);
        intent.putExtra("TEMA", tema);
        intent.putExtra("DIFICULTAD", dificultad);
        intent.putExtra("nombre", SessionManager.getInstance().getCurrentPlayer());
        startActivity(intent);
    }

    /**
     * Crea y ejecuta las animaciones del texto de bienvenida.
     * Incluye efectos de fade in, escala y animación de texto por líneas.
     */
    private void animateWelcomeText() {
        try {
            // Texto de bienvenida dividido en líneas para mejor animación
            String[] welcomeLines = {
                "¡Bienvenido al maravilloso juego",
                "EXIDES!",
                "¿Estás preparado para tu mayor",
                "aventura?"
            };
            
            // Configurar animación de fade in (aparecer gradualmente)
            AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
            fadeIn.setDuration(2000);
            fadeIn.setFillAfter(true);
            
            // Configurar animación de escala (crecer desde el centro)
            ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f,  // Escala X: de 50% a 100%
                0.5f, 1.0f,  // Escala Y: de 50% a 100%
                Animation.RELATIVE_TO_SELF, 0.5f,  // Punto de pivote X
                Animation.RELATIVE_TO_SELF, 0.5f   // Punto de pivote Y
            );
            scaleAnimation.setDuration(2000);
            
            // Combinar ambas animaciones
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(fadeIn);
            animationSet.addAnimation(scaleAnimation);
            
            // Iniciar las animaciones
            tvWelcome.startAnimation(animationSet);
            tvWelcome.animateText(welcomeLines, 500);  // Animar texto línea por línea
        } catch (Exception e) {
            Log.e("MainActivity", "Error en animateWelcomeText: " + e.getMessage());
        }
    }

    // Método para cambiar de jugador (agrégalo a tu botón de cambio de jugador si lo tienes)
    private void cambiarJugador() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Intent intent = new Intent(MainActivity.this, RegisterPlayerActivity.class);
        startActivity(intent);
    }

    /**
     * Método del ciclo de vida: cuando la actividad se pausa.
     * Gestiona la detención de música y verificaciones periódicas.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Detener la música al salir
        audioManagerPropio.stopMusic();
        // Detener las verificaciones periódicas de música
        musicCheckHandler.removeCallbacks(musicCheckRunnable);
    }

    /**
     * Método del ciclo de vida: cuando la actividad se reanuda.
     * Restaura la música y actualiza la información del jugador.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Reanudar música si no está sonando
        if (!audioManagerPropio.isMusicPlaying()) {
            audioManagerPropio.playMusic(R.raw.pantallaprincipal);
        }
        // Reanudar verificaciones periódicas
        musicCheckHandler.post(musicCheckRunnable);
        
        // Actualizar nombre del jugador si existe
        if (SessionManager.getInstance().hasCurrentPlayer()) {
            playerNameTextView.setText("Jugador: " + SessionManager.getInstance().getCurrentPlayer());
        }

        // Actualizar el puesto con las selecciones guardadas
        if (selectedTema != null && selectedDificultad != null) {
            temaSeleccionado = selectedTema;
            dificultadSeleccionada = selectedDificultad;
            actualizarPuestoJugador();
        }
    }

    /**
     * Método del ciclo de vida: cuando la actividad se destruye.
     * Limpia recursos y detiene verificaciones periódicas.
     */
    @Override
    protected void onDestroy() {
        // Detener las verificaciones periódicas de música
        musicCheckHandler.removeCallbacks(musicCheckRunnable);
        super.onDestroy();
    }

    /**
     * Actualiza la información del puesto del jugador en el ranking.
     * Consulta la base de datos y muestra la posición y puntos del jugador
     * para el tema y dificultad seleccionados actualmente.
     */
    private void actualizarPuestoJugador() {
        if (!temaSeleccionado.isEmpty() && !dificultadSeleccionada.isEmpty() && 
            SessionManager.getInstance().hasCurrentPlayer()) {
            
            runOnUiThread(() -> {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    DatabaseHelper.PuestoInfo puestoInfo = dbHelper.obtenerPuestoJugador(
                        SessionManager.getInstance().getCurrentPlayer(),
                        temaSeleccionado,
                        dificultadSeleccionada
                    );
                    
                    if (puestoTextView != null) {
                        if (puestoInfo.puesto > 0) {
                            puestoTextView.setText(String.format("Puesto: Nº%d (%d pts)", 
                                               puestoInfo.puesto, puestoInfo.puntos));
                        } else {
                            puestoTextView.setText("Puesto: Sin clasificar (0 pts)");
                        }
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Error actualizando puesto: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Maneja el evento de selección de tema.
     * Actualiza el tema seleccionado y refresca el ranking.
     * @param tema El nuevo tema seleccionado
     */
    private void onTemaSeleccionado(String tema) {
        this.temaSeleccionado = tema;
        actualizarPuestoJugador();
    }

    /**
     * Maneja el evento de selección de dificultad.
     * Actualiza la dificultad seleccionada y refresca el ranking.
     * @param dificultad La nueva dificultad seleccionada
     */
    private void onDificultadSeleccionada(String dificultad) {
        this.dificultadSeleccionada = dificultad;
        actualizarPuestoJugador();
    }

    /**
     * Muestra un mensaje Toast sin interrumpir la música de fondo.
     * @param mensaje El mensaje a mostrar
     */
    private void mostrarToastSinPausarMusica(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        // Usar la instancia de audioManagerPropio
        if (!audioManagerPropio.isMusicPlaying()) {
            audioManagerPropio.playMusic(R.raw.pantallaprincipal);
        }
    }
}