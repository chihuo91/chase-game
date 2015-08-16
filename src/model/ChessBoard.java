package model;
import java.util.*;
public class ChessBoard {
	private  ChessPiece [][]chessBoard;
	protected final static int FRIEND=0,OUTBOUND=0, CANTMOVE=0;
	protected final static int ENEMY=1;
	protected final static int NOBODY=2;
	/**
	 * chess board constructor
	 * @param width
	 * @param height
	 */
	public ChessBoard(int width, int height)
	{
		chessBoard=new ChessPiece[width][height];
		
		for(int i=0;i<width;i++)
			for(int j=0;j<height;j++)
				chessBoard[i][j]=null;
			
	}
	/**
	 * setter
	 * @param p
	 * @param x
	 * @param y
	 */
	public void setPiece(ChessPiece p, int x, int y)
	{
		chessBoard[x][y]=p;
	}

	/**
	 * getter
	 * @param x
	 * @param y
	 * @return
	 */
	public  ChessPiece getPiece(int x, int y)
	{
		return chessBoard[x][y];
	}

}	
	