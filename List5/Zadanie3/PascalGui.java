import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PascalGui {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        
        JFrame frame = new JFrame("Wywoływanie C++ ze Swinga");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        
        Font guiFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

        JLabel inputRowLabel = new JLabel("Podaj numer wiersza:");
        inputRowLabel.setFont(guiFont);
        topPanel.add(inputRowLabel);

        JTextField inputRowField = new JTextField(5);
        inputRowField.setFont(guiFont);
        topPanel.add(inputRowField);

        JLabel inputElemLabel = new JLabel("Podaj numer elementu:");
        inputElemLabel.setFont(guiFont);
        topPanel.add(inputElemLabel);

        JTextField inputElemField = new JTextField(5);
        inputElemField.setFont(guiFont);
        topPanel.add(inputElemField);

        JButton runBtn = new JButton("Uruchom kod C++");
        runBtn.setFont(guiFont);
        topPanel.add(runBtn);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
        
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String rowNumber = inputRowField.getText().trim();
                String elemNumber = inputElemField.getText().trim();

                if (rowNumber.isEmpty()) {
                    resultArea.setText("Proszę wpisać numer wiersza!");
                    return;
                }
                if (elemNumber.isEmpty()) {
                    resultArea.setText("Proszę wpisać numer elementu!");
                    return;
                }

                String cppOutput = runCppProgram(rowNumber, elemNumber);
                resultArea.setText(cppOutput);
            }
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(resultArea, BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static String runCppProgram(String row, String elem) {
        StringBuilder output = new StringBuilder();

        try {
            
            String pathToCppExe = "../../List3/Zadanie1C/zad1c";

            ProcessBuilder builder = new ProcessBuilder(pathToCppExe, row, elem);
            
            builder.redirectErrorStream(true); 

            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            output.append("\n[Zakończono z kodem: ").append(exitCode).append("]");

        } catch (Exception ex) {
            output.append("Wystąpił błąd podczas uruchamiania pliku C++:\n");
            output.append(ex.getMessage());
        }

        return output.toString();
    }
}