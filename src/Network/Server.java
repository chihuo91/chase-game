package Network;
import java.io.*;
import java.net.*;

import controller.NetworkController;

import java.util.*;
public class Server{
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
   
    Server(){}
    /**
     * run server
     * @param serverControl
     */
    void run(NetworkController serverControl)
    {
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket(2004, 10);
            //2. Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            System.out.println("Connection successful");
            sendMessage("you can start now");
            LinkedList<Integer> intMessage=null;
           
            //4. The two parts communicate via the input and output streams
            do{
                try{
                		System.out.println("server: before getting input stream");
                    message = (String)in.readObject();
                    System.out.println("server get>" + message);

                    if(message.equals("restartRequest")||message.equals("inagreeRestart")){
                    		
                    	 	serverControl.popMessage(message);
                    	 	if (serverControl.getAgreeRestart()){
                        		serverControl.setAgreeRestart();
                        		sendMessage("agreeRestart");
                        	}
                        	else if (serverControl.getInAgreeRestart()){
                        		serverControl.setInAgreeRestart();
                        		sendMessage("inagreeRestart");
                        	}
                        	else getOutMessage(intMessage,serverControl);
                    }
                    else if (message.equals("agreeRestart")){
                    		serverControl.doRestart();
                    		sendMessage("you can start now");
                    }
                    else if(message.equals("forfeit")){
                    		serverControl.popMessage(message);
                    		if (serverControl.getRestart()){
                    			serverControl.setRestart();
                    			sendMessage("restartRequest");
                    		}
                    }
                    else if(message.equals("lose")){
                    		serverControl.popMessage(message);
                    		sendMessage("you can start now");
                    }
                    else{
                    		serverControl.updateMySide(castBack(message));
                    		//unlock yourself
                    		serverControl.myMagic(1, 1,1);//unlock black pieces and also check black king
                    		if (serverControl.getRestart()){
                    			serverControl.setRestart();
                    			sendMessage("restartRequest");
                    		}
                    		else if(serverControl.getForfeit()){
                    			serverControl.setForfeit();
                    			sendMessage("forfeit");
                    		}
                    		else if(serverControl.getIsWin()){
                    			serverControl.setIsWin();
                    			sendMessage("lose");
                    		}
                    		else    getOutMessage(intMessage,serverControl);
                   
                    }
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(true);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    /**
     * send message
     * @param msg
     */
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    /**
     * 
     * @param intMessage
     * @param clientControl
     */
    private void getOutMessage(LinkedList<Integer> intMessage,NetworkController clientControl)
    {
    	 while(true)
         {
         	intMessage=clientControl.getMessage();
         	if(intMessage.size()!=0)
         		break;
         }
    	
    		 sendMessage(cast(intMessage));
    		 clientControl.clearMessage();
    	 
    }
    /**
     * cast int list to string
     * @param intMessage
     * @return
     */
	
	public String cast(LinkedList<Integer> intMessage)
	{
		String myMessage="";
		while(intMessage.size()!=0)
			myMessage+=intMessage.pollFirst().toString();
		return myMessage;
	}
	/**
	 * cast string back to int list
	 * @param myMessage
	 * @return
	 */
	public LinkedList<Integer> castBack(String myMessage)
	{
		LinkedList<Integer> intMessage=new LinkedList<Integer>();
		int length=myMessage.length();
		for(int i=0;i<length;i++)
			intMessage.add(Integer.parseInt(""+myMessage.charAt(i)));
		return intMessage;
	}
	/**
	 * check message type
	 * @param message
	 * @return
	 */
    public boolean checkMessage(String message)
    {
    	if(message.equals("you can restart")||message.equals("restartRequest"))//the other want restart
    		return true;
    	else return false;
    }
    /**
     * main function
     * @param args
     */
    
    public static void main(String args[])
    {
        Server server = new Server();
        NetworkController serverControl=new NetworkController (1);
        while(true){
            server.run(serverControl);
        }
    }
}
