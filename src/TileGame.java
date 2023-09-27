/*
 * Game Logic for 2048
 * Macy Busby
 * 5/7/2022
 */

import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class TileGame {
    
    private int numCols = 4;
    private int numRows = 4;
    
    private int[][] board;
    protected int[][] moves;
    
    protected int score;
    protected int leaderBoardMin;
    protected String[][] leaderboard;
    
    private String fileName = "TetrisLeaderBoard.txt";
    private File leaderBoard = new File(fileName);
    
    public TileGame(){
        initBoard();
        //leaderboard = readLeaderBoard();
    }
    
    public void initBoard(){
        board  = new int[numRows][numCols];
        
        board[numRows - 1][0] = 4;
        board[numRows - 2][0] = 2;
        
        score = 4;
    }
    
    public void makeMove(char move){
        moves = new int[numRows][numCols];
        
        switch(move){
            case 'U':
                moveUp();
                break;
            case 'D':
                moveDown();
                break;
            case 'R':
                moveRight();
                break;
            case 'L':
                moveLeft();
                break;
        }
        
        updateScore();
    }
    
    public void updateScore(){
        for(int col = 0; col < numCols; col++ ){
            for (int row = 0; row < numRows; row ++){
                if (board[row][col] > score)
                    score = board[row][col];
            }
        }
    }
    
    public int countEmptyTiles(){
        int counter = 0;
        
        for(int col = 0; col < numCols; col++ ){
            for (int row = 0; row < numRows; row ++){
                if (board[row][col] == 0)
                    counter++;
            }
        }
        return counter;
    }
    
    public boolean gameOver(){
        
        for(int col = 0; col < numCols; col++ ){
            for (int row = 0; row < numRows; row ++){
                if (board[row][col] == 0)
                    return false;
                if (row - 1 >= 0 && board[row][col] == board[row -1][col])
                    return false;
                if (row + 1 < numRows && board[row][col] == board[row + 1][col])
                    return false;
                if (col - 1 >= 0 && board[row][col] == board[row][col - 1])
                    return false;
                if (col + 1 < numCols && board[row][col] == board[row][col + 1])
                    return false;
            }
        }
        
        
        return true;
    }
    
    public void spawnTile(){
        int spaces = countEmptyTiles();
        
        if (spaces == 0)
            return;
        
        Random ranGen = new Random ();
        int randomValue = ranGen.nextInt(10);
        int randomSpace = ranGen.nextInt(spaces);
        
        int newTile = 2;
        if (randomValue == 0)
            newTile = 4;
        
        int counter = 0;
        for(int col = 0; col < numCols; col++ ){
            for (int row = 0; row < numRows; row ++){
                if (board[row][col] == 0){
                    counter++;
                    if (counter == randomSpace + 1){
                        board[row][col] = newTile;
                        return;
                    }
                }
            }
        }
    }
    
    public void moveUp(){
        int placeHolder;
        
        for(int col = 0; col < numCols; col++ ){
            
            for (int row = 1; row < numRows; row ++){
                
                if (board[row][col] != 0){
                    placeHolder = board[row][col];
                    
                    if(board[row - 1][col] == 0){
                        board[row - 1][col] = placeHolder;
                        board[row][col] = 0;
                        
                        moves[row - 1][col] += moves[row][col] + 1;
                        moves[row][col] = 0;
                        
                        row = 0;
                    }
                    else if(board[row - 1][col] == placeHolder && 
                            moves[row-1][col] >= 0 && moves[row][col] >= 0){
                        board[row - 1][col] = 2*placeHolder;
                        board[row][col] = 0;
                        
                        String combined = (moves[row][col] + 1)+""+moves[row-1][col];
                        
                        moves[row-1][col] = Integer.parseInt(combined)*(-1);
                        moves[row][col] = 0;
                    }
                }
            }
        }
    }
   
    public void moveDown(){
        int placeHolder;
        
        for(int col = numCols - 1; col >= 0; col-- ){
            
            for (int row = numRows - 2; row >= 0; row --){
                
                if (board[row][col] != 0){
                    placeHolder = board[row][col];
                    
                    if(board[row + 1][col] == 0){
                        board[row + 1][col] = placeHolder;
                        board[row][col] = 0;
                        
                        moves[row + 1][col] += moves[row][col] + 1;
                        moves[row][col] = 0;
                        
                        row = numRows - 1;
                    }
                    else if(board[row + 1][col] == placeHolder && 
                            moves[row+1][col] >= 0 && moves[row][col] >= 0){
                        board[row + 1][col] = 2*placeHolder;
                        board[row][col] = 0;
                        
                        String combined = (moves[row][col] + 1)+""+moves[row+1][col];
                        
                        moves[row+1][col] = Integer.parseInt(combined)*(-1);
                        moves[row][col] = 0;
                    }
                }
            }
        }
    }
    
    public void moveLeft(){
        int placeHolder;
        
        for (int row = 0; row < numRows; row ++){
            
            for(int col = 1; col < numCols; col++ ){
                
                if (board[row][col] != 0){
                    placeHolder = board[row][col];
                    
                    if(board[row][col - 1] == 0){
                        board[row][col - 1] = placeHolder;
                        board[row][col] = 0;
                        
                        moves[row][col - 1] += moves[row][col] + 1;
                        moves[row][col] = 0;
                        
                        col = 0;
                    }
                    else if(board[row][col - 1] == placeHolder && 
                            moves[row][col - 1] >= 0 && moves[row][col] >= 0){
                        board[row][col - 1] = 2*placeHolder;
                        board[row][col] = 0;
                        
                        String combined = (moves[row][col] + 1)+""+moves[row][col-1];
                        
                        moves[row][col-1] = Integer.parseInt(combined)*(-1);
                        moves[row][col] = 0;
                    }
                }
            }
        }
    }
   
    public void moveRight(){
        int placeHolder;
        
        for (int row = numRows - 1; row >= 0; row --){
            
            for(int col = numCols - 2; col >= 0; col-- ){  
                
                if (board[row][col] != 0){
                    placeHolder = board[row][col];
                    
                    if(board[row][col + 1] == 0){
                        board[row][col + 1] = placeHolder;
                        board[row][col] = 0;
                        
                        moves[row][col + 1] += moves[row][col] + 1;
                        moves[row][col] = 0;
                        
                        col = numCols - 1;
                    }
                    else if(board[row][col + 1] == placeHolder && 
                            moves[row][col + 1] >= 0 && moves[row][col] >= 0){
                        board[row][col + 1] = 2*placeHolder;
                        board[row][col] = 0;
                        
                        String combined = (moves[row][col] + 1)+""+moves[row][col+1];
                        
                        moves[row][col+1] = Integer.parseInt(combined)*(-1);
                        moves[row][col] = 0;
                    }
                }
            }
        }
    }
    
    public String[][] readLeaderBoard(){
        int boardSize = 10;
        String[][] board = new String[boardSize][2];
        
        try{
            Scanner inScan = new Scanner(leaderBoard).useDelimiter(",|\n");
            
            for (int counter = 0; counter < leaderboard.length; counter ++){
                if (inScan.hasNext()){
                    String newName = inScan.next();
                    String newScore = inScan.next();

                    board[counter][0] = newName;
                    board[counter][1] = newScore;
                }
                else{
                    board[counter][0] = "";
                    board[counter][1] = "";
                }
            }
            
        }catch(IOException ioe){
            JOptionPane.showMessageDialog(null, 
                    "There was an error reading the leaderboard.\n"
                            + "The program will start with an empty leaderboard.", "Error", 1);
            String[][] emptyBoard = {{"",""},{"",""},{"",""},{"",""},
                 {"",""},{"",""},{"",""},{"",""},{"",""},{"",""}};
            board = emptyBoard;
        }
                
        String curNum;
        String curName;
        String nextNum;
        String nextName;
        int counter = 0;
        
        while (counter < leaderboard.length - 1){
            
            curNum = leaderboard[counter][1].strip();
            curName = leaderboard[counter][0];
            nextNum = leaderboard[counter+1][1].strip();
            nextName = leaderboard[counter+1][0];
            
            if(!curNum.equals("") && !nextNum.equals("")){
                if(Integer.parseInt(curNum) < Integer.parseInt(nextNum)){
                    leaderboard[counter][0] = nextName;
                    leaderboard[counter][1] = nextNum;
                    leaderboard[counter+1][0] = curName;
                    leaderboard[counter+1][1] = curNum;
                    counter = 0;
                }
                else
                    counter ++;
            }
            else
                counter++;
        }
        
        return leaderboard;
    }
    
    public String[][] updateLeaderBoard(String name, int score){
        
        for(int dex = 0; dex < leaderboard.length; dex++){
            String curNum = leaderboard[dex][1].strip();
            String curName = leaderboard[dex][0];
            
            if(curNum.equals(""))
                curNum = "0";
            
            if (Integer.parseInt(curNum) < score){
                for(int index = board.length - 1; index > dex; index --){
                    board[index][0] = board[index - 1][0];
                    board[index][1] = board[index - 1][1];
                }
                
                leaderboard[dex][0] = name;
                leaderboard[dex][1] = score+"";
                return leaderboard;
            }
        }
        
        return leaderboard;
    }
    
    public void saveLeaderBoard(){
        String leaderboardMess = "";
        for(int dex = 0; dex < 10; dex ++){
            leaderboardMess += board[dex][0]+","+board[dex][1]+"\n";
        }
        
        try{
            FileWriter writer = new FileWriter(leaderBoard);
            writer.write(leaderboardMess);
            writer.close();
            
        }catch(IOException ioe){
            JOptionPane.showMessageDialog(null, 
                    "There was an error saving the leaderboard.\n", "Error", 1);
        }
    }
    
    public void clearLeaderBoard(){
        try{
            FileWriter writer = new FileWriter(leaderBoard);
            writer.write("");
            writer.close();
            
        }catch(IOException ioe){
            JOptionPane.showMessageDialog(null, 
                    "There was an error clearing the leaderboard.\n", "Error", 1);
        }
    }
    
    public int fetchPosition(int row, int col){
        return board[row][col];
    }
    
    public int getRows(){
        return board.length;
    }
    
    public int getCols(){
        return board[0].length;
    }
    
    public int getScore(){
        return score;
    }
}
