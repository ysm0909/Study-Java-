package kr.cbnu.lesson8;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class NetworkFileReadExample {
    public static void main(String[] args) {
        try {
            System.out.println("소켓 대기중..");
            Socket socket = openSocket();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            System.out.println("데이터 대기중..");
            System.out.println(readContents(dis));
            System.out.println("완료: ");
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket openSocket() {
    	try {
            Socket socket = new Socket("localhost", Constants.NETWORK_PORT);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readContents(DataInputStream dataInputStream) {
    	try {
            int size = dataInputStream.readInt();
            List<String> contents = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                String line = dataInputStream.readUTF();
                contents.add(line);
            }
            return contents;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
