package kr.cbnu.lesson8;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 해당 파일은 간단한 파일 쓰기를 구현하는 문제입니다.
 * <p>
 * 해당 문제는 다음을 충족해야 합니다 :
 * - {@link FileSaveExample#saveFile(File, List)}을 구현하여 주어진 내용을 대상 파일에 입력해야 합니다.
 * <p>
 * 해당 문제는 다음의 제약 조건을 가집니다 :
 * - {@link FileSaveExample#saveFile(File, List)} 메서드만 수정할 수 있습니다.
 * - 파일이 존재하지 않는 경우, 생성해야 합니다.
 */
public class FileSaveExample {
    public static void main(String[] args) {
        try {
            saveFile(new File("test.txt"), Constants.SAMPLE_TEXT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(File f, List<String> data) throws IOException {
        if (!f.exists()) {
            f.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
