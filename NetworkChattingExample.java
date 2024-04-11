package kr.cbnu.lesson7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 해당 문제는 간단한 채팅 프로그램을 구현하는 예제입니다.
 *
 * 해당 문제는 다음 조건을 충족해야 합니다:
 * - 사용자가 텍스트필드에 포커스한 상태에서 엔터를 치면, 채팅 서버에 채팅을 전송해야 합니다.
 *   채팅 서버는 Byte array로 입력을 받으며, 이는 ChattingMessage#asByteArray로 변환이 가능합니다.
 * - 서버에서 채팅이 들어오면 이를 GUI에 업데이트하여야 합니다.
 *   채팅은 가장 마지막에 들어온것이 아래에 있어야 합니다.
 * - 해당 문제는 모든 코드의 수정이 허용됩니다.
 *
 * 다음 조건을 충족시, 추가 점수를 받습니다 :
 * - (선택) 채팅시, 닉네임을 입력하고 받게끔 수정하십시오.
 * - (선택) 닉네임 옵션을 구현했다면, 닉네임을 구현하지 않은 클라이언트가 접속해도 정상 작동하게끔 수정하십시오.
 * - (선택) 채팅 접속 해제 옵션을 구현하십시오.
 * - (선택) 현재 코드에서는 사용자가 접속을 해제하면 서버에 문제가 발생합니다. 이 문제를 해결하십시오.
 * - (선택) 현재 코드에서는 서버가 강제 종료되면 클라이언트에 문제가 발생합니다. 이 문제를 해결하십시오.
 * - (선택) {@link ChatInterference}를 통해 채팅 암호화를 구현하십시오.
 * - (선택) 사용자가 채팅 암호화를 선택 가능하도록 지원하십시오. 다음은 해당 문제의 암호화 요구사항입니다. 더 추가되어도 문제는 없습니다.
 *   - RSA2048
 *   - AES256
 *   - DES
 */
public class NetworkChattingExample {

    public static void main(String[] args) {
        boolean clientConnected = setupClient();
        if (!clientConnected) {
            System.out.println("호스트를 찾을 수 없습니다. 서버부터 시작합니다.");
            setupServer();
            clientConnected = setupClient();
        }
        if (clientConnected) {
            System.out.println("호스트에 접속하였습니다.");
        } else {
            System.out.println("호스트에 접속할 수 없습니다.");
        }
    }

    private static boolean setupClient() {
        try {
            ChattingClient client = new ChattingClient();
            onClientInitialize(client);
            client.start();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private static void setupServer() {
        try {
            ChattingHost host = new ChattingHost();
            onServerInitialize(host);
            host.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void onServerInitialize(ChattingHost hostServer) {
    	// X
    }

    private static void onClientInitialize(ChattingClient client) {
    	
    	//선택1,2
    	String username = JOptionPane.showInputDialog("닉네임을 입력하십시오.");
        ChattingGui gui = new ChattingGui(client, username);
    	
    	//ChattingGui gui = new ChattingGui(client);
    }

    private static class ChattingGui extends JFrame {
        private final JTextField field = new JTextField(10);
        private final JTextArea textArea = new JTextArea(3, 30);
        private final ChattingClient client; //추가
        private final String username; //선택 1,2

        //추가
        public ChattingGui(ChattingClient client, String username) {
        	this.client = client; //
        	this.username = username; //선택 1,2
            this.client.addChattingTrigger(this::appendChat); //
        	
        	setSize(500, 200);
        	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //선택 1,2
            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("채팅을 입력하십시오."));
            panel.add(field);
            add(panel, BorderLayout.NORTH);
            add(textArea, BorderLayout.CENTER);
            
            textArea.setEditable(false); //선택 1,2
            field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        onChat(field.getText());
                        field.setText("");
                    }
                }
            });
            setVisible(true);
        }

        private void onChat(String message) {
        	//추가 
        	 
        	ChattingMessage chatMessage = new ChattingMessage(username, message); //선택1,2
        	//ChattingMessage chatMessage = new ChattingMessage("User", message);
             client.sendPacket(chatMessage.asByteArray());
        }
        
        //추가
        private void appendChat(ChattingMessage chattingMessage) {
            textArea.append(chattingMessage.getUser() + ": " + chattingMessage.getMessage() + "\n");
        }

    }

    private static class ChattingHost implements PacketSender {
        private final List<ChattingThread> threads = new ArrayList<>();

        private final List<Consumer<ChattingThread>> initializer = new ArrayList<>();
        private final Object DATA_LOCK = new Object();

        public ChattingHost() {
            addThreadInitializer(thread -> {
                thread.addInterference(new ChatInterference() {
                    @Override
                    public byte[] onSend(byte[] output) {
                        return output;
                    }

                    @Override
                    public byte[] onReceive(byte[] input) {
                        synchronized (DATA_LOCK) {
                            threads.forEach(it -> it.addMessage(input));
                        }
                        return input;
                    }
                });
            });
        }

        public void addThreadInitializer(Consumer<ChattingThread> consumer) {
            initializer.add(consumer);
        }

        public void start() throws IOException {
            ServerSocket serverSocket = new ServerSocket(9978);
            new Thread(() -> {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        synchronized (DATA_LOCK) {
                            ChattingThread chattingThread = new ChattingThread(socket);
                            initializer.forEach(it -> it.accept(chattingThread));
                            threads.add(chattingThread);
                            chattingThread.start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void sendPacket(byte[] message) {
            synchronized (DATA_LOCK) {
                threads.forEach(it -> it.addMessage(message));
            }
        }
    }


    private static class ChattingClient implements PacketSender {
        private final ChattingThread thread;
        private final List<ChattingTrigger> triggers = new ArrayList<>();

        public ChattingClient() throws IOException {
            Socket socket = new Socket("localhost", 9978);
            this.thread = new ChattingThread(socket);
            addInterference(new ChatInterference() {
                @Override
                public byte[] onSend(byte[] output) {
                    return output;
                }

                @Override
                public byte[] onReceive(byte[] input) {
                    ChattingMessage chattingMessage = new ChattingMessage(input);
                    triggers.forEach(it -> it.onChat(chattingMessage));
                    return input;
                }
            });
        }

        public void addInterference(ChatInterference interference) {
            thread.addInterference(interference);
        }

        public void addChattingTrigger(ChattingTrigger chattingTrigger) {
            this.triggers.add(chattingTrigger);
        }

        public void start() {
            thread.start();
        }

        @Override
        public void sendPacket(byte[] message) {
            thread.addMessage(message);
        }
    }

    private static class ChattingThread {
        private final DataInputStream inputStream;
        private final DataOutputStream outputStream;

        private final List<byte[]> buffer = new ArrayList<>();

        private final Object OBJECT_LOCK = new Object();

        private final List<ChatInterference> listInterference = new ArrayList<>();

        public ChattingThread(Socket socket) {
            try {
                this.inputStream = new DataInputStream(socket.getInputStream());
                this.outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void start() {
            new Thread(() -> {
                while (true) {
                    try {
                        performRead();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(1L);
                        performWrite();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void performWrite() throws IOException {
            List<byte[]> msgToWrite;
            synchronized (OBJECT_LOCK) {
                msgToWrite = new ArrayList<>(buffer);
                buffer.clear();
            }
            outputStream.writeInt(msgToWrite.size());
            for (byte[] b : msgToWrite) {
                for (ChatInterference interference : listInterference) {
                    b = interference.onSend(b);
                }
                outputStream.writeInt(b.length);
                outputStream.write(b);
            }
            outputStream.flush();
        }

        private void performRead() throws IOException {
            int packetAmount = inputStream.readInt();
            for (int i = 0; i < packetAmount; i++) {
                byte[] arr = new byte[inputStream.readInt()];
                inputStream.read(arr);
                listInterference.forEach(it -> it.onReceive(arr));
            }
        }

        public void addMessage(byte[] message) {
            synchronized (OBJECT_LOCK) {
                this.buffer.add(message);
            }
        }

        public void addInterference(ChatInterference interference) {
            this.listInterference.add(interference);
        }
    }


    private static class ChattingMessage {
        private String user;
        private String message;

        public ChattingMessage(String user, String message) {
            this.user = user;
            this.message = message;
        }

        public ChattingMessage(byte[] arr) {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(arr);
            DataInputStream dataIn = new DataInputStream(byteIn);
            try {
                this.user = dataIn.readUTF();
                this.message = dataIn.readUTF();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String getMessage() {
            return message;
        }

        public String getUser() {
            return user;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public byte[] asByteArray() {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            try {
                dataOut.writeUTF(getUser());
                dataOut.writeUTF(getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return byteOut.toByteArray();
        }
    }

    private interface ChatInterference {
        byte[] onSend(byte[] output);

        byte[] onReceive(byte[] input);
    }

    private interface ChattingTrigger {

        void onChat(ChattingMessage chattingMessage);
    }

    private interface PacketSender {
        void sendPacket(byte[] message);
    }
}