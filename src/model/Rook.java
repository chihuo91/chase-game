package model;

import java.util.LinkedList;

public class Rook extends ChessPiece {

	/**
	 * constructor
	 * @param b
	 * @param color
	 */
	public Rook(ChessBoard b, int color)
	{
		this.color=color;
		if(color==0)
		{
			if(b.getPiece(7, 0)==null)
			{	b.setPiece(this, 7, 0);curx=7;cury=0;}
			else{ b.setPiece(this, 7, 7);curx=7;cury=7;}
		}
		else
		{
			if(b.getPiece(0, 0)==null)
				{b.setPiece(this, 0, 0);curx=0;cury=0;}
			else{ b.setPiece(this, 0, 7);curx=0;cury=7;}
		}
	}
	/**
	  * move
	  */
	public LinkedList<Integer> move(ChessBoard b)
	{
		LinkedList<Integer> movePosition=new LinkedList<Integer>();
		super.checkDirection(movePosition,1,0,b);
		super.checkDirection(movePosition,0,1,b);
		super.checkDirection(movePosition,-1,0,b);
		super.checkDirection(movePosition,0,-1,b);
		return movePosition;
	}
	
	
}
