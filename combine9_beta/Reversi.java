package combine9_beta;
import java.util.*;

public class Reversi
{
	public static final int BOARD_SIZE = 8; 			//8*8�ٵ��� 
	private Level level;								//������ ����
	private Status[][] status;							//�ٵ����� ����
	private Status currentTurn;							//���� ����
	private int whiteCount;								//�� ���� ����
	private int blackCount;								//���� ���� ����
	private ArrayList <Position> whiteList;				//�� ���� ��ġ		
	private ArrayList <Position> blackList;				//���� ���� ��ġ
	private ArrayList <Position> whiteAvailableList;	//�� ���� ���� �� �ִ� ��ġ
	private ArrayList <Position> blackAvailableList;	//���� ���� ���� �� �ִ� ��ġ

	public Reversi(Level lv)
	{
		level = lv;
		
		whiteList = new ArrayList<Position>();		
		blackList = new ArrayList<Position>();
		whiteAvailableList = new ArrayList<Position>();
		blackAvailableList = new ArrayList<Position>();	
		/**/
		status = new Status[BOARD_SIZE+2][BOARD_SIZE+2];
		
		whiteCount = 0;
		blackCount = 0;
		currentTurn = Status.BLACK; //���� ��(����) ���� ����
		
		//�ʱ� �ٵϾ� ��ġ
		for (int row=0; row<BOARD_SIZE+2; row++)
			for (int col=0; col<BOARD_SIZE+2; col++)
				if (row==0 || col==0 || row==BOARD_SIZE+1 || col==BOARD_SIZE+1)
					status[row][col] = Status.BOUND;
				else if ((row==BOARD_SIZE/2 && col==BOARD_SIZE/2) || (row==BOARD_SIZE/2+1 && col==BOARD_SIZE/2+1))
					status[row][col] = Status.WHITE;
				else if ((row==BOARD_SIZE/2 && col==BOARD_SIZE/2+1) || (row==BOARD_SIZE/2+1 && col==BOARD_SIZE/2))
					status[row][col] = Status.BLACK;
				else
					status[row][col] = Status.EMPTY;

		whiteCount = 2;
		blackCount = 2;

		refreshList();			//���� �ٵϾ��� ��ġ ����
		refreshAvailableList();	//�ٵϾ��� ���� �� �ִ� ��ġ ����
	}

	//get-set
	public Level getLevel()	{ return level; }
	public void setLevel(Level level)	{ this.level = level; }
	public Status getTurn()	{ return currentTurn; }
	public Status statusAt(int row, int col)	{ return status[row][col]; }
	public int getWhiteCount()	{ return whiteCount; }
	public int getBlackCount()	{ return blackCount; }
	public int getAvailableSize(Status status)
	{ return (status==Status.WHITE ? whiteAvailableList.size() : blackAvailableList.size()); }
	
	public boolean handleInput(int row, int col)
	{
		if (isPutAvailable(row,col))
		{
			putDisc(row,col);
			refreshStatus(row,col);
			refreshList();
			refreshAvailableList();
			toggleTurn();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//���ʸ� ��Ÿ��
	public void toggleTurn()	{ currentTurn = (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); }
	
	public void putDisc(int row, int col)
	{
		//�ش� ��ġ�� �ش����� ���·� �ٲٰ� �ش� ���� ���� ������ �ø�
		if (currentTurn == Status.WHITE)
		{
			status[row][col] = Status.WHITE;
			whiteCount++;
		}
		else
		{
			status[row][col] = Status.BLACK;
			blackCount++;		
		}
	}//putDisc()
	
	public boolean isPass()
	{
		//���� �� �ִ� �ڸ��� 0���� Ȯ��
		return ( (currentTurn==Status.WHITE ? whiteAvailableList.size() : blackAvailableList.size()) == 0 );
	}
	
	public boolean isEnd()
	{
		//���α׷��� ����� �� �ִ��� Ȯ��
		return (whiteAvailableList.size()==0 && blackAvailableList.size()==0);
	}
	
	public boolean isPutAvailable(int row, int col)
	{
		//���� �� �ִ� �ڸ����� Ȯ��
		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);
		int size = availableList.size();

		//availableList�� �ִ� ���� ��ġ�ϸ� ���� �� �ִ� �ڸ� �̹Ƿ� true ����
		for (int i=0; i<size; i++)
			if (availableList.get(i).getRow()==row && availableList.get(i).getCol()==col)
				return true;

		return false;
	}//isPutAvailabe

	public void refreshStatus(int row, int col)
	{
		//���� ����� ������ �ٲ��ִ� �Լ�
		
		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition selectedPosition = new TraversingPosition();

		//���� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ ���� �̵�
		for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ ���� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}
		
		//������ ���� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ ������ ���� �̵�
		for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ ������ ���� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
					for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//���������� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ ������ ���� �̵�
		for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ ������ �� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//������ �Ʒ��� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ ������ �Ʒ��� �̵�
		for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ ������ �Ʒ��� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//�Ʒ��� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ �Ʒ��� �̵�
		for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ �Ʒ��� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//���� �Ʒ��� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ ���� �Ʒ��� �̵�
		for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ ���� �Ʒ��� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//�������� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ �������� �̵�
		for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ �������� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//���� ���� �����̸� ����
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//�� ���¿� ���� ������ ���� ���� ������ ���� ���� �̵�
		for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//�� ���¿� ���� ������ ���� ���� ������ ���� ���� �̵��ϸ鼭 �ٸ� ������ ���� ������ �ٲٰ� �� ���� ������ �ٲ�
			for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}
	}
	
	
	
	public void refreshList()
	{
		//�� ���� ���� ���� ��ġ�� ArrayList�� ����
		whiteList.clear();
		blackList.clear();
		//status�� white�� whiteList
		//status�� black �̸� blackList�� add
		for (int row=1; row<BOARD_SIZE+1; row++)
			for (int col=1; col<BOARD_SIZE+1; col++)
				 if (status[row][col] == Status.WHITE)
					 whiteList.add(new Position(row,col));
				 else if (status[row][col] == Status.BLACK)
					 blackList.add(new Position(row,col));
	}//refreshList()
	
	public void refreshAvailableList()
	{	
		int size;
		int currentRow;
		int currentCol;

		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition oneBlankGoPosition = new TraversingPosition();	//��ĭ ���� �ٵϾ� ��ġ�� �ϱ����ؼ� ���Ǵ� ����

		size = whiteList.size();
		whiteAvailableList.clear(); 	//�Ź� �ٵϾ��� ���� �� �ִ� ��ġ�� �޶����� ������ clear�� ����
		for (int i=0; i<size; i++)
		{
			currentRow = whiteList.get(i).getRow();
			currentCol = whiteList.get(i).getCol();

			//���� �� �ٵϵ��� ��ġ���� ���� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveUp();	//�ٷ� ���� ��ĭ�� ä�� �� ���⶧���� �װ��� üũ�ϱ� ���� oneBlankGoPosition.moveUp();����
			//��ĭ�� ���� ���� ���� ������ ������ ã�ư��� for��
			for (traversal.moveUp();status[traversal.getRow()][traversal.getCol()] == Status.BLACK;traversal.moveUp());
			//�ش� ĭ�� ����ְ�, �ٷ� ��ĭ�� �ƴϸ� ���� �� �ִ� �����̹Ƿ� whiteAvailableList�� �߰� 
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			
			//���� �� �ٵϵ� ��ġ���� ������ ���� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightUp();//�ٷ� �밢�� ��ĭ���� ���� ���� �� ����
			//���� ���� ���� ������ ���������� �̵�
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRightUp());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//���� �� �ٵϵ� ��ġ���� ������ ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRight();//�ٷ� ��ĭ���� ���� ���� �� ����
			//���� ���� ���� ������ ���������� �̵�
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRight());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//���� �� �ٵϵ� ��ġ���� ������ �Ʒ��� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightDown();//�ٷ� ������ �Ʒ����� ���� �� ����
			//���� ���� ���� ������ ������ �Ʒ��� �̵�
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRightDown());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//���� �� �ٵϵ� ��ġ���� �Ʒ� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveDown();//�ٷ� �Ʒ����� ���� ���� �� ����
			//���� ���� ���� ������ �Ʒ��� �̵�
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveDown());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//���� �� �ٵϵ� ��ġ���� ���� �Ʒ� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftDown();//�ٷ� ���ʾƷ����� ���� ���� �� ����
			//���� ���� ���� ������ ������ �Ʒ��� �̵�
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeftDown());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//���� �� �ٵϵ� ��ġ���� ���� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeft();//�ٷ� ���ʿ��� ���� �� ����
			//���� ���� ���� ������ �������� �̵�
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeft());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//���� �� �ٵϵ� ��ġ���� ���� ���� ��ġ�� �� ���� ���� �� �ִ� ���//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftUp();//�ٷ� ���� ������ ���� ���� �� ����
			//���� ���� ���� ������ ���� ���� �̵�
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeftUp());
			//����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);
		}

		size = blackList.size();
		blackAvailableList.clear();
		for (int i=0; i<size; i++)
		{
			currentRow = blackList.get(i).getRow();
			currentCol = blackList.get(i).getCol();
			
			//���� ���� ������ġ�� ���� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveUp();//�ٷ� ��ĭ���� ���� ���� �� ����
			//�� ���� ���� ������ ���� �̵�
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveUp());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� �����ʿ� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightUp();//�ٷ� ���������δ� ���� �� ����
			//�� ���� ���� ������ ���������� �̵�
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRightUp());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� ���������� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRight();//�ٷ� ������ ������ ���� �� ����
			//�� ���� ���� ������ ���������� �̵�
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRight());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� ������ �Ʒ��� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightDown();//�ٷ� ������ �Ʒ����� ���� �� ����
			//�� ���� ���� ������ ������ �Ʒ��� �̵�
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRightDown());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� �Ʒ��� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveDown();//�ٷ� �Ʒ����� ���� �� ����
			//�� ���� ���� ������ �Ʒ��� �̵�
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveDown());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� ���� �Ʒ��� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftDown();//�ٷ� ���� �Ʒ����� ���� �� ����
			//�� ���� ���� ������ ������ �Ʒ��� �̵�
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeftDown());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� ���ʿ� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeft();//�ٷ� ���ʿ��� ���� �� ����
			//�� ���� ���� ������ �������� �̵�
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeft());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//���� ���� ������ġ�� ���� ���� ���� ���
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftUp();//�ٷ� ���� ������ ���� �� ����
			//�� ���� ���� ������ ���� ���� �̵�
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeftUp());
			//ĭ�� ����ְ� ���� �� �ִ� �����̸� push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);
		}
	}//refreshStatus()
	
	public Position positionGenerator()
	{
		//������ ���� �ٸ��� �����Ŵ
		Position computerPosition = null;
		
		switch (level)
		{
		case EASY :		computerPosition = positionGeneratorEasy(); break;
		case NORMAL :	computerPosition = positionGeneratorNormal(); break;
		case HARD :		computerPosition = positionGeneratorHard(); break;
		case EXPERT :	computerPosition = positionGeneratorExpert(); break;
		case MASTER :	computerPosition = positionGeneratorMaster(); break;
		}
		
		return computerPosition;
	}//positionGenerator
	
	public Position positionGeneratorEasy()
	{
		int size;

		ArrayList <Position> minPositionList = minPositionListGenerator();
		ArrayList <Integer> minDistanceList = new ArrayList<Integer>();

		size = minPositionList.size();
		for (int i=0; i<size; i++)
			minDistanceList.add(minDistanceToBound(minPositionList.get(i)));

		int maxDistance = Collections.max(minDistanceList);
		
		ArrayList <Position> maxDistancePositionList = new ArrayList<Position>();

		size = minPositionList.size();
		for (int i=0; i<size; i++)
			if (maxDistance == minDistanceList.get(i))
				maxDistancePositionList.add(minPositionList.get(i));

		Position generatedPosition = maxDistancePositionList.get( (int)(Math.random() * maxDistancePositionList.size()) );

		return generatedPosition;
	}
	
	public Position positionGeneratorNormal()
	{
		if ((int)(Math.random()*2) == 0)
		{
			return positionGeneratorHard();
		}
		else
		{
			ArrayList <Position> minPositionList = minPositionListGenerator();
			Position generatedPosition = minPositionList.get( (int)(Math.random() * minPositionList.size()) );	

			return generatedPosition;
		}
	}
	
	public Position positionGeneratorHard()
	{
		return (currentTurn==Status.WHITE ?
			whiteAvailableList.get( (int)(Math.random()*whiteAvailableList.size()) ) :
			blackAvailableList.get( (int)(Math.random()*blackAvailableList.size()) ));
	}
	
	public Position positionGeneratorExpert()
	{
		if ((int)(Math.random()*2) == 0)
		{
			return positionGeneratorHard();
		}
		else
		{
			ArrayList <Position> maxPositionList = maxPositionListGenerator();
			Position generatedPosition = maxPositionList.get( (int)(Math.random() * maxPositionList.size()) );

			return generatedPosition;
		}
	}
	
	public Position positionGeneratorMaster()
	{
		int size;

		ArrayList <Position> maxPositionList = maxPositionListGenerator();
		ArrayList <Integer> minDistanceList = new ArrayList<Integer>();

		size = maxPositionList.size();
		for (int i=0; i<size; i++)
			minDistanceList.add(minDistanceToBound(maxPositionList.get(i)));

		int minDistance = Collections.min(minDistanceList);
		
		ArrayList <Position> minDistancePositionList = new ArrayList<Position>();

		size = maxPositionList.size();
		for (int i=0; i<size; i++)
			if (minDistance == minDistanceList.get(i))
				minDistancePositionList.add(maxPositionList.get(i));

		Position generatedPosition = minDistancePositionList.get( (int)(Math.random() * minDistancePositionList.size()) );

		return generatedPosition;
	}
	
	private ArrayList <Position> minPositionListGenerator()
	{
		int size;
		int currentRow;
		int currentCol;

		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition selectedPosition = new TraversingPosition();

		//���� ������ ���� ���� �� �ִ� ��ǥ�� �޾ƿ�
		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);

		int point;
		ArrayList <Integer> pointList = new ArrayList<Integer>();		//�ٲ�� ���� ����
		ArrayList <Position> positionList = new ArrayList<Position>();	//���� ���� �� �ִ� ��ġ
		
		size = availableList.size();
		for (int i=0; i<size; i++)
		{
			point = 0;
			currentRow = availableList.get(i).getRow();
			currentCol = availableList.get(i).getCol();

			//���� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
					point++;
			//������ ���� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
					point++;
			
			//���������� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
					point++;

			//������ ���� �̵��ϸ鼭 �ٲ�� ���� ���� ��			
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
					point++;

			//�Ʒ��� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
					point++;

			//���� �Ʒ��� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
					point++;
			
			//���ʷ� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
					point++;
			
			//���� ���� �̵��ϸ鼭 �ٲ�� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
					point++;

			pointList.add(point);
			positionList.add( new Position(availableList.get(i).getRow(),availableList.get(i).getCol()) );
		}

		int min = Collections.min(pointList);	//���� ������ ���� ���� �ٲ�� ��

		ArrayList <Position> minPositionList = new ArrayList<Position>();

		//���� ������ ���� ���� �ٲ�� ��ġ�� ã�� ����
		size = positionList.size();
		for (int i=0; i<size; i++)
			if (pointList.get(i) == min)
				minPositionList.add(positionList.get(i));

		return minPositionList;	
	}//minPositionListGenerator()
	
	private ArrayList <Position> maxPositionListGenerator()
	{
		//���� ������ ���� ���� �ٲ�� ��ġ ã�Ƴ�
		
		int size;
		int currentRow;
		int currentCol;

		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition selectedPosition = new TraversingPosition();

		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);

		int point;
		ArrayList <Integer> pointList = new ArrayList<Integer>();
		ArrayList <Position> positionList = new ArrayList<Position>();

		//������ �� �ִ� ���� ���� ����
		size = availableList.size();
		for (int i=0; i<size; i++)
		{
			point = 0;
			currentRow = availableList.get(i).getRow();
			currentCol = availableList.get(i).getCol();
			
			//���� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
					point++;

			//������ ���� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
					point++;

			//�����ʷ� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
					point++;

			//������ �Ʒ��� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
					point++;

			//�Ʒ��� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
					point++;

			//���� �Ʒ��� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
					point++;

			//�������� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
					point++;
			
			//���� ���� �����̸� �ٲ� �� �ִ� ���� ���� ��
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
					point++;

			pointList.add(point);
			positionList.add( new Position(availableList.get(i).getRow(),availableList.get(i).getCol()) );
		}

		int max = Collections.max(pointList);	//���� ��ġ�� ���� ���� �ٲ�� ��

		ArrayList <Position> maxPositionList = new ArrayList<Position>();

		//���� ��ġ�� ���� ���� �ٲ�� ��ġ ����
		size = positionList.size();
		for (int i=0; i<size; i++)
			if (pointList.get(i) == max)
				maxPositionList.add(positionList.get(i));

		return maxPositionList;	
	}//maxPositionListGenerator()
	
	private int minDistanceToBound(Position rPosition)
	{
		//BOUND�� ���� ���� �Ÿ��� ����� �� ����
		int currentRow = rPosition.getRow();
		int currentCol = rPosition.getCol();

		TraversingPosition traversal = new TraversingPosition();

		int distance;
		int minDistance;

		//���� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveUp(), distance++);
		minDistance = distance;

		//������ ���� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRightUp(), distance++);
		if (distance < minDistance) { minDistance = distance; }
	
		//�����ʷ� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRight(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//������ �Ʒ��� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRightDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//�Ʒ��� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//���� �Ʒ��� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeftDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//�������� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeft(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//���� ���� �����̸� Bound���� �Ÿ� ��
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeftUp(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		return minDistance;		
	}
	
	private void pushElement(ArrayList <Position> list, Position element)
	{
		//element ��ġ�� list�� ������ �߰�
		int size = list.size();
		for (int i=0; i<size; i++)
			if (list.get(i).equals(element))
				return;

		list.add(new Position(element.getRow(),element.getCol()));
	}
}