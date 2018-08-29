package combine9_beta;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlayReversi extends JPanel
{	
	//게임 화면
	private Reversi reversi;						//reversi 게임 알고리즘
	
	private JButton btnBack;						//바둑판
	private JLabel lblLevel;						//레벨 표시
	private JLabel lblWhiteCount, lblBlackCount;	//흰돌, 검은돌의 개수
	
	private PanelReversi panelRev;					//오델로 게임 패널들
	
	public PlayReversi(PanelReversi panel)
	{
		setPreferredSize(new Dimension(1280,720));
		setBackground(new Color(57,131,66));
		setLayout(null);

		reversi = null;
		panelRev = panel;
		
		//레벨, 검은돌 갯수, 흰돌 갯수 라벨의 위치, 색상 , 폰트 설정
		lblLevel = new JLabel();
		lblWhiteCount = new JLabel();
		lblBlackCount = new JLabel();
		
		lblLevel.setForeground(new Color(221,134,117));
		lblWhiteCount.setForeground(Color.white);
		lblBlackCount.setForeground(Color.black);
		
		lblLevel.setFont(new Font("Consolas",Font.BOLD,80));
		lblWhiteCount.setFont(new Font("Consolas",Font.BOLD,120));
		lblBlackCount.setFont(new Font("Consolas",Font.BOLD,120));
		
		lblLevel.setBounds(800,20,360,160);
		lblWhiteCount.setBounds(735,160,240,180);
		lblBlackCount.setBounds(985,160,240,180);
		
		lblLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblWhiteCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlackCount.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblLevel.setVerticalAlignment(SwingConstants.CENTER);
		lblWhiteCount.setVerticalAlignment(SwingConstants.CENTER);
		lblBlackCount.setVerticalAlignment(SwingConstants.CENTER);
		
		//뒤로가기 버튼 이미지 씌우기
		
		//이미지 사이즈 변환
		ImageIcon image=new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre= image.getImage();
		Image scale= pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		btnBack = new JButton(new ImageIcon(scale));
		//경계 제거
		btnBack.setBorderPainted(false);
		//내용 제거
		btnBack.setContentAreaFilled(false);
		//포커스시 경계 제거
		btnBack.setFocusPainted(false);
		
		btnBack.addActionListener(new BackListener());
		btnBack.setBounds(1050,630,200,60);
		add(btnBack);
		add(lblLevel);
		add(lblWhiteCount);
		add(lblBlackCount);
		addMouseListener(new BoardListener());
	}//PlayReversi()
	
	public void paintComponent(Graphics page)
	{
		//바둑판, 바둑알 그리기
		Graphics2D g2d = (Graphics2D) page; //2d 그래픽 환경에서 선의 두께, 스타일, 좌표 등등을 적용
		super.paintComponent(page);
		
		page.setColor(new Color(128,64,64));
		g2d.setStroke(new BasicStroke(6f));	//선의 두께

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		//바둑판 그리기
		for (int i=1; i<=9; i++)
		{
			page.drawLine(70,70*i,630,70*i);
			page.drawLine(70*i,70,70*i,630);
		}//for
		
		//바둑알 그리기
		g2d.setStroke(new BasicStroke(4f));	//선의 두계
		
		for (int row=1; row<=Reversi.BOARD_SIZE; row++)
			for (int col=1; col<=Reversi.BOARD_SIZE; col++)
				drawDisc(page,row,col,reversi.statusAt(row, col));
		
	}//paintComponent
	
	public void drawDisc(Graphics page, int row, int col, Status status)
	{
		//흰, 검은 돌 차례인지에 따라 바둑알을 그려줌
		if (status==Status.WHITE || status==status.BLACK)
		{
			//바둑알 내부 칠하기
			page.setColor((status==Status.WHITE ? Color.white : Color.black));
			page.fillOval(70*row+6, 70*col+6, 58, 58);
			//바둑알 경계 칠하기
			page.setColor(Color.black);
			page.drawOval(70*row+6, 70*col+6, 58, 58);
		}
	}//drawDisc()
	
	public void highlightLabel()
	{	//현재 차례를 표시해줌
		if (reversi.getTurn() == Status.WHITE)
		{
			lblWhiteCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
			lblBlackCount.setBorder(null);
		}
		else
		{
			lblBlackCount.setBorder(BorderFactory.createLineBorder(new Color(234, 204, 26), 10, true));
			lblWhiteCount.setBorder(null);
		}
	}//highlightLabel()
	
	public void init(Level level)
	{
		//초기 검은돌 흰돌 차례 및, 갯수 
		reversi = new Reversi(level);
		
		lblLevel.setText(reversi.getLevel().toString());
		lblWhiteCount.setText("2");
		lblBlackCount.setText("2");
		
		highlightLabel();
	}//init()
	
	public void handleEnd()
	{
		//끝났을 때 누가 이겼는지, 다시 게임을 할 것인지
		int select = JOptionPane.showConfirmDialog(this, (reversi.getBlackCount()>reversi.getWhiteCount() ? "Player" : "Computer") + " win !!"
				+ System.lineSeparator() + "Retry?", "Game finished", JOptionPane.YES_NO_OPTION);
	
		if (select == JOptionPane.YES_OPTION)
		{
			init(reversi.getLevel());
			repaint();
		}
		
	}//handlEnd()
	
	public class BackListener implements ActionListener
	{
		//버튼에 대한 처리
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			//되돌아가기 버튼 누르면 게임을 끝냄
			if (evt.getSource() == btnBack)
			{
				(new Music("back.mp3",false)).start(); // 사운드 이펙트
				
				//게임이 끝났으면 레벨 메뉴 보여줌
				if (reversi.isEnd())
					panelRev.selectLevel();
				
				//게임 도중이면 진짜로 끝낼 것인지 팝업창 띄워줌
				else
				{
					int select = JOptionPane.showConfirmDialog(PlayReversi.this, "Are you sure?", "Back to level selection", JOptionPane.YES_NO_OPTION);
					
					if (select == JOptionPane.YES_OPTION)
						panelRev.selectLevel();
				}
			}
		}
	}//BackListener
	
	public class BoardListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent evt) {}
		@Override
		public void mousePressed(MouseEvent evt) {}
		@Override
		public void mouseEntered(MouseEvent evt) {}
		@Override
		public void mouseExited(MouseEvent evt) {}
		
		@Override
		public void mouseReleased(MouseEvent evt)
		{
			Point pt = evt.getPoint();
			//클릭 된 곳의 위치를 받아옴
			int row = pt.x / 70;
			int col = pt.y / 70;
			
			//게임이 끝나거나 바둑판안에 놓지 않을 경우 return 
			if ( (row<1 || 8<row) || (col<1 || 8<col) || reversi.isEnd() )
				return;
			
			//해당 위치에 놓을 수 있으면 돌 그려줌
			if (reversi.handleInput(row, col))
			{
				(new Music("disc.mp3",false)).start(); // 사운드 이펙트
				//바둑판 다시 그림
				repaint();
				
				//돌의 갯수 바꿔줌
				lblWhiteCount.setText(Integer.toString(reversi.getWhiteCount()));
				lblBlackCount.setText(Integer.toString(reversi.getBlackCount()));
				
				//게임 종료
				if (reversi.isEnd())
				{
					handleEnd();
					return;
				}
				
				//차례 바꿔줌
				highlightLabel();
				
				//더 이상 놓을 수 있는 검은 돌이 없다면 팝업 창 띄워주고 차례 넘김
				if (reversi.isPass())
				{
					(new Music("puterror.mp3",false)).start(); // 사운드 이펙트
					JOptionPane.showMessageDialog(PlayReversi.this, "Computer can't put disc anywhere. Pass.");
					reversi.toggleTurn();
					highlightLabel();
				}
				//검은 돌을 놓을 수 있다면
				else
				{
					while (true)
					{
						//흰돌이 놓을 수있는 곳게 돌을 놓고 다시 돌의 갯수 바꾸고 바둑판 그림
						Position computerPosition = reversi.positionGenerator();
						
						//3~6 초간 랜덤으로 컴퓨터의 턴 딜레이
						JDialog jd = (new JOptionPane("",JOptionPane.PLAIN_MESSAGE)).createDialog("");
						jd.setLocation(-1000, -1000);
						
						Timer timer = new Timer(1, new ActionListener() {
					        @Override
					        public void actionPerformed(ActionEvent e) { jd.dispose(); }
					    });
						
						timer.setInitialDelay(((int)(Math.random()*4) + 3) * 1000); // 컴퓨터 기다리는 시간을 조정하려면 ms 단위로 입력하세요
						//timer.setInitialDelay(ms);
						timer.setRepeats(false);
						timer.start();
						jd.setVisible(true);

						reversi.handleInput(computerPosition.getRow(),computerPosition.getCol());
						(new Music("disc.mp3",false)).start(); // 사운드 이펙트
						repaint();
						lblWhiteCount.setText(Integer.toString(reversi.getWhiteCount()));
						lblBlackCount.setText(Integer.toString(reversi.getBlackCount()));
						
						//게임이 끝남
						if (reversi.isEnd())
						{
							handleEnd();
							break;
						}
						
						//차례 바꿔줌
						highlightLabel();
						
						//흰 돌이 더이상 놓을 곳이 없다면 차례를 넘김
						if (reversi.isPass())
						{
							(new Music("puterror.mp3",false)).start(); // 사운드 이펙트
							JOptionPane.showMessageDialog(PlayReversi.this, "Player can't put disc anywhere. Pass.");
							reversi.toggleTurn();
							highlightLabel();
						}
						else
						{
							break;
						}
					}
				}
			}
			else//돌이 놓여질 수 없는 위치
			{
				(new Music("puterror.mp3",false)).start(); // 사운드 이펙트
				JOptionPane.showMessageDialog(PlayReversi.this, "Cant put disc to there.");
			}
		}
	}
}