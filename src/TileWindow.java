/*
 * Window for 2048 Game
 * Macy Busby
 * 5/7/2022
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class TileWindow extends JFrame{
    
    private int winHeight = 550;
    private int winWidth = 400;
    
    private JMenuBar bar = new JMenuBar();
    
    private TileGame game;
    private TileDisplay display;
    
    public TileWindow(){
        
        this.setTitle("2048");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(winWidth, winHeight);
        
        game = new TileGame();
        display = new TileDisplay(game, this);
        
        this.add(display);
        
        this.setVisible(true);
    }
    
    public void initMenuBar(){
        this.setJMenuBar(bar);
        
        JMenu scores = new JMenu("Leaderboard");
        
        JMenuItem leaderboard = new JMenuItem("Leaderboard");
        JMenuItem restartBoard = new JMenuItem("Restart Leaderboard");
        
        leaderboard.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                initLeaderBoard();
            }
        });
        restartBoard.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ac){
                game.clearLeaderBoard();
                game.readLeaderBoard();
                JOptionPane.showMessageDialog(null, "Leaderboard Cleared.", "Clear Leaderboard", 1);
            }
        });
        
        scores.add(leaderboard);
        scores.add(restartBoard);
        
        bar.add(scores);
    }
    
    public void initEndScreen(){
        
        String message = "Game Over!\n";
        message += "Your score: "+game.score +"\n";
        if (game.score > game.leaderBoardMin){
            message += "Your score is in the top 10 scores!\n";
            message += "Please enter your name to\n";
            message += "add it to the leaderboard.";
        }
            
        String[] options = {"Leaderboard", "new Game", "Close"};
            
        if (game.score > game.leaderBoardMin){
            String name = JOptionPane.showInputDialog(null, message, "Game Over", 1);
            game.updateLeaderBoard(name, game.score);
            game.saveLeaderBoard();
            initLeaderBoard();
        }
        else{
            int option = JOptionPane.showOptionDialog(null,message, "Game Over", 1, 1, null, options, 0);
            if (option == 0)
                initLeaderBoard();
            else if(option == 1)
                game.initBoard();
            else
                System.exit(0);
        }
    }
    
    public void initLeaderBoard(){
        String[][] board = game.leaderboard;
        String leaderboard = "";
        
        int nameLength = 0;
        
        for(int dex = 0; dex < board.length; dex ++){
            String nextName =(dex+1) + ": " + board[dex][0];
            
            if (nameLength < nextName.length())
                nameLength = nextName.length();
        }
        
        nameLength += 7;
        
        for(int dex = 0; dex < board.length; dex ++){
            String nextName =(dex+1) + ": " + board[dex][0];
            String nextScore = board[dex][1];
            
            String blank = "";
            if (!board[dex][0].equals(""))
                blank = ".".repeat(nameLength - nextName.length() - nextScore.length());
            
            leaderboard += "<html>" + nextName + blank + nextScore + "<br /><html>";
        }
        
        JLabel leaderBoardLabel = new JLabel(leaderboard);
        leaderBoardLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        JOptionPane.showMessageDialog(null, leaderBoardLabel, "Leaderboard", 1);
    }
    
    public static void main(String[] args) {
        TileWindow win = new TileWindow();
    }
}
