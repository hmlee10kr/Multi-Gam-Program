package combine9_beta;
import javax.swing.*;

public class PanelReversi extends JPanel
{	
	private JPanel FirstScreen;	//게임 선택 화면
	LevelReversi LevelPanel;	//레벨 선택
	PlayReversi PlayPanel;		//게임 플레이 화면

	//JButton pseudoBtn;
	//ActionEvent levelEvent;
	
	public PanelReversi(JPanel primary,JPanel THEFIRSTPANEL)
	{
		FirstScreen=THEFIRSTPANEL;
		LevelPanel = new LevelReversi(this);
		PlayPanel = new PlayReversi(this);
		
		PlayPanel.setVisible(false);		//레벨이 선택되면 보이게 할 것임
		LevelPanel.setBounds(0,0,1280,720);
		PlayPanel.setBounds(0,0,1280,720);
		primary.add(LevelPanel);
		primary.add(PlayPanel);
	}//PanelReversi()
	
	public void selectLevel()
	{
		//레벨을 선택하는 화면 보여줌
		LevelPanel.setVisible(true);
		PlayPanel.setVisible(false);
	}//selectLevel
	
	public void runReversi()
	{
		//오델로 게임 실행
		LevelPanel.setVisible(false);
		PlayPanel.init(LevelPanel.getLevel()); //어떤 레벨을 설정했는지에 따라 실행이 달라짐
		PlayPanel.setVisible(true);	
	}
	
	public void exit()
	{
		//게임 종료
		LevelPanel.setVisible(false);
		PlayPanel.setVisible(false);
		setVisible(false);
		FirstScreen.setVisible(true);
	}
	
	@Override
	public void setVisible(boolean aFlag)
	{
		//메인에서 오델로 선택시 레벨선택 화면 보여줌
		super.setVisible(aFlag);
		
		if (aFlag)
			selectLevel();
	}
}