import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class javabird extends JPanel implements ActionListener {
    // Setup
    private static final int WIDTH = 900;
    private static final int HEIGHT = 300;
    
    // Game State Flag
    private boolean gameOver = false;
    
    // Initial positions
    private float bx = -300;
    private float by = 0;
    private float pu1x = 300; // Initial X position of the pipe set
    
    // Bird properties
    private float bird_velocity = 0;
    private final float gravity = -0.5f;
    private final float jump_strength = 10;
    private final int bird_radius = 15;
    
    // Pipe properties
    private final int pipe_width = 40;
    private final int pipe_height = 90;
    private final int gap_size = 100;
    
    // The bottom limit of the screen/ground for collision reference
    private final int GROUND_Y = -140;
    private final int CEILING_Y = 140;
    
    private float upper_pipe_y; // Initial random height for first pipe
    
    private Random random = new Random();
    private Timer timer;
    
    public javabird() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        
        // Initial random height for first pipe
        upper_pipe_y = random.nextInt(101);
        
        // Set up keyboard input
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || 
                    e.getKeyCode() == KeyEvent.VK_UP) {
                    jump();
                }
            }
        });
        
        // Set up mouse click for jumping
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jump();
            }
        });
        
        // Start the game loop
        timer = new Timer(33, this);
        timer.start();
    }
    
    private void bird(Graphics2D g) {
        if (!gameOver) {
            bird_velocity += gravity;
            by += bird_velocity;
            
            // Ground collision
            if (by <= GROUND_Y + bird_radius) {
                by = GROUND_Y + bird_radius;
                bird_velocity = 0;
                gameOver = true;
            }
            
            // Ceiling collision
            if (by >= CEILING_Y - bird_radius) {
                by = CEILING_Y - bird_radius;
                bird_velocity = 0;
                gameOver = true;
            }
        }
        
        // Draw bird (adjust coordinates for Swing coordinate system)
        g.setColor(Color.YELLOW);
        int screenX = (int) (bx + WIDTH/2);
        int screenY = HEIGHT/2 - (int) by; // Flip Y coordinate
        g.fillOval(screenX - bird_radius, screenY - bird_radius, 
                   bird_radius * 2, bird_radius * 2);
    }
    
    private void jump() {
        if (!gameOver) {
            bird_velocity = jump_strength;
        }
    }
    
    private void draw_rect_pipe(Graphics2D g, float px, float py_top_left, int height) {
        // Adjust coordinates for Swing
        int screenX = (int) (px + WIDTH/2);
        int screenY = HEIGHT/2 - (int) py_top_left;
        
        g.setColor(Color.GREEN);
        g.fillRect(screenX, screenY, pipe_width, height);
    }
    
    private void twopipes(Graphics2D g, float px, float upper_pipe_bottom_y) {
        // Upper pipe starts drawing at the top of the gap, going up by pipe_height
        draw_rect_pipe(g, px, upper_pipe_bottom_y + pipe_height, pipe_height);
        
        // Lower pipe starts drawing below the gap, going down by pipe_height
        float lower_pipe_top_y = upper_pipe_bottom_y - gap_size;
        draw_rect_pipe(g, px, lower_pipe_top_y, pipe_height);
    }
    
    private boolean check_pipe_collision() {
        float pipe_left = pu1x;
        float pipe_right = pu1x + pipe_width;
        
        // Calculate the Y boundaries of the gap
        float gap_top_y = upper_pipe_y;
        float gap_bottom_y = gap_top_y - gap_size;
        
        // Check X-axis overlap
        if (bx + bird_radius > pipe_left && bx - bird_radius < pipe_right) {
            // Check Y-axis overlap
            if (by + bird_radius > gap_top_y || by - bird_radius < gap_bottom_y) {
                gameOver = true;
                return true;
            }
        }
        return false;
    }
    
    private void update_game() {
        if (gameOver) {
            timer.stop();
            return;
        }
        
        pu1x -= 10;
        
        if (pu1x < -450) {
            pu1x = 450;
            // Random height for upper pipe *bottom* Y
            upper_pipe_y = random.nextInt(101);
        }
        
        check_pipe_collision();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        
        
        // Draw pipes
        twopipes(g2d, pu1x, upper_pipe_y);
        
        // Draw bird
        bird(g2d);
        
        // Draw game over text
        if (gameOver) {
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "GAME OVER";
            int x = (WIDTH - fm.stringWidth(text)) / 2;
            int y = HEIGHT / 2;
            g2d.drawString(text, x, y);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        update_game();
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flappy Bird");
            javabird game = new javabird();
            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
            
            // Request focus for keyboard input
            game.requestFocusInWindow();
        });
    }
}