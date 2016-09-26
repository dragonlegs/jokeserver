import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Hemanth on 9/25/16.
 */
public class secondaryServer extends Thread {
    private boolean Joke = true;
    static private HashMap<String, Integer> uidList = new HashMap<String, Integer>();
    static private HashMap<String, Integer> uidCycle = new HashMap<String, Integer>();
    private static Status status ;
    private static Boolean secondary = false;
    private static int port;
    public secondaryServer(HashMap<String, Integer>uidList, Status status, HashMap<String,Integer>uidCycle, Boolean secondary,int port){
        this.uidCycle = uidCycle;
        this.uidList = uidList;
        this.status = status;
        this.secondary = secondary;
        this.port = port;
    }

    @Override
    public void run(){
        //System.out.println("Secondary Server Started");
        try {
            Socket sock;
            int q_len = 6;
            ServerSocket servsock = new ServerSocket(port, q_len);
            System.out.println("Hemanth Ande Second Joke Server is running on port " + port);
            while (true) {
                try {
                    sock = servsock.accept();
                    System.out.println("received Connection");
                    //sock2 = adminsock.accept();
                    //System.out.println("Current Joke Staus: " + status.toString());
                    new Worker(sock, uidList, status, uidCycle,secondary).start();
                } catch (IOException ioe) {
                    System.out.println("Unable to start server");
                }
            }
        } catch (IOException ioe) {
            System.out.println("Unable to start second server");
            System.out.println(ioe.toString());
        }
    }

}


