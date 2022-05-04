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
        if (n < 2 || n > 4) {
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
        int width = 0, height = 0;
        int cursorRow = 0;
        int cursorCol = 0;
        if (guiIndicator == 1) {
            switch (n) {
                case 2:
                    width = 500;
                    height = 600;
                    break;
                case 3:
                    width = 800;
                    height = 900;
                    break;
                case 4:
                    width = 1000;
                    height = 1000;
                    break;
            }
            StdDraw.setCanvasSize(width, height);
            StdDraw.setXscale(0, width);
            StdDraw.setYscale(0, height);
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.enableDoubleBuffering();
            updateScreen(board, 0, 0, width, height);
        }

        boolean blockade = false;
        boolean deadEnd = false;
        boolean split = false;

        // Start of the game loop
        while (gameIsRunning) {
            // Use the hasBlockade function to determine if a blockade has been formed
            if (hasBlockade(board, k)) {
                // If the hasBlockade function returns true, call the determineScore function to
                // display the end of game messages
                // Display appropriate termination message
                if (guiIndicator == 0 && gameMode == 0) {
                    updateBoard(board, m);
                    StdOut.println("Termination: Blockade!");
                    int score = determineScore(board, moves);
                    // Display the end game messages
                    StdOut.printf("Score: %d", score);
                    StdOut.print("%\n");
                    StdOut.println("Moves: " + moves);
                    break;
                }
                blockade = true;
                // Break out of the game loop, since a blockade has been formed, which
                // terminates the game loop
            }

            if (gameMode == 1) {
                for (int i = 0; i < board.length; i++) {
                    if (hasDeadEnds(board, i)) {
                        deadEnd = true;
                        break;
                    }
                }

                if (hasSplits(board)) {
                    split = true;
                }

                if (blockade || deadEnd || split) {
                    int terminations = 0;
                    String terminationMessage = "Termination: You have caused a ";

                    if (blockade)
                        terminations++;
                    if (deadEnd)
                        terminations++;
                    if (split)
                        terminations++;

                    if (terminations == 1) {

                        terminationMessage += (blockade) ? "blockade!" : "";
                        terminationMessage += (deadEnd) ? "dead end!" : "";
                        terminationMessage += (split) ? "split!" : "";
                    } else if (terminations == 2) {
                        terminationMessage += (blockade) ? "blockade and a " : "";
                        terminationMessage += (deadEnd) ? ((blockade) ? "dead end!" : "dead end and a ") : "";
                        terminationMessage += (split) ? "split!" : "";
                    } else if (terminations == 3) {
                        terminationMessage += "blockade, a dead end, and a split!";
                    }

                    if (guiIndicator == 0) {
                        updateBoard(board, m);
                        StdOut.println(terminationMessage);
                        int score = determineScore(board, moves);
                        // Display the end game messages
                        StdOut.printf("Score: %d", score);
                        StdOut.print("%\n");
                        StdOut.println("Moves: " + moves);
                    } else if (guiIndicator == 1) {
                        int score = determineScore(board, moves);
                        String[] messages = { terminationMessage,
                                "Score: " + score + "%, Moves: " + moves };
                        updateScreen(board, cursorRow, cursorCol, width, height, messages);
                    }
                    playSound(-10, .15);
                    playSound(-15, .15);
                    playSound(-20, .3);
                    break;
                }

                if (Impasse(board, k, colors, n)) {
                    if (guiIndicator == 0) {
                        updateBoard(board, m);
                        StdOut.println("Termination: Impasse!");
                        int score = determineScore(board, moves);
                        // Display the end game messages
                        StdOut.printf("Score: %d", score);
                        StdOut.print("%\n");
                        StdOut.println("Moves: " + moves);
                    } else if (guiIndicator == 1) {
                        int score = determineScore(board, moves);
                        String[] messages = { "Termination: You have caused an Impasse!",
                                "Score: " + score + "%, Moves: " + moves };
                        updateScreen(board, cursorRow, cursorCol, width, height, messages);
                    }
                    playSound(-10, .15);
                    playSound(-15, .15);
                    playSound(-20, .3);

                    // Break out of the game loop, since a split has been formed, which
                    // terminates the game loop

                    break;
                }

            }

            // Check if all the positions of the board have been filled
            if (blocksPlaced == m * m) {
                // If all positions of the board have been filled, call the determineScore
                // function to display the end of game messages
                // Display appropriate termination message
                if (guiIndicator == 0) {
                    updateBoard(board, m);
                    StdOut.println("Termination: You have won!");
                    int score = determineScore(board, moves);
                    // Display the end game messages
                    StdOut.printf("Score: %d", score);
                    StdOut.print("%\n");
                    StdOut.println("Moves: " + moves);
                    determineScore(board, moves);
                } else if (guiIndicator == 1) {
                    int score = determineScore(board, moves);
                    String[] messages = { "Termination: You have won!", "Score: " + score + "%, Moves: " + moves };
                    updateScreen(board, cursorRow, cursorCol, width, height, messages);
                }
                playSound(0, .15);
                playSound(2, .15);
                playSound(4, .3);

                // Break out of the game loop, since the game has been won
                break;
            }

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
                    int score = determineScore(board, moves);
                    // Break out of loop, since the game has been terminated by the player
                    StdOut.printf("Score: %d", score);
                    StdOut.print("%\n");
                    StdOut.println("Moves: " + moves);
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
                int pauseTime = 200;
                if (StdDraw.isKeyPressed(87)) {
                    if (cursorRow == 0)
                        cursorRow = board.length - 1;
                    else
                        cursorRow--;
                    updateScreen(board, cursorRow, cursorCol, width, height);
                    StdDraw.pause(pauseTime);
                }

                if (StdDraw.isKeyPressed(83)) {
                    if (cursorRow == board.length - 1)
                        cursorRow = 0;
                    else
                        cursorRow++;
                    updateScreen(board, cursorRow, cursorCol, width, height);
                    StdDraw.pause(pauseTime);
                }

                if (StdDraw.isKeyPressed(65)) {
                    if (cursorCol == 0)
                        cursorCol = board.length - 1;
                    else
                        cursorCol--;
                    updateScreen(board, cursorRow, cursorCol, width, height);
                    StdDraw.pause(pauseTime);
                }

                if (StdDraw.isKeyPressed(68)) {
                    if (cursorCol == board.length - 1)
                        cursorCol = 0;
                    else
                        cursorCol++;
                    updateScreen(board, cursorRow, cursorCol, width, height);
                    StdDraw.pause(pauseTime);
                }

                if (StdDraw.isKeyPressed(88)) {
                    int row = cursorRow;
                    int col = cursorCol;

                    // Check if the position entered is either an empty or closed block
                    if (board[row][col].equals(".") || board[row][col].equals("*")) {
                        // If the position entered is either an empty or closed block, set
                        // dontUpdateBoard to true, so that the board will not be redisplayed on the
                        // next loop of the game loop
                        dontUpdateBoard = true;
                        // Display an Invalid move message
                        String[] messages = { "Invalid move: Nothing to delete!" };
                        updateScreen(board, cursorRow, cursorCol, width, height, messages);
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
                    updateScreen(board, cursorRow, cursorCol, width, height);
                    playSound(0, .15);
                    playSound(-5, .15);
                    StdDraw.pause(pauseTime);
                }

                if (StdDraw.isKeyPressed(81)) {
                    String[] messages = { "Termination: User terminated game!" };
                    updateScreen(board, cursorRow, cursorCol, width, height, messages);
                    break;
                }

                for (int i = 0; i < n; i++) {
                    if (StdDraw.isKeyPressed(48 + i)) {
                        // Check if position selected is not empty
                        if (!board[cursorRow][cursorCol].equals(".")) {
                            // If position selected is not empty, set dontUpdateBoard to true
                            dontUpdateBoard = true;
                            // Display an Invalid move message
                            String[] messages = { "Invalid move: Cell is not open!" };
                            updateScreen(board, cursorRow, cursorCol, width, height, messages);
                            StdDraw.pause(pauseTime);
                            // Continue to next loop
                            continue;
                        }

                        // Set the position selected by the player to the color selected by the player
                        board[cursorRow][cursorCol] = colors[i];
                        // If the position selected is not in the last column, set the block in the next
                        // column to a "."
                        if (cursorCol <= m - 2)
                            board[cursorRow][cursorCol + 1] = ".";

                        // Increment blocksPlaced, since one block has been placed, and moves
                        blocksPlaced++;
                        moves++;
                        updateScreen(board, cursorRow, cursorCol, width, height);
                        updateBoard(board, m);
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
        if ((gameMode > 1 && gameMode <= 2)) {
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

    public static boolean hasDeadEnds(String[][] board, int row) {
        String[] braces = new String[board.length * 5];
        int braceCount = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[row][i].equals(".") || board[row][i].equals("*"))
                continue;
            for (int j = i + 1; j < board.length; j++) {
                String brace = "";
                if (board[row][i].equals(board[row][j])) {
                    for (int k = i; k <= j; k++)
                        brace += board[row][k];
                    for (int x = 0; x < braces.length; x++)
                        if (brace.equals(braces[x]))
                            return true;
                    braces[braceCount] = brace;
                    brace = "";
                    braceCount++;
                    break;
                }
            }
        }

        return false;
    }

    public static boolean hasSplits(String[][] board) {
        String[] rows = new String[board.length];

        for (int r = 0; r < board.length; r++) {
            String row = "";
            for (int c = 0; c < board.length; c++) {
                row += board[r][c];
            }
            rows[r] = row;
        }

        for (int i = 0; i < rows.length; i++) {
            if (!rows[i].contains(".") && !rows[i].contains("*")) {
                for (int j = i + 1; j < rows.length; j++) {
                    if (!rows[j].contains(".") && !rows[j].contains("*") && rows[i].equals(rows[j])) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Void function that takes in two arguments, one 2D-String-array, and one
    // integer moves
    public static int determineScore(String[][] board, int moves) {
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
        return (int) Math.round(score);
    }

    public static void updateScreen(String[][] board, int x, int y, int width, int height) {
        double cellWidth = (width * 0.82) / board.length;

        StdDraw.clear(StdDraw.BLACK);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (x == j && y == i) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.filledSquare(
                            (i * (cellWidth * 1.1)) + (width - (board.length - 1) * (cellWidth * 1.1)) / 2,
                            height - (j * (cellWidth * 1.1)) - (width - (board.length - 1) * (cellWidth * 1.1)) / 2,
                            cellWidth * 1.2 / 2);
                }
                if (board[j][i].equals("."))
                    StdDraw.setPenColor(StdDraw.GRAY);
                else if (board[j][i].equals("*"))
                    StdDraw.setPenColor(StdDraw.WHITE);
                else if (board[j][i].equals("G"))
                    StdDraw.setPenColor(StdDraw.GREEN);
                else if (board[j][i].equals("Y"))
                    StdDraw.setPenColor(StdDraw.YELLOW);
                else if (board[j][i].equals("R"))
                    StdDraw.setPenColor(StdDraw.RED);
                else if (board[j][i].equals("B"))
                    StdDraw.setPenColor(StdDraw.BLUE);

                StdDraw.filledSquare((i * (cellWidth * 1.1)) + (width - (board.length - 1) * (cellWidth * 1.1)) / 2,
                        height - (j * (cellWidth * 1.1)) - (width - (board.length - 1) * (cellWidth * 1.1)) / 2,
                        cellWidth / 2);
            }
        }

        StdDraw.show();
    }

    public static void updateScreen(String[][] board, int x, int y, int width, int height, String[] messages) {
        double cellWidth = (width * 0.82) / board.length;

        StdDraw.clear(StdDraw.BLACK);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (x == j && y == i) {
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.filledSquare(
                            (i * (cellWidth * 1.1)) + (width - (board.length - 1) * (cellWidth * 1.12)) / 2,
                            height - (j * (cellWidth * 1.15)) - (width - (board.length - 1) * (cellWidth * 1.12)) / 2,
                            cellWidth * 1.2 / 2);
                }
                if (board[j][i].equals("."))
                    StdDraw.setPenColor(StdDraw.GRAY);
                else if (board[j][i].equals("*"))
                    StdDraw.setPenColor(StdDraw.WHITE);
                else if (board[j][i].equals("G"))
                    StdDraw.setPenColor(StdDraw.GREEN);
                else if (board[j][i].equals("Y"))
                    StdDraw.setPenColor(StdDraw.YELLOW);
                else if (board[j][i].equals("R"))
                    StdDraw.setPenColor(StdDraw.RED);
                else if (board[j][i].equals("B"))
                    StdDraw.setPenColor(StdDraw.BLUE);

                StdDraw.filledSquare((i * (cellWidth * 1.1)) + (width - (board.length - 1) * (cellWidth * 1.1)) / 2,
                        height - (j * (cellWidth * 1.1)) - (width - (board.length - 1) * (cellWidth * 1.1)) / 2,
                        cellWidth / 2);
            }
        }

        for (int i = 0; i < messages.length; i++) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(width / 2, 75 - (i * 20), messages[i]);
        }

        StdDraw.show();
    }

    public static void playSound(int pitch, double duration) {
        int SAMPLING_RATE = 44100;
        double hz = 440 * Math.pow(2, pitch / 12.0);
        int n = (int) (SAMPLING_RATE * duration);
        double[] a = new double[n + 1];
        for (int i = 0; i <= n; i++)
            a[i] = Math.sin(2 * Math.PI * i * hz / SAMPLING_RATE);
        StdAudio.play(a);
    }

    public static boolean Impasse(String[][] board, int k, String[] colors, int n) {
        String[][] testBoard = new String[board.length][board.length];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (!board[r][c].equals("."))
                    continue;
                for (int x = 0; x < board.length; x++) {
                    for (int y = 0; y < board.length; y++) {
                        testBoard[x][y] = board[x][y];
                    }
                }

                for (int i = 0; i < n; i++) {
                    testBoard[r][c] = colors[i];
                    if (c < board.length - 1)
                        testBoard[r][c + 1] = ".";
                    if (hasLegalMove(testBoard, k)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean hasLegalMove(String[][] board, int k) {
        boolean blockade = hasBlockade(board, k);
        boolean deadEnd = false;
        for (int i = 0; i < board.length; i++) {
            if (hasDeadEnds(board, i)) {
                deadEnd = true;
                break;
            }
        }
        boolean splits = hasSplits(board);
        return !blockade && !deadEnd && !splits;
    }
}