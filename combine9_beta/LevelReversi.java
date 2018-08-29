package combine9_beta;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import combine8_beta.Music;

public class LevelReversi extends JPanel
{
	private JButton[] btnLevels;	//������ ǥ�����ִ� ��ư
	private JButton btnBack;		//�ڷΰ��� ��ư
	private LevelListener lstnLevel;//�׼� ������
	private HoverListener hbtnLevel;//���콺 ������
	private Level level;			//������ ��Ÿ���� ����
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

		//�ڷΰ��� ��ư ����//
		//�̹��� ������ ��ȯ
		ImageIcon image=new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre= image.getImage();
		Image scale= pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		
		btnBack = new JButton(new ImageIcon(scale));
		//��ư ��� ����
		btnBack.setBorderPainted(false);
		//��ư ��������
		btnBack.setContentAreaFilled(false);
		//��Ŀ���� ��� ����
		btnBack.setFocusPainted(false);
		//���� ��ư ��ġ ���� ������ ����
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
		//ȣ���� ��ư ���� ����
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
			//��ư Ŭ���� �ش� ���� ����
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
			else//�ڷΰ��� ��ư ������ ��
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