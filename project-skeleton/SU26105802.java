public class SU26105802 {
    public static void main(String[] args) {

        // Initialize colors array to use later
        String[] colors = { "G", "Y", "R",
                "B" };

        // Call argumentsIsValid function to check if the arguments are valid
        boolean validArguments = argumentsIsValid(args);
        // Check if argumentsIsValid function returns false
        if (!validArguments)
            // If false, stop the method
            return;

        // Store the arguments into respective variables
        int gameMode = Integer.parseInt(args[0]);
        int guiIndicator = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int k = Integer.parseInt(args[3]);

        // Call argumentsIsSupported function to check if the arguments are supported
        boolean supportedArguments = argumentsIsSupported(gameMode, guiIndicator, n, k);
        // Check if argumentsIsSupported function returns false
        if (!supportedArguments)
            // If false, stop the method
            return;

        // If the gameMode argument is outside of the supported range, reset the
        // argument to default
        if (gameMode < 0 || gameMode > 2) {
            StdOut.println("First input reset to default.");
            gameMode = 0;
        }

        // If the guiIndicator argument is outside of the supported range, reset the
        // argument to default
        if (guiIndicator < 0 || guiIndicator > 1) {
            StdOut.println("Second input reset to default.");
            guiIndicator = 0;
        }

        // If the n argument is outside of the supported range, reset the argument to
        // default
        if (n < 2 || n > 3) {
            StdOut.println("Third input reset to default.");
            n = 2;
        }

        // Initialize and Assign a value to m, based on the value of n (using a switch)
        int m;
        switch (n) {
            case 2:
                m = 8;
                break;
            case 3:
                m = 30;
                break;
            case 4:
                m = 128;
                break;
            case 5:
                m = 650;
                break;
            case 6:
                m = 3912;
                break;
            default:
                return;
        }

        // If the k argument is outside of the supported range / greater than k, reset
        // the argument to default
        if (k < 3 || k > m) {
            StdOut.println("Fourth input reset to default.");
            k = 3;
        }

        // Print out the starting messages before entering the game loop
        StdOut.println("The dimension of your board is: " + m + "x" + m);
        StdOut.println("The length of a blockade is: " + k);

        StdDraw.setCanvasSize(500,800);
        StdDraw.setXscale(0, 500);
        StdDraw.setYscale(0, 800);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize the board by using a 2D-array
        String[][] board = new String[m][m];

        // Iterate through every 2D-index of the board and Initialize it to the starting
        // default
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m; c++) {
                if (c == 0)
                    // Initialize the first column to the "." character
                    board[r][c] = ".";
                else
                    // Initialize all other columns to the "*" character
                    board[r][c] = "*";

            }
        }

        // Initialize the gameIsRunning boolean, which is used as the conditional
        // variable of the game loop
        boolean gameIsRunning = true;
        // Initialize the dontUpdateBoard boolean, which is used to determine of the
        // board is to be redisplayed on the next loop of the game loop
        boolean dontUpdateBoard = false;
        // Initialize the blocksPlaced integer, to keep track of how many blocks have
        // been placed on the board
        int blocksPlaced = 0;
        // Initialize the moves integer, to keep track of how many moves have been made
        int moves = 0;

        boolean updateGUI = true;
        int cursorRow = 0;
        int cursorCol = 0;
        updateScreen(board, 0, 0);


        // Start of the game loop
        while (gameIsRunning) {
            if (guiIndicator == 0) {
                // If the dontUpdateBoard boolean is true, then set it to false
                if (dontUpdateBoard) {
                    dontUpdateBoard = false;
                }
                // If the dontUpdateBoard boolean is false, then redisplay the board
                else {
                    // Call updateBoard function to redisplay the updated board
                    updateBoard(board, m);
                }

                // Use the hasBlockade function to determine if a blockade has been formed
                if (hasBlockade(board, k)) {
                    // If the hasBlockade function returns true, call the determineScore function to
                    // display the end of game messages
                    // Display appropriate termination message
                    StdOut.println("Termination: Blockade!");
                    determineScore(board, moves);
                    // Break out of the game loop, since a blockade has been formed, which
                    // terminates the game loop
                    break;
                }

                // Check if all the positions of the board have been filled
                if (blocksPlaced == m * m) {
                    // If all positions of the board have been filled, call the determineScore
                    // function to display the end of game messages
                    // Display appropriate termination message
                    StdOut.println("Termination: You have won!");
                    determineScore(board, moves);
                    // Break out of the game loop, since the game has been won
                    break;
                }

                // Display a prompt, so that the player can enter the move he/she wants to make
                StdOut.print("Move: ");
                // Store the input from the player into the move variable
                int move = StdIn.readInt();

                // If move is 0, then continue accordingly
                if (move == 0) {
                    // Display a prompt, so that the player can enter the row number of the index
                    // where the row deletion must start
                    StdOut.print("Row Number: ");
                    // Store the input from the player into the row variable
                    int row = StdIn.readInt();

                    // Display a prompt, so that the player can enter the column number of the index
                    // where the row deletion must start
                    StdOut.print("Column Number: ");
                    // Store the input from the player into the col variable
                    int col = StdIn.readInt();

                    // Call the positionIsValid function to check if the position entered is inside
                    // the board
                    if (!positionIsValid(col, row, m)) {
                        // If the position is outside of the board, set dontUpdateBoard to true, so that
                        // the board will not be redisplayed on the next loop of the game loop
                        dontUpdateBoard = true;
                        // Display an Invalid move message
                        StdOut.println("Invalid move: Outside of board!");
                        // Continue to next loop
                        continue;
                    }

                    // Check if the position entered is either an empty or closed block
                    if (board[row][col].equals(".") || board[row][col].equals("*")) {
                        // If the position entered is either an empty or closed block, set
                        // dontUpdateBoard to true, so that the board will not be redisplayed on the
                        // next loop of the game loop
                        dontUpdateBoard = true;
                        // Display an Invalid move message
                        StdOut.println("Invalid move: Nothing to delete!");
                        // Continue to next loop
                        continue;
                    }

                    // Set the position selected to an open block
                    board[row][col] = ".";
                    // Increment the col variable, to move to the next block
                    col++;
                    // Decrement the blocksPlaced variable, since a placed block has been removed
                    blocksPlaced--;

                    // Enter an infinite loop
                    while (true) {
                        // If col is equal to m, that means it has reached the far right side of the
                        // board, and therefore the look must be broken
                        if (col == m)
                            break;
                        // Check if the current index is a closed block
                        if (!board[row][col].equals("*")) {
                            // Check if the current index is an empty block
                            if (!board[row][col].equals("."))
                                // If the current index is neither empty nor closed, decrement blocksPlaced
                                blocksPlaced--;

                            // Set the current index to the "*" character
                            board[row][col] = "*";
                            // Increment the col variable to move to the next block
                            col++;
                        } else {
                            // Break if other conditionals are fail
                            break;
                        }
                    }
                    // Increment moves, since row deletion counts as one move
                    moves++;
                    // If move is 1, then continue accordingly
                } else if (move == 1) {
                    // Display a prompt, so that the player can enter the row number of the index
                    // where the block will be placed
                    StdOut.print("Row Number: ");
                    // Store the input from the player into the row variable
                    int row = StdIn.readInt();

                    // Display a prompt, so that the player can enter the column number of the index
                    // where the block will be placed
                    StdOut.print("Column Number: ");
                    // Store the input from the player into the col variable
                    int col = StdIn.readInt();

                    // Display a prompt, so that the player can enter the color of the block placed
                    StdOut.print("Color: ");
                    // Store the input from the player into the color variable
                    int color = StdIn.readInt();

                    // Call the positionIsValid function to check if the position entered is inside
                    // the board
                    if (!positionIsValid(col, row, m)) {
                        dontUpdateBoard = true;
                        StdOut.println("Invalid move: Outside of board!");
                        continue;
                    }

                    // Check if the color entered is inside the range of available colors
                    if (color < 0 || color > n - 1) {
                        // If the color entered is not inside the range of available colors, set
                        // dontUpdateBoard to true
                        dontUpdateBoard = true;
                        // Display an Invalid move message
                        StdOut.println("Invalid move: Unknown color!");
                        // Continue to next loop
                        continue;
                    }

                    // Check if position selected is not empty
                    if (!board[row][col].equals(".")) {
                        // If position selected is not empty, set dontUpdateBoard to true
                        dontUpdateBoard = true;
                        // Display an Invalid move message
                        StdOut.println("Invalid move: Cell is not open!");
                        // Continue to next loop
                        continue;
                    }

                    // Set the position selected by the player to the color selected by the player
                    board[row][col] = colors[color];
                    // If the position selected is not in the last column, set the block in the next
                    // column to a "."
                    if (col <= m - 2)
                        board[row][col + 1] = ".";

                    // Increment blocksPlaced, since one block has been placed, and moves
                    blocksPlaced++;
                    moves++;
                    // If move is 2, then continue accordingly
                } else if (move == 2) {
                    // Display a termination message
                    StdOut.println("Termination: User terminated game!");
                    // Call determineScore function to show end game messages
                    determineScore(board, moves);
                    // Break out of loop, since the game has been terminated by the player
                    break;
                }

                // Check if the move entered is outside the range of available moves
                if (move < 0 || move > 2) {
                    // If the move entered is outside the range of available moves, display an
                    // Invalid move message
                    StdOut.println("Invalid move: Unknown move!");
                    // Set dontUpdateBoard to true, so that the board won't be redisplayed on the
                    // next loop of the game loop
                    dontUpdateBoard = true;
                    // Continue to the next loop
                    continue;
                }
            } else if (guiIndicator == 1) {
                if(updateGUI){
                    int pauseTime = 125;
                    if(StdDraw.isKeyPressed(87)){
                        if(cursorRow == 0) cursorRow = board.length - 1;
                        else cursorRow--;
                        updateScreen(board, cursorRow, cursorCol);
                        StdDraw.pause(pauseTime);
                    }

                    if(StdDraw.isKeyPressed(83)){
                        if(cursorRow == board.length - 1) cursorRow = 0;
                        else cursorRow++;
                        updateScreen(board, cursorRow, cursorCol);
                        StdDraw.pause(pauseTime);
                    }

                    if(StdDraw.isKeyPressed(65)){
                        if(cursorCol == 0) cursorCol = board.length - 1;
                        else cursorCol--;
                        updateScreen(board, cursorRow, cursorCol);
                        StdDraw.pause(pauseTime);
                    }

                    if(StdDraw.isKeyPressed(68)){
                        if(cursorCol == board.length - 1) cursorCol = 0;
                        else cursorCol++;
                        updateScreen(board, cursorRow, cursorCol);
                        StdDraw.pause(pauseTime);
                    }

                    if(StdDraw.isKeyPressed(68)){
                        if(cursorCol == board.length - 1) cursorCol = 0;
                        else cursorCol++;
                        updateScreen(board, cursorRow, cursorCol);
                        StdDraw.pause(pauseTime);
                    }
                }
            }

        }
        // Print out "Game ended!" once the game is finished
        StdOut.println("Game ended!");
    }

    // Boolean function that takes in one argument, which is a String array
    public static Boolean argumentsIsValid(String[] args) {
        // The arguments array from the main method is passed into the array
        // The length of the array is checked, to see if the correct amount of arguments
        // have been given
        if (args.length < 4) {
            // If there are less than 4 arguments, a message is displayed and the function
            // returns false
            StdOut.println("Not enough arguments");
            return false;
        } else if (args.length > 4) {
            // If there are more than 4 arguments, a message is displayed and the function
            // returns false
            StdOut.println("Too many arguments");
            return false;
        }
        // If there are exactly 4 arguments, the function returns true
        return true;
    }

    // Boolean function that takes in four arguments, which are the arguments given
    // by the main method
    public static Boolean argumentsIsSupported(int gameMode, int guiIndicator, int n, int k) {
        // The four arguments are checked, to see if they are inside the range of
        // supported values
        if ((gameMode >= 1 && gameMode <= 2) || (n == 4) || (guiIndicator == 2)) {
            // If one of the four arguments are not supported, a message is displayed and
            // the function returns false
            StdOut.println("Functionality currently not supported");
            return false;
        }
        // If all arguments are supported, the function returns true
        return true;
    }

    // Void function that takes in 2 arguments, one 2D-String-array, and one integer
    // m
    public static void updateBoard(String[][] board, int m) {
        // The function iterates through the board and prints out its contents line by
        // line
        StdOut.print("\n");
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m; c++) {
                StdOut.print(board[r][c]);
            }
            StdOut.println();
        }
        StdOut.print("\n");
        // There are empty lines printed before and after the board is printed
    }

    // Boolean function that takes in three arguments
    public static Boolean positionIsValid(int x, int y, int m) {
        // Check if the position entered is inside the range of the board
        if (x < 0 || x > m - 1 || y < 0 || y > m - 1) {
            // If the position is outside the board, return false
            return false;
        }
        // If the position is inside the board, return true
        return true;
    }

    // Boolean function that takes in two arguments, one 2D-String-array, and one
    // integer k
    public static Boolean hasBlockade(String[][] board, int k) {
        // Variable m is initialized to the value of the length of board
        int m = board.length;

        // Iterate through the columns of the board
        for (int c = 0; c < m; c++) {
            // Initialize numSameColor to 1
            int numSameColor = 1;
            // Set currentColor to the first element in the cth column
            String currentColor = board[0][c];
            // Iterate through the rows in the cth column
            for (int r = 1; r < m; r++) {
                // If the current element is equal to the currentColor variable, then increment
                // numSameColor
                if (board[r][c].equals(currentColor)) {
                    numSameColor++;
                    // If numSameColor is equal to k, and currentColor is not "." or "*" then return
                    // true
                    if (numSameColor == k && !currentColor.equals(".") && !currentColor.equals("*")) {
                        return true;
                    }
                } else {
                    // If the next element is not the same as currentColor, set currentColor to the
                    // current element and set numSameColor to 1
                    numSameColor = 1;
                    currentColor = board[r][c];
                }
            }
        }

        // Return false if no blockades have been found
        return false;
    }

    // Void function that takes in two arguments, one 2D-String-array, and one
    // integer moves
    public static void determineScore(String[][] board, int moves) {
        // Initialize the blocksPlaced variable to 0
        int blocksPlaced = 0;
        // Iterate through the board array, and increment blocksPlaced if the current
        // element is neither empty nor closed
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (!board[r][c].equals(".") && !board[r][c].equals("*")) {
                    blocksPlaced++;
                }
            }
        }

        // Calculate the score by dividing blocksPlaced by the number of positions in
        // the board, and then multiplying by 100
        double score = (blocksPlaced / Math.pow(board.length, 2)) * 100;
        // Display the end game messages
        StdOut.printf("Score: %.0f", score);
        StdOut.print("%\n");
        StdOut.println("Moves: " + moves);
    }

    public static void updateScreen(String[][] board, int x, int y){
        double cellWidth = 400 / board.length;

        StdDraw.clear(StdDraw.BLACK);

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(x == j && y == i) {
                    StdDraw.setPenColor(StdDraw.BOOK_RED);
                    StdDraw.filledSquare((i * (cellWidth*1.15)) + cellWidth, 800 - (j * (cellWidth*1.15)) - cellWidth, cellWidth*1.3/2);
                }
                if(board[j][i].equals(".")) StdDraw.setPenColor(StdDraw.GRAY);
                else if(board[j][i].equals("*")) StdDraw.setPenColor(StdDraw.WHITE);
                StdDraw.filledSquare((i * (cellWidth*1.15)) + cellWidth, 800 - (j * (cellWidth*1.15)) - cellWidth, cellWidth/2);
            }
        }
        StdDraw.show();
    }
}