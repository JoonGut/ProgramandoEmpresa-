package com.example.game_exides;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Actividad que gestiona la selección y presentación aleatoria de preguntas.
 * Maneja tanto el modo normal como el modo Leyenda del juego.
 */
public class PreguntasRandom extends AppCompatActivity {

    // Gestor de base de datos para obtener las preguntas
    private DatabaseHelper dbHelper;
    // Lista de preguntas seleccionadas para la partida actual
    private List<Question> questions;
    // Índice de la pregunta actual
    private int currentQuestionIndex;
    // Número de preguntas por nivel en modo Leyenda
    private int questionsPerLevel = 12; // 3 preguntas por nivel para Leyenda

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        dbHelper = new DatabaseHelper(this);
        String selectedDifficulty = getIntent().getStringExtra("selectedDifficulty");
        Log.d("GameActivity", "Dificultad seleccionada: " + selectedDifficulty);

        if (selectedDifficulty != null) {
            switch (selectedDifficulty) {
                // Modos de dificultad normal: 15 preguntas aleatorias
                case "Fácil":
                case "Media":
                case "Difícil":
                    questions = getRandomQuestions(selectedDifficulty, 15);
                    break;
                    
                // Modo Leyenda: preguntas de todos los temas y dificultades
                case "Leyenda":
                    questions = new ArrayList<>();
                    String[] temas = {"Historia", "Ciencia", "Geografia", "Cine"};
                    String[] dificultades = {"Fácil", "Media", "Difícil"};
                    
                    // Obtener preguntas para cada combinación de tema y dificultad
                    for (String tema : temas) {
                        for (String dificultad : dificultades) {
                            Log.d("GameActivity", "Obteniendo preguntas para tema: " + tema + " y dificultad: " + dificultad);
                            List<Question> preguntasTema = dbHelper.getQuestionsByDifficultyAndTopic(dificultad, tema, questionsPerLevel);
                            Log.d("GameActivity", "Preguntas obtenidas para " + tema + "/" + dificultad + ": " + preguntasTema.size());
                            questions.addAll(preguntasTema);
                        }
                    }
                    
                    Log.d("GameActivity", "Total de preguntas para Leyenda: " + questions.size());
                    
                    // Verificar si se obtuvieron preguntas
                    if (questions.isEmpty()) {
                        Log.e("GameActivity", "No se encontraron preguntas para el modo Leyenda");
                        Toast.makeText(this, "Error: No se encontraron preguntas para el modo Leyenda", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    
                    // Mezclar todas las preguntas para orden aleatorio
                    Collections.shuffle(questions);
                    break;
            }
            currentQuestionIndex = 0;
            
            // Verificar si hay preguntas disponibles antes de continuar
            if (questions != null && !questions.isEmpty()) {
                showNextQuestion();
            } else {
                Log.e("GameActivity", "No hay preguntas disponibles");
                Toast.makeText(this, "Error: No hay preguntas disponibles", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Obtiene un número específico de preguntas aleatorias para una dificultad dada.
     * @param difficulty Nivel de dificultad deseado
     * @param count Número de preguntas a obtener
     * @return Lista de preguntas aleatorias
     */
    private List<Question> getRandomQuestions(String difficulty, int count) {
        List<Question> allQuestions = dbHelper.getQuestionsByDifficulty(difficulty);
        if (allQuestions.isEmpty()) {
            Log.e("GameActivity", "No se encontraron preguntas para dificultad: " + difficulty);
            return new ArrayList<>();
        }
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, Math.min(count, allQuestions.size()));
    }

    /**
     * Muestra la siguiente pregunta en la interfaz de usuario.
     * Si no hay más preguntas, finaliza el juego.
     */
    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            Log.d("GameActivity", "Mostrando pregunta " + (currentQuestionIndex + 1) + " de " + questions.size());
            // Mostrar la pregunta actual en la interfaz de usuario
            currentQuestionIndex++;
        } else {
            Log.d("GameActivity", "Juego terminado");
            Toast.makeText(this, "¡Juego terminado!", Toast.LENGTH_SHORT).show();
            // Finalizar el juego o mostrar resultados
        }
    }
} 