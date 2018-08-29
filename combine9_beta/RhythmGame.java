package combine9_beta;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class RhythmGame {

	private JFrame th;
	private ImageIcon introBackground = new ImageIcon(Main.class.getResource("../images/crowd.jpg"));
	// 배경화면 설정
	private JPanel primary;
	private JPanel THEFirstScreen;
	private JPanel MusicPanel = new JPanel();
	private JLabel BackGround = new JLabel(introBackground);
	private JLabel LOGO= new JLabel(new ImageIcon(Main.class.getResource("../images/logo.png")));
	private ImageIcon startButtonBasicImage = new ImageIcon(Main.class.getResource("../images/start.png"));
	private ImageIcon startButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/start_Entered.png"));
	private ImageIcon backButtonBasicImage = new ImageIcon(Main.class.getResource("../images/backButtonBasic.png"));
	private ImageIcon backButtonEnteredImage = new ImageIcon(
			Main.class.getResource("../images/backButtonEntered.png"));
	private ImageIcon exitButtonBasicImage = new ImageIcon(Main.class.getResource("../images/end.png"));
	private ImageIcon exitButtonEnteredImage = new ImageIcon(Main.class.getResource("../images/end_Entered.png"));

	private JButton Back = new JButton(backButtonBasicImage);
	private JButton Start = new JButton(startButtonBasicImage);
	private JButton End = new JButton(exitButtonBasicImage);
	private RML MouseL;
	
	Music introMusic = new Music("ShineLikeaStar.mp3", true);
	
	public RhythmGame(JPanel p,JPanel MainScreen,JFrame t) {
		th=t;
		primary=p;
		THEFirstScreen=MainScreen;
		
		
		
		MusicPanel.setPreferredSize(new Dimension(Main.screenWidth, Main.screenHeight));
		MusicPanel.setLayout(null);
		MusicPanel.setBounds(0, 0, 1280, 690);// primary에 (0,30)위치부터 시작한다

		BackGround.setSize(new Dimension(1280, 690));
		BackGround.setBounds(0, 0, 1280, 690);
		BackGround.setLayout(null);
		// primary.setBackground(new Color(255,255,255));
		// 배경 이미지 넣을곳
		MouseL = new RML();

		Back.setBounds(0, 0, 50, 50);
		buttonInit(Back);
		Start.setBounds(100, 500, 400, 100);
		buttonInit(Start);
		End.setBounds(800, 500, 400, 100);
		buttonInit(End);
		LOGO.setBounds(1050,0,200,200);
		// backButton설정
		BackGround.add(Back);
		BackGround.add(Start);
		BackGround.add(End);
		BackGround.add(LOGO);
		Back.addMouseListener(MouseL);
		Start.addMouseListener(MouseL);
		End.addMouseListener(MouseL);
		MusicPanel.add(BackGround);
		primary.add(MusicPanel);
		// MusicPanel위에 모든 MusicMain에 해당하는것들을 선언후 붙인다.
		introMusic.start();

	}

	private void buttonInit(JButton k) {
		k.setBorderPainted(false);
		k.setFocusPainted(false);
		k.setContentAreaFilled(false);
	}

	private void Back() {
		// isMainScreen = true;
		// isRhythmGameMainScreen = false;
		introMusic.close();
		THEFirstScreen.setVisible(true);
		MusicPanel.setVisible(false);
	}

	private void Start() {
		// isRhythmGameMainScreen = false;
		introMusic.close();
		MusicPanel.setVisible(false);
		new RhythmGameMusicSelection(primary,THEFirstScreen,th);

	}

	private void END() {
		System.exit(0);
	}

	private class RML implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == End) {
				End.setIcon(exitButtonEnteredImage);
				End.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (obj == Back) {
				Back.setIcon(backButtonEnteredImage);
				Back.setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (obj == Start) {
				Start.setIcon(startButtonEnteredImage);
				Start.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == End) {
				End.setIcon(exitButtonBasicImage);
				End.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else if (obj == Back) {
				Back.setIcon(backButtonBasicImage);
				Back.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			} else if (obj == Start) {
				Start.setIcon(startButtonBasicImage);
				Start.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == Back) {
				Back();
			} else if (obj == Start) {
				Start();
			} else if (obj == End) {
				END();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
	
	//초기에 리듬게임을 시작할때 나타나는 패널이다.
	//버튼 3개와 로고로 이루어져 있으며 Music클래스를 사용하여 배경 음악을 틀었다.

}
