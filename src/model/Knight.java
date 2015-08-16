package model;
import java.util.*;
public class Knight extends ChessPiece{
	
	/**
	 * constructor
	 * @param b
	 * @param color
	 */
	public Knight(ChessBoard b,int color)
	{
		this.color=color;
		if(color==0)//white knights
		{
			if(b.getPiece(7, 1)==null)
			{
				b.setPiece(this, 7,1);
				curx=7;cury=1;
			}
			else 
			{
				b.setPiece(this, 7,6);
				curx=7;cury=6;
			}
		}
		else
		{
			if(b.getPiece(0, 1)==null)
			{
				b.setPiece(this, 0, 1);curx=0;cury=1;
			}
			else 
			{
				b.setPiece(this, 0, 6);curx=0;cury=6;
			}
		}
	}
	/**
	 * move
	 */
	public LinkedList<Integer> move(ChessBoard b)
	{
		LinkedList<Integer> movePosition=new LinkedList<Integer>();
		super.setLinkList(curx-2, cury-1,movePosition,b);
		super.setLinkList(curx-2, cury+1,movePosition,b);
		super.setLinkList(curx-1, cury+2,movePosition,b);
		super.setLinkList(curx+1, cury+2,movePosition,b);
		super.setLinkList(curx+2, cury+1,movePosition,b);
		super.setLinkList(curx+2, cury-1,movePosition,b);
		super.setLinkList(curx+1, cury-2,movePosition,b);
		super.setLinkList(curx-1, cury-2,movePosition,b);
		return movePosition;
	}

}
