package kr.cbnu.lesson7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 해당 문제는 입력된 문자열을 추가하고, 목록을 필터링하는 문제입니다.
 *
 * 해당 문제는 다음 조건을 충족해야 합니다:
 * - 사용자가 텍스트필드에 포커스한 상태에서 엔터를 치면, 목록에 아이템을 추가하고 8개를 넘는다면 첫번째 리스트 아이템을 삭제해야 합니다.
 * - 사용자가 입력한 글자에 따라 문자열이 검색되어야 합니다. 예를 들어, 목록에 [오렌지, 사과, 딸기] 3개가 있을때 사용자가 "오렌"을 입력했다면 "오렌지"만이 표시되어야 합니다.
 *   각 목록은 \n(line break)로 구별되어야 합니다.
 */
public class AutoCompleteExample {

    public static void main(String[] args) {
        new SampleJFrame();
    }

    private static class SampleJFrame extends JFrame {
        private final JTextField field = new JTextField(10);

        private final JTextArea textArea = new JTextArea(3, 30);

        private final java.util.List<String> displayedItems = new ArrayList<>();

        public SampleJFrame() {
            setSize(500, 200);
            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("라벨을 입력하십시오."));
            panel.add(field);
            add(panel, BorderLayout.NORTH);
            add(textArea, BorderLayout.CENTER);
            field.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        displayedItems.add(field.getText());
                        addDisplay(field.getText());
                        field.setText("");
                    }
                    updateDisplay();
                }
            });
            setVisible(true);
        }

        private java.util.List<String> getAutoCompleteList(String starting) {
            return new ArrayList<>(starting.isEmpty() ? displayedItems : displayedItems.stream().filter(it -> it.startsWith(starting)).collect(Collectors.toList()));
        }

        private void updateDisplay() {
        	String starting = field.getText();
            java.util.List<String> autoCompleteList = getAutoCompleteList(starting);
            textArea.setText(autoCompleteList.stream().collect(Collectors.joining("\n")));
        }


        private void addDisplay(String message) {
        	if (displayedItems.size() > 8) {
                displayedItems.remove(0);
            }
            updateDisplay();
        }
    }


}
