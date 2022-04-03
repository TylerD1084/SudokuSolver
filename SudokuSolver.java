import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SudokuSolver{

    private static final int GRID_SIZE = 9;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private static int moves = 0;
    private static int timeMs;
    private static int watching;

    public static void main(String [] args){

        /**
         * first arg: if > 0 then the output in the console got more details 
         * like counting moves, actual row/column and you can see the computer 
         * trying to solve the board
         */
        try{
            watching = Integer.parseInt(args[0]);
        }catch(Exception e){
            watching = 0;
        }

        /**
         * second arg: set the time between moves im ms.
         * if no arg is set the value is 10 ms.
         */
        try{
            timeMs = Integer.parseInt(args[1]);

        }catch(Exception e){
            timeMs = 10;
        }


        System.out.println();
        
        int[][] board = loadBoardFromFile();
        int[][] boardFromStart = copyBoard(board);
        
        printBoard(board);
        
        
        if (solveBoard(board)){
            clearScreen();
            printBoard(boardFromStart);
            printBoard(board);
            System.out.println("Solved successfully!");
        }
        else{
            clearScreen();
            printBoard(boardFromStart);
            printBoard(board);
            System.out.println("Unsolveable board :(");
        }
        
        // printBoard(board);
        System.out.println("Moves:  " + moves);
          
    }

    /**
     * Clearing the screen
     */
    private static void clearScreen() {
        System.out.println("\033[H\033[2J");
    }

    /**
     * Copy the to show the inputboard at the end
     * @param board
     * @return
     */

    private static int[][] copyBoard(int[][] board){
        int [][] newBoard = new int[GRID_SIZE][GRID_SIZE];

        for (int row=0; row < GRID_SIZE; row++){
            for (int col=0; col < GRID_SIZE; col++){
                newBoard[row][col] = board[row][col];
            }
        }
        return newBoard;
    }

    /**
     * Prints the board to the console.
     * If the number is 0 the color will be red, the others will be white.
     * @param board
     */
    private static void printBoard(int[][] board){
        System.out.println();

        for (int row = 0; row < GRID_SIZE; row++){
            if (row%3 == 0 && row != 0){
                System.out.println(ANSI_GREEN + "-----------" + ANSI_RESET);
            }
            for (int column = 0; column < GRID_SIZE; column++){
                if (column%3 == 0 && column != 0){
                    System.out.print(ANSI_GREEN + "|" + ANSI_RESET);
                }
                if (board[row][column] == 0){
                    System.out.print(ANSI_RED + board[row][column] + ANSI_RESET);
                }
                else{
                    System.out.print(ANSI_WHITE + board[row][column] + ANSI_RESET);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Check if the actual number in the actual row
     * @param board
     * @param number
     * @param row
     * @return
     */
    private static boolean isNumberInRow(int[][] board, int number, int row){
        for (int i=0; i<GRID_SIZE; i++){
            if (board[row][i] == number) return true;
        }
        return false;
    }
    
    /**
     * Check if the actual number is in the actual column
     * @param board
     * @param number
     * @param column
     * @return
     */
    private static boolean isNumberInColumn(int[][] board, int number, int column){
        for (int i=0; i<GRID_SIZE; i++){
            if (board[i][column] == number) return true;
        }
        return false;
    }

    /**
     * Check if the actual number inside the actual box
     * @param board
     * @param number
     * @param row
     * @param column
     * @return
     */
    private static boolean isNumberInBox(int[][] board, int number, int row, int column){
        int localBoxRow = row - row%3;
        int localBoxColumn = column - column%3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++){
            for (int j = localBoxColumn; j < localBoxColumn + 3; j++){
                if (board[i][j] == number){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Checks if the placement is valid.
     * It calls the three isNumberIn-Methods and checks if their return-value is false.
     * If it is false this method will return true - the placement was valid.
     * @param board
     * @param number
     * @param row
     * @param column
     * @return
     */
    private static boolean isValicPlacement(int[][] board, int number, int row, int column){
        return !isNumberInRow(board, number, row) && !isNumberInColumn(board, number, column) && !isNumberInBox(board, number, row, column);
    }
    
    /**
     * Solving the board
     * @param board
     * @return
     */
    private static boolean solveBoard(int[][] board){
        for (int row = 0; row < GRID_SIZE; row++){
            for (int column = 0; column < GRID_SIZE; column++){
                if (board[row][column] == 0){
                    for (int numberToTry = 1; numberToTry <= GRID_SIZE; numberToTry++){
                        moves++;
                        if (watching > 0){
                            showStats(board, row, column, numberToTry);
                        }
                        if (isValicPlacement(board, numberToTry, row, column)){
                            board[row][column] = numberToTry;
                            
                            if (solveBoard(board)){
                                return true;
                            }
                            else{
                                board[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Print out the stats if wanted (arg[0])
     * @param board
     * @param row
     * @param column
     * @param numberToTry
     */
    private static void showStats(int[][] board, int row, int column, int numberToTry) {
        clearScreen();
        System.out.println("Moves:  " + moves);
        System.out.println("Row:    " + (row +1));
        System.out.println("Column: " + (column+1));
        System.out.println("Number: " + numberToTry);
        System.out.println("___________");
        printBoard(board);
        wait(timeMs);
    }

    /**
     * Sleep-Timer in ms (arg[1]) for showStats so the user can see the computer working.
     * @param ms
     */
    private static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks if the gridsize is 9x9
     * If not it will tell the user in which row are more/less than 9 signs
     * or if
     * @return
     */
    private static void checkGridSize(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sudoku.txt"));
            int numberOfRow = 1;
            String line;

            while((line = reader.readLine()) != null){
                if (line.length() != GRID_SIZE){
                    System.out.println("Wrong length in line: " + numberOfRow);
                    reader.close();
                    System.exit(0);
                    
                }
                numberOfRow++;
            }
            if (numberOfRow > GRID_SIZE+1){
                System.out.println("The board got too much lines!");
                reader.close();
                System.exit(0);
            }
            else if (numberOfRow < GRID_SIZE+1){
                System.out.println("The board got missing lines!");
                reader.close();
                System.exit(0);
            }
            reader.close();

        }
        catch(IOException e){
        }
    }

    /**
     * Try to load a sudoku from 'sudoku.txt'.
     * If the file doesn't exist it will return the original board from
     * John's tutorial
     * @return
     */
     private static int[][] loadBoardFromFile(){
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        
        checkGridSize();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("sudoku.txt"));
            String line;
            char[][] tempBoard = new char[GRID_SIZE][GRID_SIZE];
            char[] tempCharArray;
            int row = 0;

            /* read lines from file and add numbers to char-array */
            while((line = reader.readLine()) != null){
                tempCharArray = line.toCharArray();
                for (int i=0; i<GRID_SIZE; i++){
                    tempBoard[row] = tempCharArray;
                }
                row++;
                
            }
            reader.close();
            

            /* convert char to int */
            for (int i=0; i<tempBoard.length; i++){
                for (int j=0; j<tempBoard[0].length; j++){
                    board[i][j] = Character.getNumericValue(tempBoard[i][j]);
                }
            }
        
        } catch (IOException e){
            int[][] johnsBoard = {
                {7, 0, 2, 0, 5, 0, 6, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 0},
                {1, 0, 0, 0, 0, 9, 5, 0, 0},
                {8, 0, 0, 0, 0, 0, 0, 9, 0},
                {0, 4, 3, 0, 0, 0, 7, 5, 0},
                {0, 9, 0, 0, 0, 0, 0, 0, 8},
                {0, 0, 9, 7, 0, 0, 0, 0, 5},
                {0, 0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 7, 0, 4, 0, 2, 0, 3} 
            };

            return johnsBoard;
        }

        return board;
    }

}
