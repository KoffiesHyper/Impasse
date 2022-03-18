public class SU26105802 {
    public static void main(String[] args) {
        // String[] colors = { "\u001B[32mG\u001B[0m", "\u001B[33mY\u001B[0m", "\u001B[31mR\u001B[0m",
        //         "\u001B[34mB\u001B[0m" };

        String[] colors = { "G", "Y", "R",
                "B" };

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

        if (gameMode < 0 || gameMode > 4) {
            resetDefaultMessage += "First";
            gameMode = 0;
        }

        if (guiIndicator < 0 || guiIndicator > 2) {
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

        if (k < 0 || k > 10) {
            if (resetDefaultMessage.equals(""))
                resetDefaultMessage += "Forth";
            else
                resetDefaultMessage += "/Forth";
            k = 3;
        }

        if (!resetDefaultMessage.equals("")) {
            resetDefaultMessage += " argument(s) have been reset to their default values";
            StdOut.println(resetDefaultMessage);
            return;
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

        while (gameIsRunning) {
            // StdOut.println("We have entered the Game Loop!");
            updateBoard(board, m);

            if (hasBlockade(board, k)) {
                StdOut.println("Termination: Blockade!");
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
                    StdOut.println("Invalid move: Out of bounds!");
                    continue;
                }

                board[row][col] = ". ";
                col++;
                while (true) {
                    if (col == m)
                        break;
                    if (!board[row][col].equals("* ")) {
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
                    StdOut.println("Invalid move: Out of bounds!");
                    continue;
                }

                if (color < 0 || color > n - 1) {
                    StdOut.println("Invalid move: Unknown Color!");
                    continue;
                }

                if (!board[row][col].equals(". ")) {
                    StdOut.println("Invalid Move: Cell is not open!");
                    continue;
                }

                board[row][col] = colors[color] + " ";
                if (col <= m - 2)
                    board[row][col + 1] = ". ";

            } else if (move == 2) {
                StdOut.println("Termination: User terminated game!");
                break;
            }

            if (move < 0 || move > 2) {
                StdOut.println("Invalid move: Unkown Move!");
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
        if ((gameMode >= 1 && gameMode <= 2) || (n > 3) || (guiIndicator > 0)) {
            StdOut.println("Functionality currently not supported");
            return false;
        }
        return true;
    }

    public static void updateBoard(String[][] board, int m) {
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m; c++) {
                StdOut.print(board[r][c]);
            }
        }
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
}
