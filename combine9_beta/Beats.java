package combine9_beta;

//��Ʈ�� �������� �ð��� ���� �ʱ�ȭ Ŭ����
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

	//�� Ŭ������ �ð��� � Ÿ���� ��Ʈ�� ���������� �����Ѵ�.
}
