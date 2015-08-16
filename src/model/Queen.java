package model;

import java.util.LinkedList;

public class Queen extends ChessPiece {
	/**
	 * constructor
	 * @param b
	 * @param color
	 */
	public Queen(ChessBoard b,int color)
	{
		this.color=color;
		if(color==0)//white queen
		{
			b.setPiece(this, 7,3);
			curx=7;cury=3;
		}
		else
		{
			b.setPiece(this, 0, 3);
			curx=0;cury=3;
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
		super.checkDirection(movePosition,1,0,b);
		super.checkDirection(movePosition,0,1,b);
		super.checkDirection(movePosition,-1,0,b);
		super.checkDirection(movePosition,0,-1,b);
		return movePosition;
	}
	
	
	

}
