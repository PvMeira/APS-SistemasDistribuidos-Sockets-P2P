package chatInterface;

import serverOCnfig.Conection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by pvmeira on 15/06/17.
 */
public class ChatWindow extends JFrame implements Observer {

    private Conection conection;

    private javax.swing.JTextArea jTextArea;
    private javax.swing.JButton sendButton;
    private javax.swing.JScrollPane pane1;
    private javax.swing.JScrollPane pane2;
    private javax.swing.JTextArea textArea;

    public ChatWindow(Conection connection) {
        super("P2P - Chat :: APS-3 Sockets ");
        this.conection = connection;
        initComponents();
        connection.addObserver(this);
        write("New chat starter with :  " + connection.getIp() + " : " + connection.getPort());
        textArea.requestFocusInWindow();
    }

    private void send() {
        if (!textArea.getText().isEmpty()) {
            conection.send(textArea.getText());
            write("You send : " + textArea.getText());
            textArea.setText("");
        }
    }

    private void write(String text) {
        jTextArea.append(text + "\n");
        if (!jTextArea.getText().isEmpty() && !jTextArea.isFocusOwner()) {
            jTextArea.setCaretPosition(jTextArea.getText().length() - 1);
        }

    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pane1 = new JScrollPane();
        jTextArea = new JTextArea();
        pane2 = new JScrollPane();
        textArea = new JTextArea();
        sendButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jTextArea.setEditable(false);
        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        pane1.setViewportView(jTextArea);

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                messaTextArea(evt);
            }
        });
        pane2.setViewportView(textArea);

        sendButton.setText("Send");
        sendButton.addActionListener(evt -> sendButton(evt));

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pane1)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pane2, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pane1, GroupLayout.PREFERRED_SIZE, 349, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pane2)
                                        .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }

    private void sendButton(ActionEvent evt) {
        send();
    }

    private void messaTextArea(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            send();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        write(conection.getMessage());
    }
}
