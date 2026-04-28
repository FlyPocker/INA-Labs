import java.awt.*;
import java.awt.event.*;

class MyWindowAdapter extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}

class PascalFrame extends Frame {
    TextField inputField;
    Button generateBtn;
    TextArea textArea;
    Label inputLabel;

    PascalFrame() {
        super("Trójkąt Pascala ASCII");
        setBounds(100, 100, 1500, 900);
        addWindowListener(new MyWindowAdapter());
        setLayout(new BorderLayout());
        
        Font bigFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        
        Panel topPanel = new Panel();
        
        inputLabel = new Label("Liczba wierszy:");
        inputLabel.setFont(bigFont);
        topPanel.add(inputLabel);
        
        inputField = new TextField("10", 5);
        inputField.setFont(bigFont);
        topPanel.add(inputField);
        
        generateBtn = new Button("Generuj Trójkąt");
        generateBtn.setFont(bigFont);
        topPanel.add(generateBtn);

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionGenerate();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
    }

    public void actionGenerate() {
        int rows;
        try {
            rows = Integer.parseInt(inputField.getText().trim());
        } catch (NumberFormatException ex) {
            textArea.setText("Proszę podać poprawną liczbę.");
            return;
        }

        if (rows < 1) return;

        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows - i; j++) {
                sb.append("   ");
            }
            int number = 1;
            for (int j = 0; j <= i; j++) {
                sb.append(String.format("%-6d", number));
                number = number * (i - j) / (j + 1);
            }
            sb.append("\n");
        }

        String resText = sb.toString();
    
        adjustFontSize(rows);    
        textArea.setText(resText);
    }

    private void adjustFontSize(int rows) {
        int maxLineSize = rows * 6;
        int areaWidth = textArea.getWidth() - 40;
        int areaHeight = textArea.getHeight() - 40;

        float refSize = 10f; 
        Font testFont = textArea.getFont().deriveFont(refSize);
        FontMetrics metrics = textArea.getFontMetrics(testFont);

        StringBuilder tempLine = new StringBuilder();
        for(int i = 0; i < maxLineSize; i++) tempLine.append("A");

        int testWidth = metrics.stringWidth(tempLine.toString());
        int testHeight = metrics.getHeight() * rows;

        if (testWidth == 0 || testHeight == 0) return;

        float propWidth = (float) areaWidth / testWidth;
        float propHeight = (float) areaHeight / testHeight;

        float propMax = Math.min(propWidth, propHeight);

        float targetSize = (refSize * propMax) * 0.95f;

        textArea.setFont(testFont.deriveFont(targetSize));
    }
}

public class Main {
    public static void main(String[] args) {
        PascalFrame frame = new PascalFrame();
        frame.setVisible(true);
    }
}