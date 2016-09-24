import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Hemanth on 9/23/16.
 */
public class JokeClientAdmin {


    public static void main (String args[]){
        String serverName;
        int port;
        if (args.length<2) {
            serverName = "localhost";port = 5050;
            System.out.println("Admin Using Defaults");
        }
        else {serverName = args[0]; port = Integer.parseInt(args[1]);}
        System.out.println("Admin Client, 1.8.");
        System.out.println("Connecting to Server: " + serverName + ", Port: " + port);
        String state = "Not Known";
        Socket sock;
        try{
            String name;
            do{
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Please select option, type (quit) to exit");
                System.out.println("Last Known Server State: " + state);
                //
                printOptions("Toogle Server Status",0);
                printOptions("Server State Status", 1);
                printOptions("Shutdown Server",2);
                System.out.flush();
                name = in.readLine();
                //If not 'quit' send the message
                if (!name.toLowerCase().equals("quit")){
                    //System.out.println("Sending\n"+ name);
                    String check = sendMessage(name,serverName,port);
                    if (check != "False") {
                        //Get Server State every send message
                        state = currentServerState("1", serverName, port);
                    }else{
                        //Quit Program if unable to communicate with server
                        System.exit(21);
                    }

                }
            //If quit is found stop program
            }while (!name.toLowerCase().equals("quit"));
            System.out.println("Cancelled by user request.");
        }catch (IOException ioe){System.out.println("Unable to Connect to Server");}

    }
/*
    Receives String to send,ip address and port
    Returns String from server

 */
    static String sendMessage(String name , String serverName, int port){
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        //Default Response if we cannot connect to server
        String textFromServer = "No Reply";

        try{
            sock = new Socket (serverName,port);
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            toServer = new PrintStream(sock.getOutputStream());
            //name = name ;
            //Send name to server and then clear
            toServer.println(name); toServer.flush();
            //Reads 3 lines of data one at a time
            for (int i =1 ;i <=3; i++){
                textFromServer = fromServer.readLine();
                //break since we are return the string below
                if (textFromServer != null )break;
            }

            sock.close();

        }catch (IOException ioe){System.out.println("Unable to communicate with server");return "False";}
        return textFromServer;
    }
/*
   Pretty print of options
 */
    static void printOptions(String option, int optionNo){
        System.out.println("[" + optionNo +"] " + option );
    }
/*
    Abstracted sendMessage funciontality into currentServerState for more custom return String
 */
    static String currentServerState(String choice, String serverName, int port){
        try{
            return sendMessage(choice,serverName, port);
            //System.out.println("Received State " + hello );
            //return hello;
        }catch (Exception ioe){return "Not Known";}

    }
}

