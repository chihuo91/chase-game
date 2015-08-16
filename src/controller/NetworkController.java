package controller;

import java.util.LinkedList;

import model.ChessBoard;
import model.Player;
import view.NetworkGUI;

public class NetworkController {

	ChessBoard b;
	Player whitePlayer,blackPlayer;
	NetworkGUI GUI;
	
	/**
	 * get my message
	 * @return
	 */
	public LinkedList <Integer> getMessage()
	{
		LinkedList<Integer> intMessage=GUI.getMyMessage();
		for(int i=0;i<intMessage.size();i++)
			System.out.printf("%d ", intMessage.get(i));
		return intMessage;
	}
	/**
	 * clear my message
	 */
	public void clearMessage()
	{
		GUI.clearMyMessage();
	}
	/**
	 * update my status
	 * @param myMessage
	 */
	public void updateMySide(LinkedList<Integer> myMessage)
	{
		GUI.updateMySide(myMessage);
	}
	
	/**
	 * unlock or lock the color of pieces and also check king
	 * @param color
	 * @param flag
	 * @param myKing
	 */
	public void myMagic(int color, int flag,int myKing)
	{
			GUI.able(color,flag);
			GUI.checkKing(myKing);

	
	}
	public void setRestart()
	{
		GUI.setRestartRequest(false);
	}
	
	public boolean getRestart(){
		return GUI.getRestartRequest();
	}
	public void popMessage(String message)
	{
		GUI.popMessage(message);
	}
	public boolean getAgreeRestart(){
		return GUI.getAgreeRestart();
	}
	public void setAgreeRestart()
	{
		GUI.setAgreeRestart(false);
	}
	public void doRestart(){
		GUI.doRestart();
	}
	public boolean getInAgreeRestart(){
		return GUI.getInAgreeRestart();
	}
	public void setInAgreeRestart()
	{
		GUI.setInAgreeRestart(false);
	}
	public boolean getForfeit(){
		return GUI.getForfeit();
	}
	public void setForfeit()
	{
		GUI.setForfeit(false);
	}
	public boolean getIsWin(){
		return GUI.getIsWin();
	}
	public void setIsWin()
	{
		GUI.setIsWin(false);
	}
	/**
	 * create lose message
	 * @param a
	 */
	public void loseMessage(String a){ GUI.setLost(a);}
	
	/**
	 * check end message
	 * @return
	 */
	public String checkEnd()
	{
		return GUI.getLastMessage();
	}
	/**
	 * create controller 
	 * @param color
	 */
	public NetworkController(int color)
	{
		b=new ChessBoard(8,8);
		whitePlayer=new Player(b,0);
		blackPlayer=new Player(b,1);
		GUI=new NetworkGUI(whitePlayer, blackPlayer,b,color);
		
	}


}
