package serverOCnfig;

import java.io.IOException;
import java.net.*;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pvmeira on 15/06/17.
 */
public class Conection extends Observable {

    private String ip;
    private int port;
    private String message;

    public Conection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        new Thread(new Recive()).start();
    }


    public void send(String text) {
        new Thread(new Envia(text)).start();
    }

    public void nottify(String message) {
        this.message = message;
        setChanged();
        notifyObservers();
    }

    class Recive implements Runnable {

        byte[] dateToRecive = new byte[255];
        boolean error = false;
        DatagramSocket socket = null;

        @Override
        public void run() {
            while (true) {
                try {
                    socket = new DatagramSocket(getPort());
                } catch (SocketException ex) {
                    Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
                }
                error = false;
                while (!error) {
                    DatagramPacket recivePackage = new DatagramPacket(dateToRecive, dateToRecive.length);
                    try {
                        socket.receive(recivePackage);
                        byte[] b = recivePackage.getData();
                        String s = "";
                        for (int i = 0; i < b.length; i++) {
                            if (b[i] != 0) {
                                s += (char) b[i];
                            }
                        }
                        String name = recivePackage.getAddress().toString() + " says :";
                        nottify(name + s);
                    } catch (Exception e) {
                        System.out.println("error");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        error = true;
                        continue;
                    }
                }
            }
        }
    }

    class Envia implements Runnable {

        String text;

        public Envia(String text) {
            this.text = text;
        }

        @Override
        public void run() {

            byte[] data = text.getBytes();

            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress addr = InetAddress.getByName(getIp());
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length, addr, getPort());
                clientSocket.send(datagramPacket);
                clientSocket.close();
            } catch (SocketException ex) {
                Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Conection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
