import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameFrame extends JFrame {
    private JLabel guideLabel;      // 안내 문구 ("1~100 사이 숫자를 입력하세요")
    private JTextField inputField;  // 숫자 입력창
    private JTextArea logArea;      // 기록(로그) 보여주는 곳
    private JButton checkButton, restartButton; // 확인, 재시작 버튼

    private int targetNumber;       // 정답 숫자
    private int count;              // 시도 횟수

    public GameFrame() {
        // 1. 기본 창 설정
        setTitle("숫자 맞추기 게임 (Up & Down)");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. 상단 패널 (안내 문구)
        JPanel topPanel = new JPanel();
        guideLabel = new JLabel("1부터 100 사이의 숫자를 맞춰보세요!");
        guideLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        topPanel.add(guideLabel);

        // 3. 중앙 패널 (로그 영역)
        logArea = new JTextArea();
        logArea.setEditable(false); // 사용자가 수정 못하게 막기
        JScrollPane scrollPane = new JScrollPane(logArea);

        // 4. 하단 패널 (입력창 + 버튼들)
        JPanel bottomPanel = new JPanel();
        inputField = new JTextField(10);
        checkButton = new JButton("확인");
        restartButton = new JButton("재시작");
        restartButton.setEnabled(false); // 처음엔 재시작 버튼 비활성화

        bottomPanel.add(new JLabel("입력: "));
        bottomPanel.add(inputField);
        bottomPanel.add(checkButton);
        bottomPanel.add(restartButton);

        // 5. 컴포넌트 배치
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 게임 시작 (정답 생성)
        initGame();

        // 6. 이벤트 처리
        // '확인' 버튼 클릭 시
        checkButton.addActionListener(e -> checkNumber());
        // 입력창에서 엔터 쳤을 때
        inputField.addActionListener(e -> checkNumber());

        // '재시작' 버튼 클릭 시
        restartButton.addActionListener(e -> initGame());

        setVisible(true);
    }

    // 게임 초기화 메소드
    private void initGame() {
        Random random = new Random();
        targetNumber = random.nextInt(100) + 1; // 1 ~ 100 랜덤 숫자
        count = 0;

        logArea.setText("=== 게임이 시작되었습니다 ===\n");
        guideLabel.setText("1~100 사이 숫자를 입력하세요.");
        inputField.setText("");
        inputField.setEditable(true);
        checkButton.setEnabled(true);
        restartButton.setEnabled(false);
        inputField.requestFocus();
    }

    // 숫자 비교 로직 (핵심)
    private void checkNumber() {
        String text = inputField.getText().trim();

        try {
            int input = Integer.parseInt(text); // 문자를 숫자로 변환 시도

            // 1. 범위 체크 (1~100 벗어나면 경고)
            if (input < 1 || input > 100) {
                JOptionPane.showMessageDialog(this, "1부터 100 사이의 숫자만 입력해주세요!");
                inputField.setText("");      // 입력창 비우기
                inputField.requestFocus();   // 다시 입력할 수 있게 포커스
                return; // 함수 종료
            }

            // 2. 정상적인 숫자일 때 게임 진행
            count++; // 시도 횟수 증가

            if (input > targetNumber) {
                logArea.append(count + "회: " + input + "보다 작습니다. (Down)\n");
            } else if (input < targetNumber) {
                logArea.append(count + "회: " + input + "보다 큽니다. (Up)\n");
            } else {
                // 정답인 경우
                logArea.append("축하합니다! 정답은 " + targetNumber + "입니다.\n");
                logArea.append("총 시도 횟수: " + count + "번\n");
                guideLabel.setText("정답! 재시작 버튼을 누르세요.");

                // 게임 종료 처리
                inputField.setEditable(false);
                checkButton.setEnabled(false);
                restartButton.setEnabled(true);
            }
            // 입력 후 다음 입력을 위해 비우기
            inputField.setText("");
            inputField.requestFocus();

        } catch (NumberFormatException e) {
            // 3. 숫자가 아닌 값을 입력했을 때
            JOptionPane.showMessageDialog(this, "숫자만 입력해주세요!");
            inputField.setText("");      // 입력창 비우기
            inputField.requestFocus();   // 다시 입력할 수 있게 포커스
        }
    }
}