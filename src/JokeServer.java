import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class JokeServer {

    private boolean Joke = true;
    static private HashMap<String, Integer> uidList = new HashMap<String, Integer>();
    static private HashMap<String, Integer> uidCycle = new HashMap<String, Integer>();
    private static Status status = new Status();

    //private static Boolean status = true;
    private static Boolean secondary = false;
    private static int q_len = 6;
    private static int port = 4545;
    private static int portAdmin = 5050;


    public static void main(String[] args) {
        //System.out.println(args[0]);
        System.out.println("Starting Joke Server");
//        int q_len;
//        int port;
//        int portAdmin;
        //Check if two commandland arguments for custom queue and port
        status.start();
        if (args.length > 0) {
            if (setSecondary(args)) {
                System.out.println("Secondary Server Mode");
            } else {
                try {
                    q_len = Integer.parseInt(args[0]);
                    port = Integer.parseInt(args[1]);
                    portAdmin = Integer.parseInt(args[2]);
                } catch (NumberFormatException ioe) {
                    System.out.println("Arguments are not numbers");
                    System.exit(2);
                }
            }
        }else{
            System.out.println("Using Default Values");
        }
        //Setup status Thread

        //Setup Admin Connection Thread pass status and port
        AdminServer admin = new AdminServer(status, portAdmin);
        admin.start();

        try {
            Socket sock;

            ServerSocket servsock = new ServerSocket(port, q_len);
            System.out.println("Hemanth Ande Joke Server is running on port " + port);
            while (true) {
                try {
                    sock = servsock.accept();
                    //sock2 = adminsock.accept();
                    System.out.println("Current Joke Staus: " + status.toString());
                    new Worker(sock, uidList, status, uidCycle,secondary).start();
                } catch (IOException ioe) {
                    System.out.println("Unable to start server");
                }
            }
        } catch (IOException ioe) {
            System.out.println("Unable to start server");
            System.out.println(ioe.toString());
        }
    }

    //Deprecated Used for preliminary testing only
//    public static void setJoke() {
//        //System.out.println("Current Joke status: "+status.toString());
//        try {
//            status = !status;
//        } catch (Exception ioe) {
//            System.out.println(ioe);
//        }
//        System.out.println("Changed Joke status: " + status.toString());
//    }

    //Deprecated Used for preliminary testing only
//    public static Boolean getJoke() {
//        return status;
//    }

    public static boolean setSecondary(String args[]) {
        if (args[0].toLowerCase().equals("secondary")) {
            int Secport = 4546;
            portAdmin = 5051;
            secondary = true;
            secondaryServer secondServer = new secondaryServer(uidList, status, uidCycle,secondary,Secport);
            secondServer.start();
            return true;
        }else {return false;}
    }
}
