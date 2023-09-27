/*
 * Display for 2048
 * Macy Busby
 * 5/7/2022
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.*;
import java.util.logging.*;
import java.util.*;

public class TileDisplay extends JPanel{
    
    private int titleStartY = 25;
    private int buffer = 5;
    
    private int cellSize = 70;
    private int spaceWidth = 10;
    private int curveRadius = 15;
    private int textSize = 35;
    
    private int startX = 25;
    private int startY = 175;
    
    private int current_x;
    private int current_y;
    private int num_incs = 10;
    private int[][][] currentCoords;
    private int animationSpeed = 5;
    
    public final int REST = 0;
    public final int MOVE = 1;
    private int state = REST;
    
    private TileGame game;
    private TileWindow win;
    
    private Color backgroundColor = new Color(131, 104, 83);
    private Color panelColor = new Color(167, 139, 117);
    
    private Color[] tileColors = {new Color(242, 220, 204), new Color(238, 205, 141),
        new Color(227, 180, 104), new Color(216, 157, 73), new Color(227, 128, 80),
        new Color(169, 99, 57), new Color(162, 99, 97), new Color(208, 133, 119),
        new Color(211, 161, 158), new Color(172, 129, 152), new Color(140, 105, 138),
        new Color(129, 107, 141), new Color(109, 103, 145), new Color(160, 173, 189),
        new Color(124, 145, 154), new Color(98, 147, 149)};
    
    public TileDisplay(TileGame game, TileWindow win){
        this.game = game;
        this.win = win;
        this.setBackground(panelColor);
        
        this.addKeyListener( new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
                translateKey(ke);
            }
        });
        
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }
    
    public void translateKey(KeyEvent ke){
        int code = ke.getKeyCode();
        char movement = ' ';
        
        final int KEY_LEFT = 37;
        final int KEY_RIGHT = 39;
        final int KEY_DOWN = 40;
        final int KEY_UP = 38;
        
        final int KEY_A = 65;
        final int KEY_D = 68;
        final int KEY_S = 83;
        final int KEY_W = 87;
        
        final int KEY_N = 78;
        
            switch(code){
                case KEY_UP:
                case KEY_W:
                    movement = 'U';
                    break;
                case KEY_RIGHT:
                case KEY_D:
                    movement = 'R';
                    break;
                case KEY_LEFT:
                case KEY_A:
                    movement = 'L';
                    break;
                case KEY_DOWN:
                case KEY_S:
                    movement = 'D';
                    break;
                case KEY_N:
                    break;
            }
            game.makeMove(movement);
            doTransition(movement);
            game.spawnTile();
            repaint();
    }
    
    public void doTransition(char direction){
        state = MOVE;
        
        currentCoords = new int[2][game.getRows()*2][game.getCols()];
        int[][][] steps = new int[2][game.getRows()*2][game.getCols()];
        
        int x_step = 0;
        int y_step = 0;
                
        for(int row = 0; row < game.getRows(); row++ ){
            for (int col = 0; col < game.getCols(); col ++){
        
                int newRow = row;
                int newCol = col;

                int oldRow = row;
                int oldCol = col;
                
                ArrayList tiles = new ArrayList();
                
                if(game.moves[row][col] >=0)
                    tiles.add(game.moves[row][col]);
                else{
                    tiles.add(game.moves[row][col]*(-1)/10);
                    tiles.add(game.moves[row][col]*(-1)%10);
                }

                for (int counter = 0; tiles.size() > 0; counter++){
                    switch(direction){
                        case 'U':
                            oldRow = row + (int)tiles.get(0);
                            break;
                        case 'D':
                            oldRow = row - (int)tiles.get(0);
                            break;
                        case 'R':
                            oldCol = col - (int)tiles.get(0);
                            break;
                        case 'L':
                            oldCol = col + (int)tiles.get(0);
                            break;
                    }
                    
                    int x_dis = (oldCol-newCol)*cellSize;
                    int y_dis = (oldRow-newRow)*cellSize;

                    x_step = x_dis/num_incs;
                    y_step = y_dis/num_incs;

                    current_x = startX + oldCol*cellSize;
                    current_y = startY + oldRow*cellSize;

                    currentCoords[0][row+game.getRows()*counter][col] = current_x;
                    currentCoords[1][row+game.getRows()*counter][col] = current_y;

                    steps[0][row+game.getRows()*counter][col] = x_step;
                    steps[1][row+game.getRows()*counter][col] = y_step;
                    
                    tiles.remove(0);
                }
            }
        }
        
        for(int step = 0; step < num_incs; step++){
            try{
                for(int row = 0; row < game.getRows(); row++ ){
                    for (int col = 0; col < game.getCols(); col ++){
                        currentCoords[0][row][col] -= steps[0][row][col];
                        currentCoords[1][row][col] -= steps[1][row][col];
                        
                        if(currentCoords[0][row + game.getRows()][col] != 0){
                            currentCoords[0][row + game.getRows()][col] -= 
                                    steps[0][row + game.getRows()][col];
                            currentCoords[1][row + game.getRows()][col] -= 
                                    steps[1][row + game.getRows()][col];
                        }
                    }
                }
                this.paintImmediately(0, 0, 500, 500);
                Thread.sleep(animationSpeed);
            }
            catch(InterruptedException ex){
                Logger.getLogger(TileGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        repaint();
        state = REST;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawBackground(g);
        drawSquares(g);
        drawMessage(g);
        
        if (state == REST)
            drawTiles(g);
        else
            drawMovingTiles(g);
        
        //if (game.gameOver())
        //    win.initEndScreen();
    }
    
    public void drawBackground(Graphics g){
        int start_x = startX;
        int start_y = startY;
        
        int rectWidth = game.getCols()*cellSize + (game.getCols() + 1)*spaceWidth;
        int rectHeight = game.getRows()*cellSize + (game.getRows() + 1)*spaceWidth;
        
        g.setColor(backgroundColor);
        g.fillRect(start_x + curveRadius, start_y,
                   rectWidth - curveRadius*2, rectHeight);
        
        g.fillRect(start_x, start_y + curveRadius,
                   rectWidth, rectHeight - curveRadius*2);
        
        g.fillOval(start_x, start_y, curveRadius*2, curveRadius*2);
        
        start_x += rectWidth - curveRadius*2;
        g.fillOval(start_x, start_y, curveRadius*2, curveRadius*2);
        
        start_y += rectHeight - curveRadius*2;
        g.fillOval(start_x, start_y, curveRadius*2, curveRadius*2);
        
        start_x -= rectWidth - curveRadius*2;
        g.fillOval(start_x, start_y, curveRadius*2, curveRadius*2);
    }
    
    public void drawSquares(Graphics g){
        int start_x = startX + spaceWidth;
        int start_y = startY + spaceWidth;
        int curve_x = start_x;
        int curve_y = start_y;
        
        for(int row = 0; row < game.getRows(); row++ ){
            for (int col = 0; col < game.getCols(); col ++){
                
                g.setColor(tileColors[0]);
                
                g.fillRect(start_x + curveRadius, start_y,
                           cellSize - curveRadius*2, cellSize);
                g.fillRect(start_x, start_y + curveRadius,
                           cellSize, cellSize - curveRadius*2);

                g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                curve_x += cellSize - curveRadius*2;
                g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                curve_y += cellSize - curveRadius*2;
                g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                curve_x -= cellSize - curveRadius*2;
                g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                
                start_x += spaceWidth+cellSize;
                curve_x = start_x;
                curve_y = start_y;
            }

                start_y += spaceWidth+cellSize;
                start_x = startX + spaceWidth;
                curve_x = start_x;
                curve_y = start_y;
        }
    }
    
    public void drawMessage(Graphics g){
        int titleSize = 40;
        int introSize = 15;
        int scoreSize = 20;
        int messageSize = 35;
        
        String title = "2048";
        Font titleFont = new Font("Verdana", Font.BOLD, titleSize);
        
        String intro1 = "Combine tiles to increase your score!";
        String intro2 = "Try to get to a score of 2048!";
        Font introFont = new Font("Verdana", Font.BOLD, introSize);
        
        String score = "Score: "+game.getScore();
        Font scoreFont = new Font("Verdana", Font.BOLD, scoreSize);
        
        String message = "Game over!";
        Font messageFont = new Font("Verdana", Font.BOLD, messageSize);
        
        int start_x = startX;
        int start_y = titleStartY + titleSize;
        g.setColor(Color.white);
        
        g.setFont(titleFont);
        g.drawString(title, start_x, start_y);
        
        start_y += buffer + introSize;
        g.setFont(introFont);
        g.drawString(intro1, start_x, start_y);
        start_y += introSize;
        g.drawString(intro2, start_x, start_y);
        
        start_y += buffer + scoreSize;
        g.setFont(scoreFont);
        g.drawString(score, start_x, start_y);
        
        if(game.gameOver()){
            start_y += buffer + messageSize;
            g.setFont(messageFont);
            g.drawString(message, start_x, start_y);
        }
    }
    
    public void drawTiles(Graphics g){
        int start_x = startX + spaceWidth;
        int start_y = startY + spaceWidth;
        int curve_x = start_x;
        int curve_y = start_y;
        
        int num;
        int log;
        
        for(int row = 0; row < game.getRows(); row++ ){
            for (int col = 0; col < game.getCols(); col ++){
                num = game.fetchPosition(row, col);
                if(num != 0){
                    log = (int)(Math.log(num) / Math.log(2));
                
                    int testNum = num;
                    int numDigits = 0;
                    while(testNum > 0){
                        testNum = testNum/10;
                        numDigits += 1;
                    }

                    g.setColor(tileColors[log]);

                    g.fillRect(start_x + curveRadius, start_y,
                               cellSize - curveRadius*2, cellSize);
                    g.fillRect(start_x, start_y + curveRadius,
                               cellSize, cellSize - curveRadius*2);

                    g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                    curve_x += cellSize - curveRadius*2;
                    g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                    curve_y += cellSize - curveRadius*2;
                    g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                    curve_x -= cellSize - curveRadius*2;
                    g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                
                    int textSize2 = textSize - 4*numDigits;
                    int indent = 3*numDigits;
                
                    g.setColor(Color.white);
                    Font font = new Font("Verdana", Font.BOLD, textSize2);
                    g.setFont(font);
                    
                    g.drawString(""+num,
                            start_x + cellSize/2 - (textSize2*numDigits)/2 + indent,
                            start_y + cellSize/2 + textSize2/2 - 3);
                }
                
                start_x += spaceWidth+cellSize;
                curve_x = start_x;
                curve_y = start_y;
            }
            start_y += spaceWidth+cellSize;
            start_x = startX + spaceWidth;
            curve_x = start_x;
            curve_y = start_y;
        }
    }
    
    public void drawMovingTiles(Graphics g){
        int start_x;
        int start_y;
        int curve_x;
        int curve_y;
        
        for(int row = 0; row < game.getRows(); row++ ){
            for (int col = 0; col < game.getCols(); col ++){
                ArrayList tiles = new ArrayList();
                
                tiles.add(row);
                int numTiles = 1;
                
                if(currentCoords[0][row + game.getRows()][col] != 0){
                    tiles.add(row + game.getRows());
                    numTiles++;
                }
                
                for (int counter = 0; tiles.size() > 0; counter++){
                    start_x = currentCoords[0][(int)tiles.get(0)][col] + spaceWidth*(col+1);
                    start_y = currentCoords[1][(int)tiles.get(0)][col] + spaceWidth*(row+1);
                    tiles.remove(0);
                    
                    curve_x = start_x;
                    curve_y = start_y;

                    int num;
                    int log;

                    num = game.fetchPosition(row, col)/numTiles;
                    if(num != 0){
                        log = (int)(Math.log(num) / Math.log(2));

                        int testNum = num;
                        int numDigits = 0;
                        while(testNum > 0){
                            testNum = testNum/10;
                            numDigits += 1;
                        }

                        g.setColor(tileColors[log]);

                        g.fillRect(start_x + curveRadius, start_y,
                                   cellSize - curveRadius*2, cellSize);
                        g.fillRect(start_x, start_y + curveRadius,
                                   cellSize, cellSize - curveRadius*2);

                        g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                        curve_x += cellSize - curveRadius*2;
                        g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                        curve_y += cellSize - curveRadius*2;
                        g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);
                        curve_x -= cellSize - curveRadius*2;
                        g.fillOval(curve_x, curve_y, curveRadius*2, curveRadius*2);

                        int textSize2 = textSize - 4*numDigits;
                        int indent = 3*numDigits;

                        g.setColor(Color.white);
                        Font font = new Font("Verdana", Font.BOLD, textSize2);
                        g.setFont(font);

                        g.drawString(""+num,
                                start_x + cellSize/2 - (textSize2*numDigits)/2 + indent,
                                start_y + cellSize/2 + textSize2/2 - 3);
                    }
                }
            }
        }
    }
    
    public void drawLeaderBoard(Graphics g){
            
    }
}