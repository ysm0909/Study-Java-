package kr.cbnu.lesson8;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Collections;

/**
 * 해당 문제는 네트워크로 파일을 전송하는 예제입니다.
 * <p>
 * 해당 문제는 다음을 충족해야 합니다 :
 * - {@link NetworkFileWriteExample#writeContents(DataOutputStream)}를 구현하여 로컬에 저장된 파일을 소켓으로 전송해야 합니다.
 * - 파일은 {@link FileReadExample#readFileOrNull(File)} 메서드로 읽은 내용을 전송해야 합니다.
 * <p>
 * 해당 문제는 다음의 제약 조건을 가집니다 :
 * - {@link NetworkFileWriteExample#writeContents(DataOutputStream)} 메서드만 수정할 수 있습니다.
 * - 만약 {@link FileReadExample#readFileOrNull(File)}에서 null을 반환했을 경우, 빈 리스트를 작성해야 합니다.
 */
public class NetworkFileWriteExample {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Constants.NETWORK_PORT);
            Socket socket = serverSocket.accept();
            writeContents(new DataOutputStream(socket.getOutputStream()));
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getFileContents() throws IOException {
        return FileReadExample.readFileOrNull(new File("test.txt"));
    }

    public static void writeContents(DataOutputStream dos) {
    	try {
            List<String> contents = getFileContents();
            if (contents == null) {
                contents = Collections.emptyList();
            }
            dos.writeInt(contents.size());
            for (String line : contents) {
                dos.writeUTF(line);
            }
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
