package runner;

import chatInterface.ChatWindow;
import serverOCnfig.Conection;

import javax.swing.*;

/**
 * Created by pvmeira on 15/06/17.
 */
public class Runner {

    public static void main(String[] args) {
        String ip = (String) JOptionPane.showInputDialog("Report the  IP", "192.168.0.");
        int port = Integer.parseInt(JOptionPane.showInputDialog("Report the port", "5000"));

        Conection c = new Conection(ip, port);

        ChatWindow window = new ChatWindow(c);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
