package kr.easw.lesson5;

import java.util.Random;

/**
 * 해당 문제는 생성자로 값을 받아 보관하고, 값을 설정할 떄는 제한을 두지 않되 값을 가져올떄는 비밀번호를 비교하여 접근을 제한하여야 합니다.
 */
public class InformationHidingExample {
    private static final char[] PASSWORD_CHARS = "0123456789_".toCharArray();
    private static final Random random = new Random();

    public static void main(String[] args) {
        String password = generateRandomChars();
        Vault vault = new Vault(password);
        String[] preGenerated = new String[5];
        for (int i = 0; i < preGenerated.length; i++) {
            preGenerated[i] = generateRandomChars();
        }
        for (int i = 0; i < 5; i++) {
            vault.set(i, preGenerated[i]);
        }
        int index = random.nextInt(preGenerated.length);
        if (!vault.access(password, index).equals(preGenerated[index])) {
            System.out.println("오답입니다.");
            return;
        }
        try {
            vault.access(generateRandomChars(), 9);
            System.out.println("오답입니다.");
        } catch (IllegalStateException ex) {
            System.out.println("정답입니다.");
        }
    }

    private static String generateRandomChars() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(PASSWORD_CHARS[random.nextInt(PASSWORD_CHARS.length)]);
        }
        return stringBuilder.toString();
    }

    private static class Vault {
        private final String[] stored = new String[5];
        private final String password;
        /**
         * 해당 생성자는 다음과 같은 역할을 가져야 합니다 :
         * 입력된 password를 새 변수에 보관해야 합니다.
         */
        public Vault(String password) {
        	this.password = password;
        }

        /**
         * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
         * 지정한 인덱스에 값을 삽입해야 합니다.
         *
         * 만약 인덱스가 최대 크기보다 크다면, IllegalStateException을 발생시켜야 합니다.
         */
        public void set(int index, String target) {
        	if (index >= stored.length) {
                throw new IllegalStateException("인덱스가 최대 크기보다 큽니다.");
            }
            stored[index] = target;
        }

        /**
         * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
         * 지정한 인덱스에 존재하는 값을 반환해야 합니다.
         *
         * 만약 저장된 비밀번호와 입력된 비밀번호가 다르다면 IllegalStateException을 발생시켜야 합니다.
         */
        public String access(String password, int index) {
        	 if (!this.password.equals(password)) {
                 throw new IllegalStateException("저장된 비밀번호와 입력된 비밀번호가 다릅니다.");
             }
             if (index >= stored.length) {
                 throw new IllegalStateException("인덱스가 최대 크기보다 큽니다.");
             }
             return stored[index];
        }
    }
}