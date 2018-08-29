package combine9_beta;
public class TraversingPosition extends Position
{
	public TraversingPosition()
	{
		super();	//부모드의 메소드나 필드값을 직접 부를때 사용
	}
	public TraversingPosition(int row, int col)
	{
		super(row,col);
	}
	
	public void moveUp()	{ row--; }
	public void moveDown()	{ row++; }
	public void moveLeft()	{ col--; }
	public void moveRight()	{ col++; }
	public void moveRightUp()	{ moveUp(); moveRight(); }
	public void moveRightDown()	{ moveDown(); moveRight(); }
	public void moveLeftUp()	{ moveUp(); moveLeft(); }
	public void moveLeftDown()	{ moveDown(); moveLeft(); }	
}