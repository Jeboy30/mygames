import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 480;
    private static final int GRIDSIZE = 20;
    private static final int GRID_WIDTH = SCREEN_WIDTH / GRIDSIZE;
    private static final int GRID_HEIGHT = SCREEN_HEIGHT / GRIDSIZE;
    private static final int FPS = 100;

    private final Color GREEN = new Color(0, 255, 0);
    private final Color RED = new Color(255, 0, 0);
    private final Color BLACK = new Color(0, 0, 0);
    private final Color BLUE = new Color(0, 0, 255);
    private final Color YELLOW = new Color(255, 255, 0);

    private final int[] UP = {0, -1};
    private final int[] DOWN = {0, 1};
    private final int[] LEFT = {-1, 0};
    private final int[] RIGHT = {1, 0};

    private ArrayList<Point> snake;
    private Point food;
    private int[] direction;
    private int score;
    private int highScore;
    private boolean paused;
    private boolean gameOver;
    private boolean running;

    private Timer timer;
    private JFrame frame;
    private JButton startButton;
    private Image backgroundImage;

    public SnakeGame() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        try {
            backgroundImage = ImageIO.read(Files.newInputStream(Paths.get("C:\\Users\\Jezza\\OneDrive\\Desktop\\All Folder Files\\mygame\\snake.png")));
        } catch (IOException e) {
            System.out.println("Unable to load background image: " + e.getMessage());
            backgroundImage = null;
        }

        startScreen();
    }

    private void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
        direction = randomDirection();
        food = randomFood();
        score = 0;
        paused = false;
        gameOver = false;
        running = true;
        timer = new Timer(FPS, this);
        timer.start();
        startButton.setVisible(false); // Hide start button when game starts
    }

    private int[] randomDirection() {
        Random rand = new Random();
        int[][] directions = {UP, DOWN, LEFT, RIGHT};
        return directions[rand.nextInt(directions.length)];
    }

    private Point randomFood() {
        Random rand = new Random();
        return new Point(rand.nextInt(GRID_WIDTH), rand.nextInt(GRID_HEIGHT));
    }

    private void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && direction != DOWN) {
            direction = UP;
        } else if (key == KeyEvent.VK_DOWN && direction != UP) {
            direction = DOWN;
        } else if (key == KeyEvent.VK_LEFT && direction != RIGHT) {
            direction = LEFT;
        } else if (key == KeyEvent.VK_RIGHT && direction != LEFT) {
            direction = RIGHT;
        } else if (key == KeyEvent.VK_SPACE) {
            if (gameOver) {
                initGame();
            } else {
                paused = !paused;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused && !gameOver) {
            moveSnake();
            checkCollisions();
        }
        repaint();
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x + direction[0], head.y + direction[1]);

        if (newHead.equals(food)) {
            snake.add(0, newHead);
            food = randomFood();
            score += 10;
            if (score > highScore) {
                highScore = score;
            }
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT || snake.subList(1, snake.size()).contains(head)) {
            gameOver = true;
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!running) {
            drawStartScreen(g);
        } else {
            drawGrid(g);
            drawSnake(g);
            drawFood(g);
            displayScore(g);
            if (paused) {
                displayPauseMessage(g);
            }
            if (gameOver) {
                displayGameOver(g);
            }
        }
    }

    private void drawStartScreen(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        } else {
            g.setColor(BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Snake Game", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 - 100);
    }

    private void drawGrid(Graphics g) {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                g.setColor(BLACK);
                g.drawRect(x * GRIDSIZE, y * GRIDSIZE, GRIDSIZE, GRIDSIZE);
            }
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(GREEN);
        for (Point segment : snake) {
            g.fillRect(segment.x * GRIDSIZE, segment.y * GRIDSIZE, GRIDSIZE, GRIDSIZE);
        }
        drawSnakeEyes(g, snake.get(0));
    }

    private void drawSnakeEyes(Graphics g, Point head) {
        g.setColor(Color.WHITE);
        int eyeOffset = GRIDSIZE / 3;
        int eyeRadius = 3;
        g.fillOval(head.x * GRIDSIZE + eyeOffset, head.y * GRIDSIZE + eyeOffset, eyeRadius, eyeRadius);
        g.fillOval(head.x * GRIDSIZE + GRIDSIZE - eyeOffset, head.y * GRIDSIZE + eyeOffset, eyeRadius, eyeRadius);
    }

    private void drawFood(Graphics g) {
        g.setColor(YELLOW);
        g.fillRect(food.x * GRIDSIZE, food.y * GRIDSIZE, GRIDSIZE, GRIDSIZE);
    }

    private void displayScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);
        g.drawString("High Score: " + highScore, SCREEN_WIDTH - 150, 10);
    }

    private void displayPauseMessage(Graphics g) {
        g.setColor(RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Paused", SCREEN_WIDTH / 2 - 100, SCREEN_HEIGHT / 2);
    }

    private void displayGameOver(Graphics g) {
        g.setColor(RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("Game Over", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2);
    }

    private void startScreen() {
        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setBounds(SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2 - 25, 100, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGame();
            }
        });

        setLayout(null);
        add(startButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}
