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
    private static String ip1;
    private static String ip2;
    private static String serverName = "localhost";
    private static int port = 5050;

    public static void main (String args[]){
        if (args.length>0) {
            if (checkArgs(args)){
                System.out.println("Found Custom Args");
            }else {
                serverName = "localhost";
                port = 5050;
                System.out.println("Admin Using Defaults");
            }
        }
        System.out.println("Admin Client, 1.8.");
        System.out.println("Connecting to Server: " + serverName + ", Port: " + port);
        String state = "Not Known";
        setIP(args);
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
                printOptions("Change Server Connection",3);
                System.out.println("Please Enter Selection: ");
                System.out.flush();
                name = in.readLine();
                //If not 'quit' send the message
                if (name.toLowerCase().equals("3")){
                    if (!ip1.equals(null) || !ip2.equals(null)){
                    if (serverName.equals(ip1)){
                        serverName = ip1;
                        port = 5050;
                    }else {
                        serverName = ip2;
                        port = 5151;
                    }
                    }else{
                        System.out.println("Unable to Change no second ip");
                    }

                    System.out.println("Changing  to " + serverName);
                }
                if (!name.toLowerCase().equals("quit")){
                    //System.out.println("Sending\n"+ name);
                    String check = sendMessage(name,serverName,port);
                    if (!check.equals("False")) {
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
/**
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
/**
   Pretty print of options
 */
    static void printOptions(String option, int optionNo){
        System.out.println("[" + optionNo +"] " + option );
    }
/**
    Abstracted sendMessage functionality into currentServerState for more custom return String
 */
    static String currentServerState(String choice, String serverName, int port){
        try{
            return sendMessage(choice,serverName, port);
            //System.out.println("Received State " + hello );
            //return hello;
        }catch (Exception ioe){return "Not Known";}

    }

    /**
     * Checks args to make correct number if found
     * @param args
     * @return
     */
    public static boolean checkArgs(String args[]){
        if (args.length == 2){
            ip1 = args[0];
            ip2 = args[1];
            System.out.println("Found multiple servers");
            selectIP();
        }else if (args.length ==1){
            ip1 = args[0];
        }else{
            System.out.println("Max Args is two");
            return false;
        }
        return true;

    }

    /**
     * Allows user to select IP to connect to
     */
    public static void selectIP(){
        System.out.println("Select Server to connect to");
        System.out.println("[0] " + ip1);
        System.out.println("[1] " + ip2);
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String enter = in.readLine();
            switch (enter){
                case "0":
                    serverName = ip1;
                    port = 5050;
                    break;
                case "1":
                    serverName = ip2;
                    port = 5051;
                    break;
                default:
                    System.out.println("Option not valid");
                    selectIP();
            }

        }catch (IOException ioe){System.out.println("Unable to read input");}

    }

    /**
     * Sets custom or mutiple ips for server
     * @param args
     */
    public static void setIP(String args[]){
        if (args.length == 2){
            System.out.println("Reading Commandline Arguments");
            ip1 = args[0];
            ip2 = args[1];
        }else if (args.length == 1){
            System.out.println("Found One Commandline Arg");
            serverName = args[0];
        }
        else{
            System.out.println("Unable TO Find two Commandline Args");
        }

    }
}

