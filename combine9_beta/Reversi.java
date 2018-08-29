package combine9_beta;
import java.util.*;

public class Reversi
{
	public static final int BOARD_SIZE = 8; 			//8*8바둑판 
	private Level level;								//선택한 레벨
	private Status[][] status;							//바둑판의 상태
	private Status currentTurn;							//현재 차례
	private int whiteCount;								//흰 돌의 갯수
	private int blackCount;								//검은 돌의 갯수
	private ArrayList <Position> whiteList;				//흰 돌의 위치		
	private ArrayList <Position> blackList;				//검은 돌의 위치
	private ArrayList <Position> whiteAvailableList;	//흰 돌이 놓을 수 있는 위치
	private ArrayList <Position> blackAvailableList;	//검은 돌이 놓을 수 있는 위치

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
		currentTurn = Status.BLACK; //검은 돌(유저) 부터 시작
		
		//초기 바둑알 배치
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

		refreshList();			//현재 바둑알의 위치 저장
		refreshAvailableList();	//바둑알이 놓을 수 있는 위치 저장
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
	
	//차례를 나타냄
	public void toggleTurn()	{ currentTurn = (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); }
	
	public void putDisc(int row, int col)
	{
		//해당 위치를 해당턴의 상태로 바꾸고 해당 턴의 돌의 갯수를 늘림
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
		//놓을 수 있는 자리가 0인지 확인
		return ( (currentTurn==Status.WHITE ? whiteAvailableList.size() : blackAvailableList.size()) == 0 );
	}
	
	public boolean isEnd()
	{
		//프로그램이 종료될 수 있는지 확인
		return (whiteAvailableList.size()==0 && blackAvailableList.size()==0);
	}
	
	public boolean isPutAvailable(int row, int col)
	{
		//놓을 수 있는 자리인지 확인
		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);
		int size = availableList.size();

		//availableList에 있는 값과 일치하면 놓을 수 있는 자리 이므로 true 리턴
		for (int i=0; i<size; i++)
			if (availableList.get(i).getRow()==row && availableList.get(i).getCol()==col)
				return true;

		return false;
	}//isPutAvailabe

	public void refreshStatus(int row, int col)
	{
		//돌의 색상및 갯수를 바꿔주는 함수
		
		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition selectedPosition = new TraversingPosition();

		//위로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 위로 이동
		for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 위로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}
		
		//오른쪽 위로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 오른쪽 위로 이동
		for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 오른쪽 위로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
					for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//오른쪽으로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 오른쪽 으로 이동
		for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 오른쪽 로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//오른쪽 아래로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 오른쪽 아래로 이동
		for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 오른쪽 아래로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//아래로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 아래로 이동
		for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 아래로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//왼쪽 아래로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 왼쪽 아래로 이동
		for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 왼쪽 아래로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//왼쪽으로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 왼쪽으로 이동
		for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 왼쪽으로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}

		//왼쪽 위로 움직이며 동작
		traversal.setPosition(row,col);
		selectedPosition.setPosition(row,col);
		//현 상태와 같은 상태의 돌을 만날 때까지 왼쪽 위로 이동
		for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
		if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
			//현 상태와 같은 상태의 돌을 만날 때까지 왼쪽 위로 이동하면서 다른 상태의 돌의 색상을 바꾸고 두 돌의 갯수를 바꿈
			for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
			{
				status[selectedPosition.getRow()][selectedPosition.getCol()] = (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK);
				whiteCount += (currentTurn==Status.WHITE ? 1 : -1);
				blackCount += (currentTurn==Status.WHITE ? -1 : 1);
			}
	}
	
	
	
	public void refreshList()
	{
		//흰 돌과 검은 돌의 위치를 ArrayList에 넣음
		whiteList.clear();
		blackList.clear();
		//status가 white면 whiteList
		//status가 black 이면 blackList에 add
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
		TraversingPosition oneBlankGoPosition = new TraversingPosition();	//한칸 띄우고 바둑알 배치를 하기위해서 사용되는 변수

		size = whiteList.size();
		whiteAvailableList.clear(); 	//매번 바둑알을 놓을 수 있는 위치가 달라지기 때문에 clear를 해줌
		for (int i=0; i<size; i++)
		{
			currentRow = whiteList.get(i).getRow();
			currentCol = whiteList.get(i).getCol();

			//현재 흰 바둑돌의 위치에서 위인 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveUp();	//바로 위에 한칸은 채울 수 없기때문에 그것을 체크하기 위해 oneBlankGoPosition.moveUp();진행
			//윗칸에 검은 돌이 없을 때까지 공간을 찾아가는 for문
			for (traversal.moveUp();status[traversal.getRow()][traversal.getCol()] == Status.BLACK;traversal.moveUp());
			//해당 칸이 비어있고, 바로 윗칸이 아니면 놓을 수 있는 공간이므로 whiteAvailableList에 추가 
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);

			
			//현재 흰 바둑돌 위치에서 오른쪽 위인 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightUp();//바로 대각선 위칸에는 돌을 놓을 수 없음
			//검은 돌이 없을 때까지 오른쪽위로 이동
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRightUp());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//현재 흰 바둑돌 위치에서 오른쪽 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRight();//바로 옆칸에는 돌을 놓을 수 없음
			//검을 돌이 없을 때까지 오른쪽으로 이동
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRight());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//현재 흰 바둑돌 위치에서 오른쪽 아래인 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightDown();//바로 오른쪽 아래에는 놓을 수 없음
			//검은 돌이 없을 때까지 오른쪽 아래로 이동
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveRightDown());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//현재 흰 바둑돌 위치에서 아래 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveDown();//바로 아래에는 돌을 놓을 수 없음
			//검은 돌이 없을 때까지 아래로 이동
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveDown());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//현재 흰 바둑돌 위치에서 왼쪽 아래 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftDown();//바로 왼쪽아래에는 돌을 놓을 수 없음
			//검은 돌이 없을 때까지 오른쪽 아래로 이동
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeftDown());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//현재 흰 바둑돌 위치에서 왼쪽 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeft();//바로 왼쪽에는 놓을 수 없음
			//검을 돌이 없을 때까지 왼쪽으로 이동
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeft());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);


			//현재 흰 바둑돌 위치에서 왼쪽 위인 위치에 흰 돌을 놓을 수 있는 경우//
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftUp();//바로 왼쪽 위에는 돌을 놓을 수 없음
			//검은 돌이 없을 때까지 왼쪽 위로 이동
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == Status.BLACK; traversal.moveLeftUp());
			//비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(whiteAvailableList,traversal);
		}

		size = blackList.size();
		blackAvailableList.clear();
		for (int i=0; i<size; i++)
		{
			currentRow = blackList.get(i).getRow();
			currentCol = blackList.get(i).getCol();
			
			//검은 돌을 현재위치의 위에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveUp();//바로 위칸에는 돌을 놓을 수 없음
			//흰 돌이 없을 때까지 위로 이동
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveUp());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 오른쪽에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightUp();//바로 오른쪽으로는 놓을 수 없음
			//흰 돌이 없을 때까지 오른쪽으로 이동
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRightUp());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 오른쪽위에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRight();//바로 오른쪽 위에는 놓을 수 없음
			//흰 돌이 없을 때까지 오른쪽위로 이동
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRight());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 오른쪽 아래에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveRightDown();//바로 오른쪽 아래에는 놓을 수 없음
			//흰 돌이 없을 때까지 오른쪽 아래로 이동
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveRightDown());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 아래에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveDown();//바로 아래에는 놓을 수 없음
			//흰 돌이 없을 때까지 아래로 이동
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveDown());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 왼쪽 아래에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftDown();//바로 왼쪽 아래에는 놓을 수 없음
			//흰 돌이 없을 때까지 오른쪽 아래로 이동
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeftDown());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 왼쪽에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeft();//바로 왼쪽에는 놓을 수 없음
			//흰 돌이 없을 때까지 왼쪽으로 이동
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeft());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);

			//검은 돌을 현재위치의 왼쪽 위에 놓는 경우
			traversal.setPosition(currentRow,currentCol);
			oneBlankGoPosition.setPosition(currentRow,currentCol);
			oneBlankGoPosition.moveLeftUp();//바로 왼쪽 위에는 놓을 수 없음
			//흰 돌이 없을 때까지 왼쪽 위로 이동
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == Status.WHITE; traversal.moveLeftUp());
			//칸이 비어있고 놓을 수 있는 공간이면 push
			if (status[traversal.getRow()][traversal.getCol()]==Status.EMPTY && !oneBlankGoPosition.equals(traversal))
				pushElement(blackAvailableList,traversal);
		}
	}//refreshStatus()
	
	public Position positionGenerator()
	{
		//레벨에 따라 다르게 실행시킴
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

		//현재 차례인 돌의 놓을 수 있는 좌표값 받아옴
		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);

		int point;
		ArrayList <Integer> pointList = new ArrayList<Integer>();		//바뀌는 돌의 갯수
		ArrayList <Position> positionList = new ArrayList<Position>();	//돌이 놓일 수 있는 위치
		
		size = availableList.size();
		for (int i=0; i<size; i++)
		{
			point = 0;
			currentRow = availableList.get(i).getRow();
			currentCol = availableList.get(i).getCol();

			//위로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
					point++;
			//오른쪽 위로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
					point++;
			
			//오른쪽으로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
					point++;

			//오른쪽 위로 이동하면서 바뀌는 돌의 갯수 셈			
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
					point++;

			//아래로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
					point++;

			//왼쪽 아래로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
					point++;
			
			//왼쪽로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
					point++;
			
			//왼쪽 위로 이동하면서 바뀌는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
					point++;

			pointList.add(point);
			positionList.add( new Position(availableList.get(i).getRow(),availableList.get(i).getCol()) );
		}

		int min = Collections.min(pointList);	//돌의 갯수가 가장 적게 바뀌는 값

		ArrayList <Position> minPositionList = new ArrayList<Position>();

		//돌의 갯수가 가장 적게 바뀌는 위치를 찾아 리턴
		size = positionList.size();
		for (int i=0; i<size; i++)
			if (pointList.get(i) == min)
				minPositionList.add(positionList.get(i));

		return minPositionList;	
	}//minPositionListGenerator()
	
	private ArrayList <Position> maxPositionListGenerator()
	{
		//돌의 갯수가 가장 많이 바뀌는 위치 찾아냄
		
		int size;
		int currentRow;
		int currentCol;

		TraversingPosition traversal = new TraversingPosition();
		TraversingPosition selectedPosition = new TraversingPosition();

		ArrayList <Position> availableList = (currentTurn==Status.WHITE ? whiteAvailableList : blackAvailableList);

		int point;
		ArrayList <Integer> pointList = new ArrayList<Integer>();
		ArrayList <Position> positionList = new ArrayList<Position>();

		//뒤집을 수 있는 돌의 갯수 구함
		size = availableList.size();
		for (int i=0; i<size; i++)
		{
			point = 0;
			currentRow = availableList.get(i).getRow();
			currentCol = availableList.get(i).getCol();
			
			//위로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveUp(); !selectedPosition.equals(traversal); selectedPosition.moveUp())
					point++;

			//오른쪽 위로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightUp(); !selectedPosition.equals(traversal); selectedPosition.moveRightUp())
					point++;

			//오른쪽로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRight());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRight(); !selectedPosition.equals(traversal); selectedPosition.moveRight())
					point++;

			//오른쪽 아래로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveRightDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveRightDown(); !selectedPosition.equals(traversal); selectedPosition.moveRightDown())
					point++;

			//아래로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveDown(); !selectedPosition.equals(traversal); selectedPosition.moveDown())
					point++;

			//왼쪽 아래로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftDown());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftDown(); !selectedPosition.equals(traversal); selectedPosition.moveLeftDown())
					point++;

			//왼쪽으로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeft());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeft(); !selectedPosition.equals(traversal); selectedPosition.moveLeft())
					point++;
			
			//왼쪽 위로 움직이며 바뀔 수 있는 돌의 갯수 셈
			traversal.setPosition(currentRow,currentCol);
			selectedPosition.setPosition(currentRow,currentCol);
			for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.BLACK : Status.WHITE); traversal.moveLeftUp());
			if (status[traversal.getRow()][traversal.getCol()] == (currentTurn==Status.WHITE ? Status.WHITE : Status.BLACK))
				for (selectedPosition.moveLeftUp(); !selectedPosition.equals(traversal); selectedPosition.moveLeftUp())
					point++;

			pointList.add(point);
			positionList.add( new Position(availableList.get(i).getRow(),availableList.get(i).getCol()) );
		}

		int max = Collections.max(pointList);	//돌의 위치가 가장 많이 바뀌는 값

		ArrayList <Position> maxPositionList = new ArrayList<Position>();

		//돌의 위치가 가장 많이 바뀌는 위치 리턴
		size = positionList.size();
		for (int i=0; i<size; i++)
			if (pointList.get(i) == max)
				maxPositionList.add(positionList.get(i));

		return maxPositionList;	
	}//maxPositionListGenerator()
	
	private int minDistanceToBound(Position rPosition)
	{
		//BOUND로 부터 가장 거리가 가까운 것 리턴
		int currentRow = rPosition.getRow();
		int currentCol = rPosition.getCol();

		TraversingPosition traversal = new TraversingPosition();

		int distance;
		int minDistance;

		//위로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveUp(), distance++);
		minDistance = distance;

		//오른쪽 위로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRightUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRightUp(), distance++);
		if (distance < minDistance) { minDistance = distance; }
	
		//오른쪽로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRight(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRight(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//오른쪽 아래로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveRightDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveRightDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//아래로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//왼쪽 아래로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeftDown(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeftDown(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//왼쪽으로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeft(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeft(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		//왼쪽 위로 움직이며 Bound까지 거리 잼
		distance = 1;
		traversal.setPosition(currentRow,currentCol);
		for (traversal.moveLeftUp(); status[traversal.getRow()][traversal.getCol()] != Status.BOUND; traversal.moveLeftUp(), distance++);
		if (distance < minDistance) { minDistance = distance; }

		return minDistance;		
	}
	
	private void pushElement(ArrayList <Position> list, Position element)
	{
		//element 위치가 list에 없으면 추가
		int size = list.size();
		for (int i=0; i<size; i++)
			if (list.get(i).equals(element))
				return;

		list.add(new Position(element.getRow(),element.getCol()));
	}
}