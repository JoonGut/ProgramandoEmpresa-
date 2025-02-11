package com.example.game_exides;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Vista personalizada para mostrar texto con efectos visuales mejorados.
 * Esta clase extiende View para crear un componente de texto con estilo propio,
 * incluyendo sombras, bordes y efectos visuales personalizados.
 */
public class CustomTextView extends View {

    // Paint que define las características visuales del texto
    private Paint textPaint;
    // Texto que se mostrará en la vista
    private String text = "";

    /**
     * Constructor simple usado cuando se crea la vista desde código
     */
    public CustomTextView(Context context) {
        super(context);
        init(null);
    }

    /**
     * Constructor usado cuando se crea la vista desde XML
     */
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Constructor con estilo personalizado
     */
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Inicializa la configuración visual del texto
     * Establece todos los parámetros de estilo como color, tamaño, fuente y efectos
     */
    private void init(AttributeSet attrs) {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  // Creamos el pincel con anti-aliasing para texto más suave
        
        // Configuración del estilo del texto
        textPaint.setColor(Color.WHITE);               // Color blanco para máxima visibilidad
        textPaint.setTextSize(38);                     // Tamaño de texto legible
        textPaint.setTypeface(Typeface.create(
            Typeface.SANS_SERIF,                       // Fuente sans-serif para mejor legibilidad
            Typeface.BOLD_ITALIC));                    // Estilo negrita e itálica para destacar
        
        // Efecto de sombra para dar profundidad
        textPaint.setShadowLayer(
            10,                                        // Radio de la sombra
            4, 4,                                      // Desplazamiento X e Y
            Color.argb(100, 50, 50, 50));             // Color semi-transparente
        
        textPaint.setAlpha(255);                      // Opacidad completa
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE); // Relleno y borde
        textPaint.setStrokeWidth(2);                  // Grosor del borde para texto más definido

        // Si se proporciona texto desde XML, lo recuperamos
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, new int[]{android.R.attr.text});
            text = a.getString(0);
            a.recycle();  // Liberamos recursos
        }
    }

    /**
     * Dibuja el texto en el canvas centrado horizontal y verticalmente
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Calculamos la posición para centrar el texto
        float x = (getWidth() - textPaint.measureText(text)) / 2;    // Centro horizontal
        float y = (getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2); // Centro vertical
        canvas.drawText(text, x, y, textPaint);
    }

    /**
     * Establece un nuevo texto y fuerza el redibujado de la vista
     */
    public void setText(String text) {
        this.text = text;
        invalidate();  // Solicita redibujar la vista con el nuevo texto
    }

    /**
     * Obtiene el texto actual de la vista
     */
    public String getText() {
        return text;
    }
} 