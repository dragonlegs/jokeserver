import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by Hemanth on 9/10/2016.
 */
public class JokeClient {
    private static String uniqueID = UUID.randomUUID().toString();
    private static String user = "Default";
    private static int num =0;
    public static void main (String args[]){
        String serverName;
        int port;
        if (args.length<2) {
            serverName = "localhost";port = 4545;
            System.out.println("Not using 2 Commandline arguments for servername and port using defaults");
        }
        else {serverName = args[0]; port = Integer.parseInt(args[1]);}
        System.out.println("Hemanth Ande's inet Client, 1.8.");
        System.out.println("Using Server: " + serverName + ", Port: " + port);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Scanner user_input = new Scanner(System.in);
        System.out.print("Enter your name: ");
        user = user_input.next();
        System.out.println("Welcome "+user +"!");
        try{
            String name;
            do{
                System.out.println("Press Enter to receive Joke/proverb, (quit) to end: ");
                System.out.flush();
                name = in.readLine();
                //If quit substring in name stop
                if (!name.toLowerCase().equals("quit")){
                    //System.out.println("Sending\n"+ name);
                    sendMessage(uniqueID,serverName,port);
                    num++;

                }
                //After 5 excutes print for formating reasons (5 jokes and proverbs)
                if (num % 5 == 0){
                    System.out.println("=============================================");
                }
            }while (!name.toLowerCase().equals("quit"));
            System.out.println("Cancelled by user request.");
        }catch (IOException ioe){ioe.printStackTrace();}

    }

    /**
     * Deprecated not in use
     * Converts byte to string
     * @param ip byte array to be converted to string
     * @return result String Converted IP addres
     */
    static String toText(byte ip[]){
        StringBuffer  result = new StringBuffer();
        for (int i =0; i < ip.length; ++i){
            if (i > 0) result.append(".");
            result.append(0xff & ip[i]);
        }
        return result.toString();
    }

    /**
     * Sends input name to Server receives name address of input
     * @param name String name of website
     * @param serverName IP address of website
     */
    static void sendMessage(String name , String serverName, int port){
        Socket sock;
        BufferedReader fromServer;
        PrintStream toServer;
        String textFromServer;

        try{
            sock = new Socket (serverName,port);
            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            toServer = new PrintStream(sock.getOutputStream());
            name = name + ":" + user;
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
}
