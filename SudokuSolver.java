/**
 * Class that solves the Asterisk Sudoku.
 * Prints the number of solutions of a Sudoku if there are multiple. If there is only a single solution, prints this solution instead.
 *
 * by Onno Kniknie 1421883
 * and Brent Manders 1465465
 * as group 169
 */
// TODO add Brents sID
class SudokuSolver {

    int SUDOKU_SIZE = 9;          // Size of the grid.
    int SUDOKU_MIN_NUMBER = 1;    // Minimum digit to be filled in.
    int SUDOKU_MAX_NUMBER = 9;    // Maximum digit to be filled in.
    int SUDOKU_BOX_DIMENSION = 3; // Dimension of the boxes (sub-grids that should contain all digits).

    int[][] grid = new int[][]{  // The puzzle grid; 0 represents empty.
            {0, 9, 0, 7, 3, 0, 4, 0, 0},    // One solution.
            {0, 0, 0, 0, 0, 0, 5, 0, 0},
            {3, 0, 0, 0, 0, 6, 0, 0, 0},

            {0, 0, 0, 0, 0, 2, 6, 4, 0},
            {0, 0, 0, 6, 5, 1, 0, 0, 0},
            {0, 0, 6, 9, 0, 7, 0, 0, 0},

            {5, 8, 0, 0, 0, 0, 0, 0, 0},
            {9, 0, 0, 0, 0, 3, 0, 2, 5},
            {6, 0, 3, 0, 0, 0, 8, 0, 0},
    };

    int[][] asterisks = new int[][]{{4, 1}, {2, 2}, {6, 2}, {1, 4}, {4, 4}, {7, 4}, {2, 6}, {6, 6}, {4, 7}};

    int solutionCounter = 0; // Solution counter
    int rEmpty;
    int cEmpty;

    boolean isEven(int x) {
        return x % 2 == 0;
    }

    // Is there a conflict when we fill in d at position (r, c)?
    boolean givesConflict(int r, int c, int d) {
        return rowConflict(r, d) || columnConflict(c, d) || boxConflict(r, c, d) || asteriskConflict(r, c, d);
    }

    // Is there a conflict when we fill in d in row r?
    boolean rowConflict(int r, int d) {
        for (int c = 0; c < SUDOKU_SIZE; c++) {
            if (d == grid[r][c]) {
                return true;
            }
        }

        return false;
    }

    // Is there a conflict in column c when we fill in d?
    boolean columnConflict(int c, int d) {
        for (int r = 0; r < SUDOKU_SIZE; r++) {
            if (d == grid[r][c]) {
                return true;
            }
        }

        return false;
    }

    // Is there a conflict in the box at (r, c) when we fill in d?
    boolean boxConflict(int r, int c, int d) {
        int boxR = r / SUDOKU_BOX_DIMENSION;
        int boxC = c / SUDOKU_BOX_DIMENSION;
        int boxSize = SUDOKU_SIZE / SUDOKU_BOX_DIMENSION;

        for (int i = 0; i < boxSize; i++) {
            for (int j = 0; j < boxSize; j++) {
                if (d == grid[boxR * boxSize + i][boxC * boxSize + j]) {
                    return true;
                }
            }
        }

        return false;
    }

    // Is there a conflict in the asterisk when we fill in d?
    boolean asteriskConflict(int r, int c, int d) {
        for (int i = 0; i < asterisks.length; i++) {
            if (r == asterisks[i][0] && c == asterisks[i][1]) {
                for (int j = 0; j < asterisks.length; j++) {
                    if (d == grid[asterisks[j][0]][asterisks[j][1]]) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Finds the next empty square (in "reading order").
    int[] findEmptySquare() {
        for (int i = 0; i < SUDOKU_SIZE; i++) {
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                if (grid[i][j] == 0) {
                    rEmpty = i;
                    cEmpty = j;
                    return new int[]{i, j};
                }
            }
        }

        return new int[]{-1, -1};
    }

    // Find all solutions for the grid, and stores the final solution.
    void solve() {
        int[] emptySquare = findEmptySquare().clone();
        if (emptySquare[0] != -1) {
            System.out.println("here1");
            for (int d = SUDOKU_MIN_NUMBER; d <= SUDOKU_MAX_NUMBER; d++) {
                if (!givesConflict(emptySquare[0], emptySquare[1], d)) {
                    System.out.println("here2");
                    grid[emptySquare[0]][emptySquare[1]] = d;
                    solve();
                }
            }
        } else {
            solutionCounter++;
            return;
        }
    }

    // Print the sudoku grid.
    void print() {
        int boxWidth = SUDOKU_SIZE / SUDOKU_BOX_DIMENSION;  //Width of the boxes
        int boxAmount = SUDOKU_SIZE / boxWidth;             //Amount of boxes
        int rowSeparators = boxAmount + 1;                  //Amount of solid horizontal lines
        int columnSeparators = rowSeparators;               //Amount of solid vertical lines
        int printWidth = SUDOKU_SIZE + columnSeparators + boxAmount * (boxWidth - 1);  //Total width of the printed grid
        int printHeight = SUDOKU_SIZE + rowSeparators;

        for (int i = 0; i < printHeight; i++) {
            String output = "";

            //Print horizontal lines
            if (i % rowSeparators == 0) {
                output += "+";
                for (int j = 0; j < printWidth - 2; j++) {
                    output += "-";
                }
                output += "+";
            } else {
                for (int j = 0; j < printWidth; j++) {
                    //Print rows with content
                    if (j % (columnSeparators + 2) == 0) {
                        //Print vertical lines
                        output += "|";
                    } else {
                        //Print numbers or spaces in the boxes
                        if (!isEven(j)) {
                            if (grid[i - i / rowSeparators - 1][(j - 1) / 2] != 0) {
                                //Print numbers
                                output += grid[i - i / rowSeparators - 1][(j - 1) / 2];
                            } else {
                                //Print spaces
                                output += " ";
                            }
                        } else {
                            //Insert the asterisks
                            int iGrid = i - i / rowSeparators - 1;
                            int jGrid = (j - 1) / 2;

                            if (iGrid == 1 || iGrid == 7) {
                                if (jGrid == 3) {
                                    output += ">";
                                } else if (jGrid == 4) {
                                    output += "<";
                                } else {
                                    output += " ";
                                }
                            } else if (iGrid == 4) {
                                if (jGrid == 0 || jGrid == 3 || jGrid == 6) {
                                    output += ">";
                                } else if (jGrid == 1 || jGrid == 4 || jGrid == 7) {
                                    output += "<";
                                } else {
                                    output += " ";
                                }
                            } else if (iGrid == 2 || iGrid == 6) {
                                if (jGrid == 1) {
                                    output += ">";
                                } else if (jGrid == 6) {
                                    output += "<";
                                } else {
                                    output += " ";
                                }
                            } else {
                                output += " ";
                            }
                        }
                    }
                }
            }

            System.out.println(output);
        }
    }

    // Run the actual solver.
    void solveIt() {
        solve();
        print();
    }

    public static void main(String[] args) {
        (new SudokuSolver()).solveIt();
    }
}
