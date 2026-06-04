import storage.FileStorageHandler;
import ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize storage flat text files (creates defaults if missing)
        FileStorageHandler.initializeStorage();

        // Launch the Graphical User Interface on the Swing Event Dispatch Thread (thread-safety best practice)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
