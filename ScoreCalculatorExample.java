package kr.easw.lesson5;

import java.util.Scanner;


/**
 * 해당 문제는 사용자의 입력으로 점수를 받고, 모든 점수를 받았다면 백분율과 최고 점수를 반환하는 코드를 작성해야 합니다.
 */
public class ScoreCalculatorExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("입력할 점수 개수를 입력하십시오 : ");
        ScoreData[] array = generateArray(scanner.nextInt());
        fillArray(scanner, array);
        for (ScoreData score : array) {
            System.out.printf("과목: %s (%d점, %f%%)", score.name, score.score, getPercentage(array, score));
        }
        ScoreData highestScore = getHighestScore(array);
        System.out.printf("최고 점수: %s (%d점, %f%%)", highestScore.name, highestScore.score, getPercentage(array, highestScore));
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 주어진 값만큼의 크기를 갖는 배열을 반환해야 합니다.
     */
    public static ScoreData[] generateArray(int size) {
    	return new ScoreData[size];
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 주어진 {@link Scanner} 변수를 사용해 배열을 채워야 합니다.
     *
     * 입력되는 값은 과목 이름(String), 과목 점수(Int)입니다.
     */
    public static void fillArray(Scanner scanner, ScoreData[] array) {
    	for (int i = 0; i < array.length; i++) {
            System.out.printf("과목 %d 입력 (이름, 점수): ", i + 1);
            String name = scanner.next();
            int score = scanner.nextInt();
            array[i] = new ScoreData(name, score);
        }
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 입력된 data 파라미터가 현재 배열의 점수를 합산한 값의 몇퍼센트인지 백분율로 반환해야 합니다.
     *
     * 예를 들어, 입력된 과목의 점수가 10이고, 전체 전수가 100이면 10을 반환해야 합니다.
     */
    public static double getPercentage(ScoreData[] array, ScoreData data) {
    	 double total = 0;
         for (ScoreData score : array) {
             total += score.getScore();
         }
         return (double) data.getScore() / total * 100;
    }


    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 가장 높은 과목의 인스턴스를 반환해야 합니다.
     */
    public static ScoreData getHighestScore(ScoreData[] array) {
    	ScoreData highestScore = array[0];
        for (ScoreData score : array) {
            if (score.getScore() > highestScore.getScore()) {
                highestScore = score;
            }
        }
        return highestScore;
    }

    private static class ScoreData {
        private final String name;

        private final int score;

        public ScoreData(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}