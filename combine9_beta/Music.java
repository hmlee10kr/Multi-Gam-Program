package combine9_beta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class Music extends Thread {

	private Player player;
	private boolean isLoop;
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;
	//player 및 잡다한것 선언
	public Music(String name, boolean isLoop) {
		try {
			file = new File(Main.class.getResource("../music/" + name).toURI());
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
			this.isLoop = isLoop;
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}//constructor 

	
	//3.other functions 구현
	// 시간
	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}
	//

	// 곡 종료
	public void close() {
		isLoop = false;
		player.close();
		this.interrupt();// 곡 종료
	}
	
	
	//start 부분
	@Override
	public void run() {
		// thread 상속 받으면 무조건 해야함
		try {
			do {
				player.play();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
			} while (isLoop);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	//http://kkckc.tistory.com/70참고
	//http://story0111.tistory.com/36 참고
	//http://gcyong.tistory.com/53
	//생성자로 노래 이름과  반복여부를 받는다.
	
}
