public class SU26105802 {
    public static void main(String[] args) {
        char[] colors = { 'G', 'Y', 'R', 'B' };

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

            StdOut.print("Move: ");
            int move = StdIn.readInt();

            if (move == 2)
                break;

            StdOut.print("Row Number: ");
            int row = StdIn.readInt();

            StdOut.print("Column Number: ");
            int col = StdIn.readInt();

            StdOut.print("Color: ");
            int color = StdIn.readInt();

            if (move < 0 || move > 2) {
                StdOut.println("Invalid move: Unkown Move!");
                continue;
            }

            if(row < 0 || row > m || col < 0 || col > m){
                StdOut.println("Invalid Move: Outside of board!");
                continue;
            }

            if (move == 1) {
                if (board[row][col].equals(". ")) {
                    board[row][col] = colors[color] + " ";
                    if (col <= m - 2)
                        board[row][col + 1] = ". ";
                } else {
                    StdOut.println("Invalid Move: Cell is not open!");
                }
            } else if (move == 0) {
                board[row][col] = ". ";
                    col++;
                while (true) {
                    if(col == m) break;
                    if (!board[row][col].equals("* ")) {
                        board[row][col] = "* ";
                        col++;
                    } else {
                        break;
                    }
                }
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
            StdOut.println();
        }
    }
}
