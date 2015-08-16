package Network;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import controller.NetworkController;
public class Requester{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Requester(){}
    /**
     * run client
     * @param clientControl
     */
    void run(NetworkController clientControl)
    {
        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 2004);
            System.out.println("Connected to localhost in port 2004");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            
            LinkedList<Integer> intMessage=null;
           
            	
            //3: Communicating with the server
            do{
                try{
                		
                		System.out.println("client: before getting input stream");
                    message = (String)in.readObject();
                    System.out.println("client get>" + message);
//                    if(message.equals("I am sorry about that you lose"))
//                    {	
//                    		clientControl.loseMessage(message); 
//                    		break;
//                    	}
                    if(message.equals("restartRequest")||message.equals("inagreeRestart"))
                    {
                    		clientControl.popMessage(message);
                    		if (clientControl.getAgreeRestart()){
                    			clientControl.setAgreeRestart();
                    			sendMessage("agreeRestart");
                    		}
                    		else if (clientControl.getInAgreeRestart()){
                    			clientControl.setInAgreeRestart();
                    			sendMessage("inagreeRestart");
                    		}
                    		else getOutMessage(intMessage,clientControl);
                    }
                    else if(message.equals("agreeRestart")){
                    		clientControl.doRestart();
                    		getOutMessage(intMessage,clientControl);
                    }
                    else if(message.equals("forfeit")){
                    		clientControl.popMessage(message);
                    		if (clientControl.getRestart()){
                    			clientControl.setRestart();
                    			sendMessage("restartRequest");
                    		}
                    }
                    else if(message.equals("lose")){
                    		clientControl.popMessage(message);
                    		getOutMessage(intMessage,clientControl);
                    }
                    else if(message.equals("you can start now"))
                    		getOutMessage(intMessage,clientControl);
                    else{
                    		clientControl.updateMySide(castBack(message));
                    		clientControl.myMagic(0, 1,0);//unlock white pieces and check white king
                    		if (clientControl.getRestart()){
                    			clientControl.setRestart();
                    			sendMessage("restartRequest");
                    		}
                    		else if(clientControl.getForfeit()){
                    			clientControl.setForfeit();
                    			sendMessage("forfeit");
                    		}
                    		else if(clientControl.getIsWin()){
                    			clientControl.setIsWin();
                    			sendMessage("lose");
                    		}
                    		else getOutMessage(intMessage,clientControl);
                    }
                   
                   
                   
                   if(clientControl.checkEnd().equals("I am sorry about that you lose"))//send this message to client
                   	sendMessage("I am sorry about that you lose");
                }
                catch(ClassNotFoundException classNot){
                    System.err.println("data received in unknown format");
                }
            }while(true);
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                requestSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    /**
     * change int list to string
     * @param intMessage
     * @return
     */
    private String cast(LinkedList<Integer> intMessage)
    {
    	String myMessage="";
    	while(intMessage.size()!=0)
    		myMessage+=intMessage.pollFirst().toString();
    	return myMessage;
    }
    /**
     * change string to int list
     * @param myMessage
     * @return
     */
    private LinkedList<Integer> castBack(String myMessage)
    {
    	LinkedList<Integer> intMessage=new LinkedList<Integer>();
    	int length=myMessage.length();
    	for(int i=0;i<length;i++)
    		intMessage.add(Integer.parseInt(""+myMessage.charAt(i)));
    	return intMessage;
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
     * check message type
     * @param message
     * @return
     */
    private boolean checkMessage(String message)
    {
    	if(message.equals("you can restart")||message.equals("restart"))//the other want restart
    		return true;
    	else return false;
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
            System.out.println("client>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    /**
     * main
     * @param args
     */
    public static void main(String args[])
    {
        Requester client = new Requester();
        NetworkController clientControl=new NetworkController (0);
        while(true)
        client.run(clientControl);
    }
}
