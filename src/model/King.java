package model;
import java.util.*;
public class King extends ChessPiece  {
	
	/**
	 * constructor
	 * @param b
	 * @param color
	 */
	public King(ChessBoard b,int color)
	{
		this.color=color;
		if(color==0)//white king
		{	
			b.setPiece(this, 7,4);
			curx=7;cury=4;
		}
		else
		{
			b.setPiece(this, 0, 4);
			curx=0;cury=4;
		}
	}
	
	 /**
	  * move
	  */
	public LinkedList<Integer> move(ChessBoard b)
	{
		LinkedList<Integer> movePosition=new LinkedList<Integer>();
		super.setLinkList(curx+1, cury+1,movePosition,b);
		super.setLinkList(curx+1, cury-1,movePosition,b);
		super.setLinkList(curx-1, cury+1,movePosition,b);
		super.setLinkList(curx-1, cury-1,movePosition,b);
		super.setLinkList(curx+1, cury,movePosition,b);
		super.setLinkList(curx, cury+1,movePosition,b);
		super.setLinkList(curx-1, cury,movePosition,b);
		super.setLinkList(curx, cury-1,movePosition,b);
		return movePosition;
	}
	
	

}
