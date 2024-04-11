package kr.cbnu.lesson7;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 해당 문제는 actionPerformed를 구현하여 메시지를 출력하는 예제입니다.
 *
 * 해당 문제는 다음 조건을 충족해야 합니다:
 * - 버튼을 눌렀을 경우, "Test"가 콘솔에 출력되어야 합니다.
 */
public class EventListeningExample {

    public static void main(String[] args) {
        new SampleJFrame();
    }


    private static class SampleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Text");
        }
    }

    private static class SampleJFrame extends JFrame {
        public SampleJFrame() {
            setSize(500, 500);
            JButton b = new JButton();
            b.setLabel("B u t t o n");
            b.addActionListener(new SampleListener());
            add(b);
            setVisible(true);
        }
    }
}