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
	//player �� ����Ѱ� ����
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

	
	//3.other functions ����
	// �ð�
	public int getTime() {
		if (player == null)
			return 0;
		return player.getPosition();
	}
	//

	// �� ����
	public void close() {
		isLoop = false;
		player.close();
		this.interrupt();// �� ����
	}
	
	
	//start �κ�
	@Override
	public void run() {
		// thread ��� ������ ������ �ؾ���
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
	//http://kkckc.tistory.com/70����
	//http://story0111.tistory.com/36 ����
	//http://gcyong.tistory.com/53
	//�����ڷ� �뷡 �̸���  �ݺ����θ� �޴´�.
	
}
