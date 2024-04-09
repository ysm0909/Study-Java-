package kr.easw.lesson5;



import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * 해당 문제는 문자열을 받아 이 문자열이 시간인지 확인하고, 시간이라면 초로 환산하는 코드를 구현해야 합니다.
 * 해당 문제를 푸는 이상적인 정답은 정규식(Regex)를 사용하는것이나, 너무 어려울 경우 문자열 연산만을 사용해도 됩니다.
 *
 * 모든 정상적인 입력은 다음 포맷을 따라야 합니다 :
 *
 * "x시간 y분 z초"
 *
 * 다시 말해, 다음과 같은 시간은 정상 문자열입니다 
 *
 * "1시간 15분 20초"
 */
public class RegexExample {
    public static final String EXAMPLE_TIME = "1시간 0분 15초";
    public static final int EXAMPLE_RESULT = 3615;
    private static final DecimalFormat format = new DecimalFormat("#,###");

    public static void main(String[] args) {
        if (!isValidTimeFormat(EXAMPLE_TIME) || formatTime(EXAMPLE_TIME.split(" ")) != EXAMPLE_RESULT) {
            System.out.println("오답입니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("초로 변환할 시간을 입력하십시오 : ");
        String next = scanner.nextLine();
        if (!isValidTimeFormat(next)) {
            System.out.println("잘못된 포맷입니다.");
            return;
        }
        System.out.println(next + "는 " + format.format(formatTime(next.split(" "))) + "초입니다.");
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 입력된 문자열이 조건에 일치하는 문자열이라면 true, 아니라면 false를 반환해야 합니다.
     *
     * 허용되는 문자열 조건은 다음과 같습니다 :
     *
     * "x시간 y분 z초"
     *
     * 다시 말해, 다음과 같은 시간은 정상 문자열입니다 :
     *
     * "1시간 15분 20초"
     */
    private static boolean isValidTimeFormat(String str) {
        String pattern = "^(\\d+)시간 (\\d+)분 (\\d+)초$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.find();

    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     *
     * 입력된 문자열을 초로 환산해야 합니다.
     *
     * 해당 메서드를 통해 입력되는 모든 문자열은 이미 조건에 일치하는 조건이라고 가정되며, 항상 다음의 포맷을 따릅니다 :
     *
     * "x시간 y분 z초"
     */
    private static int formatTime(String[] string) {
        
    	int hours = Integer.parseInt(string[0].replace("시간", ""))* 3600;
        int minutes = Integer.parseInt(string[1].replace("분", ""))* 60;
        int seconds = Integer.parseInt(string[2].replace("초", ""));
        return hours + minutes + seconds;
    }
}