package model;

import java.util.LinkedList;

/**
 * bishop class
 * @author shuxin
 *
 */
public class Bishop extends ChessPiece {

	/**
	 * constuctor
	 * @param b
	 * @param color
	 */
	public Bishop(ChessBoard b, int color)
	{
		this.color=color;
		if(color==0)
		{
			if(b.getPiece(7, 2)==null)
				{b.setPiece(this, 7, 2);curx=7;cury=2;}
			else{ b.setPiece(this, 7, 5);curx=7;cury=5;}
		}
		else
		{
			if(b.getPiece(0, 2)==null)
				{b.setPiece(this, 0, 2);curx=0;cury=2;}
			else{ b.setPiece(this, 0, 5);curx=0;cury=5;}
		}
	}
	/**
	  * move
	  */
	public LinkedList<Integer> move(ChessBoard b)
	{
		LinkedList<Integer> movePosition=new LinkedList<Integer>();
		super.checkDirection(movePosition,1,1,b);
		super.checkDirection(movePosition,-1,-1,b);
		super.checkDirection(movePosition,-1,1,b);
		super.checkDirection(movePosition,1,-1,b);
		return movePosition;
	}
}
