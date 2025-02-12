package cliente;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.github.sarxos.webcam.*;

public class ClienteVideo extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String SERVER = "192.168.9.119";
    private static final int PORT = 5000;
    private volatile boolean running = true;  

    private Socket socket;
    private Webcam webcam;
    private Thread sendThread;
    private Thread receiveThread;

    public ClienteVideo() throws UnknownHostException, IOException {
        socket = new Socket(SERVER, PORT);
        System.out.println("Conectado al servidor");

        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();

        JLabel label = new JLabel();
        setTitle("Cliente de video");
        add(label);
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                cleanup();
            }
        });

        sendThread = new Thread(() -> sendVideo(socket, webcam));
        sendThread.setDaemon(true);
        sendThread.start();

        receiveThread = new Thread(() -> receiveVideo(socket, label));
        receiveThread.setDaemon(true);
        receiveThread.start();
    }

    private void sendVideo(Socket socket, Webcam webcam) {
        try (OutputStream output = socket.getOutputStream()) {
            while (running && !socket.isClosed()) {
                BufferedImage image = webcam.getImage();
                if (image == null) continue;

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(image, "JPG", baos);
                    byte[] bytes = baos.toByteArray();

                    output.write(bytes.length >> 24);
                    output.write(bytes.length >> 16);
                    output.write(bytes.length >> 8);
                    output.write(bytes.length);

                    output.write(bytes);
                    output.flush();

                    Thread.sleep(33);
                }
            }
        } catch (IOException | InterruptedException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    private void receiveVideo(Socket socket, JLabel label) {
        try (InputStream input = socket.getInputStream()) {
            byte[] sizeBuffer = new byte[4];
            byte[] imageBuffer = new byte[1024 * 1024];

            while (running && !socket.isClosed()) {
                if (input.read(sizeBuffer) != 4) break;

                int length = ((sizeBuffer[0] & 0xFF) << 24) |
                             ((sizeBuffer[1] & 0xFF) << 16) |
                             ((sizeBuffer[2] & 0xFF) << 8) |
                             (sizeBuffer[3] & 0xFF);

                if (length > imageBuffer.length) {
                    System.err.println("Frame demasiado grande: " + length);
                    continue;
                }

                int totalRead = 0;
                while (totalRead < length) {
                    int read = input.read(imageBuffer, totalRead, length - totalRead);
                    if (read == -1) return;
                    totalRead += read;
                }

                try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBuffer, 0, length)) {
                    BufferedImage image = ImageIO.read(bais);
                    if (image != null) {
                        SwingUtilities.invokeLater(() -> label.setIcon(new ImageIcon(image)));
                    }
                }
            }
        } catch (IOException e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    private void cleanup() {
        running = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (webcam != null) {
                webcam.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ClienteVideo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}