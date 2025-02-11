package com.example.game_exides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.Collections;
import java.util.List;
import android.animation.ValueAnimator;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.ViewTreeObserver;
import java.util.Random;
import android.animation.ObjectAnimator;
import java.util.Arrays;
import android.view.MotionEvent;

import java.util.ArrayList;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.DisplayMetrics;
import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.media.MediaPlayer;
import androidx.cardview.widget.CardView;

public class QuestionActivity extends AppCompatActivity {

    private static final int PREGUNTAS_MODO_NORMAL = 15; //20;  // Número de preguntas para modos normales
    private static final int PREGUNTAS_POR_NIVEL_LEYENDA = 3; //3;  // Preguntas por nivel en modo Leyenda

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private ImageView gifImageView;
    private CustomTextView timerTextView;
    private int playerPoints = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 20000; // 20 segundos por defecto
    private static final long COUNTDOWN_INTERVAL = 1000; // 1 segundo
    private CustomTextView playerNameTextView;
    private CustomTextView playerPointsTextView;
    private TextView tvPregunta;
    private CustomTextView option1TextView;
    private CustomTextView option2TextView;
    private CustomTextView option3TextView;
    private CustomTextView option4TextView;
    private boolean comodinUsado = false;
    private int turnosDesdeComodin = 3;
    private ImageView ivComodin;
    private int screenWidth;
    private int screenHeight;
    private int contadorRespuestasCorrectas = 0;
    private ImageView imagenExplosion;
    private ImageView ivNubeExplota;
    private DatabaseHelper dbHelper;
    private CustomTextView puestoTextView;
    private String temaActual;
    private String dificultadActual;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerComodin;
    private MediaPlayer mediaPlayerError;
    private AudioManagerPropio audioManagerPropio;
    private boolean opcionesEnAnimacion = false;
    private boolean esperandoSiguientePregunta = false;
    private boolean isActivityActive = true;

    /**
     * Inicializa la actividad del juego de preguntas.
     * 
     * Este método realiza la configuración inicial de:
     * 1. La interfaz de usuario
     * 2. El sistema de audio
     * 3. Las dimensiones de la pantalla
     * 4. Los componentes de la UI
     * 5. La base de datos y lista de preguntas
     * 
     * @param savedInstanceState Estado guardado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Inicializar y configurar el sistema de audio
        audioManagerPropio = AudioManagerPropio.getInstance(this);
        new Handler().postDelayed(() -> {
            audioManagerPropio.playMusic(R.raw.pantallajuego);
        }, 100);

        // Configurar dimensiones de pantalla para cálculos posteriores
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        // Inicializar todas las vistas de la UI
        initializeViews();

        // Obtener parámetros del juego desde el Intent
        Intent intent = getIntent();
        String tema = intent.getStringExtra("TEMA");
        String dificultad = intent.getStringExtra("DIFICULTAD");

        // Logging para debugging
        Log.d("QuestionActivity", "Tema recibido: " + tema);
        Log.d("QuestionActivity", "Dificultad recibida: " + dificultad);

        // Inicializar base de datos y lista de preguntas
        dbHelper = new DatabaseHelper(this);
        questionList = new ArrayList<>();

        // Cargar preguntas según el modo
        if (tema.equals("Leyenda")) {
            loadLegendModeQuestions();
        } else {
            loadNormalModeQuestions(tema, dificultad);
        }

        // Iniciar el juego mostrando la primera pregunta
        if (!questionList.isEmpty()) {
            currentQuestionIndex = 0;
            mostrarPregunta(currentQuestionIndex);
            startTimer();
            actualizarPuestoJugador();
        }

        ivComodin = findViewById(R.id.ivComodin);

        // Configurar el clic en ivComodin
        ivComodin.setOnClickListener(v -> usarComodin());

        // Configurar listeners para verificar respuestas
        View.OnClickListener cardClickListener = v -> {
            CardView cardView = (CardView) v;
            CustomTextView optionTextView = (CustomTextView) ((CardView) v).getChildAt(0);
            if (!comodinUsado || !opcionesVibrando()) {
                checkAnswer(optionTextView);
            }
        };

        // Asignar el listener a los CardViews
        findViewById(R.id.cvOpcion1).setOnClickListener(cardClickListener);
        findViewById(R.id.cvOpcion2).setOnClickListener(cardClickListener);
        findViewById(R.id.cvOpcion3).setOnClickListener(cardClickListener);
        findViewById(R.id.cardViewOpcion4).setOnClickListener(cardClickListener);

        // Configurar listener para el botón "Salir"
        Button exitButton = findViewById(R.id.btSalir);
        exitButton.setOnClickListener(v -> {
            Intent mainIntent = new Intent(QuestionActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });

        // Asegurarse de que las vistas estén completamente inicializadas
        ViewTreeObserver viewTreeObserver = option1TextView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    option1TextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    animateOptionsEntry();
                }
            });
        }

        // Inicializar las imágenes de las nubes
        ImageView nube1 = findViewById(R.id.nube1);
        ImageView nube2 = findViewById(R.id.nube2);
        ImageView nube3 = findViewById(R.id.nube3);
        ImageView nube4 = findViewById(R.id.nube4);
        ImageView nube5 = findViewById(R.id.nube5);

        // Iniciar la animación de las nubes
        animateCloud(nube1, true, 0);
        animateCloud(nube2, false, 1000);
        animateCloud(nube3, true, 2000);
        animateCloud(nube4, false, 3000);
        animateCloud(nube5, true, 4000);

        // Configurar la vibración de ivComodin cada 5 segundos
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Animation shake = AnimationUtils.loadAnimation(QuestionActivity.this, R.anim.shake);
                ivComodin.startAnimation(shake);
                handler.postDelayed(this, 5000); // Repetir cada 5 segundos
            }
        };
        handler.post(runnable);

        // Inicializar la imagen para la explosión
        imagenExplosion = new ImageView(this);
        imagenExplosion.setId(View.generateViewId());
        
        // Establecer un tamaño fijo para la imagen
        int size = 300; // tamaño en pixels
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);
        imagenExplosion.setLayoutParams(params);
        
        // Añadir la imagen al layout principal
        ConstraintLayout mainLayout = findViewById(R.id.questionLayout);
        mainLayout.addView(imagenExplosion);
        
        // Centrar la imagen en la pantalla
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);
        constraintSet.connect(imagenExplosion.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(imagenExplosion.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(imagenExplosion.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(imagenExplosion.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.applyTo(mainLayout);
        
        // Ocultar inicialmente la imagen
        imagenExplosion.setVisibility(View.GONE);

        // Inicializar la imagen de explosión
        ivNubeExplota = findViewById(R.id.ivNubeExplota);
        
        // Verificar que la imagen se inicializó correctamente
        if (ivNubeExplota == null) {
            Log.e("Animacion", "Error: ivNubeExplota no se encontró");
        } else {
            Log.d("Animacion", "ivNubeExplota inicializado correctamente");
        }

        // Recuperar el nombre del jugador del Intent
        String playerName = getIntent().getStringExtra("nombre");
        
        // Añadir log para debug
        Log.d("QuestionActivity", "Nombre recibido: " + playerName);
        
        // Encontrar y actualizar el TextView con el nombre del jugador
        if (playerName != null && !playerName.isEmpty()) {
            playerNameTextView.setText("Jugador: " + playerName);
            // Añadir log para confirmar que se establece el texto
            Log.d("QuestionActivity", "Texto establecido en TextView");
        } else {
            Log.d("QuestionActivity", "Nombre es nulo o vacío");
        }

        // Obtener tema y dificultad de los extras
        temaActual = getIntent().getStringExtra("TEMA");
        dificultadActual = getIntent().getStringExtra("DIFICULTAD");
        
        // Actualizar el puesto
        actualizarPuestoJugador();

        // Inicializar el MediaPlayer para el sonido de error
        mediaPlayerError = MediaPlayer.create(this, R.raw.error);
    }

    /**
     * Inicializa y vincula todas las vistas necesarias para la UI del juego.
     */
    private void initializeViews() {
        gifImageView = findViewById(R.id.gifImageView);
        playerNameTextView = findViewById(R.id.tvNombreJugador);
        playerPointsTextView = findViewById(R.id.tvPuntosJugador);
        timerTextView = findViewById(R.id.tvTimer);
        tvPregunta = findViewById(R.id.tvPregunta);
        option1TextView = findViewById(R.id.tvOpcion1);
        option2TextView = findViewById(R.id.tvOpcion2);
        option3TextView = findViewById(R.id.tvOpcion3);
        option4TextView = findViewById(R.id.tvOpcion4);
        puestoTextView = findViewById(R.id.tvPuesto);
    }

    /**
     * Maneja la pausa de la actividad.
     * Detiene la música de fondo y marca la actividad como inactiva
     * para evitar actualizaciones de UI innecesarias.
     */
    @Override
    protected void onPause() {
        super.onPause();
        isActivityActive = false;
        audioManagerPropio.stopMusic();
    }

    /**
     * Maneja la reanudación de la actividad.
     * Reactiva la música de fondo y marca la actividad como activa.
     * Incluye un pequeño retraso para asegurar una transición suave
     * del audio.
     */
    @Override
    protected void onResume() {
        super.onResume();
        isActivityActive = true;
        new Handler().postDelayed(() -> {
            if (!audioManagerPropio.isPlaying()) {
                audioManagerPropio.playMusic(R.raw.pantallajuego);
            } else {
                audioManagerPropio.resumeMusic();
            }
        }, 100);
    }

    /**
     * Maneja la destrucción de la actividad.
     * Solo detiene la música si la actividad está siendo finalizada
     * completamente, permitiendo transiciones suaves entre actividades.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            audioManagerPropio.pauseMusic();
        }
    }

    /**
     * Inicia o reinicia el temporizador para la pregunta actual.
     * Maneja el conteo regresivo y actualiza la UI correspondiente.
     * Si la actividad no está activa, cancela el temporizador para
     * evitar actualizaciones innecesarias.
     */
    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        countDownTimer = new CountDownTimer(timeLeftInMillis, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isActivityActive) {
                    cancel();
                    return;
                }
                timeLeftInMillis = millisUntilFinished;
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (!isActivityActive) {
                    return;
                }
                timerTextView.setText("0");
                timeLeftInMillis = 0;
                manejarTiempoAgotado();
            }
        }.start();
    }

    /**
     * Maneja el evento cuando se agota el tiempo de una pregunta.
     * Reproduce un sonido de error, muestra un mensaje y prepara
     * la siguiente pregunta después de un breve retraso.
     * Incluye verificaciones de estado de la actividad para evitar
     * problemas con el ciclo de vida.
     */
    private void manejarTiempoAgotado() {
        if (!isActivityActive || esperandoSiguientePregunta) {
            return;
        }
        
        esperandoSiguientePregunta = true;
        
        if (isActivityActive) {
            Toast.makeText(this, "¡Tiempo agotado!", Toast.LENGTH_SHORT).show();
            audioManagerPropio.playSound(R.raw.error);
        }
        
        new Handler().postDelayed(() -> {
            if (!isActivityActive) {
                return;
            }
            
            currentQuestionIndex++;
            if (currentQuestionIndex < questionList.size()) {
                timeLeftInMillis = 20000;
                mostrarPregunta(currentQuestionIndex);
                startTimer();
                esperandoSiguientePregunta = false;
            } else {
                finishGame();
            }
        }, 2000);
    }

    /**
     * Verifica la respuesta seleccionada por el usuario y maneja la lógica del juego.
     * 
     * Este método realiza las siguientes acciones:
     * 1. Valida si es posible procesar la respuesta en este momento
     * 2. Compara la respuesta seleccionada con la correcta
     * 3. Maneja la lógica para respuestas correctas e incorrectas
     * 4. Actualiza puntuación y estado del juego
     * 5. Controla efectos visuales y sonoros
     * 
     * @param selectedOption La opción de respuesta seleccionada por el usuario
     */
    private void checkAnswer(CustomTextView selectedOption) {
        // Validaciones de estado para evitar múltiples respuestas
        if (esperandoSiguientePregunta || selectedOption == null || opcionesEnAnimacion) {
            return;
        }

        String selectedAnswer = selectedOption.getText().toString();
        Question currentQuestion = questionList.get(currentQuestionIndex);

        if (selectedAnswer.equals(currentQuestion.getRespuestaCorrecta())) {
            handleCorrectAnswer();
        } else {
            handleIncorrectAnswer(selectedOption);
        }
    }

    /**
     * Maneja la lógica cuando el usuario selecciona la respuesta correcta.
     * - Actualiza contadores y estado del comodín
     * - Calcula y asigna puntos
     * - Reproduce efectos visuales y sonoros
     * - Prepara la siguiente pregunta
     */
    private void handleCorrectAnswer() {
        esperandoSiguientePregunta = true;
        contadorRespuestasCorrectas++;
        
        // Gestión del comodín
        if (contadorRespuestasCorrectas >= 3 && comodinUsado) {
            reactivarComodin();
        }

        // Detener temporizador y calcular puntos
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        // Asignación de puntos basada en tiempo restante
        int puntosPorTiempo = (int) (timeLeftInMillis / 1000);
        playerPoints += puntosPorTiempo;
        playerPointsTextView.setText("Pts: " + playerPoints);
        
        // Efectos visuales y sonoros
        audioManagerPropio.playSound(R.raw.correcto);
        showCorrectGif();
        mostrarExplosionNube();
        
        // Preparar siguiente pregunta
        prepareNextQuestion();
    }

    /**
     * Maneja la lógica cuando el usuario selecciona una respuesta incorrecta.
     * - Resetea contadores
     * - Aplica penalizaciones
     * - Reproduce efectos visuales y sonoros
     * 
     * @param selectedOption La opción incorrecta seleccionada
     */
    private void handleIncorrectAnswer(CustomTextView selectedOption) {
        // Resetear contador de respuestas correctas
        contadorRespuestasCorrectas = 0;
        
        // Efectos visuales y sonoros
        audioManagerPropio.playSound(R.raw.error);
        Toast.makeText(this, "Incorrecto, intenta de nuevo.", Toast.LENGTH_SHORT).show();
        vibrateCustomTextView(selectedOption);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        selectedOption.startAnimation(shake);
        
        // Aplicar penalización de tiempo
        if (timeLeftInMillis > 3000) {
            timeLeftInMillis -= 3000;
        } else {
            timeLeftInMillis = 0;
        }
        
        // Reiniciar temporizador con el tiempo penalizado
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        startTimer();
    }

    /**
     * Prepara y muestra la siguiente pregunta o finaliza el juego.
     * Se ejecuta después de un breve retraso para permitir
     * la visualización de los efectos.
     */
    private void prepareNextQuestion() {
        new Handler().postDelayed(() -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questionList.size()) {
                timeLeftInMillis = 20000;
                mostrarPregunta(currentQuestionIndex);
                startTimer();
                esperandoSiguientePregunta = false;
            } else {
                finishGame();
            }
        }, 2000);
    }

    /**
     * Muestra una animación GIF cuando el usuario selecciona la respuesta correcta.
     * 
     * Este método:
     * 1. Verifica que la vista del GIF esté disponible
     * 2. Carga y muestra la animación usando la biblioteca Glide
     * 3. Hace visible la animación
     * 4. Oculta automáticamente la animación después de 2 segundos
     * 
     * La animación se muestra como retroalimentación visual positiva
     * para reforzar el acierto del usuario.
     */
    private void showCorrectGif() {
        if (gifImageView != null) {
            // Cargar y mostrar el GIF usando Glide
            Glide.with(this)
                .asGif()
                .load(R.drawable.correcto)
                .into(gifImageView);
            
            // Hacer visible la animación
            gifImageView.setVisibility(View.VISIBLE);
            
            // Ocultar el GIF después de 2 segundos
            new Handler().postDelayed(() -> 
                gifImageView.setVisibility(View.GONE), 
                2000
            );
        }
    }

    /**
     * Muestra una nueva pregunta y configura todos los elementos de la interfaz.
     * 
     * Este método realiza las siguientes tareas:
     * 1. Reinicia los estados de control (animación, comodín)
     * 2. Restablece la visibilidad y propiedades de las opciones
     * 3. Configura la nueva pregunta y sus opciones
     * 4. Maneja el sistema de comodín
     * 5. Inicia animaciones y temporizador
     * 
     * @param index Índice de la pregunta a mostrar en la lista de preguntas
     */
    private void mostrarPregunta(int index) {
        // Reiniciar estados de control
        resetearEstados();
        
        // Restablecer visibilidad de opciones
        restablecerVisibilidadOpciones();
        
        // Restablecer propiedades de las opciones
        for (CustomTextView option : Arrays.asList(option1TextView, option2TextView, 
                                                 option3TextView, option4TextView)) {
            restablecerPropiedadesOpcion(option);
        }

        // Configurar nueva pregunta
        configurarNuevaPregunta(index);

        // Gestionar comodín y animaciones
        gestionarComodinYAnimaciones();

        // Configurar temporizador
        configurarTemporizador();
    }

    /**
     * Reinicia los estados de control para la nueva pregunta.
     */
    private void resetearEstados() {
        esperandoSiguientePregunta = true;
        opcionesEnAnimacion = false;
        comodinUsado = false;
    }

    /**
     * Restablece la visibilidad de todas las opciones de respuesta.
     */
    private void restablecerVisibilidadOpciones() {
        option1TextView.setVisibility(View.VISIBLE);
        option2TextView.setVisibility(View.VISIBLE);
        option3TextView.setVisibility(View.VISIBLE);
        option4TextView.setVisibility(View.VISIBLE);
    }

    /**
     * Restablece todas las propiedades de una opción de respuesta.
     * @param option Opción de respuesta a restablecer
     */
    private void restablecerPropiedadesOpcion(CustomTextView option) {
        option.setTranslationX(0);
        option.setAlpha(1.0f);
        option.clearAnimation();
        option.setClickable(true);
        option.setEnabled(true);
        option.setOnTouchListener(null);
        option.setOnClickListener(v -> checkAnswer((CustomTextView) v));
    }

    /**
     * Configura la nueva pregunta y sus opciones en la interfaz.
     * @param index Índice de la pregunta a mostrar
     */
    private void configurarNuevaPregunta(int index) {
        Question question = questionList.get(index);
        tvPregunta.setText(question.getPregunta());
        option1TextView.setText(question.getOpcion1());
        option2TextView.setText(question.getOpcion2());
        option3TextView.setText(question.getOpcion3());
        option4TextView.setText(question.getOpcion4());
    }

    /**
     * Gestiona el comodín y las animaciones de la pregunta.
     */
    private void gestionarComodinYAnimaciones() {
        if (contadorRespuestasCorrectas >= 3) {
            reactivarComodin();
        }
        animateOptionsEntry();
    }

    /**
     * Configura el temporizador para la nueva pregunta y finaliza
     * el estado de espera después de un breve retraso.
     */
    private void configurarTemporizador() {
        timeLeftInMillis = 20000;
        startTimer();

        new Handler().postDelayed(() -> {
            esperandoSiguientePregunta = false;
            opcionesEnAnimacion = false;
        }, 500);
    }

    /**
     * Hace vibrar el dispositivo cuando se selecciona una opción incorrecta.
     * Proporciona retroalimentación táctil al usuario.
     * 
     * @param customTextView La vista de texto que fue seleccionada incorrectamente
     */
    private void vibrateCustomTextView(CustomTextView customTextView) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(100); // Vibración corta de 100ms
        }
    }

    /**
     * Inicia la animación de entrada secuencial para todas las opciones de respuesta.
     * Las opciones entran una tras otra con un retraso entre cada una,
     * creando un efecto visual atractivo.
     */
    private void animateOptionsEntry() {
        animateViewEntry(option1TextView, 0);    // Primera opción sin retraso
        animateViewEntry(option2TextView, 300);  // Segunda opción con 300ms de retraso
        animateViewEntry(option3TextView, 600);  // Tercera opción con 600ms de retraso
        animateViewEntry(option4TextView, 900);  // Cuarta opción con 900ms de retraso
    }

    /**
     * Anima la entrada de una vista individual desde fuera de la pantalla.
     * La vista se desliza desde la izquierda hasta su posición final.
     * 
     * @param view La vista a animar
     * @param delay Retraso en milisegundos antes de iniciar la animación
     */
    private void animateViewEntry(final View view, int delay) {
        // Posicionar la vista fuera de la pantalla inicialmente
        view.setTranslationX(-view.getWidth());
        
        // Iniciar la animación después del retraso especificado
        new Handler().postDelayed(() -> {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0);
            animator.setDuration(500);  // Duración de la animación: 500ms
            animator.start();
        }, delay);
    }

    /**
     * Anima una nube moviéndola de un lado a otro de la pantalla indefinidamente.
     * Crea un efecto de fondo dinámico y atractivo.
     * 
     * @param cloud ImageView de la nube a animar
     * @param moveRight true si la nube debe comenzar moviéndose hacia la derecha
     * @param delay Retraso inicial antes de comenzar la animación
     */
    private void animateCloud(final ImageView cloud, final boolean moveRight, int delay) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Calcular límites de movimiento
                final int screenWidth = getResources().getDisplayMetrics().widthPixels;
                float leftLimit = -500;  // Permite que las nubes se muevan más allá del borde izquierdo
                float rightLimit = screenWidth - cloud.getWidth();

                // Configurar posiciones inicial y final
                float start = moveRight ? leftLimit : rightLimit;
                float end = moveRight ? rightLimit : leftLimit;

                // Crear y configurar la animación
                ObjectAnimator animator = ObjectAnimator.ofFloat(cloud, "translationX", start, end);
                animator.setDuration(5000);  // Duración de 5 segundos por ciclo
                animator.setInterpolator(new LinearInterpolator());  // Movimiento lineal suave
                animator.setRepeatCount(ValueAnimator.INFINITE);     // Repetir indefinidamente
                animator.setRepeatMode(ValueAnimator.REVERSE);       // Invertir dirección al repetir
                animator.start();
            }
        }, delay);
    }

    /**
     * Hace vibrar las opciones incorrectas como parte del efecto del comodín.
     * Este método:
     * 1. Identifica las opciones incorrectas
     * 2. Selecciona aleatoriamente 2 de ellas
     * 3. Aplica una animación de vibración
     * 4. Permite deslizarlas para descartarlas
     */
    private void vibrateIncorrectOptions() {
        // Obtener la pregunta actual y todas las opciones
        Question currentQuestion = questionList.get(currentQuestionIndex);
        List<CustomTextView> options = Arrays.asList(option1TextView, option2TextView, 
                                                   option3TextView, option4TextView);
        List<CustomTextView> incorrectOptions = new ArrayList<>();
        
        // Identificar las opciones incorrectas comparando con la respuesta correcta
        for (CustomTextView option : options) {
            if (!option.getText().toString().equals(currentQuestion.getRespuestaCorrecta())) {
                incorrectOptions.add(option);
            }
        }
        
        // Mezclar aleatoriamente las opciones incorrectas
        Collections.shuffle(incorrectOptions);
        
        // Configurar la animación de vibración
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake.setRepeatCount(Animation.INFINITE);
        
        // Activar el estado de animación
        opcionesEnAnimacion = true;
        
        // Deshabilitar temporalmente todas las opciones para evitar interacciones no deseadas
        for (CustomTextView option : options) {
            option.setClickable(false);
        }
        
        // Aplicar animación y configurar listener de deslizamiento a solo 2 opciones incorrectas
        for (int i = 0; i < 2; i++) {
            CustomTextView incorrectOption = incorrectOptions.get(i);
            incorrectOption.startAnimation(shake);
            incorrectOption.setClickable(true);
            incorrectOption.setOnTouchListener(new SwipeDismissTouchListener(incorrectOption));
        }
    }

    /**
     * Listener personalizado que maneja el deslizamiento para descartar opciones incorrectas.
     * Permite al usuario deslizar horizontalmente una opción para eliminarla de la pantalla.
     */
    private class SwipeDismissTouchListener implements View.OnTouchListener {
        private final View view;                          // Vista que se puede deslizar
        private float downX;                             // Posición X inicial del toque
        private static final float SWIPE_THRESHOLD = 100; // Distancia mínima para considerar un deslizamiento válido
        private boolean isDismissed = false;             // Estado de descarte de la vista

        /**
         * Constructor del listener de deslizamiento.
         * @param view Vista que será deslizable
         */
        SwipeDismissTouchListener(View view) {
            this.view = view;
        }

        /**
         * Maneja los eventos de toque en la vista.
         * Implementa la lógica para:
         * - Detectar el inicio del toque
         * - Seguir el movimiento del dedo
         * - Procesar el final del toque y decidir si descartar la vista
         *
         * @param v Vista que recibe el evento
         * @param event Evento de toque
         * @return true si el evento fue manejado, false en caso contrario
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Guardar posición inicial del toque y limpiar animaciones previas
                    downX = event.getX();
                    view.clearAnimation();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    // Mover la vista horizontalmente siguiendo el dedo
                    float deltaX = event.getX() - downX;
                    view.setTranslationX(deltaX);
                    return true;

                case MotionEvent.ACTION_UP:
                    // Determinar si el deslizamiento fue suficiente para descartar
                    float displacement = Math.abs(view.getTranslationX());
                    if (displacement > SWIPE_THRESHOLD && !isDismissed) {
                        isDismissed = true;
                        dismissView();
                    } else {
                        // Si no fue suficiente, regresar a la posición original
                        view.animate()
                            .translationX(0)
                            .setDuration(200)
                            .start();
                    }
                    return true;
            }
            return false;
        }

        /**
         * Anima la salida de una vista cuando se descarta mediante deslizamiento.
         * La vista se desliza completamente fuera de la pantalla y se desvanece.
         */
        private void dismissView() {
            float width = view.getWidth();
            // Determinar la dirección del deslizamiento basado en la posición actual
            float direction = view.getTranslationX() > 0 ? width : -width;
            
            // Animar la salida de la vista
            view.animate()
                .translationX(direction)  // Mover fuera de la pantalla
                .alpha(0)                 // Desvanecer
                .setDuration(200)         // Duración de la animación
                .withEndAction(() -> {
                    view.setVisibility(View.GONE);
                    checkDismissedViews();
                })
                .start();
        }
    }

    /**
     * Verifica el número de vistas descartadas y actualiza el estado del juego.
     * Cuando se han descartado dos opciones incorrectas:
     * 1. Desactiva el estado de animación
     * 2. Rehabilita las opciones restantes para permitir su selección
     */
    private void checkDismissedViews() {
        int dismissedCount = 0;
        List<CustomTextView> options = Arrays.asList(option1TextView, option2TextView, 
                                                   option3TextView, option4TextView);
        
        // Contar opciones descartadas
        for (CustomTextView option : options) {
            if (option.getVisibility() == View.GONE) {
                dismissedCount++;
            }
        }
        
        // Si se han descartado dos opciones, rehabilitar las restantes
        if (dismissedCount == 2) {
            opcionesEnAnimacion = false;
            for (CustomTextView option : options) {
                if (option.getVisibility() == View.VISIBLE) {
                    option.setClickable(true);
                    option.setOnClickListener(v -> checkAnswer((CustomTextView) v));
                }
            }
        }
    }

    /**
     * Reactiva el comodín después de acumular suficientes respuestas correctas.
     * Este método:
     * 1. Restablece el estado del comodín
     * 2. Hace visible y habilita el botón del comodín
     * 3. Configura su listener
     * 4. Notifica al usuario de la disponibilidad
     */
    private void reactivarComodin() {
        comodinUsado = false;
        contadorRespuestasCorrectas = 0;
        
        // Configurar el botón del comodín
        ivComodin.setVisibility(View.VISIBLE);
        ivComodin.setClickable(true);
        ivComodin.setEnabled(true);
        ivComodin.setOnClickListener(v -> usarComodin());
        
        // Notificar al usuario
        Toast.makeText(this, "¡Comodín disponible!", Toast.LENGTH_SHORT).show();
    }

    private void usarComodin() {
        if (!comodinUsado) {
            try {
                // Reproducir sonido del comodín
                audioManagerPropio.playSound(R.raw.sonidocomodin);
                
                comodinUsado = true;
                contadorRespuestasCorrectas = 0;
                ivComodin.setVisibility(View.GONE);
                ivComodin.setClickable(false);
                
                // Reiniciar el temporizador a 20 segundos
                timeLeftInMillis = 20000;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                startTimer();
                
                vibrateIncorrectOptions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean opcionesVibrando() {
        return opcionesEnAnimacion;
    }

    private void animarNube(ImageView nube, Random random) {
        // Aumentamos el rango horizontal para que lleguen al borde
        float startX = -nube.getWidth() + random.nextFloat() * (screenWidth + nube.getWidth() * 2);
        float startY = -nube.getHeight();
        
        nube.setX(startX);
        nube.setY(startY);
        
        // Aumentamos la altura final para que lleguen al borde inferior real
        ValueAnimator animator = ValueAnimator.ofFloat(startY, screenHeight + nube.getHeight());
        animator.setDuration(8000 + random.nextInt(4000));
        animator.setInterpolator(new LinearInterpolator());
        
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            nube.setY(value);
            // Aquí está el movimiento horizontal
            float amplitude = screenWidth * 0.9f; // Aumentado a 30% del ancho de la pantalla
            nube.setX(startX + (float)(Math.sin(value/100) * amplitude));
            
            if (nube.getId() == R.id.ivComodin) {
                nube.setVisibility(View.VISIBLE);
                nube.setClickable(true);
            }
        });
        
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animarNube(nube, random);
            }
        });
        
        animator.start();
    }

    /**
     * Muestra una animación de explosión de nube cuando el usuario acierta una pregunta.
     * Este método maneja la lógica de animación frame por frame usando AnimationDrawable,
     * incluyendo logging para debugging y manejo de errores.
     */
    private void mostrarExplosionNube() {
        runOnUiThread(() -> {
            Log.d("Animacion", "Iniciando mostrarExplosionNube");
            
            // Verificación de seguridad
            if (ivNubeExplota == null) {
                Log.e("Animacion", "Error: ivNubeExplota es null");
                return;
            }
            
            try {
                // Preparar la vista para la animación
                prepararVistaExplosion();
                
                // Configurar y ejecutar la animación
                AnimationDrawable animacionExplosion = configurarAnimacionExplosion();
                
                if (animacionExplosion != null) {
                    ejecutarAnimacionExplosion(animacionExplosion);
                } else {
                    Log.e("Animacion", "Error: animacionExplosion es null");
                }
            } catch (Exception e) {
                Log.e("Animacion", "Error al mostrar la animación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Finaliza el juego, guarda la puntuación y regresa al menú principal.
     * Este método:
     * 1. Recupera los datos de la partida
     * 2. Guarda la puntuación en la base de datos
     * 3. Notifica al usuario del resultado
     * 4. Regresa al menú principal
     */
    private void finishGame() {
        // Recuperar datos de la partida
        String playerName = getIntent().getStringExtra("nombre");
        String tema = getIntent().getStringExtra("TEMA");
        String dificultad = getIntent().getStringExtra("DIFICULTAD");

        // Guardar puntuación en la base de datos
        boolean success = dbHelper.actualizarPuntuacion(playerName, tema, dificultad, playerPoints);

        // Notificar al usuario
        if (success) {
            Toast.makeText(this, "Puntuación guardada: " + playerPoints, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar la puntuación", Toast.LENGTH_SHORT).show();
        }

        // Regresar al menú principal
        Intent mainIntent = new Intent(QuestionActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    /**
     * Métodos auxiliares para la animación de explosión
     */
    private void prepararVistaExplosion() {
        ivNubeExplota.setVisibility(View.VISIBLE);
        ivNubeExplota.bringToFront();
        ivNubeExplota.invalidate();
    }

    private AnimationDrawable configurarAnimacionExplosion() {
        ivNubeExplota.setImageResource(R.drawable.nube_explota);
        return (AnimationDrawable) ivNubeExplota.getDrawable();
    }

    private void ejecutarAnimacionExplosion(AnimationDrawable animacion) {
        Log.d("Animacion", "Iniciando la animación");
        animacion.stop(); // Detener animación previa si existe
        animacion.start();
        
        // Ocultar la vista después de completar la animación
        new Handler().postDelayed(() -> {
            ivNubeExplota.setVisibility(View.GONE);
            Log.d("Animacion", "Animación completada");
        }, 1000);
    }

    /**
     * Actualiza la información del puesto del jugador en el ranking.
     * 
     * Este método:
     * 1. Recupera los datos del jugador actual (nombre, tema y dificultad)
     * 2. Consulta la base de datos para obtener su posición en el ranking
     * 3. Actualiza la UI mostrando el puesto y puntuación del jugador
     * 
     * La información se muestra en el formato:
     * - Si está clasificado: "Puesto: Nº[posición] ([puntos] pts)"
     * - Si no está clasificado: "Puesto: Sin clasificar (0 pts)"
     */
    private void actualizarPuestoJugador() {
        // Verificar si hay datos extras en el Intent
        if (getIntent().getExtras() != null) {
            // Recuperar datos del jugador
            String playerName = getIntent().getStringExtra("nombre");
            String tema = getIntent().getStringExtra("TEMA");
            String dificultad = getIntent().getStringExtra("DIFICULTAD");

            // Verificar que todos los datos necesarios estén presentes
            if (playerName != null && tema != null && dificultad != null) {
                // Consultar la base de datos para obtener información del puesto
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                DatabaseHelper.PuestoInfo puestoInfo = dbHelper.obtenerPuestoJugador(
                    playerName, tema, dificultad);

                // Actualizar la UI si la vista está disponible
                if (puestoTextView != null) {
                    if (puestoInfo.puesto > 0) {
                        // Jugador clasificado: mostrar puesto y puntos
                        puestoTextView.setText(String.format("Puesto: Nº%d (%d pts)", 
                            puestoInfo.puesto, puestoInfo.puntos));
                    } else {
                        // Jugador sin clasificar
                        puestoTextView.setText("Puesto: Sin clasificar (0 pts)");
                    }
                }
            }
        }
    }

    /**
     * Carga las preguntas según el modo de juego seleccionado.
     * Maneja dos modos diferentes:
     * 1. Modo Leyenda: Combina preguntas de múltiples temas y dificultades
     * 2. Modo Normal: Preguntas de un solo tema y dificultad
     * 
     * @param tema El tema seleccionado ("Leyenda" u otro)
     * @param dificultad La dificultad seleccionada (solo para modo normal)
     */
    private void loadQuestions(String tema, String dificultad) {
        if (tema.equals("Leyenda")) {
            loadLegendModeQuestions();
        } else {
            loadNormalModeQuestions(tema, dificultad);
        }
    }

    /**
     * Carga preguntas para el modo Leyenda.
     * Obtiene preguntas de múltiples temas y dificultades,
     * las combina y las mezcla aleatoriamente.
     */
    private void loadLegendModeQuestions() {
        String[] temas = {"Historia", "Ciencia", "Geografia", "Cine"};
        String[] dificultades = {"Fácil", "Media", "Difícil"};
        
        // Obtener preguntas de cada combinación tema-dificultad
        for (String t : temas) {
            for (String d : dificultades) {
                Log.d("QuestionActivity", "Obteniendo preguntas para tema: " + t + 
                      " y dificultad: " + d);
                List<Question> preguntasTema = dbHelper.getQuestionsByDifficultyAndTopic(
                    d, t, PREGUNTAS_POR_NIVEL_LEYENDA);
                Log.d("QuestionActivity", "Preguntas obtenidas: " + preguntasTema.size());
                questionList.addAll(preguntasTema);
            }
        }
        
        // Verificar si se obtuvieron preguntas
        if (questionList.isEmpty()) {
            handleEmptyQuestionList("Leyenda", null);
            return;
        }
        
        // Mezclar preguntas aleatoriamente
        Collections.shuffle(questionList);
        Log.d("QuestionActivity", "Total preguntas modo Leyenda: " + questionList.size());
    }

    /**
     * Carga preguntas para el modo normal.
     * Obtiene preguntas de un tema y dificultad específicos.
     * 
     * @param tema El tema seleccionado
     * @param dificultad La dificultad seleccionada
     */
    private void loadNormalModeQuestions(String tema, String dificultad) {
        questionList = dbHelper.getQuestionsByDifficultyAndTopic(
            dificultad, tema, PREGUNTAS_MODO_NORMAL);
        
        if (questionList.isEmpty()) {
            handleEmptyQuestionList(tema, dificultad);
        }
    }

    /**
     * Maneja el caso de no encontrar preguntas.
     * Muestra un mensaje de error y finaliza la actividad.
     * 
     * @param tema El tema que no tiene preguntas
     * @param dificultad La dificultad (null para modo Leyenda)
     */
    private void handleEmptyQuestionList(String tema, String dificultad) {
        String errorMessage = tema.equals("Leyenda") ?
            "Error: No se encontraron preguntas para modo Leyenda" :
            "No se encontraron preguntas para esta categoría";
        
        Log.e("QuestionActivity", "No se encontraron preguntas para tema: " + 
              tema + (dificultad != null ? " y dificultad: " + dificultad : ""));
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        finish();
    }

}