 package combine9_beta;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Note extends Thread {
	private Image noteBasicImage = new ImageIcon(Main.class.getResource("../images/noteBasic.png")).getImage();
	private int x, y = -100;//모든 노트들은 높이 -100에서 만들어진다.
	private String noteType;
	private boolean proceeding=true;
	
	
	
	
	public boolean isProceeding() {
		return proceeding;
	}
	
	public String getNoteType() {
		return noteType;
	}
	public void close() {
		proceeding=false;
	}
	public void KILLTHREAD() {
		if(this!=null)
		{
		interrupt();
		}
	}
	public Note(String noteType) {
		if(noteType.equals("S"))
		{
			x=228;
		}
		else if(noteType.equals("D"))
		{
			x=332;
			noteBasicImage = new ImageIcon(Main.class.getResource("../images/yellow.png")).getImage();
			
		}
		else if(noteType.equals("F"))
		{
			x=436;
		}
		else if(noteType.equals("Space"))
		{
			x=540;
			noteBasicImage = new ImageIcon(Main.class.getResource("../images/purple.png")).getImage();
			
		}
		else if(noteType.equals("J"))
		{
			x=744;
		}
		else if(noteType.equals("K"))
		{
			x=848;
			noteBasicImage = new ImageIcon(Main.class.getResource("../images/yellow.png")).getImage();
			
		}
		else if(noteType.equals("L"))
		{
			x=952;
		}
		this.noteType=noteType;
		
	}
	public void screenDraw(Graphics2D g) {
		if(noteType.equals("Space"))
		{
			g.drawImage(noteBasicImage, x, y, null);
			g.drawImage(noteBasicImage, x+100, y, null);
			
		}
		else {
			g.drawImage(noteBasicImage, x, y, null);
		}
	}
	public void drop() {
		y=y+Main.NOTE_SPEED;
		if(y>625)
		{
			System.out.println("MISS");
			Game.Gamescore+=-100;
			Game.JUDGEMENT="MISS";
			Game.combo=0;
			Game.Life+=Game.Wrong;
			close();
		}
	}
	//노트 속도만큼 계속 더해준다.
	@Override
	public void run() {
		try {
			while(true) {
				drop();
				if(proceeding) {
				Thread.sleep(Main.SLEEP_TIME);
				}
				else {
					interrupt();
					break;
				}
			}
		}catch(Exception e) {
			e.getMessage();
		}
	}
	public String judge() {
		if(y>=620)
		{
			System.out.println("LATE");
			close();
			return "LATE";
		}
		else if(y>=613) {
			System.out.println("LATE");
			close();
			return "LATE";
		}
		else if(y>=600) {
			System.out.println("Good");
			close();
			return "GOOD";
		}
		else if(y>=590) {
			System.out.println("Great");
			close();
			return "GREAT";
		}
		else if(y>=575) {
			System.out.println("Perfect");
			close();
			return "PERFECT";
		}
		else if(y>=560) {
			System.out.println("Great");
			close();
			return "GREAT";
		}
		else if(y>=550) {
			System.out.println("Good");
			close();
			return "GOOD";
		}
		else if(y>=535) {
			System.out.println("Early");
			close();
			return "EARLY";
		}
		return "";
	}
	//생성자로어떤 노트타입인지를 받는다 --> 어떤 위치에 노트가 있을지를 받는다.
	//run에서 drop함수를 호출하여 실행중이면 슬립타임만큼 멈추었다가 계속 진행하고
	//실행중이 아니면 노트를 죽인다.
	//judge함수를 사용하여 판정을 나타낸다.
	//그림 그리는것에 대해서는 space경우 x가 100만큼 떨어진 위치에 하나 더 그려서 길게 만들고
	//나머지는 그대로 나오게 한다.
}

