package combine9_beta;

//노트가 떨어지는 시간에 대한 초기화 클래스
public class Beats {
	private int time;
	private String Name;

	public Beats(int t, String name) {
		this.time = t;
		this.Name = name;
	}

	public String getNoteName() {
		return Name;
	}

	public int getTime() {
		return time;
	}

	//이 클래스는 시간과 어떤 타입의 노트가 떨어질지를 저장한다.
}
