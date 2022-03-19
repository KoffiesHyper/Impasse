public class SU26105802 {
    public static void main(String[] args) {
        String[] colors = { "\u001B[32mG\u001B[0m", "\u001B[33mY\u001B[0m", "\u001B[31mR\u001B[0m",
                "\u001B[34mB\u001B[0m" };

        // String[] colors = { "G", "Y", "R",
        // "B" };

        boolean validArguments = argumentsIsValid(args);
        if (!validArguments)
            return;

        int gameMode = Integer.parseInt(args[0]);
        int guiIndicator = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int k = Integer.parseInt(args[3]);

        boolean supportedArguments = argumentsIsSupported(gameMode, guiIndicator, n, k);
        if (!supportedArguments)
            return;

        String resetDefaultMessage = "";

        if (gameMode < 0 || gameMode > 2) {
            resetDefaultMessage += "First";
            gameMode = 0;
        }

        if (guiIndicator < 0 || guiIndicator > 1) {
            if (resetDefaultMessage.equals(""))
                resetDefaultMessage += "Second";
            else
                resetDefaultMessage += "/Second";
            guiIndicator = 0;
        }

        if (n < 2 || n > 6) {
            if (resetDefaultMessage.equals(""))
                resetDefaultMessage += "Third";
            else
                resetDefaultMessage += "/Third";
            n = 2;
        }

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

        if (k < 3 || k > m) {
            if (resetDefaultMessage.equals(""))
                resetDefaultMessage += "Fourth";
            else
                resetDefaultMessage += "/Fourth";
            k = 3;
        }

        if (!resetDefaultMessage.equals("")) {
            resetDefaultMessage += " input reset to default";
            StdOut.println(resetDefaultMessage);
        }

        StdOut.println("The dimension of your board is: " + m + "x" + m);
        StdOut.println("The length of a blockade: " + k);

        String[][] board = new String[m][m];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m; c++) {
                if (c == 0)
                    board[r][c] = ". ";
                else
                    board[r][c] = "* ";

            }
        }

        boolean gameIsRunning = true;
        boolean dontUpdateBoard = false;
        int blocksPlaced = 0;

        while (gameIsRunning) {
            // StdOut.println("We have entered the Game Loop!");
            if (dontUpdateBoard) {
                dontUpdateBoard = false;
            } else {
                updateBoard(board, m);
                StdOut.println(blocksPlaced);
            }

            if (blocksPlaced == m * m) {
                StdOut.println("Termination: You have won!");
                determineScore(board);
                break;
            }

            if (hasBlockade(board, k)) {
                StdOut.println("Termination: Blockade!");
                determineScore(board);
                break;
            }

            StdOut.print("Move: ");
            int move = StdIn.readInt();

            if (move == 0) {
                StdOut.print("Row Number: ");
                int row = StdIn.readInt();

                StdOut.print("Column Number: ");
                int col = StdIn.readInt();

                if (!positionIsValid(col, row, m)) {
                    dontUpdateBoard = true;
                    StdOut.println("\nInvalid move: Out of bounds!\n");
                    continue;
                }

                if (board[row][col].equals(". ")) {
                    dontUpdateBoard = true;
                    StdOut.println("\nInvalid move: Nothing to delete!\n");
                    continue;
                }

                board[row][col] = ". ";
                col++;
                blocksPlaced--;
                while (true) {
                    if (col == m)
                        break;
                    if (!board[row][col].equals("* ")) {
                        if (!board[row][col].equals(". "))
                            blocksPlaced--;

                        board[row][col] = "* ";
                        col++;
                    } else {
                        break;
                    }
                }
            } else if (move == 1) {
                StdOut.print("Row Number: ");
                int row = StdIn.readInt();

                StdOut.print("Column Number: ");
                int col = StdIn.readInt();

                StdOut.print("Color: ");
                int color = StdIn.readInt();

                if (!positionIsValid(col, row, m)) {
                    dontUpdateBoard = true;
                    StdOut.println("\nInvalid move: Out of bounds!\n");
                    continue;
                }

                if (color < 0 || color > n - 1) {
                    dontUpdateBoard = true;
                    StdOut.println("\nInvalid move: Unknown Color!\n");
                    continue;
                }

                if (!board[row][col].equals(". ")) {
                    dontUpdateBoard = true;
                    StdOut.println("\nInvalid Move: Cell is not open!\n");
                    continue;
                }

                board[row][col] = colors[color] + " ";
                if (col <= m - 2)
                    board[row][col + 1] = ". ";
                blocksPlaced++;

            } else if (move == 2) {
                StdOut.println("Termination: User terminated game!");
                determineScore(board);
                break;
            } else if (move == 3) {
                for (int r = 0; r < m; r++) {
                    for (int c = 0; c < m; c++) {
                        board[r][c] = colors[r % 2] + " ";
                        if (r == m - 1 && c == m - 1)
                            board[r][c] = ". ";
                    }
                }
                blocksPlaced = m * m - 1;
            }

            if (move < 0 || move > 3) {
                StdOut.println("\nInvalid move: Unkown Move!\n");
                dontUpdateBoard = true;
                continue;
            }
        }

        StdOut.println("Game ended!");
    }

    public static Boolean argumentsIsValid(String[] args) {
        if (args.length < 4) {
            StdOut.println("Not enough arguments");
            return false;
        } else if (args.length > 4) {
            StdOut.println("Too many arguments");
            return false;
        }
        return true;
    }

    public static Boolean argumentsIsSupported(int gameMode, int guiIndicator, int n, int k) {
        if ((gameMode >= 1 && gameMode <= 2) || (n > 3) || (guiIndicator == 1)) {
            StdOut.println("Functionality currently not supported");
            return false;
        }
        return true;
    }

    public static void updateBoard(String[][] board, int m) {
        StdOut.print("\n");
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m; c++) {
                StdOut.print(board[r][c]);
            }
            StdOut.println();
        }
        StdOut.print("\n");
    }

    public static Boolean positionIsValid(int x, int y, int m) {
        if (x < 0 || x > m - 1 || y < 0 || y > m - 1) {
            return false;
        }
        return true;
    }

    public static boolean hasBlockade(String[][] board, int k) {
        int m = board.length;

        for (int c = 0; c < m; c++) {
            int numSameColor = 1;
            String currentColor = board[0][c];
            for (int r = 1; r < m; r++) {
                if (board[r][c].equals(currentColor)) {
                    numSameColor++;
                    if (numSameColor == k && !currentColor.equals(". ") && !currentColor.equals("* ")) {
                        return true;
                    }
                } else {
                    numSameColor = 1;
                    currentColor = board[r][c];
                }
            }
        }

        return false;
    }

    public static void determineScore(String[][] board) {
        int blocksPlaced = 0;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board.length; c++) {
                if (!board[r][c].equals(". ") && !board[r][c].equals("* ")) {
                    blocksPlaced++;
                }
            }
        }

        double score = (blocksPlaced / Math.pow(board.length, 2)) * 100;
        StdOut.printf("Score: %.0f", score);
        StdOut.print("%\n");
        StdOut.println("Moves: " + blocksPlaced);
    }
}
