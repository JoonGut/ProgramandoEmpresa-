package com.example.game_exides;

/**
 * Gestor de sesión que utiliza el patrón Singleton para mantener
 * el estado del jugador actual en toda la aplicación.
 * Proporciona métodos para gestionar la sesión del jugador activo.
 */
public class SessionManager {
    // Instancia única del gestor de sesión (patrón Singleton)
    private static SessionManager instance;
    
    // Almacena el nombre del jugador actual en la sesión
    private String currentPlayer;
    
    /**
     * Constructor privado que inicializa el gestor de sesión.
     * Es privado para garantizar el patrón Singleton.
     */
    private SessionManager() {
        // Constructor privado para Singleton
    }
    
    /**
     * Método para obtener la instancia única del gestor de sesión.
     * Si no existe, la crea; si existe, la devuelve (patrón Singleton).
     * 
     * @return Instancia única del SessionManager
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Establece el jugador actual en la sesión.
     * 
     * @param playerName Nombre del jugador a establecer como actual
     */
    public void setCurrentPlayer(String playerName) {
        this.currentPlayer = playerName;
    }
    
    /**
     * Obtiene el nombre del jugador actual en la sesión.
     * 
     * @return Nombre del jugador actual o null si no hay ninguno
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Verifica si hay un jugador activo en la sesión.
     * 
     * @return true si hay un jugador activo, false en caso contrario
     */
    public boolean hasCurrentPlayer() {
        return currentPlayer != null && !currentPlayer.isEmpty();
    }
    
    /**
     * Limpia la sesión actual, eliminando el jugador activo.
     */
    public void clearSession() {
        currentPlayer = null;
    }
} 