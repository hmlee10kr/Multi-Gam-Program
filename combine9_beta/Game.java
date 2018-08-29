package combine9_beta;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {

	private Image noteRouteLineImage = new ImageIcon(Main.class.getResource("../images/noteRouteLine.png")).getImage();
	private Image noteRouteLineComboImage= new ImageIcon(Main.class.getResource("../images/comboOver10.png")).getImage();
	private Image noteRouteLineComboOver50Image=new ImageIcon(Main.class.getResource("../images/comboOver50.png")).getImage();
	// 판정선 부분
	private Image judgementLineImage = new ImageIcon(Main.class.getResource("../images/judgementLine.png")).getImage();
	// 게임 시 밑에서 게임 정보(곡 제목, 점수, 단계)를 알려주는 부분
	private Image gameInfoImage = new ImageIcon(Main.class.getResource("../images/GameInfoImage.png")).getImage();
	// S키 부분
	private Image noteRouteSImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// D키 부분
	private Image noteRouteDImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// F키 부분
	private Image noteRouteFImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// 스페이스바 부분
	private Image noteRouteSpace1Image = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	private Image noteRouteSpace2Image = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// J키 부분
	private Image noteRouteJImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// K키 부분
	private Image noteRouteKImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// L키 부분
	private Image noteRouteLImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
	// 화면 중간에 반짝이는 부분을, 사용자가 노트를 어떻게 눌렀나(Perfect, miss, great, good etc.)에 대한 정보 표시
	private Image FLARE = new ImageIcon(Main.class.getResource("../images/flare.png")).getImage();
	private Image ENDBACKGROUND=new ImageIcon(Main.class.getResource("../images/selectionBackground.jpg")).getImage();
	private Image LOGO=new ImageIcon(Main.class.getResource("../images/logo.png")).getImage();
	private String titleName;// 제목을 써주는 변수
	private String difficulty;// 난이도
	private String musicTitle;// 노래를 시작시켜주기 위한 변수
	private Music gameMusic;
	private Music scoreMusic;
	public static int Life;
	public static int Wrong=-3;
	public static int Gamescore = 0;// 점수 합산을 위한 변수
	// 노트의 landing에 따라good, perfect, late, early, great, miss으로 나눌 수 있으며, 각 단꼐별로 점수가
	// 다르다
	private final int GOOD = 100;
	private final int PERFECT = 500;
	private final int LATE = 50;
	private final int EARLY = 50;
	private final int GREAT = 200;
	
	private int flag = 0;
	private int LifeFlag=0;
	private String GameScoreString; // 점수
	private String returnedValueOfNoteJudgement; // 노트의 landing을 판정하기 위한 변수
	public static String JUDGEMENT; // 노트의 landing을 판정하기 위한 변수
	public static int combo;// combo 판단을 위한 변수
	public static String COMBO;
	public static int Maxcombo;
	public static String MAXCOMBO;
	private boolean MusicFlag;
	ArrayList<Note> noteList = new ArrayList<Note>();

	// Game 클래스 생성자, 초기화
	public Game(String titleName, String difficulty, String musicTitle) {
		LifeFlag=0;
		flag = 0;
		MusicFlag=false;
		Gamescore = 0;
		JUDGEMENT = new String("");
		combo = 0;
		Maxcombo=0;
		if(difficulty.equals("HARD"))
		{
			Life=60;
		}
		else {
			Life=100;
		}
		
		this.titleName = titleName;
		this.difficulty = difficulty;
		this.musicTitle = musicTitle;
		gameMusic = new Music(this.musicTitle, false);
		scoreMusic= new Music("ROBOTICS.mp3", true);
	}

	public void screenDraw(Graphics2D g) {
		if ((!gameMusic.isAlive()&& (MusicFlag==true))||(LifeFlag!=0)) {
			// 게임 뮤직이 끝날을때 추가 구현 해야함
			g.drawImage(ENDBACKGROUND, 0, 0, null);
			g.drawImage(LOGO, 700, 150, null);
			g.setColor(Color.black);
			g.setFont(new Font("Elephant", Font.BOLD, 50));
			g.drawString(titleName, 150, 400);
			MAXCOMBO = String.format("MAXCOMBO: %3d", Maxcombo);
			g.drawString(MAXCOMBO,150,500);
			g.drawString("Score:   "+GameScoreString, 150, 600);
			
			this.interrupt();
			if (flag == 0 && !gameMusic.isInterrupted()) {
				for (int i = 0; i < noteList.size(); i++) {
					Note note = noteList.get(i);
					note.KILLTHREAD();

				}//내려오는 노드 죽이고
				System.out.println("점수곡 시작");
				scoreMusic.start();//엔딩송 시작
				flag++;
				
			}
		} else {//게임이 정상 진행중일때
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			if (difficulty.equals("EASY")) { // 난이도가 easy일 때
				// 노트의 루트이미지를 그린다.
				g.drawImage(noteRouteSImage, 228, 30, null);
				g.drawImage(noteRouteDImage, 332, 30, null);
				g.drawImage(noteRouteKImage, 848, 30, null);
				g.drawImage(noteRouteLImage, 952, 30, null);

				
				g.drawImage(noteRouteLineImage, 328, 30, null);
				g.drawImage(noteRouteLineImage, 432, 30, null);
				g.drawImage(noteRouteLineImage, 844, 30, null);
				g.drawImage(noteRouteLineImage, 948, 30, null);

			} else if (difficulty.equals("HARD")) {// 난이도가 hard일 때
				// 노트의 루트이미지를 그린다.
				g.drawImage(noteRouteSImage, 228, 30, null);
				g.drawImage(noteRouteDImage, 332, 30, null);
				g.drawImage(noteRouteFImage, 436, 30, null);
				g.drawImage(noteRouteSpace1Image, 540, 30, null);
				g.drawImage(noteRouteSpace2Image, 640, 30, null);
				g.drawImage(noteRouteJImage, 744, 30, null);
				g.drawImage(noteRouteKImage, 848, 30, null);
				g.drawImage(noteRouteLImage, 952, 30, null);

				g.drawImage(noteRouteLineImage, 224, 30, null);
				g.drawImage(noteRouteLineImage, 328, 30, null);
				g.drawImage(noteRouteLineImage, 432, 30, null);
				g.drawImage(noteRouteLineImage, 536, 30, null);
				g.drawImage(noteRouteLineImage, 740, 30, null);
				g.drawImage(noteRouteLineImage, 844, 30, null);
				g.drawImage(noteRouteLineImage, 948, 30, null);
				g.drawImage(noteRouteLineImage, 1052, 30, null);
			}
			// *******
			g.drawImage(gameInfoImage, 0, 660, null);
			g.drawImage(judgementLineImage, 0, 580, null);
			
			g.setColor(Color.black);
			// 판정선과 게임 정보
			for (int i = 0; i < noteList.size(); i++) {
				Note note = noteList.get(i);//노트 리스트로부터 받아오고 새로운 note에 대입시킨다.
				if (!note.isProceeding()) {//노트가 진행중이 아닐경우 
					noteList.remove(i);//리스트에서 없애고
					i--;//하나 줄인다.
				} // 없애고
				else {
					note.screenDraw(g);
				}//나머지 경우 그린다.

			}
			// 노트 그리기
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.setColor(Color.blue);
			g.drawString(titleName, 20, 702);
			g.setColor(Color.WHITE);
			if(difficulty.equals("EASY"))
			{
				g.setColor(Color.yellow);
				g.drawString(difficulty, 1190, 702);
			}
			else
			{
				g.setColor(Color.WHITE);
				g.drawString(difficulty, 1190, 702);
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.setColor(Color.orange);
			if (difficulty.equals("EASY")) {// 난이도가 easy일 때 key setting
				g.drawString("S", 270, 609);
				g.drawString("D", 374, 609);
				g.drawString("K", 889, 609);
				g.drawString("L", 993, 609);

			} else if (difficulty.equals("HARD")) {// 난이도가 hard일 때 key setting
				g.drawString("S", 270, 609);
				g.drawString("D", 374, 609);
				g.drawString("F", 478, 609);
				g.drawString("Space Bar", 580, 609);
				g.drawString("J", 784, 609);
				g.drawString("K", 889, 609);
				g.drawString("L", 993, 609);
			}

			// 게임 점수와 게임점수 합산 방법에 대한 안내 부분
			
			g.setColor(Color.LIGHT_GRAY);// ************
			g.setFont(new Font("Elephant", Font.BOLD, 30));
			GameScoreString = String.format("%06d", Gamescore);
			g.drawString(GameScoreString, 565, 702);
			g.drawImage(FLARE, 440, 350, null);
			g.setFont(new Font("Arial", Font.BOLD, 15)); // 합산 방법 안내에 대한 글씨체 설정
			g.setColor(Color.green);// 색깔 설정
			// 방법 안내에 대한 내용 및 위치 설정
			g.drawString("MISS = -100", 1100, 60);
			g.drawString("LATE = +50", 1100, 90);
			g.drawString("EARLY = +50", 1100, 120);
			g.drawString("GOOD = +100", 1100, 150);
			g.drawString("GREAT = +200", 1100, 180);
			g.drawString("PERFECT = +500", 1100, 210);
			// ******
			g.setFont(new Font("Elephant", Font.BOLD, 30));
			g.setColor(Color.white);
			if (JUDGEMENT.equals("PERFECT")) {
				g.setFont(new Font("Elephant", Font.BOLD, 30));
				g.setColor(Color.red);
				g.drawString(JUDGEMENT, 550, 450);
				g.setFont(new Font("Elephant", Font.BOLD, 30));
				g.setColor(Color.white);
			} else {
				g.drawString(JUDGEMENT, 580, 450);
			}
			
			COMBO = String.format("COMBO: %3d", combo);
			g.setFont(new Font("SansSerif",Font.BOLD,30));
			g.setColor(Color.magenta);
			g.drawString(COMBO, 1090, 300);
			
			//라이프에 관한것 그리자
			g.setColor(Color.red);
			g.drawString("LIFE :"+Life, 0, 560);
			if(Life>80) {
				g.fillRect(0, 250,50,50);
				g.fillRect(0, 305,50,50);
				g.fillRect(0, 360,50,50);
				g.fillRect(0, 415,50,50);
				g.fillRect(0, 470,50,50);
				
			}else if(Life>60)
			{
				g.fillRect(0, 305,50,50);
				g.fillRect(0, 360,50,50);
				g.fillRect(0, 415,50,50);
				g.fillRect(0, 470,50,50);
				
			}else if(Life>40)
			{
				g.fillRect(0, 360,50,50);
				g.fillRect(0, 415,50,50);
				g.fillRect(0, 470,50,50);
				
				
			}else if(Life>20) {
				g.fillRect(0, 415,50,50);
				g.fillRect(0, 470,50,50);
				
			}else if(Life>=0) {
				g.fillRect(0, 470,50,50);
				
			}else {
				//이부분 보완
				//라이프가 0보다 작을때에는 노래를 끝내고 점수판을 보여주어야한다.
				if(LifeFlag==0) {
					gameMusic.close();
					this.interrupt();
					for (int i = 0; i < noteList.size(); i++) {
						Note note = noteList.get(i);
						note.KILLTHREAD();

					}
					g.drawImage(ENDBACKGROUND,0,0,null);
					
					LifeFlag++;
				}
			}
			
			
		} // During the game play
	}// screenDraw

	// S,D,F,SpaceBar,J,K,L을 누를 때 소리가 나도록 설정
	public void pressS() {
		judge("S");// *******
		noteRouteSImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteSImage=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteSImage=noteRouteLineComboOver50Image;
		}
		new Music("drumSmall1.mp3", false).start();
	}

	public void releaseS() {
		noteRouteSImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	public void pressD() {
		judge("D");
		noteRouteDImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteDImage=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteDImage=noteRouteLineComboOver50Image;
		}
		new Music("drumSmall1.mp3", false).start();
	}

	public void releaseD() {
		noteRouteDImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	public void pressF() {
		judge("F");
		noteRouteFImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteFImage=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteFImage=noteRouteLineComboOver50Image;
		}
		new Music("drumSmall1.mp3", false).start();
	}

	public void releaseF() {
		noteRouteFImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	public void pressSpace() {
		judge("Space");
		noteRouteSpace1Image = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		noteRouteSpace2Image = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteSpace1Image=noteRouteLineComboImage;
			noteRouteSpace2Image=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteSpace1Image=noteRouteLineComboOver50Image;
			noteRouteSpace2Image=noteRouteLineComboOver50Image;
		}
		new Music("drumBig1.mp3", false).start();
	}

	public void releaseSpace() {
		noteRouteSpace1Image = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();
		noteRouteSpace2Image = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	public void pressJ() {
		judge("J");
		noteRouteJImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteJImage=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteJImage=noteRouteLineComboOver50Image;
		}
		new Music("drumSmall1.mp3", false).start();
	}

	public void releaseJ() {
		noteRouteJImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	public void pressK() {
		judge("K");
		noteRouteKImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteKImage=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteKImage=noteRouteLineComboOver50Image;
		}
		new Music("drumSmall1.mp3", false).start();
	}

	public void releaseK() {
		noteRouteKImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	public void pressL() {
		judge("L");
		noteRouteLImage = new ImageIcon(Main.class.getResource("../images/noteRoutePressed.png")).getImage();
		if(combo>10)
		{
			noteRouteLImage=noteRouteLineComboImage;
		}
		if(combo>20)
		{
			noteRouteLImage=noteRouteLineComboOver50Image;
		}
		new Music("drumSmall1.mp3", false).start();
	}

	public void releaseL() {
		noteRouteLImage = new ImageIcon(Main.class.getResource("../images/noteRoute.png")).getImage();

	}

	@Override
	public void run() {
		START();
	}
	//start시에 note떨어트리기

	public void close() {

		gameMusic.close();
		this.interrupt();
		for (int i = 0; i < noteList.size(); i++) {
			Note note = noteList.get(i);
			note.KILLTHREAD();

		}
		//노트에 대한 쓰레드 다 죽이고
		if (scoreMusic!=null) {
			if(scoreMusic.isAlive())
			{
			scoreMusic.close();
			}
		}
		//스코어 노래가 살아있으면 끈다

	}

	// ************
	public void START() {
		Beats[] BEATS = null;//비트 넣을것 마련
		if (titleName.equals("STARGAZER")) {//Stargazer
			if (difficulty.equals("EASY")) {
				int startTime =100;
				int term = 150;// 비트 약 120
				System.out.println("START");
				BEATS = new Beats[] { 
						new Beats(startTime + term, "D"), new Beats(startTime + term, "K"),
						new Beats(startTime + term * 5, "D"), new Beats(startTime + term * 5, "K"),
						new Beats(startTime + term * 9, "S"), new Beats(startTime + term * 9, "L"),
						new Beats(startTime + term * 13, "D"), new Beats(startTime + term * 13, "K"),
						new Beats(startTime + term * 17, "D"), new Beats(startTime + term * 17, "K"),
						new Beats(startTime + term * 21, "S"),new Beats(startTime + term * 21, "L"),
						new Beats(startTime + term * 25, "D"),new Beats(startTime + term * 25, "K"),
						new Beats(startTime + term * 29, "D"),new Beats(startTime + term * 29, "K"),
						new Beats(startTime + term * 33, "S"),new Beats(startTime + term * 33, "L"),
						new Beats(startTime + term * 37, "D"),new Beats(startTime + term * 37, "K"),
						new Beats(startTime + term * 41, "D"),new Beats(startTime + term * 41, "K"),
						new Beats(startTime + term * 45, "S"),new Beats(startTime + term * 45, "L"),
						new Beats(startTime + term * 49, "D"),new Beats(startTime + term * 49, "K"),
						new Beats(startTime + term * 53, "D"),new Beats(startTime + term * 53, "K"),
						new Beats(startTime + term * 57, "S"),new Beats(startTime + term * 57, "L"),
						new Beats(startTime + term * 61, "D"),new Beats(startTime + term * 61, "K"),
						new Beats(startTime + term * 65, "D"),new Beats(startTime + term * 65, "K"),
						new Beats(startTime + term * 69, "S"),new Beats(startTime + term * 69, "L"),
						new Beats(startTime + term * 73, "D"),new Beats(startTime + term * 73, "K"),
						new Beats(startTime + term * 77, "D"),new Beats(startTime + term * 77, "K"),
						new Beats(startTime + term * 81, "S"),new Beats(startTime + term * 81, "L"),
						new Beats(startTime + term * 85, "D"),new Beats(startTime + term * 85, "K"),
						new Beats(startTime + term * 89, "D"),new Beats(startTime + term * 89, "K"),
						new Beats(startTime + term * 93, "S"),new Beats(startTime + term * 93, "L"),
						new Beats(startTime + term * 97, "D"),new Beats(startTime + term * 97, "K"),
						new Beats(startTime + term * 101, "D"),new Beats(startTime + term * 101, "K"),
						new Beats(startTime + term * 105, "S"),new Beats(startTime + term * 105, "L"),
						new Beats(startTime + term * 109, "D"),new Beats(startTime + term * 109, "K"),
						new Beats(startTime + term * 113, "D"),new Beats(startTime + term * 113, "K"),
						new Beats(startTime + term * 117, "S"),new Beats(startTime + term * 117, "L"),
						new Beats(startTime + term * 121, "D"),new Beats(startTime + term * 121, "K"),
						new Beats(startTime + term * 125, "D"),new Beats(startTime + term * 125, "K"),
						new Beats(startTime + term * 129, "S"),new Beats(startTime + term * 129, "L"),
						new Beats(startTime + term * 133, "D"),new Beats(startTime + term * 133, "K"),
						new Beats(startTime + term * 137, "D"),new Beats(startTime + term * 137, "K"),
						new Beats(startTime + term * 141, "S"),new Beats(startTime + term * 141, "L"),
						new Beats(startTime + term * 145, "D"),new Beats(startTime + term * 145, "K"),
						new Beats(startTime + term * 149, "D"),new Beats(startTime + term * 149, "K"),
						new Beats(startTime + term * 153, "S"),new Beats(startTime + term * 153, "L"),
						new Beats(startTime + term * 157, "D"),new Beats(startTime + term * 157, "K"),
						new Beats(startTime + term * 161, "D"),new Beats(startTime + term * 161, "K"),
						new Beats(startTime + term * 165, "S"),new Beats(startTime + term * 165, "L"),
						new Beats(startTime + term * 169, "D"),new Beats(startTime + term * 169, "K"),
						new Beats(startTime + term * 173, "D"),new Beats(startTime + term * 173, "K"),
						new Beats(startTime + term * 177, "S"),new Beats(startTime + term * 177, "L"),
						new Beats(startTime + term * 181, "D"),new Beats(startTime + term * 181, "K"),
						new Beats(startTime + term * 185, "D"),new Beats(startTime + term * 185, "K"),
						new Beats(startTime + term * 189, "S"),new Beats(startTime + term * 189, "L"),
						new Beats(startTime + term * 193, "D"),new Beats(startTime + term * 193, "K"),
						new Beats(startTime + term * 197, "D"),new Beats(startTime + term * 197, "K"),
						new Beats(startTime + term * 201, "S"),new Beats(startTime + term * 201, "L"),
						new Beats(startTime + term * 205, "D"),new Beats(startTime + term * 205, "K"),
						new Beats(startTime + term * 209, "D"),new Beats(startTime + term * 209, "K"),
						new Beats(startTime + term * 213, "S"),new Beats(startTime + term * 213, "L"),
						new Beats(startTime + term * 217, "D"),new Beats(startTime + term * 217, "K"),
						new Beats(startTime + term * 221, "D"),new Beats(startTime + term * 221, "K"),
						new Beats(startTime + term * 225, "S"),new Beats(startTime + term * 225, "L"),
						new Beats(startTime + term * 229, "D"),new Beats(startTime + term * 229, "K"),
						new Beats(startTime + term * 233, "D"),new Beats(startTime + term * 233, "K"),
						new Beats(startTime + term * 237, "S"),new Beats(startTime + term * 237, "L"),
						new Beats(startTime + term * 241, "D"),new Beats(startTime + term * 241, "K"),
						new Beats(startTime + term * 245, "D"),new Beats(startTime + term * 245, "K"),
						new Beats(startTime + term * 249, "S"),new Beats(startTime + term * 249, "L"),
						new Beats(startTime + term * 253, "D"),new Beats(startTime + term * 253, "K"),
						new Beats(startTime + term * 257, "D"),new Beats(startTime + term * 257, "K"),
						new Beats(startTime + term * 261, "S"),new Beats(startTime + term * 261, "L"),
						new Beats(startTime + term * 265, "D"),new Beats(startTime + term * 265, "K"),
						new Beats(startTime + term * 269, "D"),new Beats(startTime + term * 269, "K"),
						new Beats(startTime + term * 273, "S"),new Beats(startTime + term * 273, "L"),
						new Beats(startTime + term * 277, "D"),new Beats(startTime + term * 277, "K"),
						new Beats(startTime + term * 281, "D"),new Beats(startTime + term * 281, "K"),
						new Beats(startTime + term * 285, "S"),new Beats(startTime + term * 285, "L"),
						new Beats(startTime + term * 289, "D"),new Beats(startTime + term * 289, "K"),
						new Beats(startTime + term * 293, "D"),new Beats(startTime + term * 293, "K"),
						new Beats(startTime + term * 297, "S"),new Beats(startTime + term * 297, "L"),
						new Beats(startTime + term * 301, "D"),new Beats(startTime + term * 301, "K"),
						new Beats(startTime + term * 305, "D"),new Beats(startTime + term * 305, "K"),
						new Beats(startTime + term * 309, "S"),new Beats(startTime + term * 309, "L"),
						new Beats(startTime + term * 313, "D"),new Beats(startTime + term * 313, "K"),
						new Beats(startTime + term * 317, "D"),new Beats(startTime + term * 317, "K"),
						new Beats(startTime + term * 321, "S"),new Beats(startTime + term * 321, "L"),
						new Beats(startTime + term * 325, "D"),new Beats(startTime + term * 325, "K"),
						new Beats(startTime + term * 329, "D"),new Beats(startTime + term * 329, "K"),
						new Beats(startTime + term * 333, "S"),new Beats(startTime + term * 333, "L"),
						new Beats(startTime + term * 337, "D"),new Beats(startTime + term * 337, "K"),
						new Beats(startTime + term * 341, "D"),new Beats(startTime + term * 341, "K"),
						new Beats(startTime + term * 345, "S"),new Beats(startTime + term * 345, "L"),
						new Beats(startTime + term * 349, "D"),new Beats(startTime + term * 349, "K"),
						new Beats(startTime + term * 353, "D"),new Beats(startTime + term * 353, "K"),
						new Beats(startTime + term * 357, "S"),new Beats(startTime + term * 357, "L"),
						new Beats(startTime + term * 361, "D"),new Beats(startTime + term * 361, "K"),
						new Beats(startTime + term * 365, "D"),new Beats(startTime + term * 365, "K"),
						new Beats(startTime + term * 369, "S"),new Beats(startTime + term * 369, "L"),
						new Beats(startTime + term * 373, "D"),new Beats(startTime + term * 373, "K"),
						new Beats(startTime + term * 377, "D"),new Beats(startTime + term * 377, "K"),
						new Beats(startTime + term * 381, "S"),new Beats(startTime + term * 381, "L"),
						new Beats(startTime + term * 385, "D"),new Beats(startTime + term * 385, "K"),
						new Beats(startTime + term * 389, "D"),new Beats(startTime + term * 389, "K"),
						new Beats(startTime + term * 393, "S"),new Beats(startTime + term * 393, "L"),
						new Beats(startTime + term * 397, "D"),new Beats(startTime + term * 397, "K"),
						new Beats(startTime + term * 401, "D"),new Beats(startTime + term * 401, "K"),
						new Beats(startTime + term * 405, "S"),new Beats(startTime + term * 405, "L"),
						new Beats(startTime + term * 409, "D"),new Beats(startTime + term * 409, "K"),
						new Beats(startTime + term * 413, "D"),new Beats(startTime + term * 413, "K"),
						new Beats(startTime + term * 417, "S"),new Beats(startTime + term * 417, "L"),
						new Beats(startTime + term * 421, "D"),new Beats(startTime + term * 421, "K"),
						new Beats(startTime + term * 425, "D"),new Beats(startTime + term * 425, "K"),
						new Beats(startTime + term * 429, "S"),new Beats(startTime + term * 429, "L"),
						new Beats(startTime + term * 433, "D"),new Beats(startTime + term * 433, "K"),
						new Beats(startTime + term * 437, "D"),new Beats(startTime + term * 437, "K"),
						new Beats(startTime + term * 441, "S"),new Beats(startTime + term * 441, "L"),
						new Beats(startTime + term * 445, "D"),new Beats(startTime + term * 445, "K"),
						new Beats(startTime + term * 449, "D"),new Beats(startTime + term * 449, "K"),
						new Beats(startTime + term * 453, "S"),new Beats(startTime + term * 453, "L"),
						
				};
			} // stargazer 쉬움 모드
			else if (difficulty.equals("HARD")) {

				int startTime = 100;
				int term = 150;
				System.out.println("START");
				BEATS = new Beats[] { 
						new Beats(startTime + term, "D"), new Beats(startTime + term, "K"),
						new Beats(startTime + term * 5, "D"), new Beats(startTime + term * 5, "K"),
						new Beats(startTime + term * 9, "S"), new Beats(startTime + term * 9, "L"),
						new Beats(startTime + term * 13, "D"), new Beats(startTime + term * 13, "K"),
						new Beats(startTime + term * 17, "D"), new Beats(startTime + term * 17, "K"),
						new Beats(startTime + term * 21, "F"),new Beats(startTime + term * 21, "J"),
						new Beats(startTime + term * 25, "D"),new Beats(startTime + term * 25, "K"),
						new Beats(startTime + term * 29, "D"),new Beats(startTime + term * 29, "K"),
						new Beats(startTime + term * 33, "S"),new Beats(startTime + term * 33, "L"),
						new Beats(startTime + term * 37, "D"),new Beats(startTime + term * 37, "K"),
						new Beats(startTime + term * 41, "D"),new Beats(startTime + term * 41, "K"),
						new Beats(startTime + term * 45, "F"),new Beats(startTime + term * 45, "J"),
						new Beats(startTime + term * 49, "D"),new Beats(startTime + term * 49, "K"),
						new Beats(startTime + term * 53, "D"),new Beats(startTime + term * 53, "K"),
						new Beats(startTime + term * 57, "Space"),
						new Beats(startTime + term * 61, "D"),new Beats(startTime + term * 61, "K"),
						new Beats(startTime + term * 65, "D"),new Beats(startTime + term * 65, "K"),
						new Beats(startTime + term * 69, "S"),new Beats(startTime + term * 69, "L"),
						new Beats(startTime + term * 73, "D"),new Beats(startTime + term * 73, "K"),
						new Beats(startTime + term * 77, "D"),new Beats(startTime + term * 77, "K"),
						new Beats(startTime + term * 81, "F"),new Beats(startTime + term * 81, "J"),
						new Beats(startTime + term * 85, "D"),new Beats(startTime + term * 85, "K"),
						new Beats(startTime + term * 89, "D"),new Beats(startTime + term * 89, "K"),
						new Beats(startTime + term * 93, "S"),new Beats(startTime + term * 93, "L"),
						new Beats(startTime + term * 97, "D"),new Beats(startTime + term * 97, "K"),
						new Beats(startTime + term * 101, "D"),new Beats(startTime + term * 101, "K"),
						new Beats(startTime + term * 105, "F"),new Beats(startTime + term * 105, "J"),
						new Beats(startTime + term * 109, "D"),new Beats(startTime + term * 109, "K"),
						new Beats(startTime + term * 113, "D"),new Beats(startTime + term * 113, "K"),
						new Beats(startTime + term * 117, "S"),new Beats(startTime + term * 117, "L"),
						new Beats(startTime + term * 121, "D"),new Beats(startTime + term * 121, "K"),
						new Beats(startTime + term * 125, "D"),new Beats(startTime + term * 125, "K"),
						new Beats(startTime + term * 129, "Space"),
						new Beats(startTime + term * 133, "D"),new Beats(startTime + term * 133, "K"),
						new Beats(startTime + term * 137, "D"),new Beats(startTime + term * 137, "K"),
						new Beats(startTime + term * 141, "Space"),
						new Beats(startTime + term * 145, "D"),new Beats(startTime + term * 145, "K"),
						new Beats(startTime + term * 149, "D"),new Beats(startTime + term * 149, "K"),
						new Beats(startTime + term * 153, "S"),new Beats(startTime + term * 153, "L"),
						new Beats(startTime + term * 157, "D"),new Beats(startTime + term * 157, "K"),
						new Beats(startTime + term * 161, "D"),new Beats(startTime + term * 161, "K"),
						new Beats(startTime + term * 165, "F"),new Beats(startTime + term * 165, "J"),
						new Beats(startTime + term * 169, "D"),new Beats(startTime + term * 169, "K"),
						new Beats(startTime + term * 173, "D"),new Beats(startTime + term * 173, "K"),
						new Beats(startTime + term * 177, "S"),new Beats(startTime + term * 177, "L"),
						new Beats(startTime + term * 181, "D"),new Beats(startTime + term * 181, "K"),
						new Beats(startTime + term * 185, "D"),new Beats(startTime + term * 185, "K"),
						new Beats(startTime + term * 189, "S"),new Beats(startTime + term * 189, "L"),
						new Beats(startTime + term * 193, "D"),new Beats(startTime + term * 193, "K"),
						new Beats(startTime + term * 197, "D"),new Beats(startTime + term * 197, "K"),
						new Beats(startTime + term * 201, "F"),new Beats(startTime + term * 201, "J"),
						new Beats(startTime + term * 205, "D"),new Beats(startTime + term * 205, "K"),
						new Beats(startTime + term * 209, "D"),new Beats(startTime + term * 209, "K"),
						new Beats(startTime + term * 213, "S"),new Beats(startTime + term * 213, "L"),
						new Beats(startTime + term * 217, "D"),new Beats(startTime + term * 217, "K"),
						new Beats(startTime + term * 221, "D"),new Beats(startTime + term * 221, "K"),
						new Beats(startTime + term * 225, "S"),new Beats(startTime + term * 225, "L"),
						new Beats(startTime + term * 229, "D"),new Beats(startTime + term * 229, "K"),
						new Beats(startTime + term * 233, "D"),new Beats(startTime + term * 233, "K"),
						new Beats(startTime + term * 237, "Space"),
						new Beats(startTime + term * 241, "D"),new Beats(startTime + term * 241, "K"),
						new Beats(startTime + term * 245, "D"),new Beats(startTime + term * 245, "K"),
						new Beats(startTime + term * 249, "S"),new Beats(startTime + term * 249, "L"),
						new Beats(startTime + term * 253, "D"),new Beats(startTime + term * 253, "K"),
						new Beats(startTime + term * 257, "D"),new Beats(startTime + term * 257, "K"),
						new Beats(startTime + term * 261, "S"),new Beats(startTime + term * 261, "L"),
						new Beats(startTime + term * 265, "D"),new Beats(startTime + term * 265, "K"),
						new Beats(startTime + term * 269, "D"),new Beats(startTime + term * 269, "K"),
						new Beats(startTime + term * 273, "Space"),
						new Beats(startTime + term * 277, "D"),new Beats(startTime + term * 277, "K"),
						new Beats(startTime + term * 281, "D"),new Beats(startTime + term * 281, "K"),
						new Beats(startTime + term * 285, "S"),new Beats(startTime + term * 285, "L"),
						new Beats(startTime + term * 289, "D"),new Beats(startTime + term * 289, "K"),
						new Beats(startTime + term * 293, "D"),new Beats(startTime + term * 293, "K"),
						new Beats(startTime + term * 297, "S"),new Beats(startTime + term * 297, "L"),
						new Beats(startTime + term * 301, "D"),new Beats(startTime + term * 301, "K"),
						new Beats(startTime + term * 305, "D"),new Beats(startTime + term * 305, "K"),
						new Beats(startTime + term * 309, "F"),new Beats(startTime + term * 309, "J"),
						new Beats(startTime + term * 313, "D"),new Beats(startTime + term * 313, "K"),
						new Beats(startTime + term * 317, "D"),new Beats(startTime + term * 317, "K"),
						new Beats(startTime + term * 321, "Space"),
						new Beats(startTime + term * 325, "D"),new Beats(startTime + term * 325, "K"),
						new Beats(startTime + term * 329, "D"),new Beats(startTime + term * 329, "K"),
						new Beats(startTime + term * 333, "S"),new Beats(startTime + term * 333, "L"),
						new Beats(startTime + term * 337, "D"),new Beats(startTime + term * 337, "K"),
						new Beats(startTime + term * 341, "D"),new Beats(startTime + term * 341, "K"),
						new Beats(startTime + term * 345, "Space"),
						new Beats(startTime + term * 349, "D"),new Beats(startTime + term * 349, "K"),
						new Beats(startTime + term * 353, "D"),new Beats(startTime + term * 353, "K"),
						new Beats(startTime + term * 357, "S"),new Beats(startTime + term * 357, "L"),
						new Beats(startTime + term * 361, "D"),new Beats(startTime + term * 361, "K"),
						new Beats(startTime + term * 365, "D"),new Beats(startTime + term * 365, "K"),
						new Beats(startTime + term * 369, "F"),new Beats(startTime + term * 369, "J"),
						new Beats(startTime + term * 373, "D"),new Beats(startTime + term * 373, "K"),
						new Beats(startTime + term * 377, "D"),new Beats(startTime + term * 377, "K"),
						new Beats(startTime + term * 381, "F"),new Beats(startTime + term * 381, "J"),
						new Beats(startTime + term * 385, "D"),new Beats(startTime + term * 385, "K"),
						new Beats(startTime + term * 389, "D"),new Beats(startTime + term * 389, "K"),
						new Beats(startTime + term * 393, "Space"),
						new Beats(startTime + term * 397, "D"),new Beats(startTime + term * 397, "K"),
						new Beats(startTime + term * 401, "D"),new Beats(startTime + term * 401, "K"),
						new Beats(startTime + term * 405, "S"),new Beats(startTime + term * 405, "L"),
						new Beats(startTime + term * 409, "D"),new Beats(startTime + term * 409, "K"),
						new Beats(startTime + term * 413, "D"),new Beats(startTime + term * 413, "K"),
						new Beats(startTime + term * 417, "Space"),
						new Beats(startTime + term * 421, "F"),new Beats(startTime + term * 421, "J"),
						new Beats(startTime + term * 425, "F"),new Beats(startTime + term * 425, "J"),
						new Beats(startTime + term * 429, "D"),new Beats(startTime + term * 429, "K"),
						new Beats(startTime + term * 433, "F"),new Beats(startTime + term * 433, "J"),
						new Beats(startTime + term * 437, "F"),new Beats(startTime + term * 437, "J"),
						new Beats(startTime + term * 441, "S"),new Beats(startTime + term * 441, "L"),
						new Beats(startTime + term * 445, "F"),new Beats(startTime + term * 445, "J"),
						new Beats(startTime + term * 449, "F"),new Beats(startTime + term * 449, "J"),
						new Beats(startTime + term * 453, "Space"),
						new Beats(startTime + term * 457, "F"),new Beats(startTime + term * 457, "J"),
						new Beats(startTime + term * 461, "S"),new Beats(startTime + term * 461, "L"),
						new Beats(startTime + term * 465, "F"),new Beats(startTime + term * 465, "J"),
						new Beats(startTime + term * 469, "F"),new Beats(startTime + term * 469, "J"),
						new Beats(startTime + term * 473, "Space"),
						
				};
				System.out.println("INIT");
			} // 어려움 모드
		} // STARGAZER의 악보
		
		
		
		
		
		else if (titleName.equals("WE WILL ROCK YOU")) {
			if (difficulty.equals("EASY")) {
				int startTime = 4200;// 시작 시간 맞춤
				int term = 125;//
				System.out.println("START");
				BEATS = new Beats[] {
						new Beats(startTime + term, "D"), new Beats(startTime + term, "K"),
						new Beats(startTime + term * 5, "D"), new Beats(startTime + term * 5, "K"),
						new Beats(startTime + term * 9, "S"), new Beats(startTime + term * 9, "L"),
						new Beats(startTime + term * 13, "D"), new Beats(startTime + term * 13, "K"),
						new Beats(startTime + term * 17, "D"), new Beats(startTime + term * 17, "K"),
						new Beats(startTime + term * 21, "S"),new Beats(startTime + term * 21, "L"),
						new Beats(startTime + term * 25, "D"),new Beats(startTime + term * 25, "K"),
						new Beats(startTime + term * 29, "D"),new Beats(startTime + term * 29, "K"),
						new Beats(startTime + term * 33, "S"),new Beats(startTime + term * 33, "L"),
						new Beats(startTime + term * 37, "D"),new Beats(startTime + term * 37, "K"),
						new Beats(startTime + term * 41, "D"),new Beats(startTime + term * 41, "K"),
						new Beats(startTime + term * 45, "S"),new Beats(startTime + term * 45, "L"),
						new Beats(startTime + term * 49, "D"),new Beats(startTime + term * 49, "K"),
						new Beats(startTime + term * 53, "D"),new Beats(startTime + term * 53, "K"),
						new Beats(startTime + term * 57, "S"),new Beats(startTime + term * 57, "L"),
						new Beats(startTime + term * 61, "D"),new Beats(startTime + term * 61, "K"),
						new Beats(startTime + term * 65, "D"),new Beats(startTime + term * 65, "K"),
						new Beats(startTime + term * 69, "S"),new Beats(startTime + term * 69, "L"),
						new Beats(startTime + term * 73, "D"),new Beats(startTime + term * 73, "K"),
						new Beats(startTime + term * 77, "D"),new Beats(startTime + term * 77, "K"),
						new Beats(startTime + term * 81, "S"),new Beats(startTime + term * 81, "L"),
						new Beats(startTime + term * 85, "D"),new Beats(startTime + term * 85, "K"),
						new Beats(startTime + term * 89, "D"),new Beats(startTime + term * 89, "K"),
						new Beats(startTime + term * 93, "S"),new Beats(startTime + term * 93, "L"),
						new Beats(startTime + term * 97, "D"),new Beats(startTime + term * 97, "K"),
						new Beats(startTime + term * 101, "D"),new Beats(startTime + term * 101, "K"),
						new Beats(startTime + term * 105, "S"),new Beats(startTime + term * 105, "L"),
						new Beats(startTime + term * 109, "D"),new Beats(startTime + term * 109, "K"),
						new Beats(startTime + term * 113, "D"),new Beats(startTime + term * 113, "K"),
						new Beats(startTime + term * 117, "S"),new Beats(startTime + term * 117, "L"),
						new Beats(startTime + term * 121, "D"),new Beats(startTime + term * 121, "K"),
						new Beats(startTime + term * 125, "D"),new Beats(startTime + term * 125, "K"),
						new Beats(startTime + term * 129, "S"),new Beats(startTime + term * 129, "L"),
						new Beats(startTime + term * 133, "D"),new Beats(startTime + term * 133, "K"),
						new Beats(startTime + term * 137, "D"),new Beats(startTime + term * 137, "K"),
						new Beats(startTime + term * 141, "S"),new Beats(startTime + term * 141, "L"),
						new Beats(startTime + term * 145, "D"),new Beats(startTime + term * 145, "K"),
						new Beats(startTime + term * 149, "D"),new Beats(startTime + term * 149, "K"),
						new Beats(startTime + term * 153, "S"),new Beats(startTime + term * 153, "L"),
						new Beats(startTime + term * 157, "D"),new Beats(startTime + term * 157, "K"),
						new Beats(startTime + term * 161, "D"),new Beats(startTime + term * 161, "K"),
						new Beats(startTime + term * 165, "S"),new Beats(startTime + term * 165, "L"),
						new Beats(startTime + term * 169, "D"),new Beats(startTime + term * 169, "K"),
						new Beats(startTime + term * 173, "D"),new Beats(startTime + term * 173, "K"),
						new Beats(startTime + term * 177, "S"),new Beats(startTime + term * 177, "L"),
						new Beats(startTime + term * 181, "D"),new Beats(startTime + term * 181, "K"),
						new Beats(startTime + term * 185, "D"),new Beats(startTime + term * 185, "K"),
						new Beats(startTime + term * 189, "S"),new Beats(startTime + term * 189, "L"),
						new Beats(startTime + term * 193, "D"),new Beats(startTime + term * 193, "K"),
						new Beats(startTime + term * 197, "D"),new Beats(startTime + term * 197, "K"),
						new Beats(startTime + term * 201, "S"),new Beats(startTime + term * 201, "L"),
						new Beats(startTime + term * 205, "D"),new Beats(startTime + term * 205, "K"),
						new Beats(startTime + term * 209, "D"),new Beats(startTime + term * 209, "K"),
						new Beats(startTime + term * 213, "S"),new Beats(startTime + term * 213, "L"),
						new Beats(startTime + term * 217, "D"),new Beats(startTime + term * 217, "K"),
						new Beats(startTime + term * 221, "D"),new Beats(startTime + term * 221, "K"),
						new Beats(startTime + term * 225, "S"),new Beats(startTime + term * 225, "L"),
						new Beats(startTime + term * 229, "D"),new Beats(startTime + term * 229, "K"),
						new Beats(startTime + term * 233, "D"),new Beats(startTime + term * 233, "K"),
						new Beats(startTime + term * 237, "S"),new Beats(startTime + term * 237, "L"),
						new Beats(startTime + term * 241, "D"),new Beats(startTime + term * 241, "K"),
						new Beats(startTime + term * 245, "D"),new Beats(startTime + term * 245, "K"),
						new Beats(startTime + term * 249, "S"),new Beats(startTime + term * 249, "L"),
						new Beats(startTime + term * 253, "D"),new Beats(startTime + term * 253, "K"),
						new Beats(startTime + term * 257, "D"),new Beats(startTime + term * 257, "K"),
						new Beats(startTime + term * 261, "S"),new Beats(startTime + term * 261, "L"),
						new Beats(startTime + term * 265, "D"),new Beats(startTime + term * 265, "K"),
						new Beats(startTime + term * 269, "D"),new Beats(startTime + term * 269, "K"),
						new Beats(startTime + term * 273, "S"),new Beats(startTime + term * 273, "L"),
						new Beats(startTime + term * 277, "D"),new Beats(startTime + term * 277, "K"),
						new Beats(startTime + term * 281, "D"),new Beats(startTime + term * 281, "K"),
						new Beats(startTime + term * 285, "S"),new Beats(startTime + term * 285, "L"),
						new Beats(startTime + term * 289, "D"),new Beats(startTime + term * 289, "K"),
						new Beats(startTime + term * 293, "D"),new Beats(startTime + term * 293, "K"),
						new Beats(startTime + term * 297, "S"),new Beats(startTime + term * 297, "L"),
						new Beats(startTime + term * 301, "D"),new Beats(startTime + term * 301, "K"),
						new Beats(startTime + term * 305, "D"),new Beats(startTime + term * 305, "K"),
						new Beats(startTime + term * 309, "S"),new Beats(startTime + term * 309, "L"),
						new Beats(startTime + term * 313, "D"),new Beats(startTime + term * 313, "K"),
						new Beats(startTime + term * 317, "D"),new Beats(startTime + term * 317, "K"),
						new Beats(startTime + term * 321, "S"),new Beats(startTime + term * 321, "L"),
						new Beats(startTime + term * 325, "D"),new Beats(startTime + term * 325, "K"),
						new Beats(startTime + term * 329, "D"),new Beats(startTime + term * 329, "K"),
						new Beats(startTime + term * 333, "S"),new Beats(startTime + term * 333, "L"),
						new Beats(startTime + term * 337, "D"),new Beats(startTime + term * 337, "K"),
						new Beats(startTime + term * 341, "D"),new Beats(startTime + term * 341, "K"),
						new Beats(startTime + term * 345, "S"),new Beats(startTime + term * 345, "L"),
						new Beats(startTime + term * 349, "D"),new Beats(startTime + term * 349, "K"),
						new Beats(startTime + term * 353, "D"),new Beats(startTime + term * 353, "K"),
						new Beats(startTime + term * 357, "S"),new Beats(startTime + term * 357, "L"),
						new Beats(startTime + term * 361, "D"),new Beats(startTime + term * 361, "K"),
						new Beats(startTime + term * 365, "D"),new Beats(startTime + term * 365, "K"),
						new Beats(startTime + term * 369, "S"),new Beats(startTime + term * 369, "L"),
						new Beats(startTime + term * 373, "D"),new Beats(startTime + term * 373, "K"),
						new Beats(startTime + term * 377, "D"),new Beats(startTime + term * 377, "K"),
						new Beats(startTime + term * 381, "S"),new Beats(startTime + term * 381, "L"),
						new Beats(startTime + term * 385, "D"),new Beats(startTime + term * 385, "K"),
						new Beats(startTime + term * 389, "D"),new Beats(startTime + term * 389, "K"),
						new Beats(startTime + term * 393, "S"),new Beats(startTime + term * 393, "L"),
						new Beats(startTime + term * 397, "D"),new Beats(startTime + term * 397, "K"),
						new Beats(startTime + term * 401, "D"),new Beats(startTime + term * 401, "K"),
						new Beats(startTime + term * 405, "S"),new Beats(startTime + term * 405, "L"),
						new Beats(startTime + term * 409, "D"),new Beats(startTime + term * 409, "K"),
						new Beats(startTime + term * 413, "D"),new Beats(startTime + term * 413, "K"),
						new Beats(startTime + term * 417, "S"),new Beats(startTime + term * 417, "L"),
						new Beats(startTime + term * 421, "D"),new Beats(startTime + term * 421, "K"),
						new Beats(startTime + term * 425, "D"),new Beats(startTime + term * 425, "K"),
						new Beats(startTime + term * 429, "S"),new Beats(startTime + term * 429, "L"),
						new Beats(startTime + term * 433, "D"),new Beats(startTime + term * 433, "K"),
						new Beats(startTime + term * 437, "D"),new Beats(startTime + term * 437, "K"),
						new Beats(startTime + term * 441, "S"),new Beats(startTime + term * 441, "L"),
						new Beats(startTime + term * 445, "D"),new Beats(startTime + term * 445, "K"),
						new Beats(startTime + term * 449, "D"),new Beats(startTime + term * 449, "K"),
						new Beats(startTime + term * 453, "S"),new Beats(startTime + term * 453, "L"),
						
				};
				// 비트 칸
			} // 쉬움
			else if (difficulty.equals("HARD")) {
				int startTime = 4200;// 시작 시간 맞춤
				int term = 125;// 비트 약 109
				System.out.println("START");
				BEATS = new Beats[] { 
						new Beats(startTime + term, "D"), new Beats(startTime + term, "K"),
						new Beats(startTime + term * 5, "D"), new Beats(startTime + term * 5, "K"),
						new Beats(startTime + term * 9, "S"), new Beats(startTime + term * 9, "L"),
						new Beats(startTime + term * 13, "D"), new Beats(startTime + term * 13, "K"),
						new Beats(startTime + term * 17, "D"), new Beats(startTime + term * 17, "K"),
						new Beats(startTime + term * 21, "F"),new Beats(startTime + term * 21, "J"),
						new Beats(startTime + term * 25, "D"),new Beats(startTime + term * 25, "K"),
						new Beats(startTime + term * 29, "D"),new Beats(startTime + term * 29, "K"),
						new Beats(startTime + term * 33, "S"),new Beats(startTime + term * 33, "L"),
						new Beats(startTime + term * 37, "D"),new Beats(startTime + term * 37, "K"),
						new Beats(startTime + term * 41, "D"),new Beats(startTime + term * 41, "K"),
						new Beats(startTime + term * 45, "F"),new Beats(startTime + term * 45, "J"),
						new Beats(startTime + term * 49, "D"),new Beats(startTime + term * 49, "K"),
						new Beats(startTime + term * 53, "D"),new Beats(startTime + term * 53, "K"),
						new Beats(startTime + term * 57, "Space"),
						new Beats(startTime + term * 61, "D"),new Beats(startTime + term * 61, "K"),
						new Beats(startTime + term * 65, "D"),new Beats(startTime + term * 65, "K"),
						new Beats(startTime + term * 69, "S"),new Beats(startTime + term * 69, "L"),
						new Beats(startTime + term * 73, "D"),new Beats(startTime + term * 73, "K"),
						new Beats(startTime + term * 77, "D"),new Beats(startTime + term * 77, "K"),
						new Beats(startTime + term * 81, "F"),new Beats(startTime + term * 81, "J"),
						new Beats(startTime + term * 85, "D"),new Beats(startTime + term * 85, "K"),
						new Beats(startTime + term * 89, "D"),new Beats(startTime + term * 89, "K"),
						new Beats(startTime + term * 93, "S"),new Beats(startTime + term * 93, "L"),
						new Beats(startTime + term * 97, "D"),new Beats(startTime + term * 97, "K"),
						new Beats(startTime + term * 101, "D"),new Beats(startTime + term * 101, "K"),
						new Beats(startTime + term * 105, "F"),new Beats(startTime + term * 105, "J"),
						new Beats(startTime + term * 109, "D"),new Beats(startTime + term * 109, "K"),
						new Beats(startTime + term * 113, "D"),new Beats(startTime + term * 113, "K"),
						new Beats(startTime + term * 117, "S"),new Beats(startTime + term * 117, "L"),
						new Beats(startTime + term * 121, "D"),new Beats(startTime + term * 121, "K"),
						new Beats(startTime + term * 125, "D"),new Beats(startTime + term * 125, "K"),
						new Beats(startTime + term * 129, "Space"),
						new Beats(startTime + term * 133, "D"),new Beats(startTime + term * 133, "K"),
						new Beats(startTime + term * 137, "D"),new Beats(startTime + term * 137, "K"),
						new Beats(startTime + term * 141, "Space"),
						new Beats(startTime + term * 145, "D"),new Beats(startTime + term * 145, "K"),
						new Beats(startTime + term * 149, "D"),new Beats(startTime + term * 149, "K"),
						new Beats(startTime + term * 153, "S"),new Beats(startTime + term * 153, "L"),
						new Beats(startTime + term * 157, "D"),new Beats(startTime + term * 157, "K"),
						new Beats(startTime + term * 161, "D"),new Beats(startTime + term * 161, "K"),
						new Beats(startTime + term * 165, "F"),new Beats(startTime + term * 165, "J"),
						new Beats(startTime + term * 169, "D"),new Beats(startTime + term * 169, "K"),
						new Beats(startTime + term * 173, "D"),new Beats(startTime + term * 173, "K"),
						new Beats(startTime + term * 177, "S"),new Beats(startTime + term * 177, "L"),
						new Beats(startTime + term * 181, "D"),new Beats(startTime + term * 181, "K"),
						new Beats(startTime + term * 185, "D"),new Beats(startTime + term * 185, "K"),
						new Beats(startTime + term * 189, "S"),new Beats(startTime + term * 189, "L"),
						new Beats(startTime + term * 193, "D"),new Beats(startTime + term * 193, "K"),
						new Beats(startTime + term * 197, "D"),new Beats(startTime + term * 197, "K"),
						new Beats(startTime + term * 201, "F"),new Beats(startTime + term * 201, "J"),
						new Beats(startTime + term * 205, "D"),new Beats(startTime + term * 205, "K"),
						new Beats(startTime + term * 209, "D"),new Beats(startTime + term * 209, "K"),
						new Beats(startTime + term * 213, "S"),new Beats(startTime + term * 213, "L"),
						new Beats(startTime + term * 217, "D"),new Beats(startTime + term * 217, "K"),
						new Beats(startTime + term * 221, "D"),new Beats(startTime + term * 221, "K"),
						new Beats(startTime + term * 225, "S"),new Beats(startTime + term * 225, "L"),
						new Beats(startTime + term * 229, "D"),new Beats(startTime + term * 229, "K"),
						new Beats(startTime + term * 233, "D"),new Beats(startTime + term * 233, "K"),
						new Beats(startTime + term * 237, "Space"),
						new Beats(startTime + term * 241, "D"),new Beats(startTime + term * 241, "K"),
						new Beats(startTime + term * 245, "D"),new Beats(startTime + term * 245, "K"),
						new Beats(startTime + term * 249, "S"),new Beats(startTime + term * 249, "L"),
						new Beats(startTime + term * 253, "D"),new Beats(startTime + term * 253, "K"),
						new Beats(startTime + term * 257, "D"),new Beats(startTime + term * 257, "K"),
						new Beats(startTime + term * 261, "S"),new Beats(startTime + term * 261, "L"),
						new Beats(startTime + term * 265, "D"),new Beats(startTime + term * 265, "K"),
						new Beats(startTime + term * 269, "D"),new Beats(startTime + term * 269, "K"),
						new Beats(startTime + term * 273, "Space"),
						new Beats(startTime + term * 277, "D"),new Beats(startTime + term * 277, "K"),
						new Beats(startTime + term * 281, "D"),new Beats(startTime + term * 281, "K"),
						new Beats(startTime + term * 285, "S"),new Beats(startTime + term * 285, "L"),
						new Beats(startTime + term * 289, "D"),new Beats(startTime + term * 289, "K"),
						new Beats(startTime + term * 293, "D"),new Beats(startTime + term * 293, "K"),
						new Beats(startTime + term * 297, "S"),new Beats(startTime + term * 297, "L"),
						new Beats(startTime + term * 301, "D"),new Beats(startTime + term * 301, "K"),
						new Beats(startTime + term * 305, "D"),new Beats(startTime + term * 305, "K"),
						new Beats(startTime + term * 309, "F"),new Beats(startTime + term * 309, "J"),
						new Beats(startTime + term * 313, "D"),new Beats(startTime + term * 313, "K"),
						new Beats(startTime + term * 317, "D"),new Beats(startTime + term * 317, "K"),
						new Beats(startTime + term * 321, "Space"),
						new Beats(startTime + term * 325, "D"),new Beats(startTime + term * 325, "K"),
						new Beats(startTime + term * 329, "D"),new Beats(startTime + term * 329, "K"),
						new Beats(startTime + term * 333, "S"),new Beats(startTime + term * 333, "L"),
						new Beats(startTime + term * 337, "D"),new Beats(startTime + term * 337, "K"),
						new Beats(startTime + term * 341, "D"),new Beats(startTime + term * 341, "K"),
						new Beats(startTime + term * 345, "Space"),
						new Beats(startTime + term * 349, "D"),new Beats(startTime + term * 349, "K"),
						new Beats(startTime + term * 353, "D"),new Beats(startTime + term * 353, "K"),
						new Beats(startTime + term * 357, "S"),new Beats(startTime + term * 357, "L"),
						new Beats(startTime + term * 361, "D"),new Beats(startTime + term * 361, "K"),
						new Beats(startTime + term * 365, "D"),new Beats(startTime + term * 365, "K"),
						new Beats(startTime + term * 369, "F"),new Beats(startTime + term * 369, "J"),
						new Beats(startTime + term * 373, "D"),new Beats(startTime + term * 373, "K"),
						new Beats(startTime + term * 377, "D"),new Beats(startTime + term * 377, "K"),
						new Beats(startTime + term * 381, "F"),new Beats(startTime + term * 381, "J"),
						new Beats(startTime + term * 385, "D"),new Beats(startTime + term * 385, "K"),
						new Beats(startTime + term * 389, "D"),new Beats(startTime + term * 389, "K"),
						new Beats(startTime + term * 393, "Space"),
						new Beats(startTime + term * 397, "D"),new Beats(startTime + term * 397, "K"),
						new Beats(startTime + term * 401, "D"),new Beats(startTime + term * 401, "K"),
						new Beats(startTime + term * 405, "S"),new Beats(startTime + term * 405, "L"),
						new Beats(startTime + term * 409, "D"),new Beats(startTime + term * 409, "K"),
						new Beats(startTime + term * 413, "D"),new Beats(startTime + term * 413, "K"),
						new Beats(startTime + term * 417, "Space"),
						new Beats(startTime + term * 421, "F"),new Beats(startTime + term * 421, "J"),
						new Beats(startTime + term * 425, "F"),new Beats(startTime + term * 425, "J"),
						new Beats(startTime + term * 429, "D"),new Beats(startTime + term * 429, "K"),
						new Beats(startTime + term * 433, "F"),new Beats(startTime + term * 433, "J"),
						new Beats(startTime + term * 437, "F"),new Beats(startTime + term * 437, "J"),
						new Beats(startTime + term * 441, "S"),new Beats(startTime + term * 441, "L"),
						new Beats(startTime + term * 445, "F"),new Beats(startTime + term * 445, "J"),
						new Beats(startTime + term * 449, "F"),new Beats(startTime + term * 449, "J"),
						new Beats(startTime + term * 453, "Space"),
				};

			}

		} // WE WILL ROCK YOU 악보
		else {
			if(difficulty.equals("EASY")) {
				
			
				int startTime = 0;
				int term = 170;
				System.out.println("START");
				BEATS = new Beats[] { 
						new Beats(startTime + term, "D"), new Beats(startTime + term, "K"),
						new Beats(startTime + term * 5, "D"), new Beats(startTime + term * 5, "K"),
						new Beats(startTime + term * 9, "S"), new Beats(startTime + term * 9, "L"),
						new Beats(startTime + term * 13, "D"), new Beats(startTime + term * 13, "K"),
						new Beats(startTime + term * 17, "D"), new Beats(startTime + term * 17, "K"),
						new Beats(startTime + term * 21, "S"),new Beats(startTime + term * 21, "L"),
						new Beats(startTime + term * 25, "D"),new Beats(startTime + term * 25, "K"),
						new Beats(startTime + term * 29, "D"),new Beats(startTime + term * 29, "K"),
						new Beats(startTime + term * 33, "S"),new Beats(startTime + term * 33, "L"),
						new Beats(startTime + term * 37, "D"),new Beats(startTime + term * 37, "K"),
						new Beats(startTime + term * 41, "D"),new Beats(startTime + term * 41, "K"),
						new Beats(startTime + term * 45, "S"),new Beats(startTime + term * 45, "L"),
						new Beats(startTime + term * 49, "D"),new Beats(startTime + term * 49, "K"),
						new Beats(startTime + term * 53, "D"),new Beats(startTime + term * 53, "K"),
						new Beats(startTime + term * 57, "S"),new Beats(startTime + term * 57, "L"),
						new Beats(startTime + term * 61, "D"),new Beats(startTime + term * 61, "K"),
						new Beats(startTime + term * 65, "D"),new Beats(startTime + term * 65, "K"),
						new Beats(startTime + term * 69, "S"),new Beats(startTime + term * 69, "L"),
						new Beats(startTime + term * 73, "D"),new Beats(startTime + term * 73, "K"),
						new Beats(startTime + term * 77, "D"),new Beats(startTime + term * 77, "K"),
						new Beats(startTime + term * 81, "S"),new Beats(startTime + term * 81, "L"),
						new Beats(startTime + term * 85, "D"),new Beats(startTime + term * 85, "K"),
						new Beats(startTime + term * 89, "D"),new Beats(startTime + term * 89, "K"),
						new Beats(startTime + term * 93, "S"),new Beats(startTime + term * 93, "L"),
						new Beats(startTime + term * 97, "D"),new Beats(startTime + term * 97, "K"),
						new Beats(startTime + term * 101, "D"),new Beats(startTime + term * 101, "K"),
						new Beats(startTime + term * 105, "S"),new Beats(startTime + term * 105, "L"),
						new Beats(startTime + term * 109, "D"),new Beats(startTime + term * 109, "K"),
						new Beats(startTime + term * 113, "D"),new Beats(startTime + term * 113, "K"),
						new Beats(startTime + term * 117, "S"),new Beats(startTime + term * 117, "L"),
						new Beats(startTime + term * 121, "D"),new Beats(startTime + term * 121, "K"),
						new Beats(startTime + term * 125, "D"),new Beats(startTime + term * 125, "K"),
						new Beats(startTime + term * 129, "S"),new Beats(startTime + term * 129, "L"),
						new Beats(startTime + term * 133, "D"),new Beats(startTime + term * 133, "K"),
						new Beats(startTime + term * 137, "D"),new Beats(startTime + term * 137, "K"),
						new Beats(startTime + term * 141, "S"),new Beats(startTime + term * 141, "L"),
						new Beats(startTime + term * 145, "D"),new Beats(startTime + term * 145, "K"),
						new Beats(startTime + term * 149, "D"),new Beats(startTime + term * 149, "K"),
						new Beats(startTime + term * 153, "S"),new Beats(startTime + term * 153, "L"),
						new Beats(startTime + term * 157, "D"),new Beats(startTime + term * 157, "K"),
						new Beats(startTime + term * 161, "D"),new Beats(startTime + term * 161, "K"),
						new Beats(startTime + term * 165, "S"),new Beats(startTime + term * 165, "L"),
						new Beats(startTime + term * 169, "D"),new Beats(startTime + term * 169, "K"),
						new Beats(startTime + term * 173, "D"),new Beats(startTime + term * 173, "K"),
						new Beats(startTime + term * 177, "S"),new Beats(startTime + term * 177, "L"),
						new Beats(startTime + term * 181, "D"),new Beats(startTime + term * 181, "K"),
						new Beats(startTime + term * 185, "D"),new Beats(startTime + term * 185, "K"),
						new Beats(startTime + term * 189, "S"),new Beats(startTime + term * 189, "L"),
						new Beats(startTime + term * 193, "D"),new Beats(startTime + term * 193, "K"),
						new Beats(startTime + term * 197, "D"),new Beats(startTime + term * 197, "K"),
						new Beats(startTime + term * 201, "S"),new Beats(startTime + term * 201, "L"),
						new Beats(startTime + term * 205, "D"),new Beats(startTime + term * 205, "K"),
						new Beats(startTime + term * 209, "D"),new Beats(startTime + term * 209, "K"),
						new Beats(startTime + term * 213, "S"),new Beats(startTime + term * 213, "L"),
						new Beats(startTime + term * 217, "D"),new Beats(startTime + term * 217, "K"),
						new Beats(startTime + term * 221, "D"),new Beats(startTime + term * 221, "K"),
						new Beats(startTime + term * 225, "S"),new Beats(startTime + term * 225, "L"),
						new Beats(startTime + term * 229, "D"),new Beats(startTime + term * 229, "K"),
						new Beats(startTime + term * 233, "D"),new Beats(startTime + term * 233, "K"),
						new Beats(startTime + term * 237, "S"),new Beats(startTime + term * 237, "L"),
						new Beats(startTime + term * 241, "D"),new Beats(startTime + term * 241, "K"),
						new Beats(startTime + term * 245, "D"),new Beats(startTime + term * 245, "K"),
						new Beats(startTime + term * 249, "S"),new Beats(startTime + term * 249, "L"),
						new Beats(startTime + term * 253, "D"),new Beats(startTime + term * 253, "K"),
						new Beats(startTime + term * 257, "D"),new Beats(startTime + term * 257, "K"),
						new Beats(startTime + term * 261, "S"),new Beats(startTime + term * 261, "L"),
						new Beats(startTime + term * 265, "D"),new Beats(startTime + term * 265, "K"),
						new Beats(startTime + term * 269, "D"),new Beats(startTime + term * 269, "K"),
						new Beats(startTime + term * 273, "S"),new Beats(startTime + term * 273, "L"),
						new Beats(startTime + term * 277, "D"),new Beats(startTime + term * 277, "K"),
						new Beats(startTime + term * 281, "D"),new Beats(startTime + term * 281, "K"),
						new Beats(startTime + term * 285, "S"),new Beats(startTime + term * 285, "L"),
						new Beats(startTime + term * 289, "D"),new Beats(startTime + term * 289, "K"),
						new Beats(startTime + term * 293, "D"),new Beats(startTime + term * 293, "K"),
						new Beats(startTime + term * 297, "S"),new Beats(startTime + term * 297, "L"),
						new Beats(startTime + term * 301, "D"),new Beats(startTime + term * 301, "K"),
						new Beats(startTime + term * 305, "D"),new Beats(startTime + term * 305, "K"),
						new Beats(startTime + term * 309, "S"),new Beats(startTime + term * 309, "L"),
						new Beats(startTime + term * 313, "D"),new Beats(startTime + term * 313, "K"),
						new Beats(startTime + term * 317, "D"),new Beats(startTime + term * 317, "K"),
						new Beats(startTime + term * 321, "S"),new Beats(startTime + term * 321, "L"),
						new Beats(startTime + term * 325, "D"),new Beats(startTime + term * 325, "K"),
						new Beats(startTime + term * 329, "D"),new Beats(startTime + term * 329, "K"),
						new Beats(startTime + term * 333, "S"),new Beats(startTime + term * 333, "L"),
						new Beats(startTime + term * 337, "D"),new Beats(startTime + term * 337, "K"),
						new Beats(startTime + term * 341, "D"),new Beats(startTime + term * 341, "K"),
						new Beats(startTime + term * 345, "S"),new Beats(startTime + term * 345, "L"),
						new Beats(startTime + term * 349, "D"),new Beats(startTime + term * 349, "K"),
						new Beats(startTime + term * 353, "D"),new Beats(startTime + term * 353, "K"),
						new Beats(startTime + term * 357, "S"),new Beats(startTime + term * 357, "L"),
						new Beats(startTime + term * 361, "D"),new Beats(startTime + term * 361, "K"),
						new Beats(startTime + term * 365, "D"),new Beats(startTime + term * 365, "K"),
						new Beats(startTime + term * 369, "S"),new Beats(startTime + term * 369, "L"),
						new Beats(startTime + term * 373, "D"),new Beats(startTime + term * 373, "K"),
						new Beats(startTime + term * 377, "D"),new Beats(startTime + term * 377, "K"),
						new Beats(startTime + term * 381, "S"),new Beats(startTime + term * 381, "L"),
						new Beats(startTime + term * 385, "D"),new Beats(startTime + term * 385, "K"),
						new Beats(startTime + term * 389, "D"),new Beats(startTime + term * 389, "K"),
						new Beats(startTime + term * 393, "S"),new Beats(startTime + term * 393, "L"),
						new Beats(startTime + term * 397, "D"),new Beats(startTime + term * 397, "K"),
						new Beats(startTime + term * 401, "D"),new Beats(startTime + term * 401, "K"),
						new Beats(startTime + term * 405, "S"),new Beats(startTime + term * 405, "L"),
						new Beats(startTime + term * 409, "D"),new Beats(startTime + term * 409, "K"),
						new Beats(startTime + term * 413, "D"),new Beats(startTime + term * 413, "K"),
						new Beats(startTime + term * 417, "S"),new Beats(startTime + term * 417, "L"),
						new Beats(startTime + term * 421, "D"),new Beats(startTime + term * 421, "K"),
						new Beats(startTime + term * 425, "D"),new Beats(startTime + term * 425, "K"),
						new Beats(startTime + term * 429, "S"),new Beats(startTime + term * 429, "L"),
						new Beats(startTime + term * 433, "D"),new Beats(startTime + term * 433, "K"),
						new Beats(startTime + term * 437, "D"),new Beats(startTime + term * 437, "K"),
						new Beats(startTime + term * 441, "S"),new Beats(startTime + term * 441, "L"),
						new Beats(startTime + term * 445, "D"),new Beats(startTime + term * 445, "K"),
						new Beats(startTime + term * 449, "D"),new Beats(startTime + term * 449, "K"),
						new Beats(startTime + term * 453, "S"),new Beats(startTime + term * 453, "L"),
					
				};
			}
			else
			{
				int startTime = 0;
				int term = 170;
				System.out.println("START");
				BEATS = new Beats[] { 
						new Beats(startTime + term, "D"), new Beats(startTime + term, "K"),
						new Beats(startTime + term * 5, "D"), new Beats(startTime + term * 5, "K"),
						new Beats(startTime + term * 9, "S"), new Beats(startTime + term * 9, "L"),
						new Beats(startTime + term * 13, "D"), new Beats(startTime + term * 13, "K"),
						new Beats(startTime + term * 17, "D"), new Beats(startTime + term * 17, "K"),
						new Beats(startTime + term * 21, "F"),new Beats(startTime + term * 21, "J"),
						new Beats(startTime + term * 25, "D"),new Beats(startTime + term * 25, "K"),
						new Beats(startTime + term * 29, "D"),new Beats(startTime + term * 29, "K"),
						new Beats(startTime + term * 33, "S"),new Beats(startTime + term * 33, "L"),
						new Beats(startTime + term * 37, "D"),new Beats(startTime + term * 37, "K"),
						new Beats(startTime + term * 41, "D"),new Beats(startTime + term * 41, "K"),
						new Beats(startTime + term * 45, "F"),new Beats(startTime + term * 45, "J"),
						new Beats(startTime + term * 49, "D"),new Beats(startTime + term * 49, "K"),
						new Beats(startTime + term * 53, "D"),new Beats(startTime + term * 53, "K"),
						new Beats(startTime + term * 57, "Space"),
						new Beats(startTime + term * 61, "D"),new Beats(startTime + term * 61, "K"),
						new Beats(startTime + term * 65, "D"),new Beats(startTime + term * 65, "K"),
						new Beats(startTime + term * 69, "S"),new Beats(startTime + term * 69, "L"),
						new Beats(startTime + term * 73, "D"),new Beats(startTime + term * 73, "K"),
						new Beats(startTime + term * 77, "D"),new Beats(startTime + term * 77, "K"),
						new Beats(startTime + term * 81, "F"),new Beats(startTime + term * 81, "J"),
						new Beats(startTime + term * 85, "D"),new Beats(startTime + term * 85, "K"),
						new Beats(startTime + term * 89, "D"),new Beats(startTime + term * 89, "K"),
						new Beats(startTime + term * 93, "S"),new Beats(startTime + term * 93, "L"),
						new Beats(startTime + term * 97, "D"),new Beats(startTime + term * 97, "K"),
						new Beats(startTime + term * 101, "D"),new Beats(startTime + term * 101, "K"),
						new Beats(startTime + term * 105, "F"),new Beats(startTime + term * 105, "J"),
						new Beats(startTime + term * 109, "D"),new Beats(startTime + term * 109, "K"),
						new Beats(startTime + term * 113, "D"),new Beats(startTime + term * 113, "K"),
						new Beats(startTime + term * 117, "S"),new Beats(startTime + term * 117, "L"),
						new Beats(startTime + term * 121, "D"),new Beats(startTime + term * 121, "K"),
						new Beats(startTime + term * 125, "D"),new Beats(startTime + term * 125, "K"),
						new Beats(startTime + term * 129, "Space"),
						new Beats(startTime + term * 133, "D"),new Beats(startTime + term * 133, "K"),
						new Beats(startTime + term * 137, "D"),new Beats(startTime + term * 137, "K"),
						new Beats(startTime + term * 141, "Space"),
						new Beats(startTime + term * 145, "D"),new Beats(startTime + term * 145, "K"),
						new Beats(startTime + term * 149, "D"),new Beats(startTime + term * 149, "K"),
						new Beats(startTime + term * 153, "S"),new Beats(startTime + term * 153, "L"),
						new Beats(startTime + term * 157, "D"),new Beats(startTime + term * 157, "K"),
						new Beats(startTime + term * 161, "D"),new Beats(startTime + term * 161, "K"),
						new Beats(startTime + term * 165, "F"),new Beats(startTime + term * 165, "J"),
						new Beats(startTime + term * 169, "D"),new Beats(startTime + term * 169, "K"),
						new Beats(startTime + term * 173, "D"),new Beats(startTime + term * 173, "K"),
						new Beats(startTime + term * 177, "S"),new Beats(startTime + term * 177, "L"),
						new Beats(startTime + term * 181, "D"),new Beats(startTime + term * 181, "K"),
						new Beats(startTime + term * 185, "D"),new Beats(startTime + term * 185, "K"),
						new Beats(startTime + term * 189, "S"),new Beats(startTime + term * 189, "L"),
						new Beats(startTime + term * 193, "D"),new Beats(startTime + term * 193, "K"),
						new Beats(startTime + term * 197, "D"),new Beats(startTime + term * 197, "K"),
						new Beats(startTime + term * 201, "F"),new Beats(startTime + term * 201, "J"),
						new Beats(startTime + term * 205, "D"),new Beats(startTime + term * 205, "K"),
						new Beats(startTime + term * 209, "D"),new Beats(startTime + term * 209, "K"),
						new Beats(startTime + term * 213, "S"),new Beats(startTime + term * 213, "L"),
						new Beats(startTime + term * 217, "D"),new Beats(startTime + term * 217, "K"),
						new Beats(startTime + term * 221, "D"),new Beats(startTime + term * 221, "K"),
						new Beats(startTime + term * 225, "S"),new Beats(startTime + term * 225, "L"),
						new Beats(startTime + term * 229, "D"),new Beats(startTime + term * 229, "K"),
						new Beats(startTime + term * 233, "D"),new Beats(startTime + term * 233, "K"),
						new Beats(startTime + term * 237, "Space"),
						new Beats(startTime + term * 241, "D"),new Beats(startTime + term * 241, "K"),
						new Beats(startTime + term * 245, "D"),new Beats(startTime + term * 245, "K"),
						new Beats(startTime + term * 249, "S"),new Beats(startTime + term * 249, "L"),
						new Beats(startTime + term * 253, "D"),new Beats(startTime + term * 253, "K"),
						new Beats(startTime + term * 257, "D"),new Beats(startTime + term * 257, "K"),
						new Beats(startTime + term * 261, "S"),new Beats(startTime + term * 261, "L"),
						new Beats(startTime + term * 265, "D"),new Beats(startTime + term * 265, "K"),
						new Beats(startTime + term * 269, "D"),new Beats(startTime + term * 269, "K"),
						new Beats(startTime + term * 273, "Space"),
						new Beats(startTime + term * 277, "D"),new Beats(startTime + term * 277, "K"),
						new Beats(startTime + term * 281, "D"),new Beats(startTime + term * 281, "K"),
						new Beats(startTime + term * 285, "S"),new Beats(startTime + term * 285, "L"),
						new Beats(startTime + term * 289, "D"),new Beats(startTime + term * 289, "K"),
						new Beats(startTime + term * 293, "D"),new Beats(startTime + term * 293, "K"),
						new Beats(startTime + term * 297, "S"),new Beats(startTime + term * 297, "L"),
						new Beats(startTime + term * 301, "D"),new Beats(startTime + term * 301, "K"),
						new Beats(startTime + term * 305, "D"),new Beats(startTime + term * 305, "K"),
						new Beats(startTime + term * 309, "F"),new Beats(startTime + term * 309, "J"),
						new Beats(startTime + term * 313, "D"),new Beats(startTime + term * 313, "K"),
						new Beats(startTime + term * 317, "D"),new Beats(startTime + term * 317, "K"),
						new Beats(startTime + term * 321, "Space"),
						new Beats(startTime + term * 325, "D"),new Beats(startTime + term * 325, "K"),
						new Beats(startTime + term * 329, "D"),new Beats(startTime + term * 329, "K"),
						new Beats(startTime + term * 333, "S"),new Beats(startTime + term * 333, "L"),
						new Beats(startTime + term * 337, "D"),new Beats(startTime + term * 337, "K"),
						new Beats(startTime + term * 341, "D"),new Beats(startTime + term * 341, "K"),
						new Beats(startTime + term * 345, "Space"),
						new Beats(startTime + term * 349, "D"),new Beats(startTime + term * 349, "K"),
						new Beats(startTime + term * 353, "D"),new Beats(startTime + term * 353, "K"),
						new Beats(startTime + term * 357, "S"),new Beats(startTime + term * 357, "L"),
						new Beats(startTime + term * 361, "D"),new Beats(startTime + term * 361, "K"),
						new Beats(startTime + term * 365, "D"),new Beats(startTime + term * 365, "K"),
						new Beats(startTime + term * 369, "F"),new Beats(startTime + term * 369, "J"),
						new Beats(startTime + term * 373, "D"),new Beats(startTime + term * 373, "K"),
						new Beats(startTime + term * 377, "D"),new Beats(startTime + term * 377, "K"),
						new Beats(startTime + term * 381, "F"),new Beats(startTime + term * 381, "J"),
						new Beats(startTime + term * 385, "D"),new Beats(startTime + term * 385, "K"),
						new Beats(startTime + term * 389, "D"),new Beats(startTime + term * 389, "K"),
						new Beats(startTime + term * 393, "Space"),
						new Beats(startTime + term * 397, "D"),new Beats(startTime + term * 397, "K"),
						new Beats(startTime + term * 401, "D"),new Beats(startTime + term * 401, "K"),
						new Beats(startTime + term * 405, "S"),new Beats(startTime + term * 405, "L"),
						new Beats(startTime + term * 409, "D"),new Beats(startTime + term * 409, "K"),
						new Beats(startTime + term * 413, "D"),new Beats(startTime + term * 413, "K"),
						new Beats(startTime + term * 417, "Space"),
						new Beats(startTime + term * 421, "F"),new Beats(startTime + term * 421, "J"),
						new Beats(startTime + term * 425, "F"),new Beats(startTime + term * 425, "J"),
						new Beats(startTime + term * 429, "D"),new Beats(startTime + term * 429, "K"),
						new Beats(startTime + term * 433, "F"),new Beats(startTime + term * 433, "J"),
						new Beats(startTime + term * 437, "F"),new Beats(startTime + term * 437, "J"),
						new Beats(startTime + term * 441, "S"),new Beats(startTime + term * 441, "L"),
						new Beats(startTime + term * 445, "F"),new Beats(startTime + term * 445, "J"),
						new Beats(startTime + term * 449, "F"),new Beats(startTime + term * 449, "J"),
						new Beats(startTime + term * 453, "Space"),

				};
				
			}
		} // REFELECTION 악보
		int i = 0;
		gameMusic.start();
		//게임 음악 시작
		System.out.println("DROPS");
		
		while (i < BEATS.length && !this.isInterrupted()) {
			//i가 0부터 BEATS끝전까지이며 game이 interrupted가 아닐시에
			boolean dropped = false;//
			if (BEATS[i].getTime() <= gameMusic.getTime()) {
				Note note = new Note(BEATS[i].getNoteName());
				note.start();
				noteList.add(note);
				i++;
				dropped = true;
			}//시간 전꺼만 받을수 있도록
			if (!dropped) {//안떨어졌으면
				try {
					if(!Thread.interrupted())//쓰레드가 안멈쳤으면
					{
						Thread.sleep(3);//잠시 재운다
					}
				} catch (Exception e) {
					e.getMessage();
				}
			}
		} // while문
		MusicFlag=true;
	}// drop notes

	public void judge(String input) {
		for (int i = 0; i < noteList.size(); i++) {
			Note note = noteList.get(i);//노트 리스트 사이의  노트중 하나 받아와서
			if (input.equals(note.getNoteType())) {//입력받은 노트와 노트의 타입이 같은경우
				returnedValueOfNoteJudgement = note.judge();//
				if (returnedValueOfNoteJudgement.equals("EARLY")) {
					Gamescore += EARLY;
					JUDGEMENT = "EARLY";
					combo++;
				} else if (returnedValueOfNoteJudgement.equals("GOOD")) {
					Gamescore += GOOD;
					JUDGEMENT = "GOOD";
					combo++;
				} else if (returnedValueOfNoteJudgement.equals("LATE")) {
					Gamescore += LATE;
					JUDGEMENT = "LATE";
					combo++;
				} else if (returnedValueOfNoteJudgement.equals("PERFECT")) {
					Gamescore += PERFECT;
					JUDGEMENT = "PERFECT";
					combo++;
				} else if (returnedValueOfNoteJudgement.equals("GREAT")) {
					Gamescore += GREAT;
					JUDGEMENT = "GREAT";
					combo++;
				} else {//y가 535보다 위에서 찍히거나 MISS인 경우
					combo = 0;
				}
				if(combo>=Maxcombo)
					Maxcombo=combo;

				break;
			}
		}
	}
}
