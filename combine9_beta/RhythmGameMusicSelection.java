package combine9_beta;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RhythmGameMusicSelection extends JPanel {

	private Image screenImage;
	private Graphics screenGraphic;

	private JPanel primary;
	private JPanel THEFirstPanel;

	private JPanel MusicSelect = new BackGround();// .곡을 고르는 화면을 담는 Music Select 화면
	
	private Image musicSelectionImage = new ImageIcon(Main.class.getResource("../images/headphone.jpg")).getImage();

	private ImageIcon RightBasicImage = new ImageIcon(Main.class.getResource("../images/left.png"));
	private ImageIcon RightEnteredImage = new ImageIcon(Main.class.getResource("../images/left_Entered.png"));
	private ImageIcon LeftBasicImage = new ImageIcon(Main.class.getResource("../images/right.png"));
	private ImageIcon LeftEnteredImage = new ImageIcon(Main.class.getResource("../images/right_Entered.png"));
	private ImageIcon EasyBasicImage = new ImageIcon(Main.class.getResource("../images/Easy.png"));
	private ImageIcon EasyEnteredImage = new ImageIcon(Main.class.getResource("../images/Easy_selected.png"));
	private ImageIcon HardBasicImage = new ImageIcon(Main.class.getResource("../images/HARD.png"));
	private ImageIcon HardEnteredImage = new ImageIcon(Main.class.getResource("../images/HARD_selected.png"));
	private ImageIcon BackBasicImage = new ImageIcon(Main.class.getResource("../images/backButtonBasic.png"));
	private ImageIcon BackEnteredImage = new ImageIcon(Main.class.getResource("../images/backButtonEntered.png"));

	private JButton Right = new JButton(RightBasicImage);
	private JButton Left = new JButton(LeftBasicImage);
	private JButton MButton;
	private JButton Easy = new JButton(EasyBasicImage);
	private JButton Hard = new JButton(HardBasicImage);
	private JButton Back = new JButton(BackBasicImage);

	private boolean isMusicSelectionScreen;
	private boolean GameScreen;
	ArrayList<Track> trackList = new ArrayList<Track>();

	private Music selectedMusic;
	private Image titleImage; // 초기 곡 선택화면에서 곡 화면 이름
	private Image selectedImage; // 초기 곡 선택화면에서 곡 사진
	private int nowSelected = 0;// 현재 선택된 트랙 번호

	public static Game game;
	private MusicSelectListener MSL;
	

	RhythmGameMusicSelection(JPanel Primary, JPanel One, JFrame th) {
		trackList.add(new Track("mirrormirror.png", "night.png", "deemoBG.jpg", "mirror_night.mp3", "shortVer mirrornight.mp3",
				"MIRROR NIGHT"));
		trackList.add(new Track("rock.png", "Queen.png", "rockyou.jpg", "cropped rockyou.mp3",
				"shortVer rockyou.mp3", "WE WILL ROCK YOU"));
		trackList.add(new Track("stargazerr.png", "STARGAZER .jpg", "stargazer.png", "StarGazer.mp3", "shortVer stargazer.mp3",
				"STARGAZER"));
		//곡 정보, 앨범 표지, 백그라운드, 짧은 하이라이트 곡, 전체 곡, 곡 제목
		selectTrack(0);
		//첫번째곡 시작
		MSL = new MusicSelectListener();

		MusicSelect.addKeyListener(new keyListener());
		//MusicSelect.addKeyListener(new keyListener());
		MusicSelect.setFocusable(true);
		//MusicSelect.requestFocusInWindow();

		GameScreen = false;
		primary = Primary;
		THEFirstPanel = One;
		isMusicSelectionScreen = true;
		MusicSelect.setBounds(0, -30, 1280, 720);
		MusicSelect.setLayout(null);

		
		
		ImageIcon image=new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre= image.getImage();
		Image scale= pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		MButton = new JButton(new ImageIcon(scale));
		
		Right.setBounds(-10, 350, 100, 100);
		Left.setBounds(1200, 350, 100, 100);
		MButton.setBounds(0, 30, 50	, 50);
		Easy.setBounds(0, 570, 180, 160);
		Hard.setBounds(270, 570, 180, 160);
		Back.setBounds(0, 30, 50, 50);
		
		
		buttonInit(Right);
		buttonInit(Left);
		buttonInit(Easy);
		buttonInit(Hard);
		buttonInit(Back);
		buttonInit(MButton);

		Right.addMouseListener(MSL);
		MButton.addMouseListener(MSL);
		Left.addMouseListener(MSL);
		Hard.addMouseListener(MSL);
		Easy.addMouseListener(MSL);
		Back.addMouseListener(MSL);
		
		MusicSelect.add(Right);
		MusicSelect.add(Left);
		MusicSelect.add(MButton);
		MusicSelect.add(Hard);
		MusicSelect.add(Easy);
		MusicSelect.add(Back);

		Back.setVisible(false);

		primary.add(MusicSelect);
		
	}

	private void buttonInit(JButton k) {
		k.setBorderPainted(false);
		k.setFocusPainted(false);
		k.setContentAreaFilled(false);
	}

	class BackGround extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			screenImage = createImage(Main.screenWidth, Main.screenHeight);
			screenGraphic = screenImage.getGraphics();
			screenDraw((Graphics2D) screenGraphic);
			g.drawImage(screenImage, 0, 0, null);
		}// 가장 처음

		// 그림 그리는 곳
		public void screenDraw(Graphics2D g) {

			g.drawImage(musicSelectionImage, 0, 0, null);// 배경화면 출력
			if (isMusicSelectionScreen) {
				g.drawImage(selectedImage, 0, 150, null);
				g.drawImage(titleImage, 450, 150, null);
			}
			if (GameScreen) {
				game.screenDraw(g);
			}
			paintComponents(g);// 메인프레임 추가된 부분 보여주는것
			try {
				Thread.sleep(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//슬립
			repaint();
		}
	}

	public void selectTrack(int nowSelected) {
		if (selectedMusic != null)
			selectedMusic.close();
		
		titleImage = new ImageIcon(Main.class.getResource("../images/" + trackList.get(nowSelected).getTitleImage()))
				.getImage();
		selectedImage = new ImageIcon(Main.class.getResource("../images/" + trackList.get(nowSelected).getStartImage()))
				.getImage();
		selectedMusic = new Music(trackList.get(nowSelected).getStartMusic(), true);//무한 반복
		selectedMusic.start();
	}
	public void selectRight() {
		if (nowSelected == trackList.size() - 1)
		{
			nowSelected = 0;
		}
		else
		{
			nowSelected++;
		}
		selectTrack(nowSelected);
	}
	public void selectLeft() {
		if (nowSelected == 0)
		{
			nowSelected = trackList.size() - 1;
		}
		else
		{
			nowSelected--;
		}
		selectTrack(nowSelected);//시작
	}

	public void gameStart(int nowSelected, String difficult) {
		if (selectedMusic != null)
			selectedMusic.close();
		
		
		isMusicSelectionScreen = false;
		Right.setVisible(false);
		Left.setVisible(false);
		MButton.setVisible(false);
		Easy.setVisible(false);
		Hard.setVisible(false);
		Back.setVisible(true);
		
		
		
		musicSelectionImage = new ImageIcon(
				Main.class.getResource("../images/" + trackList.get(nowSelected).getGameImage())).getImage();

		game = new Game(trackList.get(nowSelected).getTitleName(), difficult,
				trackList.get(nowSelected).getGameMusic());
		game.start();
		MusicSelect.setFocusable(true);
		MusicSelect.requestFocusInWindow();
	}

	public void ReturnToMusicSelection() {

		Right.setVisible(true);
		Left.setVisible(true);
		MButton.setVisible(true);
		Easy.setVisible(true);
		Hard.setVisible(true);
		Back.setVisible(false);
		musicSelectionImage = new ImageIcon(Main.class.getResource("../images/headphone.jpg")).getImage();
		game.interrupt();
		game.close();

		selectTrack(nowSelected);
		isMusicSelectionScreen = true;
		// kill thread구현
	}

	private void RETURNTOMAIN() {

		MusicSelect.setVisible(false);
		THEFirstPanel.setVisible(true);
		selectedMusic.close();
	}

	private class MusicSelectListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == MButton) {
				RETURNTOMAIN();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj=e.getSource();
			if(obj==Right)
			{
				Right.setIcon(RightEnteredImage);
				Right.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			else if(obj==Left)
			{
				Left.setIcon(LeftEnteredImage);
				Left.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			else if(obj==Easy)
			{
				Easy.setIcon(EasyEnteredImage);
				Easy.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			else if(obj==Hard) {
				Hard.setIcon(HardEnteredImage);
				Hard.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Object obj=e.getSource();
			// TODO Auto-generated method stub
			if(obj==Right)
			{
				Right.setIcon(RightBasicImage);
				Right.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else if(obj==Left)
			{
				Left.setIcon(LeftBasicImage);
				Left.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else if(obj==Easy)
			{
				Easy.setIcon(EasyBasicImage);
				Easy.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else if(obj==Hard) {
				Hard.setIcon(HardBasicImage);
				Hard.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			Object obj = e.getSource();
			if (obj == MButton) {
				RETURNTOMAIN();
			} else if (obj == Right) {
				selectRight();
			} else if (obj == Left) {
				selectLeft();
			} else if (obj == Easy) {
				System.out.println("EASY");
				GameScreen = true;
				gameStart(nowSelected, "EASY");
			} else if (obj == Hard) {
				GameScreen = true;
				gameStart(nowSelected, "HARD");
			} else if (obj == Back) {
				GameScreen = false;
				isMusicSelectionScreen = false;
				ReturnToMusicSelection();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
