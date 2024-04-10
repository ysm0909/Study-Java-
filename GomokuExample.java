package kr.easw.lesson6;

import java.util.Scanner;
/**
 * 해당 문제는 GomokuGame을 상속받아 오목을 구현해야 합니다.
 * <p>
 * 오목 클래스는 다음 조건을 충족해야 합니다:
 * - 흑 혹은 백이 대각선 혹은 수직 / 수평으로 5줄을 두었을 경우, 승리해야 합니다.
 * - 각 턴에서 플레이어는 1개의 바둑돌만을 둘 수 있습니다.
 * - 보드에 아무것도 없을 경우 0, 흑일 경우 1, 백일 경우 2로 설정되어야 합니다.
 * - 플레이어의 입력을 받아 어느 축에 바둑돌을 둘지 정해야 합니다. 입력받는 형식은 제한되지 않습니다.
 */
public class GomokuExample {
    public static final int GOMOKU_BOARD_SIZE = 5;

    public static void main(String[] args) {
        getGame().startGame();
    }

    public static GomokuGame getGame() {
        return new GomokuGameImpl();
    }

    public static abstract class GomokuGame {
        protected final int[][] board = new int[GOMOKU_BOARD_SIZE][GOMOKU_BOARD_SIZE];
        protected boolean isBlackTurn = false;

        /**
         * 게임을 새로 시작합니다.
         * <p>
         * 게임은 각 플레이어가 승리할때까지 반복합니다.
         */
        public void startGame() {
            while (true) {
                printBoard();
                waitTurn(isBlackTurn);
                if (isWin(board, isBlackTurn)) {
                    if (isBlackTurn) {
                        System.out.println("흑이 승리하였습니다.");
                    } else {
                        System.out.println("백이 승리하였습니다.");
                    }
                    return;
                }
                isBlackTurn = !isBlackTurn;
            }
        }

        private void printBoard() {
            System.out.print("   ");
            for (int x = 0; x < GOMOKU_BOARD_SIZE; x++) {
                if (x < 10)
                    System.out.print(x + "  ");
                else
                    System.out.print(x + " ");

            }
            System.out.println();
            for (int y = 0; y < GOMOKU_BOARD_SIZE; y++) {
                System.out.print(y + "  ");
                for (int x = 0; x < GOMOKU_BOARD_SIZE; x++) {
                    switch (board[x][y]) {
                        case 0: {
                            System.out.print("X" + "  ");
                        }
                        break;
                        case 1: {
                            System.out.print("1" + "  ");
                        }
                        break;
                        case 2: {
                            System.out.print("2" + "  ");
                        }
                        break;
                    }
                }
                System.out.println();
            }
        }

        public abstract void waitTurn(boolean turn);

        public abstract boolean isWin(int[][] board, boolean isBlackTurn);
    }

    public static class GomokuGameImpl extends GomokuGame {
        private Scanner scanner = new Scanner(System.in);

        @Override
        public void waitTurn(boolean turn) {
            String color = turn ? "흑" : "백";
            System.out.println(color + " 차례입니다. 바둑돌을 놓을 위치를 입력하세요. (예: x y)");
            while (true) {
                String input = scanner.nextLine();
                String[] inputArr = input.split(" ");
                if (inputArr.length != 2) {
                    System.out.println("잘못된 입력입니다. 다시 입력하세요. (예: x y)");
                    continue;
                }

                int x, y;
                try {
                    x = Integer.parseInt(inputArr[0]);
                    y = Integer.parseInt(inputArr[1]);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 다시 입력하세요. (예: x y)");
                    continue;
                }

                if (x < 0 || x >= GOMOKU_BOARD_SIZE || y < 0 || y >= GOMOKU_BOARD_SIZE) {
                    System.out.println("잘못된 입력입니다. 다시 입력하세요. (예: x y)");
                    continue;
                }

                if (board[x][y] != 0) {
                    System.out.println("이미 돌이 놓여진 위치입니다. 다시 입력하세요. (예: x y)");
                    continue;
                }

                board[x][y] = turn ? 1 : 2;
                break;
            }
        }

        @Override
        public boolean isWin(int[][] board, boolean isBlackTurn) {
            int target = isBlackTurn ? 1 : 2;

            // 가로 검사
            for (int y = 0; y < GOMOKU_BOARD_SIZE; y++) {
                for (int x = 0; x <= GOMOKU_BOARD_SIZE - 5; x++) {
                    boolean win = true;
                    for (int i = 0; i < 5; i++) {
                        if (board[x + i][y] != target) {
                            win = false;
                            break;
                        }
                    }
                    if (win) {
                        return true;
                    }
                }
            }

            // 세로 검사
            for (int x = 0; x < GOMOKU_BOARD_SIZE; x++) {
                for (int y = 0; y <= GOMOKU_BOARD_SIZE - 5; y++) {
                    boolean win = true;
                    for (int i = 0; i < 5; i++) {
                        if (board[x][y + i] != target) {
                            win = false;
                            break;
                        }
                    }
                    if (win) {
                        return true;
                    }
                }
            }

            // 대각선 검사 (좌상단 -> 우하단)
            for (int x = 0; x <= GOMOKU_BOARD_SIZE - 5; x++) {
                for (int y = 0; y <= GOMOKU_BOARD_SIZE - 5; y++) {
                    boolean win = true;
                    for (int i = 0; i < 5; i++) {
                        if (board[x + i][y + i] != target) {
                            win = false;
                            break;
                        }
                    }
                    if (win) {
                        return true;
                    }
                }
            }

            // 대각선 검사 (우상단 -> 좌하단)
            for (int x = 0; x <= GOMOKU_BOARD_SIZE - 5; x++) {
                for (int y = GOMOKU_BOARD_SIZE - 1; y >= 4; y--) {
                    boolean win = true;
                    for (int i = 0; i < 5; i++) {
                        if (board[x + i][y - i] != target) {
                            win = false;
                            break;
                        }
                    }
                    if (win) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}