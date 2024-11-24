package Test;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class PvPCardGameGUI {

    public static void main(String[] args) {
        // Main game frame
        JFrame frame = new JFrame("AnimeAttax PvP Card Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(40, 40, 40)); // Dark gray background

        // Header panel for the game title
        JLabel headerLabel = new JLabel("AnimeAttax", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Courier New", Font.BOLD, 36));
        headerLabel.setForeground(new Color(0, 204, 255)); // Bright cyan
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Terminal-like box (center)
        JTextArea terminalTextArea = new JTextArea();
        terminalTextArea.setEditable(false);
        terminalTextArea.setFont(new Font("Courier New", Font.PLAIN, 16));
        terminalTextArea.setBackground(new Color(30, 30, 30));
        terminalTextArea.setForeground(new Color(0, 255, 0));
        terminalTextArea.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 0), 2));
        terminalTextArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane terminalScrollPane = new JScrollPane(terminalTextArea);

        // Redirect System.out and System.err to terminalTextArea
        PrintStream terminalOut = new PrintStream(new TextAreaOutputStream(terminalTextArea));
        System.setOut(terminalOut);
        System.setErr(terminalOut);

        // Input field for player commands
        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBackground(new Color(240, 240, 240));
        inputField.setForeground(Color.BLACK);
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inputField.addActionListener(e -> {
            String command = inputField.getText();
            System.out.println("> " + command);
            inputField.setText("");
            // TODO: Pass command to game logic
        });

        // Player 1 card panel (left)
        JPanel player1Panel = createCardPanel("Hero X", new Color(52, 152, 219), "Strength: 90", "Speed: 80", "Defense: 70");
        // Player 2 card panel (right)
        JPanel player2Panel = createCardPanel("Villain Y", new Color(231, 76, 60), "Strength: 85", "Speed: 85", "Defense: 65");

        // Add components to the main panel
        mainPanel.add(player1Panel, BorderLayout.WEST);
        mainPanel.add(terminalScrollPane, BorderLayout.CENTER);
        mainPanel.add(player2Panel, BorderLayout.EAST);
        mainPanel.add(inputField, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true);

        // Example game output
        System.out.println("Welcome to AnimeAttax PvP Card Game!");
        System.out.println("Player 1, it's your turn.");
    }

    // Helper method to create a styled card panel
    private static JPanel createCardPanel(String cardName, Color cardColor, String... stats) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(200, 300)); // Proportions of a trading card
        cardPanel.setBackground(new Color(40, 40, 40)); // Match the main background
        cardPanel.setOpaque(false);

        // Card itself
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 280));
        card.setBackground(cardColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 5),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Card title
        JLabel cardTitle = new JLabel(cardName, SwingConstants.CENTER);
        cardTitle.setFont(new Font("Arial", Font.BOLD, 20));
        cardTitle.setForeground(Color.WHITE);
        cardTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        card.add(cardTitle, BorderLayout.NORTH);

        // Card image placeholder
        JPanel imagePlaceholder = new JPanel();
        imagePlaceholder.setBackground(new Color(200, 200, 200)); // Placeholder gray
        imagePlaceholder.setPreferredSize(new Dimension(160, 160));
        imagePlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        card.add(imagePlaceholder, BorderLayout.CENTER);

        // Card stats
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(cardColor);
        for (String stat : stats) {
            JLabel statLabel = new JLabel(stat, SwingConstants.CENTER);
            statLabel.setFont(new Font("Courier New", Font.PLAIN, 16));
            statLabel.setForeground(Color.WHITE);
            statsPanel.add(statLabel);
        }
        card.add(statsPanel, BorderLayout.SOUTH);

        cardPanel.add(card, BorderLayout.CENTER);
        return cardPanel;
    }
}

// Custom PrintStream to redirect output to JTextArea
class TextAreaOutputStream extends java.io.OutputStream {
    private final JTextArea textArea;

    public TextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}
