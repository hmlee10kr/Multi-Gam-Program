package combine9_beta;
public class Position		//바둑알의 위치(row, column)에 대한 관리
{
	protected int row;
	protected int col;

	public Position()
	{
		this(0,0);
	}//Position()
	public Position(int row, int col)
	{
		this.row = row;
		this.col = col;
	}//Position()
	
	//get-set
	public int getRow()	{ return row; }
	public int getCol()	{ return col; }
	public void setRow(int row)	{ this.row = row; }
	public void setCol(int col)	{ this.col = col; }
	
	public void setPosition(int row, int col)
	{
		this.row = row;
		this.col = col;
	}

	public boolean equals(Position rPosition)
	{
		if (row == rPosition.row && col == rPosition.col)
			return true;
		else
			return false;
	}//equals
}//position