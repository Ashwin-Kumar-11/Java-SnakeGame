import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import javax.swing.Timer;


// ----------------- MAIN CLASS -----------------


public class snakeGame{
    public static void main(String[] args){
        new MyFrame();
    }
}


// ----------------- JFRAME -----------------


class MyFrame extends JFrame{
    
    MyFrame(){
        MyPanel panel = new MyPanel();
        this.add(panel);
        setTitle("SnakeGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
    

}
// ----------------- JPANEL -----------------


class MyPanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyparts = 6;
    int applesEaten;
    int appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    MyPanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_HEIGHT,SCREEN_WIDTH));
        this.setBackground(Color.decode("#15151e"));
        setOpaque(true);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // ----------------- FOR STARTING GAME -----------------


    public void startGame(){
            newApple();
            running = true;
            timer = new Timer(DELAY, this); 
            timer.start();
    }

    // ----------------- FOR PAINTING COMPONENT -----------------


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    // ----------------- FOR GRAPHICS -----------------


    public void draw(Graphics g){

        if(running){
                g.setColor(Color.decode("#9241f1"));
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

             for(int i = 0; i < bodyparts; i++){
                 if(i == 0){
                    g.setColor(Color.decode("#ffffff"));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(Color.decode("#b3b3b3"));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("consolas", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString(" " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth(" " + applesEaten))/2, g.getFont().getSize());
        }   
        else{
            gameOver(g);
        }
    }

    // ----------------- FRO MOVING -----------------


    public void move(){
        for(int i=bodyparts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
              y[0] = y[0] - UNIT_SIZE;
              break;
            case 'D':
            y[0] = y[0] + UNIT_SIZE;
            break;
            case 'L':
            x[0] = x[0] - UNIT_SIZE;
            break;
            case 'R':
            x[0] = x[0] + UNIT_SIZE;
            break;
        }
    }

    // ----------------- FOR NEW APPLE GENERATION -----------------


    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; 
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; 
    }

    // ----------------- FOR CHECKING APPLE -----------------


    public void checkApple(){
            if((x[0] == appleX) && (y[0] == appleY)){
                bodyparts++;
                applesEaten++;
                newApple();
            }
    }

    // ----------------- TO CHECK COLLISION -----------------


    public void checkCollision(){

        // checks head collision with body
        for(int i = bodyparts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //checks left collision
        if(x[0] < 0){
            running = false;
        }
        //checks right collision
        if(x[0] > SCREEN_WIDTH){
            running = false;
         }
        //checks top collision
        if(y[0] < 0){
            running = false;
         }
        //checks bottom collision
        if(y[0] == SCREEN_HEIGHT){ // or use > instead of ==
            running = false;
         }

         if(!running){
            timer.stop();
         }
    }

    // ----------------- FOR GAME OVER -----------------


    public void gameOver(Graphics g){

            g.setColor(Color.red);
            g.setFont(new Font("consolas", Font.BOLD, 75));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);
       
            g.setColor(Color.white);
            g.setFont(new Font("consolas", Font.BOLD, 40));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+ applesEaten))/2, SCREEN_HEIGHT/3);
        
            g.setColor(Color.white);
            g.setFont(new Font("consolas", Font.BOLD, 15));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("Press \"SPACE \" to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press \"SPACE \" to restart"))/2,(int)( SCREEN_HEIGHT/1.5));
    }

    // ----------------- FRO RESTARTING -----------------


    public void restartGame(){
        setVisible(false);
        new MyFrame();
    }
    

    // ----------------- FRO FRAME DISPOSAL -----------------


    public void dispose(){
        JFrame parent = (JFrame)this.getTopLevelAncestor();
        parent.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            repaint();
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

// ----------------- TO CHECK KEY IS PRESSED -----------------


    public class MyKeyAdapter extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                restartGame();
            }
           
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                     }
                    break;
                case KeyEvent.VK_RIGHT:
                      if(direction != 'L'){
                        direction = 'R';
                      }
                     break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            
            }
           
        }
    }

}