package kr.cbnu.lesson9;

/**
 * 해당 문제는 맵을 사용해 간단한 제너릭스를 구현해보는 예제입니다.
 * <p>
 *
 * 해당 문제는 다음과 같은 제한사항을 가집니다 :
 * - {@link GenericsExample#getProvider()}의 수정과 클래스 1개의 생성만 허용됩니디.
 * - 생성된 클래스는 {@link GenericsExample.TestGenericsClass<String>}를 상속받아야 합니다.
 * - 생성된 클래스는 {@link GenericsExample#SAMPLE_TEXT}를 반환해야 합니다.
 */
public class GenericsExample {
    private static final String SAMPLE_TEXT = "Hello, World!";

    public static void main(String[] args) {
        System.out.println("정답: " + SAMPLE_TEXT);
        TestGenericsClass<String> target = getProvider();
        System.out.println(target.get().equals(SAMPLE_TEXT));
    }

    public static <T> TestGenericsClass<T> getProvider() {
    	return new CustomGenericsClass<>();
    }

    public static interface TestGenericsClass<T> {
        T get();
    }
    public static class CustomGenericsClass<T> implements TestGenericsClass<T> {
        @Override
        public T get() {
            return (T) SAMPLE_TEXT;
        }
    }
}
