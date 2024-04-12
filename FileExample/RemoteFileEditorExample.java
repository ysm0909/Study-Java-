package kr.cbnu.lesson8;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


/**
 * 해당 문제는 네트워크로 파일을 전송하여 저장하고 불러오는 간단한 GUI 예제입니다.
 * <p>
 * 해당 문제는 다음을 충족해야 합니다 :
 * - {@link NetworkHost}를 구현해야 합니다.
 *   - {@link NetworkHost}는 ServerSocket을 사용하여 연결을 받아야 합니다. (Socket을 대체할 프레임워크를 사용해도 괜찮습니다.)
 * - {@link NetworkClient}를 구현해야 합니다.
 *   - {@link NetworkClient}는 Socket을 사용하여 연결을 받아야 합니다. (Socket을 대체할 프레임워크를 사용해도 괜찮습니다.)
 * <p>
 * 해당 문제는 다음 조건을 충족시 추가 점수를 받습니다.
 * - Socket을 대체할 수 있는 프레임워크를 사용합니다. (1점)
 *   다음은 예제이며, 다른 프레임워크를 사용해도 괜찮습니다.
 *   - Netty
 *   - gRPC
 * - 프로토콜을 수정하여 파일 선택이 가능하도록 만듭니다. (3점)
 *  - 파일 선택 다이얼로그를 추가합니다. (2점)
 * - 사용자 로그인을 구현합니다. (1점)
 *   - 고정된 사용자가 아닌, 회원가입과 로그인을 구현합니다. (3점)
 *   - 사용자마다 폴더를 구분하여 접근 가능한 폴더를 제약합니다. (2점)
 *   - 연결에 암호화를 적용합니다. (3점)
 *   - 파일 선택 다이얼로그 대신 사용자 디렉토리를 사용합니다. (2점)
 */
public class RemoteFileEditorExample {
    public static void main(String[] args) {
        try {
            getNetworkHost().startServer();
        } catch (Exception ex) {
            System.out.println("서버 시작 실패");
        }
        new RemoteFileEditorGui();
    }

    private static NetworkHost getNetworkHost() {
        return new NetworkHost() {
            @Override
            public void startServer() throws Exception {
                // 서버 소켓 생성과 로직 구현
            }
        };
    }

    private static NetworkClient getNetworkClient() {
        return new NetworkClient() {
            @Override
            public void startClient() throws Exception {
                // 클라이언트 로직 구현
            }

            @Override
            public List<String> readFileFromServer() {
                // 서버로부터 파일 읽기 로직 구현
                return new ArrayList<>();
            }

            @Override
            public void writeFileToServer(List<String> text) {
                // 서버에 파일 쓰기 로직 구현
            }
        };
    }

    interface NetworkHost {
        void startServer() throws Exception;
    }

    interface NetworkClient {
        void startClient() throws Exception;

        List<String> readFileFromServer();

        void writeFileToServer(List<String> text);
    }

    public static class RemoteFileEditorGui extends JFrame {
        private final JTextArea textArea = new JTextArea();

        public RemoteFileEditorGui() {
            setSize(600, 300);
            setTitle("메모장");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JMenuBar bar = new JMenuBar();
            JMenu menu = new JMenu("파일");
            JMenuItem item = new JMenuItem(new AbstractAction("불러오기..") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    performLoad();
                }
            });
            menu.add(item);
            item = new JMenuItem(new AbstractAction("저장..") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    performSave();
                }
            });
            menu.add(item);
            bar.add(menu);
            setJMenuBar(bar);
            add(textArea);
            setVisible(true);
            performLoad();
        }

        private void performSave() {
            try {
                getNetworkClient().writeFileToServer(Arrays.asList(textArea.getText().split("\n")));
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("저장 실패.");
            }
        }

        private void performLoad() {
            try {
                StringBuilder sb = new StringBuilder();
                for (String x : getNetworkClient().readFileFromServer()) {
                    if (sb.length() != 0) {
                        sb.append("\n");
                    }
                    sb.append(x);
                }
                textArea.setText(sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("불러오기 실패.");
            }
        }
    }
}
