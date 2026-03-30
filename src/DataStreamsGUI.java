import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsGUI extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchField;
    private JButton loadButton, searchButton, quitButton;
    private String filePath;

    public DataStreamsGUI() {
        setTitle("Data Streams Lab");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        textPanel.add(new JScrollPane(originalTextArea));
        textPanel.add(new JScrollPane(filteredTextArea));

        JPanel controlPanel = new JPanel();
        searchField = new JTextField(20);
        loadButton = new JButton("Load File");
        searchButton = new JButton("Search File");
        quitButton = new JButton("Quit");

        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(loadButton);
        controlPanel.add(searchButton);
        controlPanel.add(quitButton);

        add(textPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);


        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
                String content = lines.collect(Collectors.joining("\n"));
                originalTextArea.setText(content);
                filteredTextArea.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + ex.getMessage());
            }
        }
    }

    private void searchFile() {
        if(filePath == null) {
            JOptionPane.showMessageDialog(this, "Please load a file first!");
            return;
        }
        String searchString = searchField.getText();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            List<String> filteredLines = lines
                    .filter(line -> line.contains(searchString))
                    .collect(Collectors.toList());
            filteredTextArea.setText(String.join("\n", filteredLines));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error searching file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DataStreamsGUI::new);
    }
}