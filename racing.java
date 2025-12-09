import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class racing {
    // EXACT same variables as Python
    static int carx = -100;
    static int line = 1;
    static int obst1x = -100;
    static int obststart = 200;
    static int obsty = obststart;
    static int cary = -100;
    static Random random = new Random();
    
    // Turtle graphics components
    static JFrame frame;
    static DrawingPanel panel;
    static Timer obstacleTimer;
    static int frameCount = 0;
    
    // Store shapes for drawing
    static java.util.List<Point> carShape = new java.util.ArrayList<>();
    static java.util.List<Point> obstacleShape = new java.util.ArrayList<>();
    
    public static void main(String[] args) {
        // t.setup(300, 500)
        frame = new JFrame();
        panel = new DrawingPanel();
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setVisible(true);
        
        // t.penup() - handled by not drawing lines
        // t.hideturtle() - not needed in Java
        // t.tracer(0, 0) - we handle updates manually
        // import random - done above
        
        // Initial car drawing
        car();
        
        // Setup controls
        setupControls();
        
        // t.listen()
        panel.requestFocusInWindow();
        
        // Start game loop (while 1 == 1) but slower
        Timer gameLoop = new Timer(100, new ActionListener() { // Changed from 16ms to 100ms (10 FPS)
            public void actionPerformed(ActionEvent e) {
                frameCount++;
                
                // Only move obstacle every 5 frames (about 200ms like original)
                if (frameCount % 5 == 0) {
                    // Move obstacle down
                    int mobsty = obsty - 20;
                    obsty = mobsty;
                    
                    // Check if obstacle needs reset
                    if (obsty == -120) {
                        obsty = obststart;
                        int randint = random.nextInt(2) + 1;
                        
                        if (randint == 1) {
                            obst1x = -100;
                        } else if (randint == 2) {
                            obst1x = 100;
                        }
                    }
                }
                
                car();
            }
        });
        gameLoop.start();
        
        // t.mainloop()
        // Java Swing continues automatically
    }
    
    // EXACT same car() function
    static void car() {
        // Determine car position based on line
        int currentCarx;
        if (line == 1) {
            currentCarx = -100;
        } else {
            currentCarx = 100;
        }
        
        // t.clear()
        carShape.clear();
        
        // Start at car position
        int x = currentCarx;
        int y = cary;
        
        // t.goto(carx, cary)
        // t.pendown()
        // t.setheading(90)
        // t.begin_fill()
        
        // Starting point
        carShape.add(new Point(x, y));
        
        // t.forward(40) - heading 90
        x = currentCarx;
        y = cary + 40;
        carShape.add(new Point(x, y));
        
        // t.right(45) - now heading 135
        // t.forward(20)
        double radians = Math.toRadians(135);
        x = currentCarx + (int)(20 * Math.cos(radians));
        y = cary + 40 + (int)(20 * Math.sin(radians));
        carShape.add(new Point(x, y));
        
        // t.right(90) - now heading 225
        // t.forward(20)
        radians = Math.toRadians(225);
        x = currentCarx + (int)(20 * Math.cos(Math.toRadians(135))) + (int)(20 * Math.cos(radians));
        y = cary + 40 + (int)(20 * Math.sin(Math.toRadians(135))) + (int)(20 * Math.sin(radians));
        carShape.add(new Point(x, y));
        
        // t.right(45) - now heading 270
        // t.forward(40)
        x = currentCarx + (int)(20 * Math.cos(Math.toRadians(135))) + (int)(20 * Math.cos(Math.toRadians(225)));
        y = cary + 40 + (int)(20 * Math.sin(Math.toRadians(135))) + (int)(20 * Math.sin(Math.toRadians(225))) - 40;
        carShape.add(new Point(x, y));
        
        // t.right(90) - now heading 180
        // t.forward(29)
        x = currentCarx + (int)(20 * Math.cos(Math.toRadians(135))) + (int)(20 * Math.cos(Math.toRadians(225))) - 29;
        y = cary + 40 + (int)(20 * Math.sin(Math.toRadians(135))) + (int)(20 * Math.sin(Math.toRadians(225))) - 40;
        carShape.add(new Point(x, y));
        
        // t.end_fill()
        // t.penup()
        
        // Draw obstacle
        obstacle();
        
        // Check collision
        checkCollision();
        
        // t.update()
        panel.repaint();
    }
    
    // EXACT same obstacle() function
    static void obstacle() {
        // Clear previous obstacle shape
        obstacleShape.clear();
        
        // t.goto(obst1x, obsty)
        // t.pendown()
        // t.setheading(90)
        // t.begin_fill()
        
        int x = obst1x;
        int y = obsty;
        
        // Starting point
        obstacleShape.add(new Point(x, y));
        
        // t.forward(40) - heading 90
        x = obst1x;
        y = obsty + 40;
        obstacleShape.add(new Point(x, y));
        
        // t.right(45) - now heading 135
        // t.forward(20)
        double radians = Math.toRadians(135);
        x = obst1x + (int)(20 * Math.cos(radians));
        y = obsty + 40 + (int)(20 * Math.sin(radians));
        obstacleShape.add(new Point(x, y));
        
        // t.right(90) - now heading 225
        // t.forward(20)
        radians = Math.toRadians(225);
        x = obst1x + (int)(20 * Math.cos(Math.toRadians(135))) + (int)(20 * Math.cos(radians));
        y = obsty + 40 + (int)(20 * Math.sin(Math.toRadians(135))) + (int)(20 * Math.sin(radians));
        obstacleShape.add(new Point(x, y));
        
        // t.right(45) - now heading 270
        // t.forward(40)
        x = obst1x + (int)(20 * Math.cos(Math.toRadians(135))) + (int)(20 * Math.cos(Math.toRadians(225)));
        y = obsty + 40 + (int)(20 * Math.sin(Math.toRadians(135))) + (int)(20 * Math.sin(Math.toRadians(225))) - 40;
        obstacleShape.add(new Point(x, y));
        
        // t.right(90) - now heading 180
        // t.forward(29)
        x = obst1x + (int)(20 * Math.cos(Math.toRadians(135))) + (int)(20 * Math.cos(Math.toRadians(225))) - 29;
        y = obsty + 40 + (int)(20 * Math.sin(Math.toRadians(135))) + (int)(20 * Math.sin(Math.toRadians(225))) - 40;
        obstacleShape.add(new Point(x, y));
        
        // t.end_fill()
        // t.penup()
    }
    
    static void checkCollision() {
        // Determine current car position
        int current_carx;
        if (line == 1) {
            current_carx = -100;
        } else {
            current_carx = 100;
        }
        
        // EXACT same collision detection
        if (obst1x == current_carx && cary <= obsty && obsty <= cary + 40) {
            System.exit(0);
        }
    }
    
    // EXACT same left() function
    static void left() {
        if (line == 2) {
            line = 1;
            car();
        }
    }
    
    // EXACT same right() function
    static void right() {
        if (line == 1) {
            line = 2;
            car();
        }
    }
    
    // EXACT same autoswitch() function
    static void autoswitch(int x, int y) {
        if (line == 1) {
            line = 2;
            car();
        } else if (line == 2) {
            line = 1;
            car();
        }
    }
    
    // EXACT same autoswitchkeyboard() function
    static void autoswitchkeyboard() {
        if (line == 1) {
            line = 2;
            car();
        } else if (line == 2) {
            line = 1;
            car();
        }
    }
    
    static void setupControls() {
        // Keyboard input map
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        
        // t.onkey(autoswitchkeyboard, "space")
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "space");
        actionMap.put("space", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                autoswitchkeyboard();
            }
        });
        
        // t.onkey(left, "a")
        inputMap.put(KeyStroke.getKeyStroke("A"), "a");
        actionMap.put("a", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                left();
            }
        });
        
        // t.onkey(right, "d")
        inputMap.put(KeyStroke.getKeyStroke("D"), "d");
        actionMap.put("d", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                right();
            }
        });
        
        // t.onscreenclick(autoswitch)
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // Convert screen coordinates to turtle coordinates
                int turtleX = e.getX() - panel.getWidth() / 2;
                int turtleY = panel.getHeight() / 2 - e.getY();
                autoswitch(turtleX, turtleY);
            }
        });
    }
    
    // Drawing panel
    static class DrawingPanel extends JPanel {
        public DrawingPanel() {
            setBackground(Color.WHITE);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Transform to match turtle coordinates
            // Center origin, Y up
            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.scale(1, -1);
            
            // Draw car
            if (!carShape.isEmpty()) {
                g2d.setColor(Color.BLUE);
                drawShape(g2d, carShape);
            }
            
            // Draw obstacle
            if (!obstacleShape.isEmpty()) {
                g2d.setColor(Color.RED);
                drawShape(g2d, obstacleShape);
            }
        }
        
        void drawShape(Graphics2D g2d, java.util.List<Point> shape) {
            if (shape.size() > 0) {
                int[] xPoints = new int[shape.size()];
                int[] yPoints = new int[shape.size()];
                
                for (int i = 0; i < shape.size(); i++) {
                    Point p = shape.get(i);
                    xPoints[i] = p.x;
                    yPoints[i] = p.y;
                }
                
                g2d.fillPolygon(xPoints, yPoints, shape.size());
            }
        }
    }
    
    // Simple Point class
    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}