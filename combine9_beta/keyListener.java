package combine9_beta;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class keyListener extends KeyAdapter {
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(RhythmGameMusicSelection.game==null) {
			return;
		}
		if(e.getKeyCode()==KeyEvent.VK_S) {
			RhythmGameMusicSelection.game.pressS();
		}
		else if(e.getKeyCode()==KeyEvent.VK_D) {
			RhythmGameMusicSelection.game.pressD();
		}
		else if(e.getKeyCode()==KeyEvent.VK_F) {
			RhythmGameMusicSelection.game.pressF();
		}
		else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			RhythmGameMusicSelection.game.pressSpace();
		}
		else if(e.getKeyCode()==KeyEvent.VK_J) {
			RhythmGameMusicSelection.game.pressJ();
		}
		else if(e.getKeyCode()==KeyEvent.VK_K) {
			RhythmGameMusicSelection.game.pressK();
		}
		else if(e.getKeyCode()==KeyEvent.VK_L) {
			RhythmGameMusicSelection.game.pressL();
		}
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(RhythmGameMusicSelection.game==null) {
			return;
		}
		if(e.getKeyCode()==KeyEvent.VK_S) {
			RhythmGameMusicSelection.game.releaseS();
		}
		else if(e.getKeyCode()==KeyEvent.VK_D) {
			RhythmGameMusicSelection.game.releaseD();
		}
		else if(e.getKeyCode()==KeyEvent.VK_F) {
			RhythmGameMusicSelection.game.releaseF();
		}
		else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			RhythmGameMusicSelection.game.releaseSpace();
		}
		else if(e.getKeyCode()==KeyEvent.VK_J) {
			RhythmGameMusicSelection.game.releaseJ();
		}
		else if(e.getKeyCode()==KeyEvent.VK_K) {
			RhythmGameMusicSelection.game.releaseK();
		}
		else if(e.getKeyCode()==KeyEvent.VK_L) {
			RhythmGameMusicSelection.game.releaseL();
		}
	}
	// ��Ŭ������ ����ڿ��� Ű�� �Է¹ް� �� �Ŀ� ������ �׼ǰ� ���� �׼��� �����ų�� �ֵ��� �Ѵ�
}
