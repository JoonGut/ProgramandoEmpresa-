package com.example.game_exides;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

/**
 * Gestor de Audio personalizado que utiliza el patrón Singleton para manejar la música y efectos de sonido del juego.
 * Este gestor nos permite tener un único punto de control para toda la música y sonidos,
 * evitando problemas de múltiples reproducciones simultáneas.
 */
public class AudioManagerPropio {
    // Instancia única del gestor de audio (patrón Singleton)
    private static AudioManagerPropio instance;
    // Reproductor actual de música de fondo
    private MediaPlayer currentMusic;
    // Contexto de la aplicación necesario para acceder a los recursos
    private Context context;
    // Bandera para controlar si la música está en pausa
    private boolean isPaused = false;

    /**
     * Constructor privado que inicializa el gestor con el contexto de la aplicación.
     * Es privado para garantizar el patrón Singleton.
     */
    private AudioManagerPropio(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Método para obtener la instancia única del gestor de audio.
     * Si no existe, la crea; si existe, la devuelve (patrón Singleton).
     */
    public static synchronized AudioManagerPropio getInstance(Context context) {
        if (instance == null) {
            instance = new AudioManagerPropio(context);
        }
        return instance;
    }

    /**
     * Verifica si hay música reproduciéndose actualmente.
     */
    public boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }

    /**
     * Reproduce una música de fondo con efecto fade-in.
     * @param resourceId ID del recurso de audio a reproducir
     */
    public void playMusic(int resourceId) {
        try {
            // Si la misma música ya está sonando, no hacemos nada para evitar reinicios innecesarios
            if (currentMusic != null && currentMusic.isPlaying()) {
                return;
            }

            // Limpiamos cualquier reproducción anterior para liberar recursos
            if (currentMusic != null) {
                currentMusic.release();
                currentMusic = null;
            }

            // Configuramos el nuevo reproductor de música
            currentMusic = MediaPlayer.create(context, resourceId);
            if (currentMusic != null) {
                currentMusic.setLooping(true);  // La música se reproducirá en bucle
                currentMusic.setVolume(0.5f, 0.5f);  // Volumen al 50%
                
                // Iniciamos la música con volumen 0 para hacer el fade-in
                currentMusic.setVolume(0.0f, 0.0f);
                currentMusic.start();
                
                // Creamos el efecto fade-in aumentando gradualmente el volumen
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    float volume = 0.0f;
                    @Override
                    public void run() {
                        if (currentMusic != null && volume < 0.5f) {
                            volume += 0.1f;  // Incrementamos el volumen gradualmente
                            currentMusic.setVolume(volume, volume);
                            handler.postDelayed(this, 50);  // Repetimos cada 50ms
                        }
                    }
                }, 50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reproduce un efecto de sonido una única vez.
     * El sonido se libera automáticamente al terminar.
     */
    public void playSound(int resourceId) {
        try {
            MediaPlayer sound = MediaPlayer.create(context, resourceId);
            if (sound != null) {
                // Configuramos un listener para liberar recursos cuando termine el sonido
                sound.setOnCompletionListener(mp -> {
                    mp.release();
                });
                sound.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pausa la reproducción de la música actual
     */
    public void pauseMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
            isPaused = true;
        }
    }

    /**
     * Reanuda la reproducción de la música si estaba pausada
     */
    public void resumeMusic() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.start();
        }
    }

    /**
     * Detiene completamente la música y libera los recursos
     */
    public void stopMusic() {
        if (currentMusic != null) {
            try {
                if (currentMusic.isPlaying()) {
                    currentMusic.stop();
                }
                currentMusic.release();
                currentMusic = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si hay música reproduciéndose actualmente
     */
    public boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }
} 