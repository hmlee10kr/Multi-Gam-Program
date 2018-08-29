package combine9_beta;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlayReversi extends JPanel
{	
	//���� ȭ��
	private Reversi reversi;						//reversi ���� �˰���
	
	private JButton btnBack;						//�ٵ���
	private JLabel lblLevel;						//���� ǥ��
	private JLabel lblWhiteCount, lblBlackCount;	//��, �������� ����
	
	private PanelReversi panelRev;					//������ ���� �гε�
	
	public PlayReversi(PanelReversi panel)
	{
		setPreferredSize(new Dimension(1280,720));
		setBackground(new Color(57,131,66));
		setLayout(null);

		reversi = null;
		panelRev = panel;
		
		//����, ������ ����, �� ���� ���� ��ġ, ���� , ��Ʈ ����
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
		
		//�ڷΰ��� ��ư �̹��� �����
		
		//�̹��� ������ ��ȯ
		ImageIcon image=new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre= image.getImage();
		Image scale= pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		btnBack = new JButton(new ImageIcon(scale));
		//��� ����
		btnBack.setBorderPainted(false);
		//���� ����
		btnBack.setContentAreaFilled(false);
		//��Ŀ���� ��� ����
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
		//�ٵ���, �ٵϾ� �׸���
		Graphics2D g2d = (Graphics2D) page; //2d �׷��� ȯ�濡�� ���� �β�, ��Ÿ��, ��ǥ ����� ����
		super.paintComponent(page);
		
		page.setColor(new Color(128,64,64));
		g2d.setStroke(new BasicStroke(6f));	//���� �β�

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		
		//�ٵ��� �׸���
		for (int i=1; i<=9; i++)
		{
			page.drawLine(70,70*i,630,70*i);
			page.drawLine(70*i,70,70*i,630);
		}//for
		
		//�ٵϾ� �׸���
		g2d.setStroke(new BasicStroke(4f));	//���� �ΰ�
		
		for (int row=1; row<=Reversi.BOARD_SIZE; row++)
			for (int col=1; col<=Reversi.BOARD_SIZE; col++)
				drawDisc(page,row,col,reversi.statusAt(row, col));
		
	}//paintComponent
	
	public void drawDisc(Graphics page, int row, int col, Status status)
	{
		//��, ���� �� ���������� ���� �ٵϾ��� �׷���
		if (status==Status.WHITE || status==status.BLACK)
		{
			//�ٵϾ� ���� ĥ�ϱ�
			page.setColor((status==Status.WHITE ? Color.white : Color.black));
			page.fillOval(70*row+6, 70*col+6, 58, 58);
			//�ٵϾ� ��� ĥ�ϱ�
			page.setColor(Color.black);
			page.drawOval(70*row+6, 70*col+6, 58, 58);
		}
	}//drawDisc()
	
	public void highlightLabel()
	{	//���� ���ʸ� ǥ������
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
		//�ʱ� ������ �� ���� ��, ���� 
		reversi = new Reversi(level);
		
		lblLevel.setText(reversi.getLevel().toString());
		lblWhiteCount.setText("2");
		lblBlackCount.setText("2");
		
		highlightLabel();
	}//init()
	
	public void handleEnd()
	{
		//������ �� ���� �̰����, �ٽ� ������ �� ������
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
		//��ư�� ���� ó��
		@Override
		public void actionPerformed(ActionEvent evt)
		{
			//�ǵ��ư��� ��ư ������ ������ ����
			if (evt.getSource() == btnBack)
			{
				(new Music("back.mp3",false)).start(); // ���� ����Ʈ
				
				//������ �������� ���� �޴� ������
				if (reversi.isEnd())
					panelRev.selectLevel();
				
				//���� �����̸� ��¥�� ���� ������ �˾�â �����
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
			//Ŭ�� �� ���� ��ġ�� �޾ƿ�
			int row = pt.x / 70;
			int col = pt.y / 70;
			
			//������ �����ų� �ٵ��Ǿȿ� ���� ���� ��� return 
			if ( (row<1 || 8<row) || (col<1 || 8<col) || reversi.isEnd() )
				return;
			
			//�ش� ��ġ�� ���� �� ������ �� �׷���
			if (reversi.handleInput(row, col))
			{
				(new Music("disc.mp3",false)).start(); // ���� ����Ʈ
				//�ٵ��� �ٽ� �׸�
				repaint();
				
				//���� ���� �ٲ���
				lblWhiteCount.setText(Integer.toString(reversi.getWhiteCount()));
				lblBlackCount.setText(Integer.toString(reversi.getBlackCount()));
				
				//���� ����
				if (reversi.isEnd())
				{
					handleEnd();
					return;
				}
				
				//���� �ٲ���
				highlightLabel();
				
				//�� �̻� ���� �� �ִ� ���� ���� ���ٸ� �˾� â ����ְ� ���� �ѱ�
				if (reversi.isPass())
				{
					(new Music("puterror.mp3",false)).start(); // ���� ����Ʈ
					JOptionPane.showMessageDialog(PlayReversi.this, "Computer can't put disc anywhere. Pass.");
					reversi.toggleTurn();
					highlightLabel();
				}
				//���� ���� ���� �� �ִٸ�
				else
				{
					while (true)
					{
						//���� ���� ���ִ� ���� ���� ���� �ٽ� ���� ���� �ٲٰ� �ٵ��� �׸�
						Position computerPosition = reversi.positionGenerator();
						
						//3~6 �ʰ� �������� ��ǻ���� �� ������
						JDialog jd = (new JOptionPane("",JOptionPane.PLAIN_MESSAGE)).createDialog("");
						jd.setLocation(-1000, -1000);
						
						Timer timer = new Timer(1, new ActionListener() {
					        @Override
					        public void actionPerformed(ActionEvent e) { jd.dispose(); }
					    });
						
						timer.setInitialDelay(((int)(Math.random()*4) + 3) * 1000); // ��ǻ�� ��ٸ��� �ð��� �����Ϸ��� ms ������ �Է��ϼ���
						//timer.setInitialDelay(ms);
						timer.setRepeats(false);
						timer.start();
						jd.setVisible(true);

						reversi.handleInput(computerPosition.getRow(),computerPosition.getCol());
						(new Music("disc.mp3",false)).start(); // ���� ����Ʈ
						repaint();
						lblWhiteCount.setText(Integer.toString(reversi.getWhiteCount()));
						lblBlackCount.setText(Integer.toString(reversi.getBlackCount()));
						
						//������ ����
						if (reversi.isEnd())
						{
							handleEnd();
							break;
						}
						
						//���� �ٲ���
						highlightLabel();
						
						//�� ���� ���̻� ���� ���� ���ٸ� ���ʸ� �ѱ�
						if (reversi.isPass())
						{
							(new Music("puterror.mp3",false)).start(); // ���� ����Ʈ
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
			else//���� ������ �� ���� ��ġ
			{
				(new Music("puterror.mp3",false)).start(); // ���� ����Ʈ
				JOptionPane.showMessageDialog(PlayReversi.this, "Cant put disc to there.");
			}
		}
	}
}