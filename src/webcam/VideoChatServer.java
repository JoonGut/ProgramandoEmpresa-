package webcam;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import javax.net.ssl.*;

public class VideoChatServer {
    private static final int VIDEO_PORT = 5000;
    private static final int CHAT_PORT = 6000;
    private static final ConcurrentHashMap<Integer, VideoClientHandler> clientesVideo = new ConcurrentHashMap<>();
    private static final Map<String, ClientHandler> clientesChat = Collections.synchronizedMap(new HashMap<>());
    private static int contadorClientesVideo = 0;
    private static volatile boolean ejecutando = true;

    public static void main(String[] args) {
        // Configurar certificados SSL
        System.setProperty("javax.net.ssl.keyStore", "certificados/ChatExidesSSL");
        System.setProperty("javax.net.ssl.keyStorePassword", "1234567");

        // Iniciar servidores en hilos separados
        new Thread(VideoChatServer::iniciarServidorVideo).start();
        new Thread(VideoChatServer::iniciarServidorChat).start();
    }

    private static void iniciarServidorVideo() {
        try (ServerSocket servidor = new ServerSocket(VIDEO_PORT)) {
            System.out.println("Servidor de video iniciado en puerto " + VIDEO_PORT);

            while (ejecutando) {
                Socket socketCliente = servidor.accept();
                socketCliente.setTcpNoDelay(true);
                
                int id = contadorClientesVideo++;
                VideoClientHandler manejador = new VideoClientHandler(id, socketCliente);
                clientesVideo.put(id, manejador);
                
                new Thread(manejador).start();
                System.out.println("Cliente de video conectado: " + id);
            }
        } catch (IOException e) {
            System.err.println("Error en servidor de video: " + e.getMessage());
        }
    }

    private static void iniciarServidorChat() {
        try (SSLServerSocket servidor = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(CHAT_PORT)) {
            System.out.println("Servidor de chat iniciado en puerto " + CHAT_PORT);

            while (ejecutando) {
                SSLSocket socketCliente = (SSLSocket) servidor.accept();
                new Thread(() -> manejarClienteChat(socketCliente)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en servidor de chat: " + e.getMessage());
        }
    }

    private static void manejarClienteChat(SSLSocket socket) {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            String nombreUsuario = entrada.readLine();
            if (nombreUsuario == null || nombreUsuario.isBlank() || clientesChat.containsKey(nombreUsuario)) {
                salida.println("ERROR: Nombre de usuario inválido o en uso.");
                return;
            }

            ClientHandler manejador = new ClientHandler(nombreUsuario, salida);
            clientesChat.put(nombreUsuario, manejador);
            difundirMensaje(nombreUsuario + " se ha unido al chat.", null);

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                difundirMensaje(hora + "  -  [" + nombreUsuario + "]: " + mensaje, nombreUsuario);
            }
        } catch (IOException e) {
            System.err.println("Error con cliente de chat: " + e.getMessage());
        } finally {
            removerClienteChat(socket);
        }
    }

    private static void difundirMensaje(String mensaje, String remitente) {
        synchronized (clientesChat) {
            clientesChat.values().forEach(cliente -> {
                cliente.getSalida().println(mensaje);
            });
        }
    }

    private static void removerClienteChat(SSLSocket socket) {
        clientesChat.entrySet().removeIf(entrada -> {
            boolean eliminar = entrada.getValue().getSalida().equals(socket);
            if (eliminar) {
                difundirMensaje(entrada.getKey() + " ha salido del chat.", null);
            }
            return eliminar;
        });
    }

    private static class ClientHandler {
        private final String nombreUsuario;
        private final PrintWriter salida;

        public ClientHandler(String nombreUsuario, PrintWriter salida) {
            this.nombreUsuario = nombreUsuario;
            this.salida = salida;
        }

        public PrintWriter getSalida() { return salida; }
    }

    private static class VideoClientHandler implements Runnable {
        private final int id;
        private final Socket socket;
        private volatile boolean ejecutando = true;
        private static final int TAMANO_BUFFER = 64 * 1024;

        public VideoClientHandler(int id, Socket socket) {
            this.id = id;
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream entrada = new BufferedInputStream(socket.getInputStream());
                 OutputStream salida = new BufferedOutputStream(socket.getOutputStream())) {
                
                byte[] bufferTamano = new byte[4];
                byte[] bufferDatos = new byte[TAMANO_BUFFER];

                while (ejecutando && !socket.isClosed()) {
                    if (leerCompleto(entrada, bufferTamano, 4) != 4) break;
                    
                    int longitud = ((bufferTamano[0] & 0xFF) << 24) |
                                 ((bufferTamano[1] & 0xFF) << 16) |
                                 ((bufferTamano[2] & 0xFF) << 8) |
                                 (bufferTamano[3] & 0xFF);

                    if (longitud > TAMANO_BUFFER) continue;

                    if (leerCompleto(entrada, bufferDatos, longitud) != longitud) break;
                    difundirVideo(id, bufferTamano, bufferDatos, longitud);
                }
            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println("Error en cliente de video " + id + ": " + e.getMessage());
                }
            } finally {
                clientesVideo.remove(id);
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error cerrando socket de video: " + e.getMessage());
                }
            }
        }

        private int leerCompleto(InputStream entrada, byte[] buffer, int longitud) throws IOException {
            int totalLeido = 0;
            while (totalLeido < longitud) {
                int leido = entrada.read(buffer, totalLeido, longitud - totalLeido);
                if (leido == -1) return totalLeido;
                totalLeido += leido;
            }
            return totalLeido;
        }

        private void difundirVideo(int idRemitente, byte[] bufferTamano, byte[] bufferDatos, int longitud) {
            clientesVideo.forEach((id, manejador) -> {
                if (id != idRemitente) {
                    try {
                        Socket socketCliente = manejador.socket;
                        if (!socketCliente.isClosed()) {
                            OutputStream salida = socketCliente.getOutputStream();
                            salida.write(bufferTamano);
                            salida.write(bufferDatos, 0, longitud);
                            salida.flush();
                        }
                    } catch (IOException e) {
                        System.err.println("Error enviando video al cliente " + id);
                    }
                }
            });
        }
    }
}