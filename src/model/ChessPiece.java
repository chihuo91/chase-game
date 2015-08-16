package model;
import java.util.*;
public  class ChessPiece {
	
	protected final static int FRIEND=0,OUTBOUND=0, CANTMOVE=0;
	protected final static int ENEMY=1;
	protected final static int NOBODY=2;
	protected final static int BLACKWIN=3;
	protected final static int WHITEWIN=4;
	protected final static int UPDATED=5;
	protected int curx,cury;
	protected int color;
	private int enemyCounter=0;
	private boolean myTurn=true;
	
	/**
	 * getter and setters for protected instances
	 * @return
	 */
	public int getColor(){return color;}
	public boolean getMyTurn(){return myTurn;}
	public void setMyTurn(boolean myTurn){this.myTurn=myTurn;}
	public int getCurx() {return curx;}
	public void setCurx(int curx) {this.curx = curx;}
	public int getCury() {return cury;}
	public void setCury(int cury) {this.cury = cury;}


	/**
	 * check whether this piece out of bound
	 * @param otherX
	 * @param otherY
	 * @return
	 */
	protected boolean checkBound(int otherX, int otherY)
	{
		if(otherX<0||otherY<0||otherX>7||otherY>7)
			return false;
		else return true;
	}
	/**
	 * check whether there is something at location(otherX,otherY)
	 * @param otherX
	 * @param otherY
	 * @param b
	 * @return
	 */
	public int isMovable(int otherX,int otherY,ChessBoard b)
	{
		if(checkBound(otherX, otherY)==false)
		{//System.out.printf("from color %d, go out of bound, x is %d, y is %d\n",color, otherX,otherY);
			return OUTBOUND;
		}
		if(!myTurn)
			return CANTMOVE;
		return isEorF(otherX,otherY,b);
	}
	/**
	 * check whether other piece is enemy or friend
	 * @param otherX
	 * @param otherY
	 * @param b
	 * @return
	 */
	protected int isEorF(int otherX,int otherY, ChessBoard b)
	{
		ChessPiece otherPiece=b.getPiece(otherX,otherY);
		if(otherPiece==null)//nobody is there
			return NOBODY;
		int otherColor=otherPiece.getColor();
		if(otherColor==this.color)//this is friend,so I can't go there
		{
			//System.out.println("there is a friend at destination");
			return FRIEND;
		}
		else return ENEMY;
		
	}
	/**
	 * do move
	 * @param otherX
	 * @param otherY
	 * @param b
	 * @param otherPlayer
	 * @return
	 */
	public int doMove(int otherX, int otherY, ChessBoard b,Player otherPlayer)
	{
		ChessPiece target=doMoveHelper(otherX,otherY,b);
		if(target!=null)//this is an eat action
			return updatePlayer(otherPlayer,target);
		else return NOBODY;
	}
	/**
	 * 
	 * @param otherX
	 * @param otherY
	 * @param x
	 * @param y
	 * @param b
	 * @return
	 */
	public ChessPiece doMoveHelper(int otherX, int otherY,ChessBoard b)
	{
		ChessPiece target=b.getPiece(otherX, otherY);
		b.setPiece(null, curx, cury);
		b.setPiece(this, otherX, otherY);
		curx=otherX;cury=otherY;
		return target;
	}
	/**
	 * 
	 * @param player
	 * @param p
	 * @return
	 */
	public int updatePlayer(Player otherPlayer, ChessPiece p)
	{
		if(p instanceof King)//what we want to remove from player is King
		{
			if(otherPlayer.getColor()==0)//white King is being eaten
				return BLACKWIN;
			else return WHITEWIN;
		}
		
		//remove the eaten piece from player
		otherPlayer.removePiece(p);
		return UPDATED;

	}
	/**
	 * set up int list
	 * @param x
	 * @param y
	 * @param movePosition
	 * @param b
	 * @return
	 */
	protected int setLinkList(int x, int y,LinkedList<Integer> movePosition,ChessBoard b)
	{
		int isMove=isMovable(x,y,b);
		if(isMove==NOBODY||isMove==ENEMY)
		{
			movePosition.add(x);
			movePosition.add(y);
			
		}
		return isMove;
		
	}
	/**
	 * check direction and also set up int list
	 * @param movePosition
	 * @param dx
	 * @param dy
	 * @param b
	 */
	protected void checkDirection(LinkedList<Integer> movePosition,int dx, int dy, ChessBoard b)
	{
		
		int targetX=curx;int targetY=cury;
		while(true)
		{
			targetX=targetX+dx;
			targetY=targetY+dy;
			int result=setLinkList(targetX, targetY,movePosition,b);
			if(CANTMOVE==result||result==ENEMY)
				break;
		}
	}
	/**	
	 * extend in subclasses
	 * @param b
	 * @return
	 */
	public  LinkedList<Integer> move(ChessBoard b)
	{
		return new LinkedList<Integer>();
	}

}
