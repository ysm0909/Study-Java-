package kr.cbnu.lesson9;

import java.util.HashMap;
import java.util.Map;


/**
 * 해당 문제는 맵을 사용해 간단한 로그인을 구현하는 예제입니다.
 * <p>
 *
 * 해당 문제는 다음과 같은 제한사항을 가집니다 :
 * - {@link MapExample#registerAccount(String, String)}와 {@link MapExample#login(String, String)}의 수정만 허용됩니다.
 * - {@link MapExample#registerAccount(String, String)}는 맵에 데이터를 넣어야 합니다.
 * - {@link MapExample#login(String, String)}는 맵에서 데이터를 가져와 비교해야 합니다.
 */
public class MapExample {
    private static final Map<String, String> accounts = new HashMap<>();

    public static void main(String[] args) {
        registerAccount("test", "test2");
        if (!login("test", "test2")) {
            System.out.println("오답입니다.");
            return;
        }
        if (login("test3", "test")) {
            System.out.println("오답입니다.");
            return;
        }
        System.out.println("정답입니다.");
    }

    public static void registerAccount(String id, String pw) {
    	accounts.put(id, pw);
    }

    public static boolean login(String id, String pw) {
    	String storedPw = accounts.get(id);
        return storedPw != null && storedPw.equals(pw);
    }
}
