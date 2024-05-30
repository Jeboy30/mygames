import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TicTacToeGame extends JFrame {
    private JButton[] buttons = new JButton[9];
    private JLabel playerXLabel, playerOLabel;
    private JButton resetButton, exitButton, startButton;
    private String currentPlayer = "X";
    private int playerXScore = 0, playerOScore = 0;
    private JPanel mainPanel, gamePanel, scorePanel, controlPanel, bottomPanel;
    private Clip backgroundClip;

    public TicTacToeGame() {
        setPreferredSize(new Dimension(700, 700)); // Set the preferred size of the window
        playBackgroundMusic("C:\\Users\\admin\\Desktop\\Finalproject_games\\mygames\\java\\music\\background.mp3");
        showStartMenu();
    }

    private void playBackgroundMusic(String filePath) {
        try {
            File musicPath = new File(filePath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioInput);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Can't find file");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    private void playSoundEffect(String filePath) {
        try {
            File soundPath = new File(filePath);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Can't find file");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void showStartMenu() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tic-Tac-Toe Clash Duel");
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 206, 209));
        mainPanel.setLayout(new GridLayout(3, 1, 20, 20));

        JLabel titleLabel = new JLabel("Tic-Tac-Toe Clash Duel", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);

        startButton = new JButton("START");
        startButton.setFont(new Font("Tahoma", Font.BOLD, 30));
        startButton.setBackground(new Color(153, 255, 153));
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                playSoundEffect("C:\\Users\\admin\\Desktop\\Finalproject_games\\mygames\\java\\music\\background.mp3");
                showPlayerSelection();
            }
        });

        exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 30));
        exitButton.setBackground(new Color(255, 153, 153));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopBackgroundMusic();
                System.exit(0);
            }
        });

        mainPanel.add(startButton);
        mainPanel.add(exitButton);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null); // Center the frame
    }

    private void showPlayerSelection() {
        mainPanel.removeAll();

        JLabel playerLabel = new JLabel("Select First Player", SwingConstants.CENTER);
        playerLabel.setFont(new Font("Segoe UI", Font.BOLD, 50));
        playerLabel.setForeground(Color.WHITE);
        mainPanel.add(playerLabel);

        JButton playerXButton = new JButton("Player X");
        playerXButton.setFont(new Font("Tahoma", Font.BOLD, 30));
        playerXButton.setBackground(new Color(255, 153, 153));
        playerXButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentPlayer = "X";
                initComponents();
            }
        });

        JButton playerOButton = new JButton("Player O");
        playerOButton.setFont(new Font("Tahoma", Font.BOLD, 30));
        playerOButton.setBackground(new Color(153, 153, 255));
        playerOButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                currentPlayer = "O";
                initComponents();
            }
        });

        mainPanel.add(playerXButton);
        mainPanel.add(playerOButton);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void initComponents() {
        getContentPane().removeAll();

        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 48));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonClickListener(i));
            gamePanel.add(buttons[i]);
        }
        
        scorePanel = new JPanel();
        scorePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.darkGray));
        scorePanel.setLayout(new GridLayout(2, 1));

        playerXLabel = new JLabel("XXXX", SwingConstants.CENTER);
        playerXLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scorePanel.add(new JLabel("Player X:", SwingConstants.RIGHT));
        scorePanel.add(playerXLabel);

        playerOLabel = new JLabel("XXXX", SwingConstants.CENTER);
        playerOLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scorePanel.add(new JLabel("Player O:", SwingConstants.RIGHT));
        scorePanel.add(playerOLabel);

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 2));

        exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Tahoma", Font.BOLD, 24));
        exitButton.setBackground(new Color(255, 153, 153));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopBackgroundMusic();
                System.exit(0);
            }
        });
        controlPanel.add(exitButton);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(scorePanel, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(gamePanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
        pack();
        setLocationRelativeTo(null); // Center the frame
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
            button.setBackground(null); // Reset the background color
        }
        currentPlayer = "X"; // Optionally reset to the default starting player
    }

    private void checkForWin() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = buttons[i].getText();
        }

        String winner = null;

        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].isEmpty()) {
                winner = board[i][0];
            }
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].isEmpty()) {
                winner = board[0][i];
            }
        }

        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].isEmpty()) {
            winner = board[0][0];
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].isEmpty()) {
            winner = board[0][2];
        }

        if (winner != null) {
            JOptionPane.showMessageDialog(this, "Player " + winner + " wins!");
            updateScore(winner);
            resetGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "The game is a draw!");
            resetGame();
        }
    }

    private boolean isBoardFull() {
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void updateScore(String winner) {
        if (winner.equals("X")) {
            playerXScore++;
            playerXLabel.setText(String.valueOf(playerXScore));
        } else {
            playerOScore++;
            playerOLabel.setText(String.valueOf(playerOScore));
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final int index;

        public ButtonClickListener(int index) {
            this.index = index;
        }

        public void actionPerformed(ActionEvent evt) {
            JButton button = buttons[index];
            if (button.getText().isEmpty()) {
                button.setText(currentPlayer);
                button.setEnabled(false);
                button.setBackground(currentPlayer.equals("X") ? Color. PINK: Color.BLUE); // Set button color
                playSoundEffect("path/to/your/click_sound.wav");
                checkForWin();
                currentPlayer = currentPlayer.equals("X") ? "O" : "X";
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TicTacToeGame frame = new TicTacToeGame();
            frame.setVisible(true);
        });
    }
}
