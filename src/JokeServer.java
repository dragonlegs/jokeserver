import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class JokeServer {

    private boolean Joke = true;
    static private HashMap<String,Integer> uidList = new HashMap<String, Integer>();
    private static Boolean status =true;



    public static void main(String[] args) {
        System.out.println("Starting Joke Server");
        int q_len;
        int port;
        int portAdmin;
        //Check if two commandland arguments for custom queue and port
        if (args.length == 2){
            q_len = Integer.parseInt(args[0]);
             port = Integer.parseInt(args[1]);
            portAdmin = Integer.parseInt(args[2]);
        }else{
             q_len =6;
             port = 1565;
            portAdmin = 4545;
        }
        Status status = new Status();
        AdminServer admin = new AdminServer(status,portAdmin);
        status.start();
        admin.start();

        try{
            Socket sock;

            ServerSocket servsock = new ServerSocket(port,q_len);
            System.out.println("Hemanth Ande Joke Server is running on port "+ port);
            while (true){
                try{
                    sock = servsock.accept();
                    //sock2 = adminsock.accept();
                    System.out.println("Current Joke Staus: "+ status.toString());
                    new Worker(sock,uidList,status).start();
                }catch (IOException ioe){System.out.println("Unable to start server");}
            }
        }catch (IOException ioe){
            System.out.println("Unable to start server");
            System.out.println(ioe.toString());
        }
    }
    public static void  setJoke(){
        //System.out.println("Current Joke status: "+status.toString());
        try {
            status = !status;
        }catch (Exception ioe) {System.out.println(ioe);}
        System.out.println("Changed Joke status: "+status.toString());
    }
    public static Boolean getJoke(){
        return status;
    }
}

