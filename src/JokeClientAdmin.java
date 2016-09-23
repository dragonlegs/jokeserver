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
            serverName = "localhost";port = 4545;
            System.out.println("Admin Connecting to defaults");
        }
        else {serverName = args[0]; port = Integer.parseInt(args[1]);}
        System.out.println("Admin Client, 1.8.");
        System.out.println("Connecting to Server: " + serverName + ", Port: " + port);

        Socket sock;
        try{
            String name;
            do{
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Connected to Admin Server, (quit) to end: ");
                System.out.println("Please select option");
                System.out.println("Current Server State: ");
                printOptions("Toogle Server Status",0);
                printOptions("Shutdown Server",1);
                System.out.flush();
                name = in.readLine();
                //If quit substring in name stop
                sendMessage(name,serverName,port);
                if (!name.toLowerCase().equals("quit")){
                    System.out.println("Sending\n"+ name);
                    //sendMessage("Shutdown",serverName,port);

                }
            }while (!name.toLowerCase().equals("quit"));
            System.out.println("Cancelled by user request.");
        }catch (IOException ioe){ioe.printStackTrace();}

    }

    static void sendMessage(String name , String serverName, int port){
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try{
            sock = new Socket (serverName,port);
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            toServer = new PrintStream(sock.getOutputStream());
            name = name ;
            //Send name to server and then clear
            toServer.println(name); toServer.flush();
            //Reads blocks of data from the server (up to 3 lines)
            for (int i =1 ;i <=3; i++){
                textFromServer = fromServer.readLine();
                if (textFromServer != null )System.out.println(textFromServer);
            }
            sock.close();

        }catch (IOException ioe){System.out.println("Socket Error");ioe.printStackTrace();}
    }

    static void printOptions(String option, int optionNo){
        System.out.println("[" + optionNo +"] " + option );
        optionNo++;
    }
}

