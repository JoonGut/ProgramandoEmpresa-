package cliente;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ChatCliente extends JFrame {
    private JTextArea areaChat;
    private JTextField campoMensaje;
    private PrintWriter escritor;
    private static final String IP_SERVIDOR = "192.168.9.119";
    private static final int PUERTO = 6000;

    public static void main(String[] args) {
        // Configurar certificados SSL
        System.setProperty("javax.net.ssl.trustStore", "certificados/UsuarioChatSSL");
        System.setProperty("javax.net.ssl.trustStorePassword", "890123");
        
        EventQueue.invokeLater(() -> {
            ChatCliente chat = new ChatCliente();
            chat.setVisible(true);
        });
    }

    public ChatCliente() {
        configurarVentana();
        configurarComponentes();
        conectarAlServidor();
    }

    private void configurarVentana() {
        setTitle("Chat Seguro");
        setSize(620, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void configurarComponentes() {
        // Configurar área de chat
        areaChat = new JTextArea();
        areaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaChat);

        // Configurar campo de mensaje y botones
        campoMensaje = new JTextField();
        JButton botonEnviar = new JButton("Enviar");
        botonEnviar.setBackground(new Color(0, 255, 0));
        JButton botonVideo = new JButton("Iniciar video");
        botonVideo.setBackground(new Color(0, 255, 255));

        // Panel para entrada de mensaje y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(botonEnviar);
        panelBotones.add(botonVideo);
        
        panelInferior.add(campoMensaje, BorderLayout.CENTER);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        // Añadir componentes a la ventana
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelInferior, BorderLayout.SOUTH);

        // Añadir listeners
        botonEnviar.addActionListener(e -> enviarMensaje());
        campoMensaje.addActionListener(e -> enviarMensaje());
        botonVideo.addActionListener(e -> iniciarVideo());
    }

    private void iniciarVideo() {
        try {
            ClienteVideo clienteVideo = new ClienteVideo();
            clienteVideo.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al iniciar video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void conectarAlServidor() {
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(IP_SERVIDOR, PUERTO);
            escritor = new PrintWriter(socket.getOutputStream(), true);

            // Solicitar nombre de usuario
            String nombreUsuario = JOptionPane.showInputDialog(this, "Introduce tu nombre de usuario:");
            if (nombreUsuario != null && !nombreUsuario.isBlank()) {
                escritor.println(nombreUsuario);
                // Iniciar hilo para escuchar mensajes
                new Thread(() -> escucharMensajes(socket)).start();
            } else {
                System.exit(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor: " + e.getMessage());
            System.exit(1);
        }
    }

    private void enviarMensaje() {
        String mensaje = campoMensaje.getText().trim();
        if (!mensaje.isEmpty()) {
        	mensaje = reemplazarAtajosEmojis(mensaje);
        	escritor.println(mensaje);
        	campoMensaje.setText("");

        }
    }
    
    private String reemplazarAtajosEmojis(String mensaje) {
        return mensaje.replace(":smile:", "😊")
                      .replace(":heart:", "❤️")
                      .replace(":thumbsup:", "👍")
                      .replace(":sad:", "😢")
                      .replace(":laugh:", "😂");
    }

    private void escucharMensajes(SSLSocket socket) {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String mensaje;
            while ((mensaje = lector.readLine()) != null) {
                final String mensajeFinal = mensaje;
                SwingUtilities.invokeLater(() -> areaChat.append(mensajeFinal + "\n"));
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> areaChat.append("Conexión perdida.\n"));
        }
    }
}