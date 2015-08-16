package view;

import model.Player;

import java.util.*;

import model.Pawn;
import model.ChessBoard;
import model.ChessPiece;
import model.King;
import model.Knight;
import model.Bishop;
import model.Queen;
import model.Rook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.*;
import javax.swing.text.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;

import controller.NetworkController;


/**
 * 
 * create a GUI object which contains a basic board and grid
 *
 */
public class NetworkGUI implements ActionListener {

	JPanel totalPanel,buttonPanel,chessPanel;
	JFrame window;
	JButton[][] cell;
	JMenuBar menuBar ;
	JMenu fileMenu, editMenu;
	JMenuItem quitMenuItem, restartItem,forfeitItem,undoMenuItem, redoMenuItem;
	JLabel scoreWhite, scoreBlack, whitePlayer, blackPlayer;
	int whitescore,blackscore;

	ChessBoard b;
	Player white, black;
	ChessPiece lastPiece=null;
	static int update=0,flip=0,blackRestart=0,whiteRestart=0;
	public static int isTarget=0;
	public static int curX,curY,targetX,targetY;
	protected final static int BLACKWIN=3;
	protected final static int WHITEWIN=4;
	protected final static int UPDATED=5;
	protected final static int NOBODY=2;
	int undoPointer=-1;
	LinkedList<Integer> myMessage;
	LinkedList <ChessPiece> trackPiece;
	LinkedList <LinkedList<Integer>> trackLocation;
	boolean isKingCheck=false, restartRequest=false, agreeRestart=false, inagreeRestart=false, Forfeit=false, isWin=false;
	int myColor;
	String lastMessage="",lost="",otherRequest="",myRequest="";
	/**
	 * GUI constructor,set up chess board and two player and also the GUI on window
	 * @param whitePlayer
	 * @param blackPlayer
	 * @param b
	 * @param color
	 */
	public NetworkGUI(Player whitePlayer,Player blackPlayer, ChessBoard b,int color)
	{
		myColor=color;
		this.b=b;
		white=whitePlayer;black=blackPlayer;
		trackPiece=new LinkedList<ChessPiece>();
		trackLocation=new LinkedList<LinkedList<Integer>>();
		initialPanel(this.b);
		initialMenu();
		initialWindow();
		myMessage=new LinkedList<Integer>();
		if(myColor==0)//only white can go 
		{
			able(1,0);
			JOptionPane.showInternalMessageDialog(totalPanel,"you first");
		}
		else 
		{
			able(0,0);
			able(1,0);
			JOptionPane.showInternalMessageDialog(totalPanel,"you can move only after white is done");
		}
	}

	/**
	 * getters
	 * @return
	 */
	public LinkedList<Integer> getMyMessage(){return myMessage;}
	public String getLastMessage(){return lastMessage;}

	public boolean getRestartRequest(){return restartRequest;}
	public void setRestartRequest(boolean value){restartRequest=value;}
	public boolean getAgreeRestart(){return agreeRestart;}
	public void setAgreeRestart(boolean value){agreeRestart=value;}
	public boolean getInAgreeRestart(){return inagreeRestart;}
	public void setInAgreeRestart(boolean value){inagreeRestart=value;}
	public boolean getForfeit(){return Forfeit;}
	public void setForfeit(boolean value){Forfeit=value;}
	public boolean getIsWin(){return isWin;}
	public void setIsWin(boolean value){isWin=value;}

	/**
	 * set the last message
	 * @param a
	 */
	public void setLost(String a){ 
		JOptionPane.showInternalMessageDialog(totalPanel,a);
	}
	/**
	 * clear message after it has been sent to the other user
	 */
	public void clearMyMessage()
	{
		myMessage.clear();
	}

	/**
	 * initial window
	 */
	private  void initialWindow()
	{
		window=new JFrame("Welcome to play the Chess!!");
		window.setDefaultLookAndFeelDecorated(true);
		window.setContentPane(totalPanel);
		window.setJMenuBar(menuBar);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	public void initialMenu(){
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");
		menuBar.add(fileMenu);
		menuBar.add(editMenu);

		// Create a "quit" menu item and add it to the file menu
		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		fileMenu.add(quitMenuItem);
		restartItem=new JMenuItem("Restart");
		restartItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showInternalMessageDialog(totalPanel,"Your restart request has been sent to your opponent.");
				restartRequest=true;

			}
		});
		editMenu.add(restartItem);
		forfeitItem=new JMenuItem("Forfeit");
		forfeitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				int n=JOptionPane.showConfirmDialog(totalPanel,"Do you really want to forfeit?","Forfeit",JOptionPane.YES_NO_OPTION);
				if (n==JOptionPane.YES_OPTION){
					Forfeit=true;
				}
			}

		});
		editMenu.add(forfeitItem);
		// Create our edit menu items
//		undoMenuItem = new JMenuItem("Undo");
//		redoMenuItem = new JMenuItem("Redo");
//		editMenu.add(undoMenuItem);
//		editMenu.add(redoMenuItem);

		// Set undo/redo to be disabled, initially
//		if (undoPointer<=0)
//			undoMenuItem.setEnabled(false);
//		else undoMenuItem.setEnabled(true);
//
//		if (undoPointer==(trackPiece.size()-1))
//			redoMenuItem.setEnabled(false);
//		else redoMenuItem.setEnabled(true);

		/*
		 * Create our action event listeners for undo/redo For these, we simply call undoManager's undo or redo command,
		 * then update the menu items' enabled state based on whether the undo manager says we can undo or redo at the
		 * present time
		 */
//		undoMenuItem.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent event)
//			{
//				if(undoPointer%2==1)
//					undoPointer--;
//				else
//					undoPointer-=2;
//				ChessPiece currPiece=trackPiece.get(undoPointer);
//				int lastX=currPiece.getCurx();int lastY=currPiece.getCury();
//				int x=trackLocation.get(undoPointer).getFirst();
//				int y=trackLocation.get(undoPointer).getLast();
//				currPiece.doMoveHelper(x, y, b);
//				setupMessage(myMessage,lastX,lastY,x,y);//send message to server or client
//				refresh(b);
//				if (undoPointer<=0)
//					undoMenuItem.setEnabled(false);
//				else
//					undoMenuItem.setEnabled(true);
//			}
//		});
//		redoMenuItem.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent event)
//			{
//				if(undoPointer%2==0)
//					undoPointer++;
//				else
//					undoPointer+=2;
//				ChessPiece currPiece=trackPiece.get(undoPointer);
//				int lastX=currPiece.getCurx();int lastY=currPiece.getCury();
//				int x=trackLocation.get(undoPointer).getFirst();
//				int y=trackLocation.get(undoPointer).getLast();
//				setupMessage(myMessage,lastX,lastY,x,y);//send message to server or client
//				currPiece.doMoveHelper(x, y, b);
//				refresh(b);
//				if (undoPointer==(trackPiece.size()-1))
//					redoMenuItem.setEnabled(false);
//				else redoMenuItem.setEnabled(true);
//			}
//		});
	}
	/**
	 * initial panel
	 * @param b
	 */

	private void initialPanel(ChessBoard b)
	{
		totalPanel=new JPanel();
		chessPanel=new JPanel();
		buttonPanel=new JPanel();
		chessPanel=new JPanel(new GridLayout(8,8));
		chessPanel.setPreferredSize(new Dimension(600,600));
		buttonPanel.setPreferredSize(new Dimension(200, 300));
		totalPanel.setPreferredSize(new Dimension(700, 900));
		initialChessPanel(b);
		initialButtonPanel();
		totalPanel.add(chessPanel,BorderLayout.NORTH);
		totalPanel.add(buttonPanel,BorderLayout.SOUTH);
		totalPanel.setOpaque(true);

	}
	/**
	 * create button and initialize them
	 * @param color
	 * @param size
	 * @return
	 */
	private JButton createButton(Color color,int size)
	{
		JButton cell=new JButton();
		cell.addActionListener(this);
		cell.setBackground(color);
		cell.setOpaque(true);
		cell.setBorderPainted(false);
		cell.setFont( new Font("Arial Unicode MS",Font.BOLD,30));

		cell.setPreferredSize(new Dimension(size, size));
		return cell;
	}
	/**
	 * initial chess board
	 * @param b
	 */
	private void initialChessPanel(ChessBoard b)
	{
		cell=new JButton[8][8];
		drawBoard(b);

	}
	/**
	 * drawboard
	 * @param b
	 */
	private  void drawBoard(ChessBoard b)
	{
		int color;
		for(int i=0;i<8;i++)
		{
			color=(i % 2==0)?0:1;
			for(int j=0;j<8;j++)
			{
				if(color==0)
				{
					if(j % 2 ==0)
						cell[i][j]=createButton(Color.white,60);/*white*/
					else
						cell[i][j]=createButton(Color.gray,60);//black
				}
				else
				{
					if(j % 2 !=0)
						cell[i][j]=createButton(Color.white,60);/*white*/
					else
						cell[i][j]=createButton(Color.gray,60);//black
				}
				ChessPiece chess=b.getPiece(i,j);
				if(chess!=null)
					setImage(chess,i,j);
				chessPanel.add(cell[i][j]);
			}
		}
	}
	/**
	 * draw pieces on board
	 * @param chess
	 * @param x
	 * @param y
	 */
	private void setImage(ChessPiece chess, int x, int y)
	{
		if(chess instanceof King)
		{
			if(chess.getColor()==0)//white king
				cell[x][y].setText(""+'\u2654');
			else cell[x][y].setText(""+'\u265A');
		}
		else if(chess instanceof Queen)
		{
			if(chess.getColor()==0)//white queen
				cell[x][y].setText(""+'\u2655');
			else cell[x][y].setText(""+'\u265B');
		}
		else if(chess instanceof Rook)
		{
			if(chess.getColor()==0)//white rook
				cell[x][y].setText(""+'\u2656');
			else cell[x][y].setText(""+'\u265C');
		}
		else if(chess instanceof Knight)
		{
			if(chess.getColor()==0)//white knight
				cell[x][y].setText(""+'\u2658');
			else cell[x][y].setText(""+'\u265E');
		}
		else if(chess instanceof Bishop)
		{
			if(chess.getColor()==0)//white bishop
				cell[x][y].setText(""+'\u2657');
			else cell[x][y].setText(""+'\u265D');
		}
		else if(chess instanceof Pawn)
		{
			if(chess.getColor()==0)//white pawn
				cell[x][y].setText(""+'\u2659');
			else cell[x][y].setText(""+'\u265F');
		}
	}
	/**
	 * initial button panel
	 */
	private void initialButtonPanel()
	{
		if(myColor==0){
			whitePlayer=new JLabel("White Player");
			whitePlayer.setPreferredSize(new Dimension(80,50));
			whitePlayer.setHorizontalAlignment(0);
			buttonPanel.add(whitePlayer);
		}
		//		scoreWhite=new JLabel("0");
		//		scoreWhite.setPreferredSize(new Dimension(90,70));
		//		scoreWhite.setHorizontalAlignment(0);
		//		buttonPanel.add(scoreWhite);
		else{
			blackPlayer=new JLabel("Black Player");
			blackPlayer.setPreferredSize(new Dimension(80,50));

			blackPlayer.setHorizontalAlignment(0);
			buttonPanel.add(blackPlayer);
		}
		//		scoreBlack=new JLabel("0");
		//		scoreBlack.setPreferredSize(new Dimension(90,70));
		//
		//		scoreBlack.setHorizontalAlignment(0);
		//		buttonPanel.add(scoreBlack);

	}
	/**
	 * when do some change in model, refresh GUI
	 * @param b
	 */
	private void refresh(ChessBoard b)
	{
		initialPanel(b);
		initialMenu();
		window.setDefaultLookAndFeelDecorated(true);
		window.setContentPane(totalPanel);
		window.setJMenuBar(menuBar);

		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}


	/**
	 * lock or unlock chess pieces
	 * @param color
	 * @param flag
	 */
	public void able(int color,int flag)//flag=0 lock,flag=1 unlock
	{
		for(int i=0;i<8;i++)
		{	
			for(int j=0;j<8;j++)
			{	
				ChessPiece temp=b.getPiece(i, j);
				if(temp!=null&&temp.getColor()==color)
				{
					if(flag==0)temp.setMyTurn(false);
					else temp.setMyTurn(true);
				}
			}
		}

	}
	public void doRestart(){
		this.b=new ChessBoard(8,8);
		this.white=new Player(this.b,0);
		this.black=new Player(this.b,1);
		trackPiece=new LinkedList<ChessPiece>();
		trackLocation=new LinkedList<LinkedList<Integer>>();
		undoPointer=-1;
		refresh(this.b);
		if(myColor==0)//only white can go 
		{
			able(1,0);
			JOptionPane.showInternalMessageDialog(totalPanel,"you first");
		}
		else 
		{
			able(0,0);
			able(1,0);
			JOptionPane.showInternalMessageDialog(totalPanel,"you can move only after white player is done");

		}

	}
	/**
	 * check button message type
	 * @param message
	 */
	public void popMessage(String message)
	{
		if (message.equals("restartRequest")){
			int n=JOptionPane.showConfirmDialog(totalPanel,"your opponent wants to restart the game. Do you agree?","Restart Request",JOptionPane.YES_NO_OPTION);
			if (n==JOptionPane.NO_OPTION)
				inagreeRestart=true;
			else{ 
				agreeRestart=true;
				doRestart();
			}
		}
		if (message.equals("inagreeRestart"))
			JOptionPane.showInternalMessageDialog(totalPanel,"Your oppontent disagree to restart.");
		if(message.equals("forfeit")){
			int n=JOptionPane.showConfirmDialog(totalPanel,"Congratulation! your opponent forfeite. Do you want to restart?","Restart Request",JOptionPane.YES_NO_OPTION);
			if (n==JOptionPane.YES_OPTION)
				restartRequest=true;
		}
		if(message.equals("agreeRestart")){
			JOptionPane.showInternalMessageDialog(totalPanel,"Your oppontent agree to restart.");
			doRestart();
		}
		if(message.equals("lose")){
			JOptionPane.showInternalMessageDialog(totalPanel,"Sorry, you lose the game.");
			doRestart();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton selected=(JButton)e.getSource();

		int x=0,y=0;
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++)
				if(selected==cell[i][j])
				{	x=i;y=j;}
		//get chess piece from model
		ChessPiece myPiece=b.getPiece(x, y);
		if(myPiece==null&&lastPiece==null) return;//doesn't choose any piece to move
		if(myPiece!=null&&lastPiece==null)//choose myPiece to move
		{
			selectPiece( b, myPiece, x,y);
			return;
		}
		if(lastPiece!=null)
		{
			if(isKingCheck==true)
			{
				if((cell[x][y].getBackground().equals(Color.green))==false)
				{	
					JOptionPane.showInternalMessageDialog(totalPanel,"you must choose green squares, otherwise you are dead!!!");
					return;
				}
				moveTo(x,y);
				isKingCheck=false;
				return;
			}
			if(myPiece==null||myPiece.getColor()!=lastPiece.getColor())//eat enemy or go to empty place
			{
				if((cell[x][y].getBackground().equals(Color.green))==false)
				{	
					JOptionPane.showInternalMessageDialog(totalPanel,"Sorry, you can only choose the green squares to move");
					return;
				}
				moveTo(x,y);
			}
			else //I want to choose another piece to move
			{
				refresh(b);
				selectPiece( b, myPiece, x,y);
			}
		}
	}
		/**
	 * make the piece move to some place and also check the final state of game
	 * @param x
	 * @param y
	 */
	public void moveTo(int x,int y)
	{
//		if( undoPointer!=(trackPiece.size()-1))
//		{
//			if(undoPointer%2==0)
//				undoPointer--;
//			while((trackPiece.size()-1)!=undoPointer){
//				trackPiece.remove(undoPointer+1);
//				trackLocation.remove(undoPointer+1);
//			}
//		}
//		undoMenuItem.setEnabled(true);
//		redoMenuItem.setEnabled(true);
		int lastX=lastPiece.getCurx();int lastY=lastPiece.getCury();
		//remember the old location
//		trackPiece.add(lastPiece);
//		LinkedList <Integer> previousLocation=new LinkedList<Integer>();
//		previousLocation.add(lastX);previousLocation.add(lastY);
//		trackLocation.add(previousLocation);
		//remember the current location
//		trackPiece.add(lastPiece);
//		LinkedList<Integer> currentLocation=new LinkedList<Integer>();
//		currentLocation.add(x);currentLocation.add(y);
//		trackLocation.add(currentLocation);
//		undoPointer+=2;
		
		int check,color=lastPiece.getColor();
		if(color==0)
		{
			check=lastPiece.doMove(x, y, b, black);//after do move, it's black's turn.so lock all white
			able(0,0);//lock white part and show message
		}
		else 
		{
			check=lastPiece.doMove(x,y,b,white);
			able(1,0);
		}

		setupMessage(myMessage,lastX,lastY,x,y);//send message to server or client
		lastPiece=null;
		refresh(b);
		if (check!=UPDATED&&check!=NOBODY){
			JOptionPane.showInternalMessageDialog(totalPanel,"my dear, you win!! Congratulation!!");
			isWin=true;
		}
	}

	/**
	 * after get message from the other player, update myself GUI and model
	 * @param myMessage
	 */
	public void updateMySide(LinkedList<Integer> myMessage)
	{
		//update model
		int curx=myMessage.pollFirst();
		int cury=myMessage.pollFirst();
		ChessPiece target=b.getPiece(curx, cury);
		if(target.getColor()==0)
			target.doMove(myMessage.pollFirst(),myMessage.pollFirst(), b, black);
		else
			target.doMove(myMessage.pollFirst(),myMessage.pollFirst(), b,white);
		//update GUI
		refresh(b);
	}
	/**
	 * setup the message I want to send to other player
	 * @param myMessage
	 * @param curx
	 * @param cury
	 * @param targetX
	 * @param targetY
	 */
	private void setupMessage(LinkedList<Integer>myMessage, int curx, int cury, int targetX, int targetY)
	{

		myMessage.add(curx);
		myMessage.add(cury);
		myMessage.add(targetX);
		myMessage.add(targetY);
	}
	/**
	 * select the pieces I wanna move
	 * @param b
	 * @param myPiece
	 * @param x
	 * @param y
	 */
	private void selectPiece(ChessBoard b, ChessPiece myPiece, int x,int y)
	{
		if(!myPiece.getMyTurn())
		{
			JOptionPane.showInternalMessageDialog(totalPanel,"You can't cheat!!");
			return;
		}
		LinkedList<Integer> myMove=myPiece.move(b);
		if(myMove.size()==0)//this piece has nowhere to go
		{
			JOptionPane.showInternalMessageDialog(totalPanel,"Sorry, this piece has no where to go");
			return;
		}
		cell[x][y].setBackground(Color.YELLOW);
		while(myMove.size()!=0)//color the legal movement
		{
			cell[myMove.pollFirst()][myMove.pollFirst()].setBackground(Color.green);
		}
		lastPiece=myPiece;
	}
	/**
	 * check king function
	 * @param color
	 */
	public void checkKing(int color)//check king with this color
	{
		LinkedList<Integer> kingMove=checkKingHelper(color);
		Player player;
		player=color==0?white:black;
		if(kingMove==null)
			return;
		else
		{
			ChessPiece king=player.getPiece(0);
			cell[king.getCurx()][king.getCury()].setBackground(Color.YELLOW);

			while(kingMove.size()!=0)//color the legal movement
				cell[kingMove.pollFirst()][kingMove.pollFirst()].setBackground(Color.green);
			lastPiece=king;
			isKingCheck=true;
		}

	}
	/**
	 * check king helper function
	 * @param color
	 * @return
	 */
	private LinkedList<Integer> checkKingHelper(int color)//
	{
		boolean check;LinkedList<Integer> kingMove;
		if(color==0)//check white king
		{
			kingMove=white.checkKing(b,black);
			if(white.getKingIsChecked()==false)
				return null;
			else//king is being checked
			{
				white.setKingIsChecked(false);
				if(kingMove.size()==0)
				{	
					JOptionPane.showInternalMessageDialog(totalPanel,"Ops!! there is no way for your king to escaping from death. I am sorry");
					return null;
				}
				else return kingMove;
			}
		}
		else//black go
		{
			kingMove=black.checkKing(b,white);
			if(black.getKingIsChecked()==false)
				return null;
			else//king is being checked
			{
				black.setKingIsChecked(false);
				if(kingMove.size()==0)
				{	
					JOptionPane.showInternalMessageDialog(totalPanel,"Ops!! there is no way for your king to escaping from death. I am sorry");
					return null;
				}
				else return kingMove;
			}
		}
	}
	
}