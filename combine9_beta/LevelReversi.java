package combine9_beta;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import combine8_beta.Music;

public class LevelReversi extends JPanel
{
	private JButton[] btnLevels;	//레벨을 표시해주는 버튼
	private JButton btnBack;		//뒤로가기 버튼
	private LevelListener lstnLevel;//액션 리스너
	private HoverListener hbtnLevel;//마우스 리스너
	private Level level;			//레벨을 나타내는 변수
	public static Music BACKGROUNDMUSIC;
	private PanelReversi panelRev;	//
	
	public LevelReversi(PanelReversi panel)
	{
		BACKGROUNDMUSIC=new Music("ReversiMain.mp3",true);
		BACKGROUNDMUSIC.start();
		setPreferredSize(new Dimension(1280,720));
		//setBackground(new Color(182,109,109));
		setBackground(new Color(57,131,66));
		setLayout(null);
		
		panelRev = panel;
		
		hbtnLevel=new HoverListener();
		lstnLevel = new LevelListener();
		btnLevels = new JButton[5];
		btnLevels[0] = new JButton("EASY");
		btnLevels[1] = new JButton("NORMAL");
		btnLevels[2] = new JButton("HARD");
		btnLevels[3] = new JButton("EXPERT");
		btnLevels[4] = new JButton("MASTER");

		//뒤로가기 버튼 설정//
		//이미지 사이즈 변환
		ImageIcon image=new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre= image.getImage();
		Image scale= pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		
		btnBack = new JButton(new ImageIcon(scale));
		//버튼 경계 제거
		btnBack.setBorderPainted(false);
		//버튼 내용제거
		btnBack.setContentAreaFilled(false);
		//포커스시 경계 제거
		btnBack.setFocusPainted(false);
		//레벨 버튼 위치 색상 리스너 설정
		for (int i=0; i<5; i++)
		{
			btnLevels[i].setBackground(new Color(232,224,223));
			btnLevels[i].setFont(new Font("Consolas", Font.BOLD, 30));
			btnLevels[i].addActionListener(lstnLevel);
			btnLevels[i].addMouseListener(hbtnLevel);
			btnLevels[i].setBounds(460,60+i*120,360,100);
			add(btnLevels[i]);
		}
		btnBack.addActionListener(lstnLevel);
		btnBack.setBounds(1050,630,200,60);
		add(btnBack);
		
	}//LevelReversi
	
	//get
	public Level getLevel() { return level; }
	
	public class HoverListener implements MouseListener{
		public void mouseClicked(MouseEvent evt) {}
		public void mousePressed(MouseEvent evt) {}
		public void mouseReleased(MouseEvent evt) {}
		//호버시 버튼 색상 변경
		public void mouseEntered(MouseEvent evt) {
			int i;
			Object obj=evt.getSource();
			for( i=0;i<5 && obj!=btnLevels[i];i++) ;
			btnLevels[i].setBackground(new Color(221, 134, 117));
			btnLevels[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		public void mouseExited(MouseEvent evt) {
			int i;
			Object obj=evt.getSource();
			for( i=0;i<5 && obj!=btnLevels[i];i++) ;
			btnLevels[i].setBackground(new Color(232,224,223));
			btnLevels[i].setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
}//HoverListener()
	
	public class LevelListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			//버튼 클릭시 해당 레벨 실행
			int idx;
			Object obj = evt.getSource();
			
			for (idx=0; idx<5 && obj!=btnLevels[idx]; idx++);
			
			switch (idx)
			{
			case 0 : level = Level.EASY; break;
			case 1 : level = Level.NORMAL; break;
			case 2 : level = Level.HARD; break;
			case 3 : level = Level.EXPERT; break;
			case 4 : level = Level.MASTER; break;
			}
			
			if (idx < 5)
			{
				(new Music("levelselect.mp3",false)).start();
				panelRev.runReversi();
			}
			else//뒤로가기 버튼 눌렀을 때
			{
				(new Music("back.mp3",false)).start();
				panelRev.exit();
			}
			if(obj==btnBack)
			{
				BACKGROUNDMUSIC.close();
			}
		}
	}
}//LevelListener()