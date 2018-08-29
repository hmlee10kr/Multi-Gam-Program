package combine9_beta;
import javax.swing.*;

public class PanelReversi extends JPanel
{	
	private JPanel FirstScreen;	//���� ���� ȭ��
	LevelReversi LevelPanel;	//���� ����
	PlayReversi PlayPanel;		//���� �÷��� ȭ��

	//JButton pseudoBtn;
	//ActionEvent levelEvent;
	
	public PanelReversi(JPanel primary,JPanel THEFIRSTPANEL)
	{
		FirstScreen=THEFIRSTPANEL;
		LevelPanel = new LevelReversi(this);
		PlayPanel = new PlayReversi(this);
		
		PlayPanel.setVisible(false);		//������ ���õǸ� ���̰� �� ����
		LevelPanel.setBounds(0,0,1280,720);
		PlayPanel.setBounds(0,0,1280,720);
		primary.add(LevelPanel);
		primary.add(PlayPanel);
	}//PanelReversi()
	
	public void selectLevel()
	{
		//������ �����ϴ� ȭ�� ������
		LevelPanel.setVisible(true);
		PlayPanel.setVisible(false);
	}//selectLevel
	
	public void runReversi()
	{
		//������ ���� ����
		LevelPanel.setVisible(false);
		PlayPanel.init(LevelPanel.getLevel()); //� ������ �����ߴ����� ���� ������ �޶���
		PlayPanel.setVisible(true);	
	}
	
	public void exit()
	{
		//���� ����
		LevelPanel.setVisible(false);
		PlayPanel.setVisible(false);
		setVisible(false);
		FirstScreen.setVisible(true);
	}
	
	@Override
	public void setVisible(boolean aFlag)
	{
		//���ο��� ������ ���ý� �������� ȭ�� ������
		super.setVisible(aFlag);
		
		if (aFlag)
			selectLevel();
	}
}