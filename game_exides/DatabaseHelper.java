package com.example.game_exides;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase auxiliar para gestionar la base de datos SQLite del juego.
 * Maneja todas las operaciones de la base de datos: creación, actualización,
 * consultas de preguntas y gestión de puntuaciones.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Constantes de configuración de la base de datos
    private static final String DATABASE_NAME = "game_exides.db";    // Nombre del archivo de la base de datos
    private static final int DATABASE_VERSION = 2;                   // Versión actual de la base de datos
    private static final String TAG = "DatabaseHelper";             // Tag para logging

    // Nombres de las tablas principales
    private static final String TABLE_QUESTIONS = "questions";       // Tabla general de preguntas
    private static final String TABLE_JUGADORES = "jugadores";      // Tabla de jugadores y puntuaciones

    // Columnas comunes para todas las tablas de preguntas
    private static final String COLUMN_ID = "id";                   // ID único de cada registro
    private static final String COLUMN_TEMA = "tema";               // Categoría de la pregunta
    private static final String COLUMN_DIFICULTAD = "dificultad";   // Nivel de dificultad
    private static final String COLUMN_PREGUNTA = "pregunta";       // Texto de la pregunta
    private static final String COLUMN_OPCION1 = "opcion1";         // Primera opción de respuesta
    private static final String COLUMN_OPCION2 = "opcion2";         // Segunda opción de respuesta
    private static final String COLUMN_OPCION3 = "opcion3";         // Tercera opción de respuesta
    private static final String COLUMN_OPCION4 = "opcion4";         // Cuarta opción de respuesta
    private static final String COLUMN_RESPUESTA_CORRECTA = "respuesta_correcta"; // Respuesta correcta

    // Columnas específicas para la tabla de jugadores
    private static final String COLUMN_NOMBRE = "nombre";           // Nombre del jugador
    private static final String COLUMN_CONTRASEÑA = "contraseña";   // Contraseña del jugador

    // Columnas para almacenar puntuaciones por categoría y dificultad
    private static final String COLUMN_HISTORIA_FACIL = "historia_facil";
    private static final String COLUMN_HISTORIA_MEDIO = "historia_medio";
    private static final String COLUMN_HISTORIA_DIFICIL = "historia_dificil";
    private static final String COLUMN_CIENCIA_FACIL = "ciencia_facil";
    private static final String COLUMN_CIENCIA_MEDIO = "ciencia_medio";
    private static final String COLUMN_CIENCIA_DIFICIL = "ciencia_dificil";
    private static final String COLUMN_GEOGRAFIA_FACIL = "geografia_facil";
    private static final String COLUMN_GEOGRAFIA_MEDIO = "geografia_medio";
    private static final String COLUMN_GEOGRAFIA_DIFICIL = "geografia_dificil";
    private static final String COLUMN_CINE_FACIL = "cine_facil";
    private static final String COLUMN_CINE_MEDIO = "cine_medio";
    private static final String COLUMN_CINE_DIFICIL = "cine_dificil";
    private static final String COLUMN_LEYENDA_FACIL = "leyenda";   // Puntuación especial para modo leyenda

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Historia con estructura para preguntas del tema
        db.execSQL("CREATE TABLE IF NOT EXISTS Historia (" +
                   COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   COLUMN_TEMA + " TEXT, " +
                   COLUMN_DIFICULTAD + " TEXT, " +
                   COLUMN_PREGUNTA + " TEXT, " +
                   COLUMN_OPCION1 + " TEXT, " +
                   COLUMN_OPCION2 + " TEXT, " +
                   COLUMN_OPCION3 + " TEXT, " +
                   COLUMN_OPCION4 + " TEXT, " +
                   COLUMN_RESPUESTA_CORRECTA + " TEXT)");

        // Crear tabla Ciencia con la misma estructura
        db.execSQL("CREATE TABLE IF NOT EXISTS Ciencia (" +
                   COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   COLUMN_TEMA + " TEXT, " +
                   COLUMN_DIFICULTAD + " TEXT, " +
                   COLUMN_PREGUNTA + " TEXT, " +
                   COLUMN_OPCION1 + " TEXT, " +
                   COLUMN_OPCION2 + " TEXT, " +
                   COLUMN_OPCION3 + " TEXT, " +
                   COLUMN_OPCION4 + " TEXT, " +
                   COLUMN_RESPUESTA_CORRECTA + " TEXT)");

        // Crear tabla Geografia con la misma estructura
        db.execSQL("CREATE TABLE IF NOT EXISTS Geografia (" +
                   COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   COLUMN_TEMA + " TEXT, " +
                   COLUMN_DIFICULTAD + " TEXT, " +
                   COLUMN_PREGUNTA + " TEXT, " +
                   COLUMN_OPCION1 + " TEXT, " +
                   COLUMN_OPCION2 + " TEXT, " +
                   COLUMN_OPCION3 + " TEXT, " +
                   COLUMN_OPCION4 + " TEXT, " +
                   COLUMN_RESPUESTA_CORRECTA + " TEXT)");

        // Crear tabla Cine con la misma estructura
        db.execSQL("CREATE TABLE IF NOT EXISTS Cine (" +
                   COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   COLUMN_TEMA + " TEXT, " +
                   COLUMN_DIFICULTAD + " TEXT, " +
                   COLUMN_PREGUNTA + " TEXT, " +
                   COLUMN_OPCION1 + " TEXT, " +
                   COLUMN_OPCION2 + " TEXT, " +
                   COLUMN_OPCION3 + " TEXT, " +
                   COLUMN_OPCION4 + " TEXT, " +
                   COLUMN_RESPUESTA_CORRECTA + " TEXT)");

        // Insertar preguntas en la tabla Historia
        String insertHistoria = "INSERT INTO Historia (" +
                                COLUMN_TEMA + ", " +
                                COLUMN_DIFICULTAD + ", " +
                                COLUMN_PREGUNTA + ", " +
                                COLUMN_OPCION1 + ", " +
                                COLUMN_OPCION2 + ", " +
                                COLUMN_OPCION3 + ", " +
                                COLUMN_OPCION4 + ", " +
                                COLUMN_RESPUESTA_CORRECTA + ") VALUES " +
                                "('Historia', 'Fácil', '¿Quién fue el primer presidente de los Estados Unidos?', 'Abraham Lincoln', 'George Washington', 'John Adams', 'Thomas Jefferson', 'George Washington')," +
                                "('Historia', 'Fácil', '¿En qué año llegó Cristóbal Colón a América?', '1492', '1485', '1500', '1498', '1492')," +
                                "('Historia', 'Fácil', '¿Qué imperio construyó la Gran Muralla China?', 'Dinastía Ming', 'Dinastía Qin', 'Dinastía Han', 'Dinastía Tang', 'Dinastía Qin')," +
                                "('Historia', 'Fácil', '¿Quién escribió la Ilíada y la Odisea?', 'Sócrates', 'Homero', 'Aristóteles', 'Platón', 'Homero')," +
                                "('Historia', 'Fácil', '¿En qué año comenzó la Primera Guerra Mundial?', '1914', '1912', '1916', '1918', '1914')," +
                                "('Historia', 'Fácil', '¿Qué faraón ordenó construir las Pirámides de Giza?', 'Keops', 'Ramsés II', 'Tutankamón', 'Akhenatón', 'Keops')," +
                                "('Historia', 'Fácil', '¿Quién conquistó el Imperio Azteca?', 'Hernán Cortés', 'Cristóbal Colón', 'Francisco Pizarro', 'Pedro de Alvarado', 'Hernán Cortés')," +
                                "('Historia', 'Fácil', '¿En qué año terminó la Segunda Guerra Mundial?', '1945', '1943', '1947', '1950', '1945')," +
                                "('Historia', 'Fácil', '¿Qué país fue fundado por Pedro el Grande?', 'Rusia', 'Francia', 'España', 'Italia', 'Rusia')," +
                                "('Historia', 'Fácil', '¿Qué monarca inglés tuvo seis esposas?', 'Enrique VIII', 'Enrique VII', 'Carlos I', 'Eduardo VI', 'Enrique VIII')," +
                                "('Historia', 'Fácil', '¿En qué año cayó el Imperio Romano de Occidente?', '476', '395', '500', '527', '476')," +
                                "('Historia', 'Fácil', '¿Qué tratado puso fin a la Primera Guerra Mundial?', 'Tratado de Versalles', 'Tratado de París', 'Tratado de Gante', 'Tratado de Utrecht', 'Tratado de Versalles')," +
                                "('Historia', 'Fácil', '¿Qué nación inventó la pólvora?', 'China', 'Japón', 'India', 'Arabia', 'China')," +
                                "('Historia', 'Fácil', '¿Quién dirigió la Marcha de la Sal en la India?', 'Mahatma Gandhi', 'Jawaharlal Nehru', 'Subhas Chandra Bose', 'Indira Gandhi', 'Mahatma Gandhi')," +
                                "('Historia', 'Fácil', '¿Qué reina egipcia fue amante de Julio César y Marco Antonio?', 'Cleopatra', 'Nefertiti', 'Hatshepsut', 'Isis', 'Cleopatra')," +
                                "('Historia', 'Fácil', '¿Qué muro dividió a Berlín durante la Guerra Fría?', 'Muro de Berlín', 'Cortina de Hierro', 'Muro de Europa', 'Muro de Hierro', 'Muro de Berlín')," +
                                "('Historia', 'Fácil', '¿Qué nave espacial fue la primera en aterrizar en la Luna?', 'Apollo 10', 'Apollo 12', 'Soyuz', 'Apollo 11', 'Apollo 11')," +
                                "('Historia', 'Fácil', '¿Qué ciudad fue destruida por el Vesubio en el año 79 d.C.?', 'Roma', 'Nápoles', 'Atenas', 'Pompeya', 'Pompeya')," +
                                "('Historia', 'Fácil', '¿En qué año llegó el hombre a la Luna?', '1967', '1965', '1969', '1971', '1969')," +
                                "('Historia', 'Fácil', '¿Qué evento marcó el inicio de la Revolución Francesa?', 'Declaración de Derechos', 'La ejecución de Luis XVI', 'La coronación de Napoleón', 'La Toma de la Bastilla', 'La Toma de la Bastilla')," +
                                "('Historia', 'Fácil', '¿Qué país lideró la expedición que circunnavegó el mundo primero?', 'Francia', 'Inglaterra', 'Portugal', 'España', 'España')," +
                                "('Historia', 'Fácil', '¿Qué tratado puso fin a la Segunda Guerra Mundial?', 'Tratado de Versalles', 'Tratado de Ginebra', 'Tratado de París', 'Tratado San Francisco', 'Tratado San Francisco')," +
                                "('Historia', 'Fácil', '¿En qué guerra luchó Simón Bolívar?', 'Independencia de Venezuela', 'Revolución Mexicana', 'Guerra Civil Española', 'Guerra de los Mil Días', 'Independencia de Venezuela')," +
                                "('Historia', 'Fácil', '¿Qué país utilizó por primera vez la pólvora en la guerra?', 'China', 'Japón', 'India', 'Turquía', 'China')," +
                                "('Historia', 'Fácil', '¿Quién proclamó la independencia de México?', 'Miguel Hidalgo', 'José María Morelos', 'Benito Juárez', 'Porfirio Díaz', 'Miguel Hidalgo')," +
                                "('Historia', 'Fácil', '¿Qué presidente abolió la esclavitud en los Estados Unidos?', 'Thomas Jefferson', 'George Washington', 'Abraham Lincoln', 'John Adams', 'Abraham Lincoln')," +
                                "('Historia', 'Fácil', '¿Qué líder fue conocido como \"El Libertador\" en América del Sur?', 'Simón Bolívar', 'José de San Martín', 'Antonio José de Sucre', 'Francisco de Miranda', 'Simón Bolívar')," +
                                "('Historia', 'Fácil', '¿Qué imperio construyó el Coliseo en Roma?', 'Imperio Romano', 'Imperio Griego', 'Imperio Bizantino', 'Imperio Persa', 'Imperio Romano')," +
                                "('Historia', 'Fácil', '¿Quién descubrió la penicilina?', 'Marie Curie', 'Alexander Fleming', 'Louis Pasteur', 'Isaac Newton', 'Alexander Fleming')," +
                                "('Historia', 'Fácil', '¿Qué país fue invadido por Alemania al inicio de la Segunda Guerra Mundial?', 'Francia', 'Reino Unido', 'Polonia', 'Noruega', 'Polonia')," +
                                "('Historia', 'Fácil', '¿En qué país se encuentra la Gran Pirámide de Giza?', 'México', 'Egipto', 'China', 'India', 'Egipto')," +
                                "('Historia', 'Fácil', '¿Qué inventó Johannes Gutenberg?', 'Teléfono', 'Imprenta', 'Telégrafo', 'Microscopio', 'Imprenta')," +
                                "('Historia', 'Fácil', '¿Qué famoso físico desarrolló la teoría de la relatividad?', 'Isaac Newton', 'Nikola Tesla', 'Albert Einstein', 'Stephen Hawking', 'Albert Einstein')," +
                                "('Historia', 'Fácil', '¿Qué faraón egipcio tenía una famosa máscara de oro?', 'Keops', 'Tutankamón', 'Ramsés II', 'Akhenatón', 'Tutankamón')," +
                                "('Historia', 'Fácil', '¿Qué ciudad fue conocida como Constantinopla?', 'Roma', 'Atenas', 'Estambul', 'Cartago', 'Estambul')," +
                                "('Historia', 'Fácil', '¿Quién fue el líder de los nazis durante la Segunda Guerra Mundial?', 'Joseph Stalin', 'Benito Mussolini', 'Winston Churchill', 'Adolf Hitler', 'Adolf Hitler')," +
                                "('Historia', 'Fácil', '¿En qué país se originaron los Juegos Olímpicos antiguos?', 'Italia', 'Grecia', 'Egipto', 'India', 'Grecia')," +
                                "('Historia', 'Fácil', '¿Qué río es conocido como \"El río de los faraones\"?', 'Nilo', 'Amazonas', 'Tigris', 'Éufrates', 'Nilo')," +
                                "('Historia', 'Fácil', '¿Qué revolución comenzó en Inglaterra en el siglo XVIII?', 'Revolución Industrial', 'Revolución Francesa', 'Revolución Rusa', 'Revolución Americana', 'Revolución Industrial')," +
                                "('Historia', 'Fácil', '¿Quién fue el primer hombre en el espacio?', 'Buzz Aldrin', 'Neil Armstrong', 'Yuri Gagarin', 'Alan Shepard', 'Yuri Gagarin')," +
                                "('Historia', 'Fácil', '¿Qué imperio fue dirigido por Alejandro Magno?', 'Imperio Egipcio', 'Imperio Persa', 'Imperio Griego', 'Imperio Macedonio', 'Imperio Macedonio')," +
                                "('Historia', 'Fácil', '¿En qué país se encuentra el Muro de Berlín?', 'Alemania', 'Francia', 'Polonia', 'Rusia', 'Alemania')," +
                                // Preguntas de nivel medio
                                "('Historia', 'Media', '¿Qué evento marcó el inicio de la Revolución Francesa?', 'La Toma de la Bastilla', 'La Declaración', 'La coronación', 'La ejecución de Luis XVI', 'La Toma de la Bastilla'), " +
                                "('Historia', 'Media', '¿En qué guerra luchó Juana de Arco?', 'Guerra de los Cien Años', 'Guerra de las Rosas', 'Guerra Napoleónica', 'Guerra de Independencia', 'Guerra de los Cien Años'), " +
                                "('Historia', 'Media', '¿Quién fue el último zar de Rusia?', 'Nicolás II', 'Iván el Terrible', 'Pedro el Grande', 'Alejandro II', 'Nicolás II'), " +
                                "('Historia', 'Media', '¿Qué emperador romano hizo del cristianismo la religión oficial?', 'Constantino', 'Nerón', 'Marco Aurelio', 'Augusto', 'Constantino'), " +
                                "('Historia', 'Media', '¿Qué faraón es conocido por su tumba intacta?', 'Tutankamón', 'Ramsés II', 'Akhenatón', 'Keops', 'Tutankamón'), " +
                                "('Historia', 'Media', '¿Qué movimiento luchó por la abolición de la esclavitud en EE.UU?', 'Movimiento Abolicionista', 'Movimiento Progresista', 'Movimiento Revolucionario', 'Movimiento de Derechos Civiles', 'Movimiento Abolicionista'), " +
                                "('Historia', 'Media', '¿En qué año se produjo la Revolución Mexicana?', '1910', '1920', '1898', '1905', '1910'), " +
                                "('Historia', 'Media', '¿Qué país lideró la expedición que circunnavegó el mundo primero?', 'España', 'Portugal', 'Inglaterra', 'Francia', 'España'), " +
                                "('Historia', 'Media', '¿Qué revolución ocurrió en 1917?', 'Revolución Rusa', 'Revolución China', 'Revolución Mexicana', 'Revolución Cubana', 'Revolución Rusa'), " +
                                "('Historia', 'Media', '¿Qué tratado puso fin a la Guerra de Independencia de los Estados Unidos?', 'Tratado de París', 'Tratado de Versalles', 'Tratado de Ginegra', 'Tratado de Utrecht', 'Tratado de París'), " +
                                "('Historia', 'Media', '¿Quién lideró la unificación de Italia en el siglo XIX?', 'Giuseppe Mazzini', 'Giuseppe Garibaldi', 'Víctor Manuel II', 'Camillo Cavour', 'Giuseppe Garibaldi'), " +
                                "('Historia', 'Media', '¿Qué país fue gobernado por Napoleón Bonaparte?', 'Alemania', 'Italia', 'España', 'Francia', 'Francia'), " +
                                "('Historia', 'Media', '¿Qué dinastía gobernó China durante la construcción de la Ciudad Prohibida?', 'Dinastía Ming', 'Dinastía Qin', 'Dinastía Tang', 'Dinastía Han', 'Dinastía Ming'), " +
                                "('Historia', 'Media', '¿Qué líder dirigió la Revolución Cubana?', 'Fidel Castro', 'Che Guevara', 'Camilo Cienfuegos', 'Raúl Castro', 'Fidel Castro'), " +
                                "('Historia', 'Media', '¿Qué famosa batalla marcó la derrota final de Napoleón?', 'Batalla de Trafalgar', 'Batalla de Borodino', 'Batalla de Leipzig', 'Batalla de Waterloo', 'Batalla de Waterloo'), " +
                                "('Historia', 'Media', '¿Qué tratado dividió las tierras del Nuevo Mundo entre España y Portugal?', 'Tratado de Gante', 'Tratado de Tordesillas', 'Tratado de Versalles', 'Tratado de París', 'Tratado de Tordesillas'), " +
                                "('Historia', 'Media', '¿Qué líder sudafricano luchó contra el apartheid?', 'Desmond Tutu', 'Nelson Mandela', 'Mahatma Gandhi', 'Steve Biko', 'Nelson Mandela'), " +
                                "('Historia', 'Media', '¿Qué guerra fue conocida como la \"Guerra de Secesión\"?', 'Guerra Civil Americana', 'Guerra de Independencia', 'Guerra Revolucionaria', 'Guerra de los Confederados', 'Guerra Civil Americana'), " +
                                "('Historia', 'Media', '¿En qué país se encuentra la ciudad perdida de Machu Picchu?', 'Chile', 'Argentina', 'Perú', 'Bolivia', 'Perú'), " +
                                "('Historia', 'Media', '¿Qué invento de Johannes Gutenberg revolucionó la comunicación?', 'Teléfono', 'Microscopio', 'Imprenta', 'Radio', 'Imprenta'), " +
                                "('Historia', 'Media', '¿Qué reina fue conocida como la \"Reina Virgen\"?', 'María Estuardo', 'Isabel I', 'Catalina de Aragón', 'Ana Bolena', 'Isabel I'), " +
                                "('Historia', 'Media', '¿Qué conflicto se conoce como la \"Gran Guerra\"?', 'Primera Guerra Mundial', 'Segunda Guerra Mundial', 'Guerra de Secesión', 'Guerra de los Treinta Años', 'Primera Guerra Mundial'), " +
                                "('Historia', 'Media', '¿Qué líder mundial fue apodado el \"Zorro del Desierto\"?', 'Dwight D. Eisenhower', 'Bernard Montgomery', 'Erwin Rommel', 'George Patton', 'Erwin Rommel'), " +
                                "('Historia', 'Media', '¿Quién fue el primer emperador de Roma?', 'Julio César', 'Tiberio', 'Nerón', 'Augusto', 'Augusto'), " +
                                "('Historia', 'Media', '¿En qué año ocurrió la caída del Muro de Berlín?', '1987', '1988', '1989', '1990', '1989'), " +
                                "('Historia', 'Media', '¿Qué filósofo escribió \"El Príncipe\"?', 'Aristóteles', 'Maquiavelo', 'Platón', 'Sócrates', 'Maquiavelo'), " +
                                "('Historia', 'Media', '¿Qué batalla fue la última de la Revolución Mexicana?', 'Batalla de Puebla', 'Batalla de Celaya', 'Batalla de Zacatecas', 'Batalla de Juárez', 'Batalla de Celaya'), " +
                                "('Historia', 'Media', '¿Quién lideró la independencia de Venezuela?', 'José de San Martín', 'Simón Bolívar', 'Antonio José de Sucre', 'Francisco Miranda', 'Simón Bolívar'), " +
                                "('Historia', 'Media', '¿Qué imperio construyó la famosa ciudad de Tenochtitlán?', 'Inca', 'Azteca', 'Maya', 'Tolteca', 'Azteca'), " +
                                "('Historia', 'Media', '¿Qué país fue invadido por Alemania durante la Blitzkrieg?', 'Francia', 'Reino Unido', 'Polonia', 'Noruega', 'Polonia'), " +
                                "('Historia', 'Media', '¿Qué líder griego fundó Alejandría?', 'Sócrates', 'Platón', 'Alejandro Magno', 'Aristóteles', 'Alejandro Magno'), " +
                                "('Historia', 'Media', '¿Qué civilización construyó Chichén Itzá?', 'Maya', 'Inca', 'Azteca', 'Olmeca', 'Maya'), " +
                                "('Historia', 'Media', '¿Qué explorador fue el primero en rodear el mundo?', 'Cristóbal Colón', 'Ferdinand Magellan', 'James Cook', 'Vasco da Gama', 'Ferdinand Magellan'), " +
                                "('Historia', 'Media', '¿Qué rey inglés firmó la Carta Magna?', 'Enrique VIII', 'Juan Sin Tierra', 'Ricardo Corazón de León', 'Eduardo I', 'Juan Sin Tierra'), " +
                                "('Historia', 'Media', '¿Qué revolución trajo la Declaración de los Derechos del Hombre?', 'Revolución Americana', 'Revolución Francesa', 'Revolución Industrial', 'Revolución Rusa', 'Revolución Francesa'), " +
                                "('Historia', 'Media', '¿Qué ciudad fue la capital del Imperio Bizantino?', 'Atenas', 'Cartago', 'Roma', 'Constantinopla', 'Constantinopla'), " +
                                "('Historia', 'Media', '¿Qué país fue conocido como \"La Nueva España\"?', 'México', 'Perú', 'Argentina', 'Chile', 'México'), " +
                                "('Historia', 'Media', '¿Qué nación fue la primera en abolir la esclavitud?', 'Francia', 'Reino Unido', 'Dinamarca', 'Estados Unidos', 'Dinamarca'), " +
                                "('Historia', 'Media', '¿Qué guerra fue liderada por Oliver Cromwell en Inglaterra?', 'Guerra de los Cien Años', 'Revolución Gloriosa', 'Guerra Civil Inglesa', 'Guerra de las Rosas', 'Guerra Civil Inglesa'), " +
                                "('Historia', 'Media', '¿Qué rey fue conocido como \"El Sol\"?', 'Luis XIV', 'Felipe II', 'Carlos I', 'Enrique IV', 'Luis XIV'), " +
                                "('Historia', 'Media', '¿En qué país ocurrió la masacre de Tiananmén en 1989?', 'Corea del Norte', 'Vietnam', 'China', 'Japón', 'China'), " +
                                "('Historia', 'Media', '¿Qué movimiento obrero comenzó en Chicago en 1886?', 'Movimiento Comunista', 'Movimiento Feminista', 'Movimiento Anarquista', 'Mártires de Chicago', 'Mártires de Chicago'), " +

                                "('Historia', 'Difícil', '¿Qué imperio gobernó Babilonia antes de su caída?', 'Imperio Asirio', 'Imperio Persa', 'Imperio Acadio', 'Imperio Hitita', 'Imperio Persa')," +
                                "('Historia', 'Difícil', '¿Qué cultura inventó el sistema numérico de base 60?', 'Sumerios', 'Egipcios', 'Babilonios', 'Fenicios', 'Babilonios')," +
                                "('Historia', 'Difícil', '¿En qué año ocurrió la Batalla de Hastings?', '1066', '1071', '1054', '1085', '1066')," +
                                "('Historia', 'Difícil', '¿Qué científico descubrió la penicilina?', 'Louis Pasteur', 'Alexander Fleming', 'Gregor Mendel', 'Robert Koch', 'Alexander Fleming')," +
                                "('Historia', 'Difícil', '¿Qué tipo de enlace químico se forma entre el sodio y el cloro en la sal?', 'Covalente', 'Metálico', 'Iónico', 'De hidrógeno', 'Iónico')," +
                                "('Historia', 'Difícil', '¿Qué fuerza actúa sobre los objetos en caída libre?', 'Fricción', 'Gravedad', 'Electromagnética', 'Centrífuga', 'Gravedad')," +
                                "('Historia', 'Difícil', '¿Qué elemento tiene el número atómico 79?', 'Oro', 'Mercurio', 'Plomo', 'Plata', 'Oro')," +
                                "('Historia', 'Difícil', '¿Qué ley explica cómo se expanden los gases?', 'Ley de Boyle', 'Ley de Charles', 'Ley de Avogadro', 'Ley de Dalton', 'Ley de Charles')," +
                                "('Historia', 'Difícil', '¿Qué órgano controla la homeostasis en el cuerpo humano?', 'Cerebro', 'Riñones', 'Corazón', 'Hígado', 'Cerebro')," +
                                "('Historia', 'Difícil', '¿Qué tipo de estrella es el Sol?', 'Gigante roja', 'Enana blanca', 'Estrella de neutrones', 'Enana amarilla', 'Enana amarilla')," +
                                "('Historia', 'Difícil', '¿Qué científico descubrió la teoría de la radiactividad?', 'Marie Curie', 'Henri Becquerel', 'Enrico Fermi', 'Ernest Rutherford', 'Henri Becquerel')," +
                                "('Historia', 'Difícil', '¿Qué proteína transporta oxígeno en la sangre?', 'Colágeno', 'Hemoglobina', 'Queratina', 'Insulina', 'Hemoglobina')," +
                                "('Historia', 'Difícil', '¿Qué compuesto es conocido como agua pesada?', 'H2O2', 'D2O', 'H2O', 'O2H', 'D2O')," +
                                "('Historia', 'Difícil', '¿Qué mineral tiene una dureza de 10 en la escala de Mohs?', 'Cuarzo', 'Diamante', 'Rubí', 'Esmeralda', 'Diamante')," +
                                "('Historia', 'Difícil', '¿Qué proceso convierte el nitrógeno atmosférico en una forma utilizable para las plantas?', 'Fotosíntesis', 'Fijación de nitrógeno', 'Respiración', 'Fermentación', 'Fijación de nitrógeno')," +
                                "('Historia', 'Difícil', '¿Qué órgano del cuerpo produce glóbulos rojos?', 'Riñones', 'Hígado', 'Médula ósea', 'Pulmones', 'Médula ósea')," +
                                "('Historia', 'Difícil', '¿Qué físico desarrolló el principio de incertidumbre?', 'Niels Bohr', 'Werner Heisenberg', 'Max Planck', 'Albert Einstein', 'Werner Heisenberg')," +
                                "('Historia', 'Difícil', '¿Qué planeta tiene una inclinación axial extrema?', 'Saturno', 'Urano', 'Neptuno', 'Júpiter', 'Urano')," +
                                "('Historia', 'Difícil', '¿Qué partícula es responsable de la interacción nuclear fuerte?', 'Fotón', 'Bosón Z', 'Gluón', 'Neutrino', 'Gluón')," +
                                "('Historia', 'Difícil', '¿Qué gas es el principal componente del aire exhalado?', 'Oxígeno', 'Nitrógeno', 'Dióxido de carbono', 'Hidrógeno', 'Dióxido de carbono')," +
                                "('Historia', 'Difícil', '¿Qué teoría unifica la electricidad y el magnetismo?', 'Relatividad', 'Termodinámica', 'Electromagnetismo', 'Mecánica cuántica', 'Electromagnetismo')," +
                                "('Historia', 'Difícil', '¿Qué elemento químico tiene el símbolo \"W\"?', 'Tungsteno', 'Wolframio', 'Plomo', 'Cromo', 'Tungsteno')," +
                                "('Historia', 'Difícil', '¿Qué fenómeno astronómico ocurre cuando la Luna pasa por la sombra de la Tierra?', 'Eclipse solar', 'Eclipse lunar', 'Marea alta', 'Aurora', 'Eclipse lunar')," +
                                "('Historia', 'Difícil', '¿Qué unidad mide la energía?', 'Joule', 'Watt', 'Newton', 'Hertz', 'Joule')," +
                                "('Historia', 'Difícil', '¿Qué fuerza causa que los planetas se mantengan en órbita?', 'Inercia', 'Gravedad', 'Fricción', 'Electromagnética', 'Gravedad')," +
                                "('Historia', 'Difícil', '¿Qué órgano del cuerpo humano controla la temperatura?', 'Piel', 'Corazón', 'Cerebro', 'Riñones', 'Cerebro')," +
                                "('Historia', 'Difícil', '¿Qué científicos propusieron el modelo helicoidal del ADN?', 'Watson y Crick', 'Newton y Leibniz', 'Einstein y Bohr', 'Darwin y Mendel', 'Watson y Crick')," +
                                "('Historia', 'Difícil', '¿Qué dispositivo mide la intensidad de la luz?', 'Termómetro', 'Fotómetro', 'Espectrómetro', 'Barómetro', 'Fotómetro')," +
                                "('Historia', 'Difícil', '¿Qué proceso convierte la glucosa en energía utilizable en las células?', 'Fotosíntesis', 'Respiración celular', 'Fermentación', 'Gluconeogénesis', 'Respiración celular')," +
                                "('Historia', 'Difícil', '¿Qué galaxia se encuentra más cerca de la Vía Láctea?', 'Andrómeda', 'Magallanes', 'Triangulum', 'Sagitario', 'Andrómeda')," +
                                "('Historia', 'Difícil', '¿Qué científico propuso las leyes de la termodinámica?', 'Isaac Newton', 'Rudolf Clausius', 'James Watt', 'Albert Einstein', 'Rudolf Clausius')," +
                                "('Historia', 'Difícil', '¿Qué fenómeno describe la expansión acelerada del universo?', 'Inflación', 'Big Bang', 'Energía oscura', 'Materia oscura', 'Energía oscura')," +
                                "('Historia', 'Difícil', '¿Qué estructura celular contiene clorofila?', 'Mitocondria', 'Cloroplasto', 'Núcleo', 'Ribosomas', 'Cloroplasto')," +
                                "('Historia', 'Difícil', '¿Qué científico desarrolló el modelo atómico que incluye niveles de energía?', 'Rutherford', 'Niels Bohr', 'Heisenberg', 'Schrödinger', 'Niels Bohr')," +
                                "('Historia', 'Difícil', '¿Qué órgano regula el equilibrio hídrico del cuerpo humano?', 'Riñones', 'Hígado', 'Pulmones', 'Estómago', 'Riñones')," +
                                "('Historia', 'Difícil', '¿Qué descubrimiento científico permitió los rayos X?', 'Radiactividad', 'Luz ultravioleta', 'Radiación electromagnética', 'Difracción de electrones', 'Radiación electromagnética')," +
                                "('Historia', 'Difícil', '¿Qué científico desarrolló el concepto de los orbitales atómicos?', 'Heisenberg', 'Schrödinger', 'Bohr', 'Einstein', 'Schrödinger')," +
                                "('Historia', 'Difícil', '¿Qué tipo de roca contiene fósiles?', 'Ígnea', 'Sedimentaria', 'Metamórfica', 'Pétrea', 'Sedimentaria')," +
                                "('Historia', 'Difícil', '¿Qué unidad mide el tiempo de semidesintegración de un elemento radiactivo?', 'Segundo', 'Minuto', 'Año', 'Curie', 'Segundo') ";



        db.execSQL(insertHistoria);

        // Insertar preguntas en la tabla Ciencia
        String insertCiencia = "INSERT INTO Ciencia (" +
                               COLUMN_TEMA + ", " +
                               COLUMN_DIFICULTAD + ", " +
                               COLUMN_PREGUNTA + ", " +
                               COLUMN_OPCION1 + ", " +
                               COLUMN_OPCION2 + ", " +
                               COLUMN_OPCION3 + ", " +
                               COLUMN_OPCION4 + ", " +
                               COLUMN_RESPUESTA_CORRECTA + ") VALUES " +
                                //Ciencia
                "('Ciencia', 'Fácil', '¿Cuál es el planeta más grande del sistema solar?', 'Tierra', 'Júpiter', 'Saturno', 'Neptuno', 'Júpiter')," +
                "('Ciencia', 'Fácil', '¿Cuál es el elemento químico representado por la letra H en la tabla periódica?', 'Helio', 'Hidrógeno', 'Hierro', 'Hafnio', 'Hidrógeno')," +
                "('Ciencia', 'Fácil', '¿Qué órgano del cuerpo humano bombea sangre?', 'Pulmones', 'Hígado', 'Corazón', 'Cerebro', 'Corazón')," +
                "('Ciencia', 'Fácil', '¿Qué gas respiramos que es esencial para la vida?', 'Oxígeno', 'Nitrógeno', 'Dióxido de carbono', 'Hidrógeno', 'Oxígeno')," +
                "('Ciencia', 'Fácil', '¿Cómo se llama el proceso por el cual las plantas producen su propio alimento?', 'Fotosíntesis', 'Respiración', 'Fermentación', 'Oxidación', 'Fotosíntesis')," +
                "('Ciencia', 'Fácil', '¿Cuántos planetas tiene el sistema solar?', '8', '7', '9', '10', '8')," +
                "('Ciencia', 'Fácil', '¿Qué animal es conocido como el más rápido en tierra?', 'Leopardo', 'Cheetah', 'Caballo', 'Tigre', 'Cheetah')," +
                "('Ciencia', 'Fácil', '¿Qué sustancia da color verde a las plantas?', 'Clorofila', 'Hemoglobina', 'Pigmentos', 'Caroteno', 'Clorofila')," +
                "('Ciencia', 'Fácil', '¿Cuál es el metal más abundante en la corteza terrestre?', 'Oro', 'Plata', 'Hierro', 'Aluminio', 'Aluminio')," +
                "('Ciencia', 'Fácil', '¿Qué órgano del cuerpo humano filtra la sangre?', 'Pulmones', 'Hígado', 'Riñones', 'Corazón', 'Riñones')," +
                "('Ciencia', 'Fácil', '¿Qué fuerza mantiene a los planetas en órbita alrededor del sol?', 'Magnetismo', 'Electromagnetismo', 'Gravedad', 'Inercia', 'Gravedad')," +
                "('Ciencia', 'Fácil', '¿Qué unidad se utiliza para medir la intensidad de la corriente eléctrica?', 'Newton', 'Voltio', 'Amperio', 'Ohmio', 'Amperio')," +
                "('Ciencia', 'Fácil', '¿Qué tipo de animal es una ballena?', 'Pez', 'Mamífero', 'Reptil', 'Anfibio', 'Mamífero')," +
                "('Ciencia', 'Fácil', '¿Cuál es el órgano más grande del cuerpo humano?', 'Hígado', 'Cerebro', 'Corazón', 'Piel', 'Piel')," +
                "('Ciencia', 'Fácil', '¿Qué planeta es conocido como el planeta rojo?', 'Venus', 'Marte', 'Saturno', 'Júpiter', 'Marte')," +
                "('Ciencia', 'Fácil', '¿Qué porcentaje de la superficie terrestre está cubierta de agua?', '50%', '60%', '70%', '80%', '70%')," +
                "('Ciencia', 'Fácil', '¿Qué gas es el más abundante en la atmósfera terrestre?', 'Oxígeno', 'Nitrógeno', 'Dióxido de carbono', 'Hidrógeno', 'Nitrógeno')," +
                "('Ciencia', 'Fácil', '¿Qué científico desarrolló la teoría de la relatividad?', 'Isaac Newton', 'Nikola Tesla', 'Albert Einstein', 'Stephen Hawking', 'Albert Einstein')," +
                "('Ciencia', 'Fácil', '¿Qué planeta tiene un sistema de anillos visibles?', 'Júpiter', 'Urano', 'Saturno', 'Neptuno', 'Saturno')," +
                "('Ciencia', 'Fácil', '¿Qué instrumento se usa para medir la temperatura?', 'Barómetro', 'Microscopio', 'Termómetro', 'Telescopio', 'Termómetro')," +
                "('Ciencia', 'Fácil', '¿Qué es el ADN?', 'Una proteína', 'Un carbohidrato', 'Un ácido nucleico', 'Un lípido', 'Un ácido nucleico')," +
                "('Ciencia', 'Fácil', '¿Qué mineral es esencial para la formación de huesos y dientes?', 'Hierro', 'Calcio', 'Fósforo', 'Sodio', 'Calcio')," +
                "('Ciencia', 'Fácil', '¿Qué tipo de energía utiliza un panel solar?', 'Nuclear', 'Térmica', 'Eólica', 'Luz solar', 'Luz solar')," +
                "('Ciencia', 'Fácil', '¿Qué gas es necesario para la combustión?', 'Dióxido de carbono', 'Oxígeno', 'Nitrógeno', 'Hidrógeno', 'Oxígeno')," +
                "('Ciencia', 'Fácil', '¿Qué animal puede regenerar partes de su cuerpo?', 'Tiburón', 'Salamandra', 'Águila', 'Elefante', 'Salamandra')," +
                "('Ciencia', 'Fácil', '¿Qué estrella es el centro del sistema solar?', 'Proxima Centauri', 'La Luna', 'El Sol', 'Andrómeda', 'El Sol')," +
                "('Ciencia', 'Fácil', '¿Qué estado de la materia tiene forma y volumen definidos?', 'Líquido', 'Sólido', 'Gas', 'Plasma', 'Sólido')," +
                "('Ciencia', 'Fácil', '¿Qué dispositivo convierte energía mecánica en eléctrica?', 'Motor', 'Generador', 'Pila', 'Batería', 'Generador')," +
                "('Ciencia', 'Fácil', '¿Qué planeta es conocido por sus fuertes tormentas y manchas rojas?', 'Venus', 'Saturno', 'Júpiter', 'Urano', 'Júpiter')," +
                "('Ciencia', 'Fácil', '¿Qué animal pone huevos pero es un mamífero?', 'Ballena', 'Canguro', 'Ornitorrinco', 'Delfín', 'Ornitorrinco')," +
                "('Ciencia', 'Fácil', '¿Qué unidad se utiliza para medir el peso?', 'Metro', 'Kilogramo', 'Litro', 'Joule', 'Kilogramo')," +
                "('Ciencia', 'Fácil', '¿Qué planeta tiene el día más largo en el sistema solar?', 'Mercurio', 'Venus', 'Tierra', 'Marte', 'Venus')," +
                "('Ciencia', 'Fácil', '¿Qué gas hace burbujas en las bebidas gaseosas?', 'Dióxido de carbono', 'Oxígeno', 'Hidrógeno', 'Metano', 'Dióxido de carbono')," +
                "('Ciencia', 'Fácil', '¿Qué músculo es responsable de bombear sangre?', 'Hígado', 'Pulmones', 'Corazón', 'Riñón', 'Corazón')," +
                "('Ciencia', 'Fácil', '¿Qué animal tiene ocho patas?', 'Araña', 'Hormiga', 'Mariposa', 'Escorpión', 'Araña')," +
                "('Ciencia', 'Fácil', '¿Qué metal es líquido a temperatura ambiente?', 'Oro', 'Mercurio', 'Cobre', 'Plomo', 'Mercurio')," +
                "('Ciencia', 'Fácil', '¿Qué cuerpo celeste provoca las mareas?', 'El Sol', 'La Luna', 'Júpiter', 'Marte', 'La Luna')," +
                "('Ciencia', 'Fácil', '¿Qué partícula subatómica tiene carga positiva?', 'Electrón', 'Protón', 'Neutrón', 'Quark', 'Protón')," +
                "('Ciencia', 'Fácil', '¿Qué animal es conocido por su capacidad para cambiar de color?', 'Cangrejo', 'Camaleón', 'Iguana', 'Serpiente', 'Camaleón')," +
                "('Ciencia', 'Fácil', '¿Qué planeta es conocido como el gemelo de la Tierra?', 'Marte', 'Venus', 'Urano', 'Mercurio', 'Venus')," +
                "('Ciencia', 'Fácil', '¿Qué enfermedad es causada por la falta de vitamina C?', 'Anemia', 'Escorbuto', 'Raquitismo', 'Ceguera nocturna', 'Escorbuto')," +
                "('Ciencia', 'Fácil', '¿Qué aparato se utiliza para observar objetos lejanos?', 'Microscopio', 'Termómetro', 'Telescopio', 'Barómetro', 'Telescopio')," +
                "('Ciencia', 'Fácil', '¿Qué órgano produce insulina en el cuerpo humano?', 'Riñones', 'Páncreas', 'Hígado', 'Estómago', 'Páncreas')," +
                "('Ciencia', 'Fácil', '¿Qué planeta tiene la mayor cantidad de lunas conocidas?', 'Saturno', 'Júpiter', 'Urano', 'Neptuno', 'Saturno')," +
                "('Ciencia', 'Fácil', '¿Qué animal tiene el cerebro más grande en proporción a su cuerpo?', 'Delfín', 'Hormiga', 'Ballena azul', 'Pulpo', 'Pulpo')," +
                "('Ciencia', 'Fácil', '¿Qué órgano del cuerpo humano se encarga de la digestión?', 'Cerebro', 'Hígado', 'Estómago', 'Riñones', 'Estómago')," +
                "('Ciencia', 'Fácil', '¿Qué órgano del cuerpo humano produce la bilis?', 'Estómago', 'Hígado', 'Páncreas', 'Vesícula', 'Hígado')," +
                "('Ciencia', 'Fácil', '¿Qué hueso es el más largo del cuerpo humano?', 'Radio', 'Húmero', 'Fémur', 'Tibia', 'Fémur')," +
                "('Ciencia', 'Fácil', '¿Qué hueso protege al cerebro?', 'Fémur', 'Cráneo', 'Costillas', 'Columna', 'Cráneo')," +
                "('Ciencia', 'Fácil', '¿Qué sentido detecta los sabores?', 'Olfato', 'Gusto', 'Tacto', 'Vista', 'Gusto')," +
                "('Ciencia', 'Fácil', '¿Qué gas necesitan las plantas para la fotosíntesis?', 'Oxígeno', 'Nitrógeno', 'Dióxido de carbono', 'Hidrógeno', 'Dióxido de carbono')," +
               
                //Medio
                "('Ciencia', 'Media', '¿Qué tipo de célula no tiene núcleo?', 'Célula animal', 'Célula vegetal', 'Célula procariota', 'Célula eucariota', 'Célula procariota')," +
                "('Ciencia', 'Media', '¿Qué científico desarrolló las leyes del movimiento?', 'Albert Einstein', 'Isaac Newton', 'Galileo Galilei', 'Stephen Hawking', 'Isaac Newton')," +
                "('Ciencia', 'Media', '¿Qué unidad se utiliza para medir la frecuencia?', 'Hertz', 'Newton', 'Amperio', 'Ohmio', 'Hertz')," +
                "('Ciencia', 'Media', '¿Qué gas se libera durante la fotosíntesis?', 'Oxígeno', 'Nitrógeno', 'Dióxido de carbono', 'Hidrógeno', 'Oxígeno')," +
                "('Ciencia', 'Media', '¿Qué partícula subatómica no tiene carga eléctrica?', 'Electrón', 'Protón', 'Neutrón', 'Quark', 'Neutrón')," +
                "('Ciencia', 'Media', '¿Qué científico es conocido como el padre de la genética?', 'Gregor Mendel', 'Charles Darwin', 'Louis Pasteur', 'Alexander Fleming', 'Gregor Mendel')," +
                "('Ciencia', 'Media', '¿Qué es el pH?', 'Una medida de densidad', 'Una medida de acidez', 'Una medida de masa', 'Una medida de longitud', 'Una medida de acidez')," +
                "('Ciencia', 'Media', '¿Qué compuesto químico se encuentra en la sal de mesa?', 'Cloruro de sodio', 'Hidróxido de sodio', 'Sulfato de potasio', 'Carbonato de calcio', 'Cloruro de sodio')," +
                "('Ciencia', 'Media', '¿Qué planeta tiene una temperatura superficial más alta?', 'Mercurio', 'Venus', 'Marte', 'Júpiter', 'Venus')," +
                "('Ciencia', 'Media', '¿Qué capa de la atmósfera contiene la capa de ozono?', 'Troposfera', 'Estratosfera', 'Mesosfera', 'Termosfera', 'Estratosfera')," +
                "('Ciencia', 'Media', '¿Qué órgano produce bilis en el cuerpo humano?', 'Riñones', 'Estómago', 'Hígado', 'Intestino delgado', 'Hígado')," +
                "('Ciencia', 'Media', '¿Qué es la velocidad de la luz en el vacío?', '300,000 km/s', '150,000 km/s', '450,000 km/s', '600,000 km/s', '300,000 km/s')," +
                "('Ciencia', 'Media', '¿Qué tipo de enlace une los átomos en una molécula de agua?', 'Enlace iónico', 'Enlace covalente', 'Enlace metálico', 'Enlace de hidrógeno', 'Enlace covalente')," +
                "('Ciencia', 'Media', '¿Qué órgano regula los niveles de azúcar en la sangre?', 'Hígado', 'Riñones', 'Páncreas', 'Estómago', 'Páncreas')," +
                "('Ciencia', 'Media', '¿Qué mineral es esencial para prevenir la anemia?', 'Calcio', 'Magnesio', 'Hierro', 'Zinc', 'Hierro')," +
                "('Ciencia', 'Media', '¿Qué se utiliza para medir la presión atmosférica?', 'Termómetro', 'Barómetro', 'Anemómetro', 'Higrómetro', 'Barómetro')," +
                "('Ciencia', 'Media', '¿Qué reino incluye a las setas y hongos?', 'Animalia', 'Plantae', 'Fungi', 'Protista', 'Fungi')," +
                "('Ciencia', 'Media', '¿Qué descubrimiento hizo Alexander Fleming?', 'Penicilina', 'Teoría de la evolución', 'Vacuna contra la rabia', 'Radiactividad', 'Penicilina')," +
                "('Ciencia', 'Media', '¿Qué energía utiliza un molino de viento?', 'Nuclear', 'Eólica', 'Solar', 'Térmica', 'Eólica')," +
                "('Ciencia', 'Media', '¿Qué molécula transporta oxígeno en la sangre?', 'Insulina', 'Hemoglobina', 'Glucosa', 'Adrenalina', 'Hemoglobina')," +
                "('Ciencia', 'Media', '¿Qué científico formuló la teoría de la evolución por selección natural?', 'Gregor Mendel', 'Charles Darwin', 'Isaac Newton', 'Albert Einstein', 'Charles Darwin')," +
                "('Ciencia', 'Media', '¿Qué planeta tiene el día más corto?', 'Marte', 'Júpiter', 'Mercurio', 'Venus', 'Júpiter')," +
                "('Ciencia', 'Media', '¿Qué tipo de radiación es responsable de causar cáncer de piel?', 'Infrarroja', 'Ultravioleta', 'Microondas', 'Radiación visible', 'Ultravioleta')," +
                "('Ciencia', 'Media', '¿Qué molécula almacena información genética?', 'Proteínas', 'Carbohidratos', 'ADN', 'Lípidos', 'ADN')," +
                "('Ciencia', 'Media', '¿Qué unidad mide la fuerza?', 'Hertz', 'Newton', 'Watt', 'Joule', 'Newton')," +
                "('Ciencia', 'Media', '¿Qué elemento tiene el símbolo químico Fe?', 'Flúor', 'Hierro', 'Francio', 'Fermio', 'Hierro')," +
                "('Ciencia', 'Media', '¿Qué gas es necesario para la combustión?', 'Nitrógeno', 'Oxígeno', 'Dióxido de carbono', 'Hidrógeno', 'Oxígeno')," +
                "('Ciencia', 'Media', '¿Qué tipo de animal es una estrella de mar?', 'Molusco', 'Equinodermo', 'Artrópodo', 'Cnidario', 'Equinodermo')," +
                "('Ciencia', 'Media', '¿Qué planeta tiene el mayor volcán del sistema solar?', 'Tierra', 'Venus', 'Marte', 'Mercurio', 'Marte')," +
                "('Ciencia', 'Media', '¿Qué tejido conecta los músculos con los huesos?', 'Cartílago', 'Ligamento', 'Tendón', 'Fascia', 'Tendón')," +
                "('Ciencia', 'Media', '¿Qué glándula produce la hormona del crecimiento?', 'Tiroides', 'Glándula suprarrenal', 'Glándula pituitaria', 'Timo', 'Glándula pituitaria')," +
                "('Ciencia', 'Media', '¿Qué tipo de roca se forma por enfriamiento de lava?', 'Roca metamórfica', 'Roca sedimentaria', 'Roca ígnea', 'Roca magmática', 'Roca ígnea')," +
                "('Ciencia', 'Media', '¿Qué instrumento mide la velocidad del viento?', 'Barómetro', 'Anemómetro', 'Higrómetro', 'Pluviómetro', 'Anemómetro')," +
                "('Ciencia', 'Media', '¿Qué ley describe la relación entre la fuerza, la masa y la aceleración?', 'Ley de la gravitación universal', 'Ley de Ohm', 'Segunda ley de Newton', 'Ley de Boyle', 'Segunda ley de Newton')," +
                "('Ciencia', 'Media', '¿Qué planeta tiene el mayor campo magnético del sistema solar?', 'Urano', 'Neptuno', 'Júpiter', 'Saturno', 'Júpiter')," +
                "('Ciencia', 'Media', '¿Qué órgano es responsable de eliminar toxinas de la sangre?', 'Riñones', 'Estómago', 'Hígado', 'Pulmones', 'Hígado')," +
                "('Ciencia', 'Media', '¿Qué glándula regula el metabolismo?', 'Glándula pituitaria', 'Páncreas', 'Tiroides', 'Timo', 'Tiroides')," +
                "('Ciencia', 'Media', '¿Qué fuerza se opone al movimiento de los objetos?', 'Gravedad', 'Fricción', 'Inercia', 'Aceleración', 'Fricción')," +
                "('Ciencia', 'Media', '¿Qué tipo de célula produce anticuerpos?', 'Glóbulos rojos', 'Plaquetas', 'Linfocitos', 'Neutrófilos', 'Linfocitos')," +
                "('Ciencia', 'Media', '¿Qué estructura celular produce energía?', 'Núcleo', 'Ribosomas', 'Mitocondria', 'Cloroplasto', 'Mitocondria')," +
                "('Ciencia', 'Media', '¿Qué metal líquido se encuentra en los termómetros antiguos?', 'Plata', 'Mercurio', 'Oro', 'Plomo', 'Mercurio')," +
                "('Ciencia', 'Media', '¿Qué fórmula representa el agua?', 'CO2', 'H2O', 'CH4', 'O2', 'H2O')," +
                "('Ciencia', 'Media', '¿Qué sistema del cuerpo humano incluye el cerebro y la médula espinal?', 'Sistema digestivo', 'Sistema nervioso', 'Sistema circulatorio', 'Sistema respiratorio', 'Sistema nervioso')," +
                "('Ciencia', 'Media', '¿Qué fenómeno natural ocurre debido al movimiento de las placas tectónicas?', 'Erupciones volcánicas', 'Auroras', 'Huracanes', 'Tsunamis', 'Erupciones volcánicas')," +
                "('Ciencia', 'Media', '¿Qué órgano en los peces les permite respirar bajo el agua?', 'Pulmones', 'Branquias', 'Páncreas', 'Aletas', 'Branquias')," +
                "('Ciencia', 'Media', '¿Qué partícula subatómica tiene carga negativa?', 'Protón', 'Neutrón', 'Electrón', 'Quark', 'Electrón'), " +

                //Difícil
                
                "('Ciencia', 'Difícil', '¿Qué físico propuso la ecuación E=mc²?', 'Isaac Newton', 'Albert Einstein', 'Marie Curie', 'Niels Bohr', 'Albert Einstein')," +
                "('Ciencia', 'Difícil', '¿Qué planeta tiene el mayor número de lunas?', 'Saturno', 'Júpiter', 'Neptuno', 'Urano', 'Saturno')," +
                "('Ciencia', 'Difícil', '¿Qué partícula subatómica descubrió J.J. Thomson?', 'Protón', 'Electrón', 'Neutrón', 'Quark', 'Electrón')," +
                "('Ciencia', 'Difícil', '¿Qué científico descubrió la penicilina?', 'Louis Pasteur', 'Alexander Fleming', 'Gregor Mendel', 'Robert Koch', 'Alexander Fleming')," +
                "('Ciencia', 'Difícil', '¿Qué tipo de enlace químico se forma entre el sodio y el cloro en la sal?', 'Covalente', 'Metálico', 'Iónico', 'De hidrógeno', 'Iónico')," +
                "('Ciencia', 'Difícil', '¿Qué fuerza actúa sobre los objetos en caída libre?', 'Fricción', 'Gravedad', 'Electromagnética', 'Centrífuga', 'Gravedad')," +
                "('Ciencia', 'Difícil', '¿Qué elemento tiene el número atómico 79?', 'Oro', 'Mercurio', 'Plomo', 'Plata', 'Oro')," +
                "('Ciencia', 'Difícil', '¿Qué ley explica cómo se expanden los gases?', 'Ley de Boyle', 'Ley de Charles', 'Ley de Avogadro', 'Ley de Dalton', 'Ley de Charles')," +
                "('Ciencia', 'Difícil', '¿Qué órgano controla la homeostasis en el cuerpo humano?', 'Cerebro', 'Riñones', 'Corazón', 'Hígado', 'Cerebro')," +
                "('Ciencia', 'Difícil', '¿Qué tipo de estrella es el Sol?', 'Gigante roja', 'Enana blanca', 'Estrella de neutrones', 'Enana amarilla', 'Enana amarilla')," +
                "('Ciencia', 'Difícil', '¿Qué científico descubrió la teoría de la radiactividad?', 'Marie Curie', 'Henri Becquerel', 'Enrico Fermi', 'Ernest Rutherford', 'Henri Becquerel')," +
                "('Ciencia', 'Difícil', '¿Qué proteína transporta oxígeno en la sangre?', 'Colágeno', 'Hemoglobina', 'Queratina', 'Insulina', 'Hemoglobina')," +
                "('Ciencia', 'Difícil', '¿Qué compuesto es conocido como agua pesada?', 'H2O2', 'D2O', 'H2O', 'O2H', 'D2O')," +
                "('Ciencia', 'Difícil', '¿Qué mineral tiene una dureza de 10 en la escala de Mohs?', 'Cuarzo', 'Diamante', 'Rubí', 'Esmeralda', 'Diamante')," +
                "('Ciencia', 'Difícil', '¿Qué proceso convierte el nitrógeno atmosférico en una forma utilizable para las plantas?', 'Fotosíntesis', 'Fijación de nitrógeno', 'Respiración', 'Fermentación', 'Fijación de nitrógeno')," +
                "('Ciencia', 'Difícil', '¿Qué órgano del cuerpo produce glóbulos rojos?', 'Riñones', 'Hígado', 'Médula ósea', 'Pulmones', 'Médula ósea')," +
                "('Ciencia', 'Difícil', '¿Qué físico desarrolló el principio de incertidumbre?', 'Niels Bohr', 'Werner Heisenberg', 'Max Planck', 'Albert Einstein', 'Werner Heisenberg')," +
                "('Ciencia', 'Difícil', '¿Qué planeta tiene una inclinación axial extrema?', 'Saturno', 'Urano', 'Neptuno', 'Júpiter', 'Urano')," +
                "('Ciencia', 'Difícil', '¿Qué partícula es responsable de la interacción nuclear fuerte?', 'Fotón', 'Bosón Z', 'Gluón', 'Neutrino', 'Gluón')," +
                "('Ciencia', 'Difícil', '¿Qué gas es el principal componente del aire exhalado?', 'Oxígeno', 'Nitrógeno', 'Dióxido de carbono', 'Hidrógeno', 'Dióxido de carbono')," +
                "('Ciencia', 'Difícil', '¿Qué teoría unifica la electricidad y el magnetismo?', 'Relatividad', 'Termodinámica', 'Electromagnetismo', 'Mecánica cuántica', 'Electromagnetismo')," +
                "('Ciencia', 'Difícil', '¿Qué elemento químico tiene el símbolo \"W\"?', 'Tungsteno', 'Wolframio', 'Plomo', 'Cromo', 'Tungsteno')," +
                "('Ciencia', 'Difícil', '¿Qué tipo de célula nerviosa transmite impulsos eléctricos?', 'Axón', 'Dendrita', 'Neurona', 'Célula glial', 'Neurona')," +
                "('Ciencia', 'Difícil', '¿Qué técnica permite replicar ADN?', 'Transcripción', 'PCR', 'Traducción', 'Metilación', 'PCR')," +
                "('Ciencia', 'Difícil', '¿Qué tipo de energía produce el núcleo de un átomo?', 'Química', 'Térmica', 'Nuclear', 'Eléctrica', 'Nuclear')," +
                "('Ciencia', 'Difícil', '¿Qué científico descubrió las leyes de la herencia?', 'Charles Darwin', 'Gregor Mendel', 'Louis Pasteur', 'Alexander Fleming', 'Gregor Mendel')," +
                "('Ciencia', 'Difícil', '¿Qué fenómeno astronómico ocurre cuando la Luna pasa por la sombra de la Tierra?', 'Eclipse solar', 'Eclipse lunar', 'Marea alta', 'Aurora', 'Eclipse lunar')," +
                "('Ciencia', 'Difícil', '¿Qué unidad mide la energía?', 'Joule', 'Watt', 'Newton', 'Hertz', 'Joule')," +
                "('Ciencia', 'Difícil', '¿Qué fuerza causa que los planetas se mantengan en órbita?', 'Inercia', 'Gravedad', 'Fricción', 'Electromagnética', 'Gravedad')," +
                "('Ciencia', 'Difícil', '¿Qué órgano del cuerpo humano controla la temperatura?', 'Piel', 'Corazón', 'Cerebro', 'Riñones', 'Cerebro')," +
                "('Ciencia', 'Difícil', '¿Qué científicos propusieron el modelo helicoidal del ADN?', 'Watson y Crick', 'Newton y Leibniz', 'Einstein y Bohr', 'Darwin y Mendel', 'Watson y Crick')," +
                "('Ciencia', 'Difícil', '¿Qué dispositivo mide la intensidad de la luz?', 'Termómetro', 'Fotómetro', 'Espectrómetro', 'Barómetro', 'Fotómetro')," +
                "('Ciencia', 'Difícil', '¿Qué proceso convierte la glucosa en energía utilizable en las células?', 'Fotosíntesis', 'Respiración celular', 'Fermentación', 'Gluconeogénesis', 'Respiración celular')," +
                "('Ciencia', 'Difícil', '¿Qué galaxia se encuentra más cerca de la Vía Láctea?', 'Andrómeda', 'Magallanes', 'Triangulum', 'Sagitario', 'Andrómeda')," +
                "('Ciencia', 'Difícil', '¿Qué científico propuso las leyes de la termodinámica?', 'Isaac Newton', 'Rudolf Clausius', 'James Watt', 'Albert Einstein', 'Rudolf Clausius')," +
                "('Ciencia', 'Difícil', '¿Qué fenómeno describe la expansión acelerada del universo?', 'Inflación', 'Big Bang', 'Energía oscura', 'Materia oscura', 'Energía oscura')," +
                "('Ciencia', 'Difícil', '¿Qué estructura celular contiene clorofila?', 'Mitocondria', 'Cloroplasto', 'Núcleo', 'Ribosomas', 'Cloroplasto')," +
                "('Ciencia', 'Difícil', '¿Qué científico desarrolló el modelo atómico que incluye niveles de energía?', 'Rutherford', 'Niels Bohr', 'Heisenberg', 'Schrödinger', 'Niels Bohr')," +
                "('Ciencia', 'Difícil', '¿Qué órgano regula el equilibrio hídrico del cuerpo humano?', 'Riñones', 'Hígado', 'Pulmones', 'Estómago', 'Riñones')," +
                "('Ciencia', 'Difícil', '¿Qué descubrimiento científico permitió los rayos X?', 'Radiactividad', 'Luz ultravioleta', 'Radiación electromagnética', 'Difracción de electrones', 'Radiación electromagnética')," +
                "('Ciencia', 'Difícil', '¿Qué científico desarrolló el concepto de los orbitales atómicos?', 'Heisenberg', 'Schrödinger', 'Bohr', 'Einstein', 'Schrödinger')," +
                "('Ciencia', 'Difícil', '¿Qué tipo de roca contiene fósiles?', 'Ígnea', 'Sedimentaria', 'Metamórfica', 'Pétrea', 'Sedimentaria')," +
                "('Ciencia', 'Difícil', '¿Qué unidad mide el tiempo de semidesintegración de un elemento radiactivo?', 'Segundo', 'Minuto', 'Año', 'Curie', 'Segundo') ";



        db.execSQL(insertCiencia);

        // Insertar preguntas en la tabla Geografia
        String insertGeografia = "INSERT INTO Geografia (" +
                                 COLUMN_TEMA + ", " +
                                 COLUMN_DIFICULTAD + ", " +
                                 COLUMN_PREGUNTA + ", " +
                                 COLUMN_OPCION1 + ", " +
                                 COLUMN_OPCION2 + ", " +
                                 COLUMN_OPCION3 + ", " +
                                 COLUMN_OPCION4 + ", " +
                                 COLUMN_RESPUESTA_CORRECTA + ") VALUES " +
                                 "('Geografía', 'Fácil', '¿Cuál es el continente más grande del mundo?', 'África', 'Asia', 'Europa', 'América del Norte', 'Asia')," +
                                 "('Geografía', 'Fácil', '¿Cuál es el río más largo del mundo?', 'Río Nilo', 'Río Amazonas', 'Río Yangtsé', 'Río Misisipi', 'Río Amazonas')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene la Torre Eiffel como símbolo?', 'Italia', 'Alemania', 'Francia', 'España', 'Francia')," +
                                 "('Geografía', 'Fácil', '¿En qué continente se encuentra el desierto del Sahara?', 'Asia', 'América del Sur', 'África', 'Australia', 'África')," +
                                 "('Geografía', 'Fácil', '¿Cuál es el océano más grande del mundo?', 'Océano Atlántico', 'Océano Índico', 'Océano Pacífico', 'Océano Ártico', 'Océano Pacífico')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene la forma de una bota?', 'España', 'Francia', 'Italia', 'Grecia', 'Italia')," +
                                 "('Geografía', 'Fácil', '¿Cuál es la capital de Japón?', 'Seúl', 'Tokio', 'Pekín', 'Osaka', 'Tokio')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene el monte Everest?', 'India', 'Nepal', 'China', 'Pakistán', 'Nepal')," +
                                 "('Geografía', 'Fácil', '¿Qué isla es conocida como la tierra de los canguros?', 'Madagascar', 'Australia', 'Nueva Zelanda', 'Tasmania', 'Australia')," +
                                 "('Geografía', 'Fácil', '¿En qué país está la Gran Muralla?', 'Japón', 'Corea del Norte', 'China', 'Mongolia', 'China')," +
                                 "('Geografía', 'Fácil', '¿Cuál es la capital de España?', 'Barcelona', 'Madrid', 'Valencia', 'Sevilla', 'Madrid')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene la ciudad de Río de Janeiro?', 'Argentina', 'Chile', 'Brasil', 'Perú', 'Brasil')," +
                                 "('Geografía', 'Fácil', '¿Qué país es famoso por sus tulipanes y molinos?', 'Alemania', 'Bélgica', 'Países Bajos', 'Dinamarca', 'Países Bajos')," +
                                 "('Geografía', 'Fácil', '¿En qué continente está México?', 'América del Sur', 'América del Norte', 'Europa', 'África', 'América del Norte')," +
                                 "('Geografía', 'Fácil', '¿Qué océano rodea las islas de Hawái?', 'Océano Índico', 'Océano Atlántico', 'Océano Pacífico', 'Océano Ártico', 'Océano Pacífico')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene la mayor cantidad de habitantes?', 'Estados Unidos', 'India', 'China', 'Indonesia', 'China')," +
                                 "('Geografía', 'Fácil', '¿Qué continente tiene la mayor cantidad de países?', 'África', 'Asia', 'Europa', 'América del Sur', 'África')," +
                                 "('Geografía', 'Fácil', '¿Cuál es la capital de Argentina?', 'Santiago', 'Montevideo', 'Lima', 'Buenos Aires', 'Buenos Aires')," +
                                 "('Geografía', 'Fácil', '¿Qué desierto es el más frío del mundo?', 'Desierto del Sahara', 'Desierto de Gobi', 'Antártida', 'Atacama', 'Antártida')," +
                                 "('Geografía', 'Fácil', '¿Qué río pasa por Egipto?', 'Río Amazonas', 'Río Yangtsé', 'Río Misisipi', 'Río Nilo', 'Río Nilo')," +
                                 "('Geografía', 'Fácil', '¿Cuál es el país más pequeño del mundo?', 'San Marino', 'Mónaco', 'El Vaticano', 'Liechtenstein', 'El Vaticano')," +
                                 "('Geografía', 'Fácil', '¿Qué cordillera atraviesa Sudamérica?', 'Montes Urales', 'Montañas Rocosas', 'Andes', 'Himalayas', 'Andes')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene el Gran Cañón?', 'México', 'Canadá', 'Estados Unidos', 'Argentina', 'Estados Unidos')," +
                                 "('Geografía', 'Fácil', '¿Qué continente tiene los canguros como animales nativos?', 'África', 'Asia', 'América del Norte', 'Australia', 'Australia')," +
                                 "('Geografía', 'Fácil', '¿Qué continente tiene la mayor selva tropical?', 'América del Sur', 'África', 'Asia', 'Oceanía', 'América del Sur')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene la ciudad de Estambul?', 'Grecia', 'Turquía', 'Egipto', 'Israel', 'Turquía')," +
                                 "('Geografía', 'Fácil', '¿Qué capital está más al norte?', 'Reikiavik', 'Helsinki', 'Oslo', 'Moscú', 'Reikiavik')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene las cataratas del Niágara?', 'Canadá', 'Estados Unidos', 'Ambos', 'México', 'Ambos')," +
                                 "('Geografía', 'Fácil', '¿Cuál es el lago más grande de América del Sur?', 'Lago Titicaca', 'Lago Maracaibo', 'Lago de Chapala', 'Lago Poopó', 'Lago Titicaca')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene el glaciar Perito Moreno?', 'Chile', 'Argentina', 'Perú', 'Bolivia', 'Argentina')," +
                                 "('Geografía', 'Fácil', '¿Qué archipiélago pertenece a Ecuador?', 'Islas Malvinas', 'Islas Galápagos', 'Islas Canarias', 'Islas Cook', 'Islas Galápagos')," +
                                 "('Geografía', 'Fácil', '¿Qué continente tiene el mayor desierto cálido del mundo?', 'Oceanía', 'Asia', 'África', 'América del Sur', 'África')," +
                                 "('Geografía', 'Fácil', '¿Cuál es la capital de Alemania?', 'Múnich', 'Frankfurt', 'Berlín', 'Colonia', 'Berlín')," +
                                 "('Geografía', 'Fácil', '¿En qué país está la Basílica de San Pedro?', 'España', 'Italia', 'El Vaticano', 'Francia', 'El Vaticano')," +
                                 "('Geografía', 'Fácil', '¿Qué océano rodea Australia?', 'Atlántico', 'Pacífico', 'Índico', 'Ártico', 'Índico')," +
                                 "('Geografía', 'Fácil', '¿Qué país tiene forma de hoja de arce en su bandera?', 'Canadá', 'Australia', 'Dinamarca', 'Noruega', 'Canadá')," +
                                 "('Geografía', 'Fácil', '¿Qué ciudad está dividida por el río Támesis?', 'París', 'Londres', 'Ámsterdam', 'Bruselas', 'Londres')," +
                                 "('Geografía', 'Fácil', '¿Cuál es el punto más alto de África?', 'Monte Everest', 'Monte Kilimanjaro', 'Montes Drakensberg', 'Monte Camerún', 'Monte Kilimanjaro')," +
                                 "('Geografía', 'Fácil', '¿Cuál es la capital de Brasil?', 'São Paulo', 'Brasilia', 'Río de Janeiro', 'Salvador', 'Brasilia')," +
                                 "('Geografía', 'Fácil', '¿Qué país está rodeado completamente por Sudáfrica?', 'Lesoto', 'Botsuana', 'Namibia', 'Eswatini', 'Lesoto')," +
                                 "('Geografía', 'Fácil', '¿Qué país está al sur de los Estados Unidos?', 'México', 'Canadá', 'Cuba', 'Guatemala', 'México')," +
                                 "('Geografía', 'Fácil', '¿Qué mar separa Europa de África?', 'Mar Mediterráneo', 'Mar Caribe', 'Mar Rojo', 'Mar de Arabia', 'Mar Mediterráneo')," +
                                 "('Geografía', 'Fácil', '¿Qué capital está más al sur?', 'Canberra', 'Wellington', 'Pretoria', 'Buenos Aires', 'Wellington')," +
                                 "('Geografía', 'Fácil', '¿Qué continente es conocido como la cuna de la humanidad?', 'Asia', 'África', 'Europa', 'Oceanía', 'África')," +
                                 "('Geografía', 'Fácil', '¿Qué isla italiana es famosa por su forma triangular?', 'Córcega', 'Sicilia', 'Cerdeña', 'Elba', 'Sicilia')," +
                                 "('Geografía', 'Fácil', '¿Qué río forma parte de las Cataratas del Iguazú?', 'Río Paraguay', 'Río Paraná', 'Río Uruguay', 'Río Amazonas', 'Río Paraná')," +
                                 "('Geografía', 'Media', '¿Qué cordillera es la más larga del mundo?', 'Montañas Rocosas', 'Andes', 'Himalayas', 'Alpes', 'Andes')," +
                                "('Geografía', 'Media', '¿En qué país se encuentra el desierto de Atacama?', 'Chile', 'Argentina', 'Perú', 'Bolivia', 'Chile')," +
                                "('Geografía', 'Media', '¿Qué lago es conocido como el más profundo del mundo?', 'Lago Victoria', 'Lago Superior', 'Lago Baikal', 'Lago Titicaca', 'Lago Baikal')," +
                                "('Geografía', 'Media', '¿Qué país tiene la mayor línea costera del mundo?', 'Australia', 'Rusia', 'Canadá', 'Estados Unidos', 'Canadá')," +
                                "('Geografía', 'Media', '¿Qué montaña es la más alta fuera del Himalaya?', 'Monte Kilimanjaro', 'Monte Aconcagua', 'Denali', 'Monte McKinley', 'Monte Aconcagua')," +
                                "('Geografía', 'Media', '¿Qué estrecho conecta el Mar Mediterráneo con el Océano Atlántico?', 'Estrecho de Magallanes', 'Estrecho de Gibraltar', 'Estrecho de Bering', 'Estrecho de Malaca', 'Estrecho de Gibraltar')," +
                                "('Geografía', 'Media', '¿En qué país se encuentra la región de Transilvania?', 'Hungría', 'Rumania', 'Bulgaria', 'Serbia', 'Rumania')," +
                                "('Geografía', 'Media', '¿Qué país europeo tiene más volcanes activos?', 'Grecia', 'Italia', 'Islandia', 'España', 'Italia')," +
                                "('Geografía', 'Media', '¿Qué río fluye a través de París?', 'Río Danubio', 'Río Rin', 'Río Sena', 'Río Elba', 'Río Sena')," +
                                "('Geografía', 'Media', '¿Qué país tiene la ciudad de Dubái?', 'Arabia Saudita', 'Qatar', 'Emiratos Árabes Unidos', 'Omán', 'Emiratos Árabes Unidos')," +
                                "('Geografía', 'Media', '¿Qué país es conocido como la tierra de los mil lagos?', 'Noruega', 'Suecia', 'Finlandia', 'Dinamarca', 'Finlandia')," +
                                "('Geografía', 'Media', '¿Qué país conecta Europa y Asia?', 'Grecia', 'Turquía', 'Rusia', 'Kazajistán', 'Turquía')," +
                                "('Geografía', 'Media', '¿Qué río es conocido como el Danubio Azul?', 'Río Danubio', 'Río Sena', 'Río Rin', 'Río Po', 'Río Danubio')," +
                                "('Geografía', 'Media', '¿Qué mar es conocido por su alta salinidad?', 'Mar Mediterráneo', 'Mar Rojo', 'Mar Caspio', 'Mar Muerto', 'Mar Muerto')," +
                                "('Geografía', 'Media', '¿En qué país se encuentra el Monte Fuji?', 'China', 'Japón', 'Corea del Sur', 'Filipinas', 'Japón')," +
                                "('Geografía', 'Media', '¿Qué desierto cubre gran parte de Mongolia?', 'Desierto del Sahara', 'Desierto de Gobi', 'Desierto de Kalahari', 'Desierto de Atacama', 'Desierto de Gobi')," +
                                "('Geografía', 'Media', '¿Qué país tiene la ciudad de Marrakech?', 'Argelia', 'Túnez', 'Marruecos', 'Egipto', 'Marruecos')," +
                                "('Geografía', 'Media', '¿Qué río forma parte de la frontera entre México y Estados Unidos?', 'Río Bravo', 'Río Colorado', 'Río Usumacinta', 'Río Suchiate', 'Río Bravo')," +
                                "('Geografía', 'Media', '¿Qué ciudad es conocida como la \"Venecia del Norte\"?', 'Estocolmo', 'Copenhague', 'San Petersburgo', 'Ámsterdam', 'San Petersburgo')," +
                                "('Geografía', 'Media', '¿Qué país tiene la ciudad de Kuala Lumpur?', 'Tailandia', 'Indonesia', 'Malasia', 'Vietnam', 'Malasia')," +
                                "('Geografía', 'Media', '¿Qué volcán destruyó la ciudad de Pompeya?', 'Etna', 'Vesubio', 'Stromboli', 'Krakatoa', 'Vesubio')," +
                                "('Geografía', 'Media', '¿Qué país tiene la región de la Toscana?', 'España', 'Francia', 'Italia', 'Grecia', 'Italia')," +
                                "('Geografía', 'Media', '¿Qué cordillera separa Europa de Asia?', 'Montes Urales', 'Himalayas', 'Alpes', 'Andes', 'Montes Urales')," +
                                "('Geografía', 'Media', '¿Qué mar se encuentra al norte de Turquía?', 'Mar Mediterráneo', 'Mar Negro', 'Mar Caspio', 'Mar de Arabia', 'Mar Negro')," +
                                "('Geografía', 'Media', '¿Qué continente es conocido como la \"tierra de los canguros\"?', 'Asia', 'África', 'Oceanía', 'América del Sur', 'Oceanía')," +
                                "('Geografía', 'Media', '¿Qué país tiene como capital a Helsinki?', 'Noruega', 'Suecia', 'Finlandia', 'Dinamarca', 'Finlandia')," +
                                "('Geografía', 'Media', '¿Qué río cruza el Amazonas?', 'Río Negro', 'Río Solimões', 'Río Marañón', 'Río Amazonas', 'Río Amazonas')," +
                                "('Geografía', 'Media', '¿Qué país limita con Uruguay al oeste?', 'Brasil', 'Argentina', 'Paraguay', 'Chile', 'Argentina')," +
                                "('Geografía', 'Media', '¿Qué país tiene la ciudad de Cartagena?', 'Chile', 'Colombia', 'España', 'México', 'Colombia')," +
                                "('Geografía', 'Media', '¿Qué región española tiene Sevilla como capital?', 'Cataluña', 'Andalucía', 'Galicia', 'Valencia', 'Andalucía')," +
                                "('Geografía', 'Media', '¿Qué ciudad es conocida como la \"Perla del Pacífico\"?', 'Lima', 'Valparaíso', 'Guayaquil', 'Acapulco', 'Guayaquil')," +
                                "('Geografía', 'Media', '¿Qué país africano tiene forma de cuerno?', 'Somalia', 'Etiopía', 'Kenia', 'Sudán', 'Somalia')," +
                                "('Geografía', 'Media', '¿Qué cadena montañosa atraviesa Suiza?', 'Alpes', 'Andes', 'Himalayas', 'Montes Cárpatos', 'Alpes')," +
                                "('Geografía', 'Media', '¿Qué país tiene la ciudad de Seúl como capital?', 'China', 'Japón', 'Corea del Sur', 'Vietnam', 'Corea del Sur')," +
                                "('Geografía', 'Media', '¿Qué isla en el Caribe es compartida por Haití y República Dominicana?', 'Cuba', 'Puerto Rico', 'Jamaica', 'La Española', 'La Española')," +
                                "('Geografía', 'Media', '¿Qué país es famoso por su Ruta de la Seda?', 'Irán', 'China', 'Turquía', 'Uzbekistán', 'China')," +
                                "('Geografía', 'Media', '¿Qué isla es conocida por las estatuas Moái?', 'Isla de Pascua', 'Islas Galápagos', 'Isla Fiji', 'Isla Córcega', 'Isla de Pascua')," +
                                "('Geografía', 'Media', '¿Qué ciudad es conocida como \"La Gran Manzana\"?', 'Chicago', 'Nueva York', 'Los Ángeles', 'Miami', 'Nueva York')," +
                                "('Geografía', 'Media', '¿Qué país europeo tiene forma de lágrima?', 'Islandia', 'Italia', 'Chipre', 'Sri Lanka', 'Chipre')," +
                                "('Geografía', 'Media', '¿Qué río cruza el Parque Nacional Kruger en Sudáfrica?', 'Río Limpopo', 'Río Zambeze', 'Río Orange', 'Río Vaal', 'Río Limpopo')," +
                                "('Geografía', 'Media', '¿Qué ciudad europea tiene la Sagrada Familia?', 'Madrid', 'Barcelona', 'Valencia', 'Bilbao', 'Barcelona')," +
                                "('Geografía', 'Media', '¿Qué país limita con Afganistán al este?', 'Irán', 'India', 'China', 'Pakistán', 'Pakistán')," +
                                "('Geografía', 'Media', '¿Qué continente tiene la mayor cantidad de selvas tropicales?', 'América del Sur', 'África', 'Asia', 'Oceanía', 'América del Sur')," +
                                "('Geografía', 'Media', '¿Qué ciudad italiana está rodeada de canales?', 'Milán', 'Venecia', 'Roma', 'Florencia', 'Venecia')," +
                                "('Geografía', 'Media', '¿Qué país europeo tiene el río Rin?', 'Francia', 'Alemania', 'Suiza', 'Países Bajos', 'Alemania')," +
                                "('Geografía', 'Media', '¿Qué país tiene el Gran Desierto Arenoso?', 'Sudán', 'Australia', 'Arabia Saudita', 'Argelia', 'Australia')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el mayor número de islas en el mundo?', 'Indonesia', 'Noruega', 'Suecia', 'Filipinas', 'Suecia')," +
                                "('Geografía', 'Difícil', '¿Qué río forma la frontera entre Alemania y Polonia?', 'Río Elba', 'Río Danubio', 'Río Oder', 'Río Rin', 'Río Oder')," +
                                "('Geografía', 'Difícil', '¿Cuál es el país más pequeño de África continental?', 'Lesoto', 'Gambia', 'Esuatini', 'Seychelles', 'Gambia')," +
                                "('Geografía', 'Difícil', '¿Qué país es el mayor exportador de café del mundo?', 'Colombia', 'Vietnam', 'Brasil', 'Etiopía', 'Brasil')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el mayor lago de agua dulce del mundo?', 'Canadá', 'Rusia', 'Estados Unidos', 'Tanzania', 'Rusia')," +
                                "('Geografía', 'Difícil', '¿Qué océano es el más profundo?', 'Océano Atlántico', 'Océano Pacífico', 'Océano Índico', 'Océano Ártico', 'Océano Pacífico')," +
                                "('Geografía', 'Difícil', '¿Qué desierto tiene las temperaturas más altas registradas en la Tierra?', 'Desierto del Sahara', 'Desierto de Lut', 'Desierto de Atacama', 'Desierto de Kalahari', 'Desierto de Lut')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el lago más salado del mundo?', 'Jordania', 'Israel', 'China', 'Estados Unidos', 'Israel')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene la cordillera de los Montes Apalaches?', 'Canadá', 'México', 'Estados Unidos', 'Argentina', 'Estados Unidos')," +
                                "('Geografía', 'Difícil', '¿Qué ciudad tiene el puerto natural más grande del mundo?', 'Hong Kong', 'Río de Janeiro', 'Sídney', 'Singapur', 'Sídney')," +
                                "('Geografía', 'Difícil', '¿Qué río desemboca en el Mar Caspio?', 'Río Volga', 'Río Don', 'Río Ural', 'Río Dniéper', 'Río Volga')," +
                                "('Geografía', 'Difícil', '¿Qué isla es la segunda más grande del mundo por superficie?', 'Nueva Guinea', 'Borneo', 'Madagascar', 'Groenlandia', 'Nueva Guinea')," +
                                "('Geografía', 'Difícil', '¿Qué cordillera se encuentra entre Francia y España?', 'Alpes', 'Cárpatos', 'Pirineos', 'Apeninos', 'Pirineos')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el salar más grande del mundo?', 'Chile', 'Bolivia', 'Argentina', 'Perú', 'Bolivia')," +
                                "('Geografía', 'Difícil', '¿Qué estrecho separa Asia de América del Norte?', 'Estrecho de Magallanes', 'Estrecho de Bering', 'Estrecho de Gibraltar', 'Estrecho de Malaca', 'Estrecho de Bering')," +
                                "('Geografía', 'Difícil', '¿Qué ciudad es conocida como la \"ciudad de los vientos\"?', 'Nueva York', 'Toronto', 'Chicago', 'Boston', 'Chicago')," +
                                "('Geografía', 'Difícil', '¿Qué país es el más montañoso del mundo?', 'Nepal', 'Bhután', 'Suiza', 'Tíbet', 'Nepal')," +
                                "('Geografía', 'Difícil', '¿Qué lago se encuentra en el cráter de un volcán extinto en Estados Unidos?', 'Lago Tahoe', 'Lago Crater', 'Lago Michigan', 'Lago Salado', 'Lago Crater')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene las montañas de los Cárpatos?', 'Eslovaquia', 'Rumania', 'Polonia', 'Hungría', 'Rumania')," +
                                "('Geografía', 'Difícil', '¿Qué río atraviesa más países en el mundo?', 'Río Amazonas', 'Río Danubio', 'Río Nilo', 'Río Yangtsé', 'Río Danubio')," +
                                "('Geografía', 'Difícil', '¿Qué desierto es conocido como el más seco del mundo?', 'Desierto del Sahara', 'Desierto de Atacama', 'Desierto de Gobi', 'Desierto del Kalahari', 'Desierto de Atacama')," +
                                "('Geografía', 'Difícil', '¿Qué archipiélago en el Océano Índico es un paraíso turístico?', 'Islas Maldivas', 'Islas Canarias', 'Islas Galápagos', 'Islas Feroe', 'Islas Maldivas')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el segundo idioma más hablado en el mundo?', 'India', 'Estados Unidos', 'China', 'México', 'India')," +
                                "('Geografía', 'Difícil', '¿Qué ciudad es la capital más alta del mundo?', 'Katmandú', 'Quito', 'La Paz', 'Lhasa', 'La Paz')," +
                                "('Geografía', 'Difícil', '¿Qué río es el más caudaloso del mundo?', 'Río Amazonas', 'Río Yangtsé', 'Río Nilo', 'Río Congo', 'Río Amazonas')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el lago Titicaca?', 'Perú y Bolivia', 'Argentina y Chile', 'Brasil y Paraguay', 'Colombia y Ecuador', 'Perú y Bolivia')," +
                                "('Geografía', 'Difícil', '¿Qué océano rodea Groenlandia?', 'Océano Ártico', 'Océano Atlántico', 'Océano Pacífico', 'Océano Índico', 'Océano Ártico')," +
                                "('Geografía', 'Difícil', '¿Qué isla caribeña es famosa por su forma de hoja?', 'Jamaica', 'Barbados', 'Cuba', 'Puerto Rico', 'Cuba')," +
                                "('Geografía', 'Difícil', '¿Qué cordillera se extiende por Marruecos?', 'Montañas Ruwenzori', 'Montañas Atlas', 'Montañas Zagros', 'Montañas Rif', 'Montañas Atlas')," +
                                "('Geografía', 'Difícil', '¿Qué ciudad se encuentra en dos continentes?', 'Estambul', 'Moscú', 'Nueva Delhi', 'San Petersburgo', 'Estambul')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el mayor archipiélago del mundo?', 'Indonesia', 'Filipinas', 'Noruega', 'Malasia', 'Indonesia')," +
                                "('Geografía', 'Difícil', '¿Qué país limita con 14 naciones diferentes?', 'India', 'China', 'Rusia', 'Brasil', 'China')," +
                                "('Geografía', 'Difícil', '¿Qué isla mediterránea es la más grande?', 'Córcega', 'Sicilia', 'Creta', 'Malta', 'Sicilia')," +
                                "('Geografía', 'Difícil', '¿Qué país es famoso por la cordillera de los Montes Urales?', 'Rusia', 'Kazajistán', 'Bielorrusia', 'Ucrania', 'Rusia')," +
                                "('Geografía', 'Difícil', '¿Qué lago tiene una alta concentración de sal y no permite vida acuática?', 'Lago Superior', 'Mar Muerto', 'Lago Salado', 'Lago Baikal', 'Mar Muerto')," +
                                "('Geografía', 'Difícil', '¿Qué volcán activo es el más alto del mundo?', 'Monte Kilimanjaro', 'Monte Aconcagua', 'Monte Cotopaxi', 'Monte Ojos del Salado', 'Monte Ojos del Salado')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene las Llanuras del Serengeti?', 'Sudáfrica', 'Tanzania', 'Zimbabue', 'Botsuana', 'Tanzania')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el glaciar más grande del mundo?', 'Canadá', 'Argentina', 'Groenlandia', 'Islandia', 'Groenlandia')," +
                                "('Geografía', 'Difícil', '¿Qué mar conecta el Mar Rojo con el Océano Índico?', 'Mar Caspio', 'Mar de Omán', 'Mar Arábigo', 'Golfo de Adén', 'Golfo de Adén')," +
                                "('Geografía', 'Difícil', '¿Qué región autónoma está rodeada por Sudáfrica?', 'Lesoto', 'Botsuana', 'Esuatini', 'Namibia', 'Lesoto')," +
                                "('Geografía', 'Difícil', '¿Qué península es conocida como la más grande del mundo?', 'Península del Sinaí', 'Península Arábiga', 'Península de Yucatán', 'Península de Crimea', 'Península Arábiga')," +
                                "('Geografía', 'Difícil', '¿Qué continente tiene el mayor número de zonas horarias?', 'Asia', 'Europa', 'África', 'Antártida', 'Antártida')," +
                                "('Geografía', 'Difícil', '¿Qué país tiene el mayor sistema de ríos del mundo?', 'Brasil', 'Estados Unidos', 'Rusia', 'India', 'Brasil')," +
                                "('Geografía', 'Difícil', '¿Qué país es conocido como el \"techo del mundo\"?', 'Nepal', 'India', 'Bhután', 'Tíbet', 'Tíbet') ";
                

                 
        db.execSQL(insertGeografia);

        // Insertar preguntas en la tabla Cine
        String insertCine = "INSERT INTO Cine (" +
                            COLUMN_TEMA + ", " +
                            COLUMN_DIFICULTAD + ", " +
                            COLUMN_PREGUNTA + ", " +
                            COLUMN_OPCION1 + ", " +
                            COLUMN_OPCION2 + ", " +
                            COLUMN_OPCION3 + ", " +
                            COLUMN_OPCION4 + ", " +
                            COLUMN_RESPUESTA_CORRECTA + ") VALUES " +
                            "('Cine', 'Fácil', '¿Quién dirigió la película \"Titanic\"?', 'Steven Spielberg', 'James Cameron', 'Christopher Nolan', 'Martin Scorsese', 'James Cameron')," +
                            "('Cine', 'Fácil', '¿Qué actor interpreta a Jack en \"Titanic\"?', 'Leonardo DiCaprio', 'Brad Pitt', 'Matt Damon', 'Tom Cruise', 'Leonardo DiCaprio')," +
                            "('Cine', 'Fácil', '¿Qué película presenta la frase \"Que la fuerza te acompañe\"?', 'Harry Potter', 'El Señor de los Anillos', 'Star Wars', 'Matrix', 'Star Wars')," +
                            "('Cine', 'Fácil', '¿Qué película animada tiene como protagonista a un pez llamado Nemo?', 'Toy Story', 'Buscando a Nemo', 'Shrek', 'Frozen', 'Buscando a Nemo')," +
                            "('Cine', 'Fácil', '¿Qué película ganó el Oscar a Mejor Película en 1994?', 'Titanic', 'Forrest Gump', 'Pulp Fiction', 'El Rey León', 'Forrest Gump')," +
                            "('Cine', 'Fácil', '¿Qué actor interpreta a Iron Man en el Universo Cinematográfico de Marvel?', 'Chris Evans', 'Robert Downey Jr.', 'Chris Hemsworth', 'Mark Ruffalo', 'Robert Downey Jr.')," +
                            "('Cine', 'Fácil', '¿En qué película escuchamos la frase \"Volveré\"?', 'Terminator', 'Matrix', 'Robocop', 'Alien', 'Terminator')," +
                            "('Cine', 'Fácil', '¿Quién es el villano principal en \"El Rey León\"?', 'Mufasa', 'Simba', 'Scar', 'Timon', 'Scar')," +
                            "('Cine', 'Fácil', '¿Qué película animada tiene como protagonista a una princesa llamada Elsa?', 'Enredados', 'Frozen', 'Cenicienta', 'Blancanieves', 'Frozen')," +
                            "('Cine', 'Fácil', '¿Qué película presenta a un ogro llamado Shrek?', 'Madagascar', 'Shrek', 'Kung Fu Panda', 'Cómo entrenar a tu dragón', 'Shrek')," +
                            "('Cine', 'Fácil', '¿Qué actor protagonizó la película \"Misión Imposible\"?', 'Matt Damon', 'Keanu Reeves', 'Tom Cruise', 'Bruce Willis', 'Tom Cruise')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de juguetes que cobran vida?', 'Los Increíbles', 'Toy Story', 'Monstruos, Inc.', 'Cars', 'Toy Story')," +
                            "('Cine', 'Fácil', '¿Qué superhéroe es conocido como \"El Caballero de la Noche\"?', 'Superman', 'Spiderman', 'Batman', 'Flash', 'Batman')," +
                            "('Cine', 'Fácil', '¿Qué actor interpreta al Capitán América?', 'Chris Evans', 'Chris Hemsworth', 'Robert Downey Jr.', 'Mark Ruffalo', 'Chris Evans')," +
                            "('Cine', 'Fácil', '¿Qué película musical cuenta la historia de una niñera mágica?', 'El Rey León', 'Mary Poppins', 'La Bella y la Bestia', 'Aladdin', 'Mary Poppins')," +
                            "('Cine', 'Fácil', '¿Quién dirigió la trilogía original de \"El Señor de los Anillos\"?', 'George Lucas', 'Steven Spielberg', 'Peter Jackson', 'James Cameron', 'Peter Jackson')," +
                            "('Cine', 'Fácil', '¿Qué película tiene como villano al Joker?', 'Avengers', 'El Caballero Oscuro', 'Superman', 'Hulk', 'El Caballero Oscuro')," +
                            "('Cine', 'Fácil', '¿Qué película de Disney tiene una lámpara mágica?', 'Pocahontas', 'Aladdin', 'Hércules', 'La Sirenita', 'Aladdin')," +
                            "('Cine', 'Fácil', '¿Qué actor protagonizó \"Piratas del Caribe\" como Jack Sparrow?', 'Orlando Bloom', 'Johnny Depp', 'Tom Hanks', 'Brad Pitt', 'Johnny Depp')," +
                            "('Cine', 'Fácil', '¿Qué película presenta el parque temático Jurassic Park?', 'King Kong', 'Godzilla', 'Jurassic Park', 'Avatar', 'Jurassic Park')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de un perro dálmata?', '101 Dálmatas', 'La Dama y el Vagabundo', 'Zootopia', 'Bolt', '101 Dálmatas')," +
                            "('Cine', 'Fácil', '¿Quién es el protagonista de \"Rocky\"?', 'Arnold Schwarzenegger', 'Sylvester Stallone', 'Jean-Claude Van Damme', 'Bruce Lee', 'Sylvester Stallone')," +
                            "('Cine', 'Fácil', '¿Qué película presenta a un pez payaso buscando a su hijo?', 'Buscando a Nemo', 'La Vida Secreta de las Mascotas', 'Shark Tale', 'Ratatouille', 'Buscando a Nemo')," +
                            "('Cine', 'Fácil', '¿Qué película animada tiene como protagonista a un ratón chef?', 'Cars', 'Ratatouille', 'Toy Story', 'Los Increíbles', 'Ratatouille')," +
                            "('Cine', 'Fácil', '¿En qué película un niño viaja en el Expreso Polar?', 'Frozen', 'El Expreso Polar', 'Harry Potter', 'Narnia', 'El Expreso Polar')," +
                            "('Cine', 'Fácil', '¿Qué actor interpreta al arqueólogo Indiana Jones?', 'Harrison Ford', 'Sean Connery', 'Tom Hanks', 'Brad Pitt', 'Harrison Ford')," +
                            "('Cine', 'Fácil', '¿Qué película animada cuenta la historia de una hormiga obrera?', 'Bichos', 'Antz', 'Zootopia', 'Ratatouille', 'Antz')," +
                            "('Cine', 'Fácil', '¿Qué director es conocido por las películas de suspenso como \"Psicosis\"?', 'Alfred Hitchcock', 'Steven Spielberg', 'Martin Scorsese', 'Stanley Kubrick', 'Alfred Hitchcock')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de una sirenita llamada Ariel?', 'La Sirenita', 'Moana', 'Frozen', 'Aladdin', 'La Sirenita')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de un león llamado Simba?', 'Zootopia', 'El Rey León', 'Madagascar', 'Kung Fu Panda', 'El Rey León')," +
                            "('Cine', 'Fácil', '¿Qué película animada cuenta la historia de un panda aprendiz de kung-fu?', 'Shrek', 'Kung Fu Panda', 'Ratatouille', 'Monstruos, Inc.', 'Kung Fu Panda')," +
                            "('Cine', 'Fácil', '¿Qué película musical cuenta la historia de una aspirante a actriz en Los Ángeles?', 'El Gran Showman', 'La La Land', 'Cats', 'Mamma Mia!', 'La La Land')," +
                            "('Cine', 'Fácil', '¿Qué personaje dice \"Hasta el infinito y más allá\"?', 'Woody', 'Buzz Lightyear', 'Shrek', 'Mufasa', 'Buzz Lightyear')," +
                            "('Cine', 'Fácil', '¿Qué película de Disney tiene como protagonista a un ciervo llamado Bambi?', 'Dumbo', 'Bambi', 'El Libro de la Selva', 'Zootopia', 'Bambi')," +
                            "('Cine', 'Fácil', '¿Qué película tiene a un mago llamado Harry?', 'El Señor de los Anillos', 'Harry Potter', 'Las Crónicas de Narnia', 'Percy Jackson', 'Harry Potter')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de juguetes y su dueño Andy?', 'Cars', 'Toy Story', 'Monstruos, Inc.', 'Buscando a Nemo', 'Toy Story')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de un héroe arácnido?', 'Superman', 'Batman', 'Spiderman', 'Iron Man', 'Spiderman')," +
                            "('Cine', 'Fácil', '¿Qué película de Pixar cuenta la historia de un robot que limpia basura en la Tierra?', 'Ratatouille', 'Cars', 'WALL-E', 'Up', 'WALL-E')," +
                            "('Cine', 'Fácil', '¿Qué película de Disney tiene como protagonista a una heroína llamada Moana?', 'Moana', 'Frozen', 'La Sirenita', 'Enredados', 'Moana')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de un ogro y una princesa?', 'Shrek', 'Toy Story', 'Enredados', 'Frozen', 'Shrek')," +
                            "('Cine', 'Fácil', '¿Qué película tiene como villano al Capitán Garfio?', 'Peter Pan', 'Aladdin', 'La Sirenita', 'Cenicienta', 'Peter Pan')," +
                            "('Cine', 'Fácil', '¿Qué película presenta la canción \"Let It Go\"?', 'Enredados', 'Frozen', 'La Bella y la Bestia', 'Aladdin', 'Frozen')," +
                            "('Cine', 'Fácil', '¿Qué película de Disney tiene como protagonista a un elefante volador?', 'Dumbo', 'Bambi', 'El Rey León', '101 Dálmatas', 'Dumbo')," +
                            "('Cine', 'Fácil', '¿Qué película cuenta la historia de un robot y una niña llamada Eve?', 'Cars', 'WALL-E', 'Up', 'Toy Story', 'WALL-E')," +
                            "('Cine', 'Fácil', '¿Qué película tiene como protagonista a un hombre de hierro?', 'Batman', 'Iron Man', 'Superman', 'Spiderman', 'Iron Man')," +
                            "('Cine', 'Media', '¿Qué película de Quentin Tarantino ganó el Oscar a Mejor Guion Original?', 'Django Unchained', 'Pulp Fiction', 'Kill Bill', 'Reservoir Dogs', 'Pulp Fiction')," +
                            "('Cine', 'Media', '¿Qué actor interpretó al Joker en \"El Caballero Oscuro\"?', 'Jared Leto', 'Joaquin Phoenix', 'Heath Ledger', 'Jack Nicholson', 'Heath Ledger')," +
                            "('Cine', 'Media', '¿Qué película tiene la famosa escena de la ducha dirigida por Alfred Hitchcock?', 'Vértigo', 'Psicosis', 'La Ventana Indiscreta', 'Rebeca', 'Psicosis')," +
                            "('Cine', 'Media', '¿Qué director es conocido como el \"maestro del suspenso\"?', 'Steven Spielberg', 'Martin Scorsese', 'Alfred Hitchcock', 'Francis Ford Coppola', 'Alfred Hitchcock')," +
                            "('Cine', 'Media', '¿Qué película de Marvel es la más taquillera de la historia?', 'Avengers: Endgame', 'Iron Man', 'Black Panther', 'Spider-Man: No Way Home', 'Avengers: Endgame')," +
                            "('Cine', 'Media', '¿Quién dirigió \"El Resplandor\"?', 'Stanley Kubrick', 'Steven Spielberg', 'Martin Scorsese', 'Ridley Scott', 'Stanley Kubrick')," +
                            "('Cine', 'Media', '¿Qué actor interpretó a Forrest Gump?', 'Tom Cruise', 'Leonardo DiCaprio', 'Tom Hanks', 'Matt Damon', 'Tom Hanks')," +
                            "('Cine', 'Media', '¿Qué película de Pixar cuenta la historia de una familia de superhéroes?', 'Toy Story', 'Los Increíbles', 'Up', 'Monstruos, Inc.', 'Los Increíbles')," +
                            "('Cine', 'Media', '¿Qué saga de películas tiene un anillo como eje principal de su trama?', 'Harry Potter', 'El Señor de los Anillos', 'Star Wars', 'Las Crónicas de Narnia', 'El Señor de los Anillos')," +
                            "('Cine', 'Media', '¿Qué película tiene como protagonista a un boxeador llamado Rocky?', 'Rocky', 'Raging Bull', 'Creed', 'Million Dollar Baby', 'Rocky')," +
                            "('Cine', 'Media', '¿Qué actor protagonizó \"El Lobo de Wall Street\"?', 'Matthew McConaughey', 'Leonardo DiCaprio', 'Brad Pitt', 'Christian Bale', 'Leonardo DiCaprio')," +
                            "('Cine', 'Media', '¿Qué película tiene la famosa frase \"Aquí está Johnny!\"?', 'Psicosis', 'El Resplandor', 'Carrie', 'It', 'El Resplandor')," +
                            "('Cine', 'Media', '¿Qué director es conocido por películas como \"Inception\" y \"Dunkerque\"?', 'Quentin Tarantino', 'Christopher Nolan', 'Ridley Scott', 'James Cameron', 'Christopher Nolan')," +
                            "('Cine', 'Media', '¿Qué actor interpretó a Jack Sparrow en \"Piratas del Caribe\"?', 'Johnny Depp', 'Orlando Bloom', 'Tom Hanks', 'Robert Downey Jr.', 'Johnny Depp')," +
                            "('Cine', 'Media', '¿Qué película cuenta la historia de la creación de Facebook?', 'Jobs', 'The Social Network', 'Steve Jobs', 'El Gran Hackeo', 'The Social Network')," +
                            "('Cine', 'Media', '¿Qué actriz protagonizó \"El Diario de Bridget Jones\"?', 'Anne Hathaway', 'Renée Zellweger', 'Cameron Diaz', 'Julia Roberts', 'Renée Zellweger')," +
                            "('Cine', 'Media', '¿Qué película de ciencia ficción presenta un hotel en el espacio llamado Hal 9000?', 'Blade Runner', 'Interstellar', '2001: Odisea del Espacio', 'Gravity', '2001: Odisea del Espacio')," +
                            "('Cine', 'Media', '¿Qué película cuenta la historia de un robot llamado TARS?', 'WALL-E', 'Interstellar', 'Blade Runner 2049', 'Ex Machina', 'Interstellar')," +
                            "('Cine', 'Media', '¿Qué película presenta la famosa frase \"Francamente, querida, me importa un comino\"?', 'Casablanca', 'Lo que el viento se llevó', 'Ciudadano Kane', 'Desayuno con diamantes', 'Lo que el viento se llevó')," +
                            "('Cine', 'Media', '¿Qué director dirigió \"El Padrino\"?', 'Francis Ford Coppola', 'Martin Scorsese', 'Alfred Hitchcock', 'Steven Spielberg', 'Francis Ford Coppola')," +
                            "('Cine', 'Media', '¿Qué película animada cuenta la historia de una niña llamada Boo?', 'Toy Story', 'Monstruos, Inc.', 'Shrek', 'Ratatouille', 'Monstruos, Inc.')," +
                            "('Cine', 'Media', '¿Qué película tiene como protagonista a un auto llamado Herbie?', 'Rápidos y Furiosos', 'Cars', 'Cupido Motorizado', 'El Auto Fantástico', 'Cupido Motorizado')," +
                            "('Cine', 'Media', '¿Qué película tiene a un arqueólogo llamado Indiana Jones?', 'Jurassic Park', 'Indiana Jones', 'La Momia', 'Tomb Raider', 'Indiana Jones')," +
                            "('Cine', 'Media', '¿Qué actriz protagonizó \"Legalmente Rubia\"?', 'Anne Hathaway', 'Reese Witherspoon', 'Sandra Bullock', 'Julia Roberts', 'Reese Witherspoon')," +
                            "('Cine', 'Media', '¿Qué película de Disney tiene un genio azul?', 'Aladdin', 'Hércules', 'La Sirenita', 'Enredados', 'Aladdin')," +
                            "('Cine', 'Media', '¿Qué película musical presenta la canción \"The Greatest Show\"?', 'La La Land', 'Mamma Mia!', 'El Gran Showman', 'Cats', 'El Gran Showman')," +
                            "('Cine', 'Media', '¿Qué director es conocido por películas como \"Pulp Fiction\" y \"Kill Bill\"?', 'Quentin Tarantino', 'Steven Spielberg', 'James Cameron', 'Christopher Nolan', 'Quentin Tarantino')," +
                            "('Cine', 'Media', '¿Qué película tiene como protagonista a un tiburón gigante?', 'Tiburón', 'Meg', 'Deep Blue Sea', 'Sharknado', 'Tiburón')," +
                            "('Cine', 'Media', '¿Qué actriz interpretó a la Mujer Maravilla en el Universo DC?', 'Gal Gadot', 'Scarlett Johansson', 'Brie Larson', 'Jennifer Lawrence', 'Gal Gadot')," +
                            "('Cine', 'Media', '¿Qué película presenta un parque temático lleno de dinosaurios?', 'King Kong', 'Jurassic Park', 'Avatar', 'Planeta de los Simios', 'Jurassic Park')," +
                            "('Cine', 'Media', '¿Qué director dirigió \"Avatar\"?', 'James Cameron', 'Steven Spielberg', 'Ridley Scott', 'Christopher Nolan', 'James Cameron')," +
                            "('Cine', 'Media', '¿Qué película tiene como villano a Darth Vader?', 'Star Wars', 'El Señor de los Anillos', 'Matrix', 'Dune', 'Star Wars')," +
                            "('Cine', 'Media', '¿Qué actriz protagonizó \"El Diablo viste a la moda\"?', 'Meryl Streep', 'Anne Hathaway', 'Sandra Bullock', 'Natalie Portman', 'Anne Hathaway')," +
                            "('Cine', 'Media', '¿Qué película de Disney cuenta la historia de Hércules?', 'Hércules', 'Tarzán', 'La Sirenita', 'Aladdin', 'Hércules')," +
                            "('Cine', 'Media', '¿Qué película de ciencia ficción presenta replicantes?', 'Blade Runner', 'Interstellar', 'Gravity', 'Star Wars', 'Blade Runner')," +
                            "('Cine', 'Media', '¿Qué película de terror presenta a Freddy Krueger?', 'Halloween', 'Pesadilla en Elm Street', 'Viernes 13', 'It', 'Pesadilla en Elm Street')," +
                            "('Cine', 'Media', '¿Qué película tiene como protagonista a un hombre que envejece al revés?', 'El Gran Gatsby', 'El Curioso Caso de Benjamin Button', 'Titanic', 'La La Land', 'El Curioso Caso de Benjamin Button')," +
                            "('Cine', 'Media', '¿Qué actor protagonizó \"Matrix\"?', 'Keanu Reeves', 'Tom Cruise', 'Will Smith', 'Brad Pitt', 'Keanu Reeves')," +
                            "('Cine', 'Media', '¿Qué película cuenta la historia de un piloto de caza llamado Maverick?', 'Top Gun', 'Pearl Harbor', 'Independence Day', 'Dunkerque', 'Top Gun')," +
                            "('Cine', 'Media', '¿Qué película presenta la famosa frase \"Yo soy tu padre\"?', 'El Señor de los Anillos', 'Star Wars', 'Matrix', 'Dune', 'Star Wars')," +
                            "('Cine', 'Media', '¿Qué película cuenta la historia de un payaso aterrador llamado Pennywise?', 'It', 'El Conjuro', 'Halloween', 'Annabelle', 'It')," +
                            "('Cine', 'Media', '¿Qué actor protagonizó \"Gladiador\"?', 'Russell Crowe', 'Brad Pitt', 'Orlando Bloom', 'Leonardo DiCaprio', 'Russell Crowe')," +
                            "('Cine', 'Media', '¿Qué película cuenta la historia de una inteligencia artificial llamada Skynet?', 'Ex Machina', 'Matrix', 'Terminator', '2001: Odisea del Espacio', 'Terminator')," +
                            "('Cine', 'Media', '¿Qué película musical cuenta la historia de una joven en una isla griega?', 'Cats', 'Mamma Mia!', 'La La Land', 'El Gran Showman', 'Mamma Mia!'), " +
                            "('Cine', 'Difícil', '¿Qué director tiene el récord de más Oscars ganados como Mejor Director?', 'Steven Spielberg', 'John Ford', 'Alfred Hitchcock', 'Frank Capra', 'John Ford')," +
                            "('Cine', 'Difícil', '¿Qué película ganó el primer Oscar a Mejor Película?', 'Lo que el viento se llevó', 'Wings', 'Casablanca', 'Amanecer', 'Wings')," +
                            "('Cine', 'Difícil', '¿Qué película de Stanley Kubrick está basada en la novela de Anthony Burgess?', 'El Resplandor', '2001: Odisea del Espacio', 'La Naranja Mecánica', 'Barry Lyndon', 'La Naranja Mecánica')," +
                            "('Cine', 'Difícil', '¿Qué actor ganó un Oscar por su papel en \"El Silencio de los Inocentes\"?', 'Anthony Hopkins', 'Jodie Foster', 'Jack Nicholson', 'Robert De Niro', 'Anthony Hopkins')," +
                            "('Cine', 'Difícil', '¿Qué película de Alfred Hitchcock fue su primer largometraje sonoro?', 'Psicosis', 'Rebeca', 'La Soga', 'Blackmail', 'Blackmail')," +
                            "('Cine', 'Difícil', '¿Qué director italiano es conocido por películas como \"La Dolce Vita\" y \"8½\"?', 'Federico Fellini', 'Roberto Rossellini', 'Luchino Visconti', 'Vittorio De Sica', 'Federico Fellini')," +
                            "('Cine', 'Difícil', '¿Qué película tiene como protagonista a un hombre llamado Rick Blaine?', 'Ciudadano Kane', 'Casablanca', 'Lo que el viento se llevó', 'Rebeca', 'Casablanca')," +
                            "('Cine', 'Difícil', '¿Qué director japonés es conocido por \"Los Siete Samuráis\"?', 'Hayao Miyazaki', 'Yasujiro Ozu', 'Akira Kurosawa', 'Kenji Mizoguchi', 'Akira Kurosawa')," +
                            "('Cine', 'Difícil', '¿Qué actor protagonizó \"El Paciente Inglés\"?', 'Ralph Fiennes', 'Colin Firth', 'Hugh Grant', 'Anthony Hopkins', 'Ralph Fiennes')," +
                            "('Cine', 'Difícil', '¿Qué película animada de Disney fue la primera en ganar un Globo de Oro como Mejor Película?', 'La Bella y la Bestia', 'Aladdin', 'El Rey León', 'Pocahontas', 'La Bella y la Bestia')," +
                            "('Cine', 'Difícil', '¿Qué director es conocido por películas de terror como \"La Cosa\" y \"Halloween\"?', 'John Carpenter', 'Wes Craven', 'George A. Romero', 'Tobe Hooper', 'John Carpenter')," +
                            "('Cine', 'Difícil', '¿Qué película presenta el personaje de Norma Desmond?', 'Rebeca', 'Sunset Boulevard', 'Vértigo', 'All About Eve', 'Sunset Boulevard')," +
                            "('Cine', 'Difícil', '¿Qué película francesa ganó el Oscar a Mejor Película Extranjera en 2002?', 'Amélie', 'La Vida en Rosa', 'Cinema Paradiso', 'No Man''s Land', 'No Man''s Land')," +
                            "('Cine', 'Difícil', '¿Qué actriz ganó un Oscar por interpretar a Margaret Thatcher?', 'Helen Mirren', 'Meryl Streep', 'Cate Blanchett', 'Frances McDormand', 'Meryl Streep')," +
                            "('Cine', 'Difícil', '¿Qué película de Orson Welles es considerada una de las mejores de la historia?', 'El Ciudadano Kane', 'La Dama de Shanghái', 'Sed de Mal', 'Macbeth', 'El Ciudadano Kane')," +
                            "('Cine', 'Difícil', '¿Qué película de ciencia ficción ganó el Oscar a Mejores Efectos Especiales en 1982?', 'Blade Runner', 'E.T.', 'Tron', 'Star Wars: Episodio V', 'E.T.')," +
                            "('Cine', 'Difícil', '¿Qué director es conocido por películas como \"Birdman\" y \"El Renacido\"?', 'Guillermo del Toro', 'Alfonso Cuarón', 'Alejandro González Iñárritu', 'Pedro Almodóvar', 'Alejandro González Iñárritu')," +
                            "('Cine', 'Difícil', '¿Qué película presenta la famosa escena del carrito de bebé cayendo por las escaleras?', 'El Acorazado Potemkin', 'Ciudadano Kane', 'Metrópolis', 'Napoleón', 'El Acorazado Potemkin')," +
                            "('Cine', 'Difícil', '¿Qué película de Quentin Tarantino está dividida en capítulos?', 'Django Unchained', 'Pulp Fiction', 'Kill Bill', 'Reservoir Dogs', 'Pulp Fiction')," +
                            "('Cine', 'Difícil', '¿Qué película ganó el Oscar a Mejor Película en 2019?', '1917', 'Joker', 'Parasite', 'Once Upon a Time in Hollywood', 'Parasite')," +
                            "('Cine', 'Difícil', '¿Qué actor ganó un Oscar por interpretar a Stephen Hawking?', 'Eddie Redmayne', 'Benedict Cumberbatch', 'Daniel Day-Lewis', 'Hugh Jackman', 'Eddie Redmayne')," +
                            "('Cine', 'Difícil', '¿Qué película dirigida por Francis Ford Coppola es una obra maestra del cine bélico?', 'El Padrino', 'La Conversación', 'Apocalypse Now', 'Platoon', 'Apocalypse Now')," +
                            "('Cine', 'Difícil', '¿Qué película es conocida como el primer musical animado de Disney?', 'Blancanieves y los Siete Enanitos', 'Fantasía', 'La Bella y la Bestia', 'Pinocho', 'Blancanieves y los Siete Enanitos')," +
                            "('Cine', 'Difícil', '¿Qué película de Ridley Scott popularizó la frase \"En el espacio, nadie puede oír tus gritos\"?', 'Blade Runner', 'Alien', 'Prometheus', 'Gravity', 'Alien')," +
                            "('Cine', 'Difícil', '¿Qué director es conocido por películas como \"Taxi Driver\" y \"El Lobo de Wall Street\"?', 'Francis Ford Coppola', 'Martin Scorsese', 'Brian De Palma', 'Steven Spielberg', 'Martin Scorsese')," +
                            "('Cine', 'Difícil', '¿Qué película de Steven Spielberg es considerada una de las mejores sobre el Holocausto?', 'Munich', 'Rescatando al Soldado Ryan', 'La Lista de Schindler', 'El Imperio del Sol', 'La Lista de Schindler')," +
                            "('Cine', 'Difícil', '¿Qué actriz ganó un Oscar por su papel en \"Black Swan\"?', 'Natalie Portman', 'Scarlett Johansson', 'Anne Hathaway', 'Emma Stone', 'Natalie Portman')," +
                            "('Cine', 'Difícil', '¿Qué película presenta la frase \"Yo podría haber sido un contendiente\"?', 'Casablanca', 'On the Waterfront', 'Ciudadano Kane', 'El Padrino', 'On the Waterfront')," +
                            "('Cine', 'Difícil', '¿Qué director mexicano ganó el Oscar a Mejor Director por \"Roma\"?', 'Guillermo del Toro', 'Alejandro González Iñárritu', 'Alfonso Cuarón', 'Amat Escalante', 'Alfonso Cuarón')," +
                            "('Cine', 'Difícil', '¿Qué película de ciencia ficción popularizó la figura de los replicantes?', 'Blade Runner', 'Alien', 'Tron', 'Minority Report', 'Blade Runner')," +
                            "('Cine', 'Difícil', '¿Qué director es conocido por películas como \"El Gran Hotel Budapest\" y \"Isla de Perros\"?', 'Wes Anderson', 'Paul Thomas Anderson', 'Noah Baumbach', 'Christopher Nolan', 'Wes Anderson')," +
                            "('Cine', 'Difícil', '¿Qué película animada japonesa fue dirigida por Hayao Miyazaki y ganó un Oscar?', 'El Viaje de Chihiro', 'Mi Vecino Totoro', 'La Princesa Mononoke', 'Ponyo', 'El Viaje de Chihiro')," +
                            "('Cine', 'Difícil', '¿Qué película de guerra de Stanley Kubrick es famosa por su realismo?', 'Full Metal Jacket', 'La Naranja Mecánica', 'Senderos de Gloria', 'Espartaco', 'Full Metal Jacket')," +
                            "('Cine', 'Difícil', '¿Qué película italiana ganó el Oscar a Mejor Película Extranjera en 1999?', 'Cinema Paradiso', 'La Vida es Bella', 'El Cartero', 'Malena', 'La Vida es Bella')," +
                            "('Cine', 'Difícil', '¿Qué película es considerada la obra maestra de Federico Fellini?', 'Amarcord', 'La Dolce Vita', '8½', 'Roma', '8½')," +
                            "('Cine', 'Difícil', '¿Qué director británico dirigió \"Lawrence de Arabia\"?', 'David Lean', 'Alfred Hitchcock', 'Ridley Scott', 'Christopher Nolan', 'David Lean')," +
                            "('Cine', 'Difícil', '¿Qué película de terror está basada en la novela de William Peter Blatty?', 'Psicosis', 'El Exorcista', 'La Profecía', 'Carrie', 'El Exorcista')" ;            
        db.execSQL(insertCine);

        // Crear tabla jugadores con todas las columnas de puntuación
        String createTableJugadores = "CREATE TABLE IF NOT EXISTS " + TABLE_JUGADORES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOMBRE + " TEXT UNIQUE, " +
            COLUMN_CONTRASEÑA + " TEXT, " +
            // Puntuaciones Historia
            COLUMN_HISTORIA_FACIL + " INTEGER DEFAULT 0, " +
            COLUMN_HISTORIA_MEDIO + " INTEGER DEFAULT 0, " +
            COLUMN_HISTORIA_DIFICIL + " INTEGER DEFAULT 0, " +
            // Puntuaciones Ciencia
            COLUMN_CIENCIA_FACIL + " INTEGER DEFAULT 0, " +
            COLUMN_CIENCIA_MEDIO + " INTEGER DEFAULT 0, " +
            COLUMN_CIENCIA_DIFICIL + " INTEGER DEFAULT 0, " +
            // Puntuaciones Geografía
            COLUMN_GEOGRAFIA_FACIL + " INTEGER DEFAULT 0, " +
            COLUMN_GEOGRAFIA_MEDIO + " INTEGER DEFAULT 0, " +
            COLUMN_GEOGRAFIA_DIFICIL + " INTEGER DEFAULT 0, " +
            // Puntuaciones Cine
            COLUMN_CINE_FACIL + " INTEGER DEFAULT 0, " +
            COLUMN_CINE_MEDIO + " INTEGER DEFAULT 0, " +
            COLUMN_CINE_DIFICIL + " INTEGER DEFAULT 0, " +
            // Puntuación Leyenda
            COLUMN_LEYENDA_FACIL + " INTEGER DEFAULT 0)";

        db.execSQL(createTableJugadores);
        Log.d("DatabaseHelper", "Tabla jugadores creada con todas las columnas de puntuación");

        // Crear tabla auxiliar de preguntas para consultas generales
        String createTableQuestions = "CREATE TABLE questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pregunta TEXT NOT NULL, " +
                "opcion1 TEXT NOT NULL, " +
                "opcion2 TEXT NOT NULL, " +
                "opcion3 TEXT NOT NULL, " +
                "opcion4 TEXT NOT NULL, " +
                "respuesta_correcta TEXT NOT NULL, " +
                "tema TEXT NOT NULL, " +
                "dificultad TEXT NOT NULL)";
        db.execSQL(createTableQuestions);

        // Crear tabla de puntuaciones para rankings
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS puntuaciones (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "tema TEXT NOT NULL, " +
                "dificultad TEXT NOT NULL, " +
                "puntos INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE);

        // Aquí iría la inserción de todas las preguntas (insertHistoria, insertCiencia, etc.)
        // Se ha omitido por brevedad, pero incluye todas las preguntas predefinidas
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS puntuaciones");
        onCreate(db);
    }





    public List<Question> getQuestionsByDifficulty(String difficulty) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTIONS, null, COLUMN_DIFICULTAD + " = ?", new String[]{difficulty}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tema = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMA));
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PREGUNTA));
                String option1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION1));
                String option2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION2));
                String option3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION3));
                String option4 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION4));
                String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESPUESTA_CORRECTA));

                Question question = new Question(tema, difficulty, questionText, option1, option2, option3, option4, correctAnswer);
                questionList.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }



    public long insertarJugador(String nombre, String contraseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_CONTRASEÑA, contraseña);
        return db.insert(TABLE_JUGADORES, null, values);
    }

    public boolean verifyPlayer(String nombre, String contraseña) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JUGADORES, new String[]{COLUMN_NOMBRE},
                COLUMN_NOMBRE + "=? AND " + COLUMN_CONTRASEÑA + "=?",
                new String[]{nombre, contraseña}, null, null, null);

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }


    public List<Question> getQuestionsByDifficultyAndTopic(String dificultad, String tema, int limit) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        // Normalizar el nombre de la tabla y la dificultad
        String tableName = tema;
        String normalizedDificultad = dificultad;
        
        // Corregir la diferencia entre "Medio" y "Media"
        if (dificultad.equals("Medio")) {
            normalizedDificultad = "Media";
        }
        
        Log.d("DatabaseHelper", "Consultando tabla: " + tableName + 
              " para dificultad: " + normalizedDificultad + " límite: " + limit);
        
        try {
            String query = "SELECT * FROM " + tableName + 
                          " WHERE dificultad = ? ORDER BY RANDOM() LIMIT ?";
            
            Cursor cursor = db.rawQuery(query, 
                                      new String[]{normalizedDificultad, String.valueOf(limit)});
            
            Log.d("DatabaseHelper", "Número de preguntas encontradas: " + cursor.getCount());
            
            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFICULTAD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PREGUNTA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION1)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION3)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPCION4)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESPUESTA_CORRECTA))
                    );
                    questions.add(question);
                    Log.d("DatabaseHelper", "Pregunta añadida: " + question.getPregunta());
                } while (cursor.moveToNext());
            } else {
                Log.e("DatabaseHelper", "No se encontraron preguntas para " + tableName + " con dificultad " + normalizedDificultad);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error al obtener preguntas de " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        
        return questions;
    }

    // Método para actualizar la puntuación de un tema y dificultad específicos
    public boolean actualizarPuntuacion(String playerName, String tema, String dificultad, int puntos) {
        SQLiteDatabase db = this.getWritableDatabase();
        String columnaPuntuacion = obtenerColumnaSegunTemaYDificultad(tema, dificultad);

        ContentValues values = new ContentValues();
        values.put(columnaPuntuacion, puntos);

        int rowsAffected = db.update(TABLE_JUGADORES, 
            values, 
            COLUMN_NOMBRE + " = ?",
            new String[]{playerName});

        return rowsAffected > 0;
    }

    /**
     * Determina la columna de la base de datos según el tema y dificultad.
     * Este método auxiliar normaliza los nombres de temas y dificultades para
     * obtener la columna correcta en la tabla de puntuaciones.
     * 
     * @param tema Categoría del juego (Historia, Ciencia, etc.)
     * @param dificultad Nivel de dificultad (Fácil, Medio, Difícil)
     * @return Nombre de la columna correspondiente en la base de datos
     */
    private String obtenerColumnaSegunTemaYDificultad(String tema, String dificultad) {
        // Normalizar el tema eliminando acentos
        String temaNormalizado = tema.toLowerCase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u");
        
        // Determinar la columna según el tema y dificultad
        switch (temaNormalizado) {
            case "historia":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return COLUMN_HISTORIA_FACIL;
                    case "medio": return COLUMN_HISTORIA_MEDIO;
                    case "difícil": return COLUMN_HISTORIA_DIFICIL;
                }
                break;
            case "ciencia":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return COLUMN_CIENCIA_FACIL;
                    case "medio": return COLUMN_CIENCIA_MEDIO;
                    case "difícil": return COLUMN_CIENCIA_DIFICIL;
                }
                break;
            case "geografia":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return COLUMN_GEOGRAFIA_FACIL;
                    case "medio": return COLUMN_GEOGRAFIA_MEDIO;
                    case "difícil": return COLUMN_GEOGRAFIA_DIFICIL;
                }
                break;
            case "cine":
                switch (dificultad.toLowerCase()) {
                    case "fácil": return COLUMN_CINE_FACIL;
                    case "medio": return COLUMN_CINE_MEDIO;
                    case "difícil": return COLUMN_CINE_DIFICIL;
                }
                break;
            case "leyenda":
                return COLUMN_LEYENDA_FACIL;
        }
        // Si no se encuentra una coincidencia, registrar error y devolver columna por defecto
        Log.e("DatabaseHelper", "Tema o dificultad no reconocidos: " + tema + ", " + dificultad);
        return COLUMN_HISTORIA_FACIL;
    }

    // Método para obtener la puntuación de un tema y dificultad específicos
    public int obtenerPuntuacion(String nombreJugador, String tema, String dificultad) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String columna = tema.toLowerCase() + "_" + dificultad.toLowerCase()
                            .replace("á", "a")
                            .replace("é", "e")
                            .replace("í", "i")
                            .replace("ó", "o")
                            .replace("ú", "u");
        
        if (tema.equals("Leyenda")) {
            columna = "leyenda";
        }
        
        Cursor cursor = db.query(TABLE_JUGADORES, 
                                new String[]{columna}, 
                                COLUMN_NOMBRE + "=?", 
                                new String[]{nombreJugador}, 
                                null, null, null);
        
        int puntuacion = 0;
        if (cursor.moveToFirst()) {
            puntuacion = cursor.getInt(0);
        }
        
        cursor.close();
        db.close();
        return puntuacion;
    }



    public class PuestoInfo {
        public int puesto;
        public int puntos;
        
        public PuestoInfo(int puesto, int puntos) {
            this.puesto = puesto;
            this.puntos = puntos;
        }
    }



    public PuestoInfo obtenerPuestoJugador(String playerName, String tema, String dificultad) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Determinar qué columna consultar basado en tema y dificultad
            String columnaPuntuacion = obtenerColumnaSegunTemaYDificultad(tema, dificultad);
            
            // Primero obtener los puntos del jugador actual
            String queryPuntos = "SELECT " + columnaPuntuacion + " FROM " + TABLE_JUGADORES + 
                               " WHERE " + COLUMN_NOMBRE + " = ?";
            
            cursor = db.rawQuery(queryPuntos, new String[]{playerName});

            if (cursor.moveToFirst()) {
                int puntos = cursor.getInt(0);
                cursor.close();

                // Ahora obtener el puesto contando jugadores con más puntos
                String queryPuesto = "SELECT COUNT(*) + 1 FROM " + TABLE_JUGADORES + 
                                   " WHERE " + columnaPuntuacion + " > ?";
                
                cursor = db.rawQuery(queryPuesto, new String[]{String.valueOf(puntos)});

                if (cursor.moveToFirst()) {
                    return new PuestoInfo(cursor.getInt(0), puntos);
                }
            }

            return new PuestoInfo(0, 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


} 