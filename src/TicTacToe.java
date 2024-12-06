import java.util.*;

public class TicTacToe {

    // Global variables to store the positions chosen by the player and the CPU
    static ArrayList<Integer> playerPositions = new ArrayList<>();
    static ArrayList<Integer> cpuPositions = new ArrayList<>();

    public static void main(String[] args) {
        // Initialize the Tic Tac Toe game board
        char[][] gameBoard = {
                {' ', '|', ' ', '|', ' '},
                {'-', '+', '-', '+', '-'},
                {' ', '|', ' ', '|', ' '},
                {'-', '+', '-', '+', '-'},
                {' ', '|', ' ', '|', ' '}
        };

        // Game loop, runs until the user decides to quit
        while (true) {
            // Display the current state of the game board
            printGameBoard(gameBoard);

            Scanner scan = new Scanner(System.in);
            System.out.print("Enter your placement (1 - 9): ");
            int playerPosition = scan.nextInt();

            // Ensure the position is not already taken by the player or CPU
            while (playerPositions.contains(playerPosition) || cpuPositions.contains(playerPosition)) {
                System.out.println("Position taken! Enter a correct position:");
                playerPosition = scan.nextInt();
            }

            // Place the player's symbol on the board
            placePiece(gameBoard, playerPosition, "player");

            // Check if the player has won or if the game is a tie
            String result = checkWinner();
            if (!result.isEmpty()) {
                printGameBoard(gameBoard);
                System.out.println(result);
                System.out.print("Do you want to play again? (yes/no): ");
                String playAgain = scan.next().toLowerCase();

                // Reset the game or exit the loop based on the player's choice
                if (playAgain.equals("yes")) {
                    resetGame(gameBoard);
                    continue;
                } else {
                    break;
                }
            }

            // Get the CPU's move
            int cpuPosition = getSmartCpuMove();
            // Place the CPU's symbol on the board
            placePiece(gameBoard, cpuPosition, "cpu");

            // Check if the CPU has won or if the game is a tie
            result = checkWinner();
            if (!result.isEmpty()) {
                printGameBoard(gameBoard);
                System.out.println(result);
                System.out.print("Do you want to play again? (yes/no): ");
                String playAgain = scan.next().toLowerCase();
                if (playAgain.equals("yes")) {
                    resetGame(gameBoard);
                    continue;
                } else {
                    break;
                }
            }
        }
    }

    // Prints the current state of the game board
    public static void printGameBoard(char[][] gameBoard) {
        for (char[] row : gameBoard) {
            for (char c : row) {
                System.out.print(c); // Print each character in the row
            }
            System.out.println(); // Move to the next line
        }
    }

    // Places the player's or CPU's symbol on the game board
    public static void placePiece(char[][] gameBoard, int position, String user) {
        char symbol = ' ';

        // Determine the symbol to place ('X' for player, 'O' for CPU)
        if (user.equals("player")) {
            symbol = 'X';
            playerPositions.add(position);
        } else if (user.equals("cpu")) {
            symbol = 'O';
            cpuPositions.add(position);
        }

        // Map the input position (1-9) to the correct row and column on the game board
        switch (position) {
            case 1:
                gameBoard[0][0] = symbol;
                break;
            case 2:
                gameBoard[0][2] = symbol;
                break;
            case 3:
                gameBoard[0][4] = symbol;
                break;
            case 4:
                gameBoard[2][0] = symbol;
                break;
            case 5:
                gameBoard[2][2] = symbol;
                break;
            case 6:
                gameBoard[2][4] = symbol;
                break;
            case 7:
                gameBoard[4][0] = symbol;
                break;
            case 8:
                gameBoard[4][2] = symbol;
                break;
            case 9:
                gameBoard[4][4] = symbol;
                break;
            default:
                break;
        }
    }

    // Checks if there is a winner or a tie
    public static String checkWinner() {
        // Define the possible winning conditions
        List<List<Integer>> winningConditions = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9),
                Arrays.asList(1, 4, 7),
                Arrays.asList(2, 5, 8),
                Arrays.asList(3, 6, 9),
                Arrays.asList(1, 5, 9),
                Arrays.asList(3, 5, 7)
        );

        // Check if the player or CPU satisfies any winning condition
        for (List<Integer> condition : winningConditions) {
            if (playerPositions.containsAll(condition)) {
                return "Congratulations, you won!";
            } else if (cpuPositions.containsAll(condition)) {
                return "CPU wins! Sorry :(";
            }
        }

        // Check for a tie (all positions filled)
        if (playerPositions.size() + cpuPositions.size() == 9) {
            return "It's a tie!";
        }

        return ""; // No winner or tie yet
    }

    // Resets the game state for a new round
    public static void resetGame(char[][] gameBoard) {
        playerPositions.clear();
        cpuPositions.clear();

        // Reset the game board rows
        for (int i = 0; i < gameBoard.length; i++) {
            Arrays.fill(gameBoard[i], ' ');
        }

        // Restore the horizontal separators and column dividers
        for (int i = 1; i < gameBoard.length; i += 2) {
            gameBoard[i][0] = '-';
            gameBoard[i][1] = '+';
            gameBoard[i][2] = '-';
            gameBoard[i][3] = '+';
            gameBoard[i][4] = '-';
        }

        // Restore the vertical separators
        for (int i = 0; i < gameBoard.length; i += 2) {
            gameBoard[i][1] = '|';
            gameBoard[i][3] = '|';
        }
    }

    // Determines the CPU's next move intelligently
    public static int getSmartCpuMove() {
        List<List<Integer>> winningConditions = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9),
                Arrays.asList(1, 4, 7),
                Arrays.asList(2, 5, 8),
                Arrays.asList(3, 6, 9),
                Arrays.asList(1, 5, 9),
                Arrays.asList(3, 5, 7)
        );

        // Check for a winning move
        for (List<Integer> condition : winningConditions) {
            int countCpu = 0;
            int emptySpot = 0;
            for (int pos : condition) {
                if (cpuPositions.contains(pos)) countCpu++;
                else if (!playerPositions.contains(pos)) emptySpot = pos;
            }
            if (countCpu == 2 && !playerPositions.contains(emptySpot) && emptySpot != 0) {
                return emptySpot;
            }
        }

        // Check for a blocking move
        for (List<Integer> condition : winningConditions) {
            int countPlayer = 0;
            int emptySpot = 0;
            for (int pos : condition) {
                if (playerPositions.contains(pos)) countPlayer++;
                else if (!cpuPositions.contains(pos)) emptySpot = pos;
            }
            if (countPlayer == 2 && !cpuPositions.contains(emptySpot) && emptySpot != 0) {
                return emptySpot;
            }
        }

        // Take the center if available
        if (!playerPositions.contains(5) && !cpuPositions.contains(5)) {
            return 5;
        }

        // Take a corner if available
        List<Integer> corners = Arrays.asList(1, 3, 7, 9);
        for (int corner : corners) {
            if (!playerPositions.contains(corner) && !cpuPositions.contains(corner)) {
                return corner;
            }
        }

        // Take a side if available
        List<Integer> sides = Arrays.asList(2, 4, 6, 8);
        for (int side : sides) {
            if (!playerPositions.contains(side) && !cpuPositions.contains(side)) {
                return side;
            }
        }

        // Fallback: Random move
        Random rand = new Random();
        int cpuMove;
        do {
            cpuMove = rand.nextInt(9) + 1;
        } while (playerPositions.contains(cpuMove) || cpuPositions.contains(cpuMove));

        return cpuMove;
    }
}