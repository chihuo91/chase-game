package model;

import java.util.LinkedList;
public class Player {
	private LinkedList<ChessPiece> Pieces=new LinkedList<ChessPiece>();
	private int color;
	private ChessPiece king,queen,rook1,rook2,knight1,knight2,bishop1,bishop2,p1,p2,p3,p4,p5,p6,p7,p8;
	private final static int ENEMY=1;
	private final static int FRIEND=0,OUTBOUND=0;
	private boolean kingIsChecked=false;
	/**
	 * set up pieces in chess board and put them in link list
	 * @param b
	 * @param color
	 */
	public Player(ChessBoard b,int color)
	{
		this.color=color;
		Pieces.add(king=new King(b,color));
		Pieces.add(queen=new Queen(b,color));
		Pieces.add(rook1=new Rook(b,color));
		Pieces.add(knight1=new Knight(b,color));
		Pieces.add(bishop1=new Bishop(b,color));
		Pieces.add(rook2=new Rook(b,color));
		Pieces.add(knight2=new Knight(b,color));
		Pieces.add(bishop2=new Bishop(b,color));Pieces.add(p2=new Pawn(b,color));
		Pieces.add(p1=new Pawn(b,color));Pieces.add(p3=new Pawn(b,color));Pieces.add(p4=new Pawn(b,color));
		Pieces.add(p5=new Pawn(b,color));Pieces.add(p6=new Pawn(b,color));Pieces.add(p7=new Pawn(b,color));Pieces.add(p8=new Pawn(b,color));
	}
	/**
	 * getter and setters
	 * @return
	 */
	public int getColor()
	{
		return this.color;
	}
	public boolean getKingIsChecked()
	{
		return kingIsChecked;
	}
	public void setKingIsChecked(boolean flag)
	{
		kingIsChecked=flag;
	}
	public void removePiece(ChessPiece p)
	{
		Pieces.remove(p);
	}
	
	public int getSize()
	{
		return Pieces.size();
	}
	public ChessPiece getPiece(int x)
	{
		return Pieces.get(x);
	}
	public boolean checkExist(ChessPiece p)
	{
		return Pieces.contains(p);
	}
	/**
	 * check king and return the only steps it can move
	 * @param b
	 * @param otherPlayer
	 * @return
	 */
	public LinkedList<Integer> checkKing(ChessBoard b,Player otherPlayer)
	{
		LinkedList<Integer> kingMove=king.move(b);
		if(kingMove.size()==0)
		{
			kingIsChecked=false;//king is not checked
			return kingMove;
		}
		int curx=king.getCurx();int cury=king.getCury();
		checkEnemyQBRP(curx, cury,1,1,b,kingMove);
		checkEnemyQBRP(curx, cury,-1,-1,b,kingMove);
		checkEnemyQBRP(curx, cury,-1,1,b,kingMove);
		checkEnemyQBRP(curx, cury,1,-1,b,kingMove);
		checkEnemyQBRP(curx, cury,0,1,b,kingMove);
		checkEnemyQBRP(curx, cury,1,0,b,kingMove);
		checkEnemyQBRP(curx, cury,0,-1,b,kingMove);
		checkEnemyQBRP(curx, cury,-1,0,b,kingMove);
		checkEnemyKnight(otherPlayer,kingMove,b);
		return kingMove;
	}
	/**
	 * check the enemy
	 * @param otherPlayer
	 * @param kingMove
	 * @param b
	 */
	
	private void checkEnemyKnight(Player otherPlayer,LinkedList<Integer> kingMove,ChessBoard b)
	{
		ChessPiece checkPiece;
		int size=otherPlayer.getSize();
		for(int i=0;i<size;i++)
		{
			checkPiece=otherPlayer.getPiece(i);
			if(checkPiece instanceof Knight)
				this.knightMovement(checkPiece,kingMove,b);
		}
	}
	/**
	 * check knight
	 * @param knight
	 * @param kingMove
	 * @param b
	 */
	private void knightMovement(ChessPiece knight, LinkedList<Integer> kingMove,ChessBoard b)
	{
		LinkedList<Integer> knightMove=knight.move(b);
		while(knightMove.size()!=0)
		{
			int kx=knightMove.pollFirst();
			int ky=knightMove.pollFirst();
			this.removeHelper(kx,ky, kingMove);
		}
	}
	/**
	 * check queen, bishop, rook and pawn
	 * @param curx
	 * @param cury
	 * @param dx
	 * @param dy
	 * @param b
	 * @param kingMove
	 */
	private void checkEnemyQBRP(int curx, int cury,int dx, int dy, ChessBoard b,LinkedList<Integer> kingMove)
	{
		int targetX=curx;int targetY=cury;
		int count=1;
		while(true)
		{
			targetX=targetX+dx;
			targetY=targetY+dy;
			int result=king.isMovable(targetX,targetY,b);
			if(result==ENEMY)
			{	
				//System.out.printf("color %d get enemy, targetX is %d, targetY is %d, count is %d\n",color,targetX,targetY,count);
				ChessPiece enemy=b.getPiece(targetX, targetY);
				checkEnemyType(targetX,targetY,dx,dy,count,enemy,kingMove);
				return;
			}
			else if(result==FRIEND||result==OUTBOUND)
				return;
			count++;
		}
	}
	/**
	 * check which enemy is checking king
	 * @param targetX
	 * @param targetY
	 * @param dx
	 * @param dy
	 * @param count
	 * @param p
	 * @param kingMove
	 */
	private void checkEnemyType(int targetX, int targetY,int dx, int dy,int count,ChessPiece p,LinkedList<Integer> kingMove)
	{
		if(count==1&&(p instanceof King)) reset(targetX,targetY,kingMove);//check king
		if(count==1&&(p instanceof Pawn)&&color==0&&(dx==-1&&(dy==1||dy==-1)))//white king may be checked by black pawn
			reset(targetX,targetY,kingMove);
		if(count==1&&(p instanceof Pawn)&&color==1&&(dx==1&&(dy==1||dy==-1)))//black king is checed by white pawn
			reset(targetX,targetY,kingMove);
		if(Math.abs(dx)==1&&Math.abs(dy)==1)//check diagionally, queen bishop
		{
			if(p instanceof Queen||p instanceof Bishop)
			{
				if(count==1)//enemy's queen and bishop is at the attack range of my king, so the king must eat it
					reset(targetX,targetY,kingMove);
				else//not in the attack range of my king, so I need to avoid to be eaten
				{System.out.println("get queen and bishop");
					removeHelper(targetX-((count-1)*dx),targetY-((count-1)*dy),kingMove);
					removeHelper(targetX-((count+1)*dx),targetY-((count+1)*dy),kingMove);
				}
			}
		}
		if(Math.abs(dx)==1&dy==0||dx==0&&Math.abs(dy)==1)//check straight, queen, rook
		{
			if(p instanceof Queen||p instanceof Rook)
			{
				if(count==1) reset(targetX,targetY,kingMove);
				else
				{System.out.println("get queen and rook");
					if(dx!=0)
					{
						removeHelper(targetX-((count-1)*dx),targetY,kingMove);
						removeHelper(targetX-((count+1)*dx),targetY,kingMove);
					}
					else//dy!=0
					{
						removeHelper(targetX,targetY-((count-1)*dy),kingMove);
						removeHelper(targetX,targetY-((count+1)*dy),kingMove);
					}
				}
			}
		}
	}
	/**
	 * king can only go to targetX and targetY,so we reset the moveable link list for king
	 * @param targetX
	 * @param targetY
	 * @param kingMove
	 * @return
	 */
	private void reset(int targetX, int targetY,LinkedList<Integer> kingMove)
	{
		kingMove.clear();
		kingMove.add(targetX);
		kingMove.add(targetY);
		kingIsChecked=true;
		System.out.println("get reset");
		
	}
	/**
	 * need to remove a pair of x and y together
	 * @param x
	 * @param y
	 * @param kingMove
	 */
	private void removeHelper(int x,int y, LinkedList<Integer> kingMove)
	{
		System.out.printf("get removehelper, x is %d y is %d,color is %d\n",x,y,color);
		kingIsChecked=true;
		int size=kingMove.size();
		for(int i=0;i<size-1;i+=2)
		{
			if(kingMove.get(i)==x&&kingMove.get(i+1)==y)
			{
				kingMove.remove(i);
				kingMove.remove(i);
				return;
			}
		}
	}
}
