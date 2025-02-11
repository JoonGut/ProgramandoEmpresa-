package com.example.game_exides;

/**
 * Clase que representa una pregunta del juego.
 * Contiene toda la información necesaria para presentar y evaluar una pregunta:
 * tema, dificultad, texto de la pregunta, opciones y respuesta correcta.
 */
public class Question {
    // Atributos de la pregunta
    private String tema;              // Categoría de la pregunta (Historia, Ciencia, etc.)
    private String dificultad;        // Nivel de dificultad (Fácil, Media, Difícil)
    private String pregunta;          // Texto de la pregunta
    private String opcion1;           // Primera opción de respuesta
    private String opcion2;           // Segunda opción de respuesta
    private String opcion3;           // Tercera opción de respuesta
    private String opcion4;           // Cuarta opción de respuesta
    private String respuestaCorrecta; // Respuesta correcta de la pregunta

    /**
     * Constructor principal que inicializa todos los campos de la pregunta.
     * @param tema Categoría de la pregunta
     * @param dificultad Nivel de dificultad
     * @param pregunta Texto de la pregunta
     * @param opcion1 Primera opción
     * @param opcion2 Segunda opción
     * @param opcion3 Tercera opción
     * @param opcion4 Cuarta opción
     * @param respuestaCorrecta Respuesta correcta
     */
    public Question(String tema, String dificultad, String pregunta, String opcion1, 
                   String opcion2, String opcion3, String opcion4, String respuestaCorrecta) {
        this.tema = tema;
        this.dificultad = dificultad;
        this.pregunta = pregunta;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.opcion4 = opcion4;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    /**
     * Constructor alternativo para compatibilidad con formato antiguo.
     * @param pregunta Texto de la pregunta
     * @param opcion1 Primera opción
     * @param opcion2 Segunda opción
     * @param opcion3 Tercera opción
     * @param opcion4 Cuarta opción
     * @param respuestaCorrecta Respuesta correcta
     * @param temaPregunta Categoría de la pregunta
     */
    public Question(String pregunta, String opcion1, String opcion2, String opcion3, 
                   String opcion4, String respuestaCorrecta, String temaPregunta) {
        // Constructor vacío para compatibilidad
    }

    // Métodos getter para acceder a los atributos de la pregunta
    
    /**
     * @return Categoría de la pregunta
     */
    public String getTema() {
        return tema;
    }

    /**
     * @return Nivel de dificultad de la pregunta
     */
    public String getDificultad() {
        return dificultad;
    }

    /**
     * @return Texto de la pregunta
     */
    public String getPregunta() {
        return pregunta;
    }

    /**
     * @return Primera opción de respuesta
     */
    public String getOpcion1() {
        return opcion1;
    }

    /**
     * @return Segunda opción de respuesta
     */
    public String getOpcion2() {
        return opcion2;
    }

    /**
     * @return Tercera opción de respuesta
     */
    public String getOpcion3() {
        return opcion3;
    }

    /**
     * @return Cuarta opción de respuesta
     */
    public String getOpcion4() {
        return opcion4;
    }

    /**
     * @return Respuesta correcta de la pregunta
     */
    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }
} 