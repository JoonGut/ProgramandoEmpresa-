package com.example.game_exides;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad que maneja el registro y autenticación de jugadores.
 * Permite a los usuarios registrarse con un nuevo nombre y contraseña,
 * o iniciar sesión con una cuenta existente.
 */
public class RegisterPlayerActivity extends AppCompatActivity {

    // Campos de entrada para el registro de nuevo jugador
    private EditText editTextPlayerName;         // Nombre del nuevo jugador
    private EditText editTextPassword;           // Contraseña del nuevo jugador
    private EditText editTextConfirmPassword;    // Confirmación de contraseña

    // Campos de entrada para jugador existente
    private EditText editTextExistingPlayerName; // Nombre de jugador existente
    private EditText editTextExistingPassword;   // Contraseña de jugador existente

    // Botones de la interfaz
    private Button buttonRegister;               // Botón para registrar nuevo jugador
    private Button buttonAcceptExistingPlayer;   // Botón para autenticar jugador existente
    private Button buttonBack;                   // Botón para volver atrás

    // Componentes del sistema
    private DatabaseHelper databaseHelper;        // Helper para acceso a base de datos
    private AudioManagerPropio audioManagerPropio;// Gestor de audio del juego
    private TextView currentPlayerTextView;       // Muestra el jugador actual

    /**
     * Inicializa la actividad y configura todos los componentes de la interfaz
     * y los listeners necesarios para el registro y autenticación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_player);

        // Inicializar AudioManagerPropio
        audioManagerPropio = AudioManagerPropio.getInstance(this);
        
        // Reproducir música específica de esta pantalla
        new Handler().postDelayed(() -> {
            audioManagerPropio.playMusic(R.raw.pantallajugador);
        }, 100);

        // Inicializar el TextView del jugador actual
        currentPlayerTextView = findViewById(R.id.currentPlayerTextView);
        updateCurrentPlayerDisplay();

        editTextPlayerName = findViewById(R.id.etNombreJugador);
        editTextPassword = findViewById(R.id.etContraseña);
        editTextConfirmPassword = findViewById(R.id.etConfirContraseña);
        editTextExistingPlayerName = findViewById(R.id.etNombreJugadorExistente);
        editTextExistingPassword = findViewById(R.id.etContraseñaExistente);
        buttonRegister = findViewById(R.id.btRegistrar);
        buttonAcceptExistingPlayer = findViewById(R.id.btAceptarJugadorExistente);
        buttonBack = findViewById(R.id.btVolver);
        databaseHelper = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = editTextPlayerName.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (playerName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterPlayerActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterPlayerActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    long result = databaseHelper.insertarJugador(playerName, password);
                    if (result != -1) {
                        Toast.makeText(RegisterPlayerActivity.this, "Jugador registrado exitosamente", Toast.LENGTH_SHORT).show();
                        handlePlayerSelection(playerName);
                    } else {
                        Toast.makeText(RegisterPlayerActivity.this, "Error al registrar el jugador", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonAcceptExistingPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String existingPlayerName = editTextExistingPlayerName.getText().toString();
                String existingPassword = editTextExistingPassword.getText().toString();

                if (existingPlayerName.isEmpty() || existingPassword.isEmpty()) {
                    Toast.makeText(RegisterPlayerActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = databaseHelper.verifyPlayer(existingPlayerName, existingPassword);
                    if (isValid) {
                        Toast.makeText(RegisterPlayerActivity.this, "Jugador autenticado exitosamente", Toast.LENGTH_SHORT).show();
                        handlePlayerSelection(existingPlayerName);
                    } else {
                        Toast.makeText(RegisterPlayerActivity.this, "Nombre o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPlayerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Maneja la pausa de la actividad deteniendo la música de fondo.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Detener la música al salir
        audioManagerPropio.stopMusic();
    }

    /**
     * Maneja la reanudación de la actividad, reiniciando la música
     * y actualizando la información del jugador actual.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!audioManagerPropio.isPlaying()) {
            new Handler().postDelayed(() -> {
                audioManagerPropio.playMusic(R.raw.pantallajugador);
            }, 100);
        } else {
            audioManagerPropio.resumeMusic();
        }
        // Actualizar la visualización del jugador actual
        updateCurrentPlayerDisplay();
    }

    /**
     * Maneja la destrucción de la actividad.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // No detener la música aquí para permitir la transición suave
        // audioManager.stopMusic();
    }

    /**
     * Actualiza la visualización del jugador actual en la interfaz.
     * Si hay un jugador en sesión, muestra su nombre.
     * Si no hay jugador, muestra un mensaje indicándolo.
     */
    private void updateCurrentPlayerDisplay() {
        if (SessionManager.getInstance().hasCurrentPlayer()) {
            currentPlayerTextView.setText("Jugador actual: " + 
                                       SessionManager.getInstance().getCurrentPlayer());
        } else {
            currentPlayerTextView.setText("No hay jugador seleccionado");
        }
    }

    /**
     * Maneja la selección de un jugador después del registro o autenticación exitosa.
     * Establece el jugador actual en la sesión, actualiza la interfaz y
     * redirige a la actividad principal.
     * 
     * @param playerName Nombre del jugador seleccionado
     */
    private void handlePlayerSelection(String playerName) {
        SessionManager.getInstance().setCurrentPlayer(playerName);
        updateCurrentPlayerDisplay();
        
        // Detener la música de esta pantalla antes de cambiar
        audioManagerPropio.stopMusic();
        
        Intent intent = new Intent(RegisterPlayerActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
} 