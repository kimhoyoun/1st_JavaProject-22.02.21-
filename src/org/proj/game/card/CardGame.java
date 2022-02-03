package org.proj.game.card;

import static org.proj.Resource.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.proj.RoundJButton;
import org.proj.controller.Controller;
import org.proj.view.GameView;

public class CardGame extends GameView {

	static ImageIcon backIcon = new ImageIcon("images/background.png");
	static ImageIcon gameBackIcon = new ImageIcon("images/gamebackImg.png");
	static ImageIcon startBackIcon = new ImageIcon("images/startback.png");
	static ImageIcon pauseIcon = new ImageIcon("images/pause.png");
	static ImageIcon checkIcon = new ImageIcon("images/checked.png");
	static JLabel Title; // 횟수 보여주기
	static JLabel gameBack; // 흰색 배경
	static JLabel back; // 초록 배경
	static LineBorder bb = new LineBorder(Color.black, 2, true); // 상단 라벨 테두리 맞추는 용도
	static EmptyBorder eb = new EmptyBorder(0, 0, 0, 0);
	static JPanel cardBack; // 카드 넣는 패널
	static JLabel startCardBack;
	static JLabel checkLabel;
	static RoundJButton bottomBtn01; // 시작하기
	static JButton pauseBtn;
	static JButton[] Btn = new JButton[12]; // 카드 12개
	static String[] img = { // 카드 이미지 주소 배열
			"img01.png", "img02.png", "img03.png", "img04.png", "img05.png", "img06.png", "img01.png", "img02.png",
			"img03.png", "img04.png", "img05.png", "img06.png" };

	static int sucessCount = 0;
	static int buttonIndexSave1 = 0; // 먼저 선택된 카드 인덱스 저장
	static int buttonIndexSave2 = 0; // 두번째 선택된 카드 인덱스 저장
	static int openCount = 0; // 카드가 2개 뒤집히면 닫히기 전까지 다음 카드 안열리게 하는 변수
	static int tryCount = 0;
	static Timer timer;
	static java.util.Timer countTimer;
	static int startCount;

//	public CardGame() {
//		display();
//		
//	}

	// 카드 이미지 변환
	static ImageIcon chageImage(String filname) {
		ImageIcon icon = new ImageIcon("images/" + filname);
		Image originImage = icon.getImage();
		Image changeImage = originImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		ImageIcon icon_new = new ImageIcon(changeImage);
		return icon_new;
	}

	// 시작 시 전체 카드 보여주기
	public void showCardAll() {

		countDown();

		for (int i = 0; i < Btn.length; i++) {
			Btn[i].setIcon(chageImage(img[i]));
		}

		timer = new Timer(3000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < Btn.length; i++) {
					Btn[i].setIcon(chageImage("leaf.png"));
				}
				timer.stop();
			}
		});
		timer.start();
	}

	public void countDown() {

		countTimer = new java.util.Timer();

		// 3초 카운트
		countTimer.scheduleAtFixedRate(new TimerTask() {

			int i = 3;

			public void run() {

				Title.setText("GAME START : " + i);

				i--;

				if (i < 0) {
					countTimer.cancel();

					Title.setText("시도 횟수 : " + tryCount);

				}
			}
		}, 0, 1000);
	}

	// 카드 섞기
	private void mixCard() {
		Random rand = new Random();
		for (int i = 0; i < 1000; i++) {
			int random = rand.nextInt(11) + 1;

			// swap
			String temp = img[0];
			img[0] = img[random];
			img[random] = temp;
		}
	}

	@Override
	public void display() {

		this.add(resultPane);
		resultPane.setBounds(FRAME_WIDTH / 2 - 300 / 2, FRAME_HEIGHT / 2 - 350 / 2, 300, 350);
		resultPane.setVisible(false);

		checkLabel = new JLabel(checkIcon);
		checkLabel.setBounds(680, 20, 150, 150);
		checkLabel.setVisible(false);

		pauseBtn = new JButton(pauseIcon);
		pauseBtn.setBounds(920, 30, 50, 50);
		pauseBtn.setBorderPainted(false);
		pauseBtn.setContentAreaFilled(false);

		pauseBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int yn = JOptionPane.showConfirmDialog(CardGame.this,"게임을 종료하시겠습니까? ", "확인", JOptionPane.YES_NO_OPTION);

				if (yn == 0) {
					Controller c = Controller.getController();
					gameNum = 0;
					gametrue = 0;
					c.Viewchange(MainPage);
				}
			}
		});

		// 시작시 정지화면
		startCardBack = new JLabel(startBackIcon);
		startCardBack.setBounds(250, 170, 510, 450);

		// 시작하기 버튼
		bottomBtn01 = new RoundJButton("시작하기");
		bottomBtn01.setBorderPainted(false);
		bottomBtn01.setContentAreaFilled(false);
		bottomBtn01.setBackground(Color.orange);
		bottomBtn01.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		bottomBtn01.setBounds(435, 630, 150, 40);

		// 시작하기 버튼 이벤트
		bottomBtn01.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (startCount == 1) {
					return;
				}

				startCardBack.setVisible(false);

				for (int i = 0; i < Btn.length; i++) {
					Btn[i].setEnabled(true);
				}

				Title.setText("시도 횟수 : " + tryCount);

				showCardAll();

				startCount++;

			} // end of actionPerformed

		});// 시작하기 버튼

		cardBack = new JPanel(new GridLayout(4, 3));
		cardBack.setBounds(251, 170, 505, 450);
		cardBack.setBackground(Color.white);

		Title = new JLabel("Card Game");
		Title.setLayout(null);
		Title.setForeground(Color.black);
		Title.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		Title.setHorizontalAlignment(JLabel.CENTER);
		// Title.setOpaque(true); JLabel background 컬러를 바꿀 때 사용
		// Title.setBackground(Color.black);
		Title.setBounds(250, 120, 510, 50);
//		Title.setBorder(bb); // 라벨 테두리 잡는 용도

		gameBack = new JLabel(gameBackIcon);
		gameBack.setLayout(null);
		gameBack.setBounds(180, 20, 650, 700);

		back = new JLabel(backIcon);
		back.setLayout(null);
		back.setBounds(0, 0, 1024, 768);

		// 카드 붙이기
		for (int i = 0; i < 12; i++) {
			Btn[i] = new JButton();
			Btn[i].setPreferredSize(new Dimension(100, 150));
//			Btn[i].setBorderPainted(false);
//			Btn[i].setContentAreaFilled(false);
			Btn[i].setBorder(eb);
			Btn[i].setBackground(Color.white);
			Btn[i].addActionListener(this);
			Btn[i].setIcon(chageImage("leaf.png"));
			Btn[i].setEnabled(false);
			cardBack.add(Btn[i]);
		}

		this.setLayout(null);
		this.setBackground(Color.black);
		this.setBounds(0, 0, 1024, 768);

		this.add(bottomBtn01);

		this.add(checkLabel);
		this.add(pauseBtn);
		this.add(startCardBack);
		this.add(cardBack);
		this.add(Title);

		this.add(gameBack);
		this.add(back);
		mixCard();
	}

	// 그림 맞추기 실패시 카드 되돌리기
	public void backToQuestion() {

		timer = new Timer(300, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				System.out.println("timer");

				openCount = 0;

				Btn[buttonIndexSave1].setIcon(chageImage("leaf.png"));
				Btn[buttonIndexSave2].setIcon(chageImage("leaf.png"));

				timer.stop();
			}

		});
		timer.start();
	}

	// 뒤집힌 카드 확인
	private boolean checkCard(int index1, int index2) {
		if (index1 == index2) {
			return false;
		}

		if (img[index1].equals(img[index2])) {
			return true;
		} else {
			return false;
		}
	}

	// 누른 카드 인덱스 번호 가져오기
	private int getIndex(JButton btn) {
		int index = 0;
		for (int i = 0; i < Btn.length; i++) {
			if (Btn[i] == btn) {
				index = i;
			}
		}
		return index;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (openCount == 2) {
			return;
		}

		JButton btn = (JButton) e.getSource();
		int index = getIndex(btn);
		System.out.println("index " + index);
		btn.setIcon(chageImage(img[index]));

		openCount++;

		if (openCount == 1) {
			buttonIndexSave1 = index;

		} else if (openCount == 2) {
			buttonIndexSave2 = index;

			if (Btn[buttonIndexSave1] == Btn[buttonIndexSave2]) {
				tryCount = tryCount;
			} else {
				tryCount++;
			}
			Title.setText("시도 횟수 : " + tryCount);

			boolean isBingo = checkCard(buttonIndexSave1, buttonIndexSave2);
			if (isBingo == true) {
				Btn[buttonIndexSave1].setEnabled(false);
				Btn[buttonIndexSave2].setEnabled(false);
				openCount = 0;
				sucessCount++;
				if (sucessCount == 6) {
					gameNum++;
					gametrue++;
					checkLabel.setVisible(true);
					JOptionPane.showMessageDialog(CardGame.this, "수고하셨습니다.");

					tryCount = 0;
					sucessCount = 0;
					startCount = 0;
					next();
				}
			} else {

				backToQuestion();

			}

		}

	}

	public void next() {
		if (gameNum == endGameNum) {
			resultPane.display();
		} else {
			Controller c = Controller.getController();
			c.Viewchange(CARD);
		}
	}

	@Override
	public String toString() {
		return CARD;
	}
}
