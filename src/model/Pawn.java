package model;

import java.util.LinkedList;

public class Pawn extends ChessPiece{
	private int isFirst=0;
	private int initialX;
	private int initialY;
	/**
	 * constructor
	 * @param b
	 * @param color
	 */
	public Pawn (ChessBoard b,int color)
	{
		this.color=color;
		if(color==0)
		{
			for(int j=0;j<8;j++)
				if(b.getPiece(6, j)==null)
				{	
					b.setPiece(this, 6, j);
					initialX=6;initialY=j;
					curx=6;cury=j;
					return;
				}
		}
		else{
			for(int j=0;j<8;j++)
				if(b.getPiece(1, j)==null)
				{	
					b.setPiece(this, 1, j);
					initialX=1;initialY=j;
					curx=1;cury=j;
					return;
				}
		}
	}
	/**
	 * check whether it is first move 
	 */
	private void checkFirst()
	{
		if(initialX!=curx||initialY!=cury)
			isFirst=1;
	}
	/**
	 * move
	 */
	public LinkedList<Integer> move(ChessBoard b)
	{
		LinkedList<Integer> movePosition=new LinkedList<Integer>();
		checkFirst();
		if(isFirst==0)//the first time to move
		{
			if(this.color==0)
			{
				if(super.isMovable(curx-1,cury,b)==NOBODY)
				{
					addPosition(curx-1,cury, movePosition);
					if(super.isMovable(curx-2,cury,b)==NOBODY)
						addPosition(curx-2,cury, movePosition);
				}
				eatEnemy(movePosition,-1,-1,b);
				eatEnemy(movePosition,-1,1,b);
			}
			else
			{
				if(super.isMovable(curx+1,cury,b)==NOBODY)
				{
					addPosition(curx+1,cury, movePosition);
					if(super.isMovable(curx+2,cury,b)==NOBODY)
						addPosition(curx+2,cury, movePosition);
				}
				eatEnemy(movePosition,1,-1,b);
				eatEnemy(movePosition,1,1,b);
			}
		}
		else
		{
			if(this.color==0)
			{
				if(super.isMovable(curx-1,cury,b)==NOBODY)addPosition(curx-1,cury, movePosition);
				eatEnemy(movePosition,-1,-1,b);
				eatEnemy(movePosition,-1,1,b);
			}
			else
			{	if(super.isMovable(curx+1,cury,b)==NOBODY)addPosition(curx+1,cury, movePosition);
				
				eatEnemy(movePosition,1,-1,b);
				eatEnemy(movePosition,1,1,b);
			}
		}
		return movePosition;
	}
	/**
	 * whether there is an enemy in the attack range
	 * @param movePosition
	 * @param dx
	 * @param dy
	 * @param b
	 */
	private void eatEnemy(LinkedList<Integer> movePosition, int dx, int dy, ChessBoard b)
	{
		if(super.isMovable(curx+dx,cury+dy,b)==ENEMY)
		{
			addPosition(curx+dx,cury+dy, movePosition);
			
		}
	}
	/**
	 * helper function to build int list
	 * @param x
	 * @param y
	 * @param movePosition
	 */
	private void addPosition(int x,int y,LinkedList<Integer> movePosition)
	{
		movePosition.add(x);
		movePosition.add(y);
	}
}
