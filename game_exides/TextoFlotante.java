package com.example.game_exides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Vista personalizada que muestra texto flotante con efectos visuales.
 * Proporciona animaciones de texto, sombras y bordes para crear
 * un efecto visual atractivo en el texto mostrado.
 */
public class TextoFlotante extends View {
    // Objetos Paint para diferentes efectos visuales
    private Paint textPaint;      // Para el texto principal
    private Paint strokePaint;    // Para el borde del texto
    private Paint shadowPaint;    // Para la sombra del texto
    
    // Variables de estado del texto
    private String text = "¡Bienvenido al juego EXIDES!\n¿Estás preparado para una gran\naventura?";
    private float textSize;       // Tamaño del texto
    private String[] textLines;   // Líneas de texto para animación
    private int currentLine = 0;  // Línea actual en la animación
    private boolean isAnimating = false; // Estado de la animación

    /**
     * Constructor para crear la vista desde código.
     * @param context Contexto de la aplicación
     */
    public TextoFlotante(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor para crear la vista desde XML.
     * @param context Contexto de la aplicación
     * @param attrs Atributos definidos en XML
     */
    public TextoFlotante(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructor para crear la vista desde XML con estilo.
     * @param context Contexto de la aplicación
     * @param attrs Atributos definidos en XML
     * @param defStyleAttr Estilo por defecto
     */
    public TextoFlotante(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Se llama cuando la vista termina de inflarse desde XML.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    /**
     * Inicializa los objetos Paint y configura los estilos visuales.
     */
    private void init() {
        // Configuración de tamaño de texto
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            24,
            getResources().getDisplayMetrics()
        );

        // Configurar Paint para el texto principal (optimizado)
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.parseColor("#F0FFFF")); // Blanco eléctrico
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFilterBitmap(true);
        
        // Configurar Paint para el borde (optimizado)
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        strokePaint.setColor(Color.parseColor("#000000")); // Negro puro para el borde
        strokePaint.setTextSize(textSize);
        strokePaint.setTextAlign(Paint.Align.CENTER);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3); // Reducido para mejor rendimiento
        strokePaint.setFilterBitmap(true);

        // Configurar Paint para la sombra (optimizado)
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        shadowPaint.setColor(Color.parseColor("#2F4F4F")); // Gris pizarra oscuro
        shadowPaint.setTextSize(textSize);
        shadowPaint.setTextAlign(Paint.Align.CENTER);
        shadowPaint.setAlpha(120); // Reducida la opacidad
        shadowPaint.setFilterBitmap(true);

        // Habilitar hardware acceleration
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    /**
     * Dibuja el texto con efectos visuales en el canvas.
     * @param canvas Canvas donde se dibujará el texto
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Dividir el texto en líneas
        String[] lines = text.split("\n");
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float lineHeight = fm.bottom - fm.top;
        float totalHeight = lineHeight * lines.length;
        float startY = centerY - (totalHeight / 2) + lineHeight;

        // Dibujar cada línea
        for (String line : lines) {
            // Dibujar la sombra
            canvas.drawText(line, centerX + 5, startY + 5, shadowPaint);
            // Dibujar el borde
            canvas.drawText(line, centerX, startY, strokePaint);
            // Dibujar el texto principal
            canvas.drawText(line, centerX, startY, textPaint);
            
            startY += lineHeight;
        }
    }

    /**
     * Actualiza el texto mostrado en la vista.
     * @param newText Nuevo texto a mostrar
     */
    public void setText(String newText) {
        text = newText;
        invalidate(); // Volver a dibujar la vista
    }

    /**
     * Calcula las dimensiones necesarias para la vista.
     * @param widthMeasureSpec Especificación del ancho
     * @param heightMeasureSpec Especificación del alto
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Obtener las dimensiones mínimas necesarias para el texto
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float textHeight = fm.bottom - fm.top;
        float textWidth = textPaint.measureText(text);

        // Añadir padding para la sombra y el borde
        int desiredWidth = (int) (textWidth + 20); // +20 para el padding
        int desiredHeight = (int) (textHeight + 20); // +20 para el padding

        // Resolver las dimensiones finales
        int width = resolveSize(desiredWidth, widthMeasureSpec);
        int height = resolveSize(desiredHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    /**
     * Anima el texto línea por línea con un retraso especificado.
     * @param lines Array de líneas de texto a animar
     * @param delayBetweenLines Retraso entre cada línea en milisegundos
     */
    public void animateText(String[] lines, long delayBetweenLines) {
        this.textLines = lines;
        this.currentLine = 0;
        this.isAnimating = true;
        
        text = "";
        Handler handler = new Handler(Looper.getMainLooper());
        
        for (int i = 0; i < lines.length; i++) {
            final int lineIndex = i;
            handler.postDelayed(() -> {
                if (!isAnimating) return; // Evitar animaciones si la vista no está visible
                
                StringBuilder newText = new StringBuilder(text);
                if (lineIndex > 0) {
                    newText.append("\n");
                }
                newText.append(lines[lineIndex]);
                text = newText.toString();
                
                postInvalidateOnAnimation(); // Más eficiente que invalidate()
                
                if (lineIndex == lines.length - 1) {
                    isAnimating = false;
                }
            }, delayBetweenLines * i);
        }
    }

    /**
     * Verifica si la animación está en curso.
     * @return true si la animación está activa, false en caso contrario
     */
    public boolean isAnimating() {
        return isAnimating;
    }

    /**
     * Obtiene el texto actual mostrado en la vista.
     * @return Texto actual
     */
    public String getText() {
        return text;
    }
} 