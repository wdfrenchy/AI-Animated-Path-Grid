import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
//import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.awt.Font;
//import java.awt.Point;

public class PathGrid {
    private JFrame frame;

    public PathGrid() {
        frame = new JFrame("Path Finding");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new TheGrid(frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... argv) {
        new PathGrid();
    }

    public static class TheGrid extends JPanel implements Runnable, MouseListener  {

        private Thread animator;
        Dimension d;
        String str = "";
        int xPos = 0;
        int yPos = 0;
        int startSpot = 0;
        int stopSpot = 4;
        Point stopPoint = new Point(0,6);
        int fontSize = 20;
        int tick = 0;
        int step = 0;
        List<Integer> path = new ArrayList<>();
        ArrayList<Integer> steps = new ArrayList<>();

       // Point startPoint = new Point(0, 0);
        
      //  Point stopPoint = new Point(0,6);
      CreateGraph grd = new CreateGraph();
        
        Color co = new Color(255,255,255);
        Color[] coArray = {
                new Color(255,255,255), new Color(0,255,255), new Color(255,255,0),new Color(255,0,255),new Color(0,0,255)	
            };

        public TheGrid (Dimension dimension) {
            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            addKeyListener(new TAdapter());
            setFocusable(true);
            d = getSize();

            calcPath();

            //for animating the screen - you won't need to edit
            if (animator == null) {
                animator = new Thread(this);
                animator.start();
            }
            setDoubleBuffered(true);
        }

       

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.black);
            g2.fillRect(0, 0,(int)d.getWidth() , (int)d.getHeight());
            tick++;

            g2.setColor(Color.white);
            g2.setColor(co);
            int startx = 10;
            int starty = 100;
            int counter = 0;
            int stepsCounter = 0;

            if(tick % 20 == 0){
                step++;
                if(step == path.size() || step > path.size()){
                    step = 0;
                }
            }

            for(int r = 0; r < 6; r++){
                for(int c = 0; c < 8; c++){
                    g2.setColor(Color.white);
                    g2.drawRect(startx, starty, 50 ,50);
                    if(grd.grid[r][c]==startSpot){
                        g2.setColor(Color.red);
                        g2.fillRect(startx, starty, 50 ,50);
                    }
                    if(grd.grid[r][c]==stopSpot){
                        g2.setColor(Color.green);
                        g2.fillRect(startx, starty, 50 ,50);
                    }

                    if(grd.grid[r][c]==-1){
                        g2.setColor(Color.gray);
                        g2.fillRect(startx, starty, 50 ,50);
                    }
                    if(grd.grid[r][c]==path.get(step)){
                        g2.setColor(Color.orange);
                        g2.fillRect(startx, starty, 50 ,50);
                        steps.add(grd.grid[r][c]);
                        stepsCounter++;
                    }
                    if(stepsCounter < steps.size()){
                        if(grd.grid[r][c] == steps.get(stepsCounter)){
                            g2.setColor(Color.yellow);
                            g2.fillRect(startx, starty, 50 ,50);
                        }
                    }

                    startx += 50;

                    counter++;
                }
                startx = 10;
                starty += 50;
            }

            /*
             * come up with a mechanism for animating?
             * have a counter that you mod by a certain number
             * and do something when result is zero
             */

            //g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
            //g2.drawString("String " + str,20,40);

        }

        public void mousePressed(MouseEvent e) {
            xPos = e.getX();
            yPos = e.getY();

            int startx = 10;
            int starty = 100;

            xPos = (xPos - startx)/50;

            yPos = (yPos - starty)/50;

            System.out.println(" c " + xPos + " r " + yPos );
            //System.out.println(" c " + xPos + " r " + yPos + " " + grd.grid[yPos][xPos]);
            
            calcPath();
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        private class TAdapter extends KeyAdapter {

            public void keyReleased(KeyEvent e) {
                int keyr = e.getKeyCode();

            }

            public void keyPressed(KeyEvent e) {

                int kkey = e.getKeyChar();
                String   cc = Character.toString((char) kkey);
                str = " " + kkey;

                //key events related to strings below. You should NOT need
                // int key = e.getKeyCode();
                //String c = KeyEvent.getKeyText(e.getKeyCode());
                // String   c = Character.toString((char) key);

            }
        }//end of adapter

        public void calcPath() {
            path = grd.AStar2(startSpot,stopSpot,stopPoint);
        }

        public void run() {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 37;
            long time = System.currentTimeMillis();
            while (true) {// infinite loop
                // spriteManager.update();
                repaint();
                try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                } // end catch
            } // end while loop
        }// end of run

    }//end of class
}
