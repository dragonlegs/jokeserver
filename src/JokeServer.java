import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class JokeServer {

    private boolean Joke = true;
    static private HashMap<String,Integer> uidList = new HashMap<String, Integer>();


    public static void main(String[] args) {
        System.out.println("Starting Joke Server");
        int q_len;
        int port;
        //Check if two commandland arguments for custom queue and port
        if (args.length == 2){
            q_len = Integer.parseInt(args[0]);
             port = Integer.parseInt(args[1]);
        }else{
             q_len =6;
             port = 1565;
        }

        try{
            Socket sock;
            ServerSocket servsock = new ServerSocket(port,q_len);
            System.out.println("Hemanth Ande Joke Server is running on port "+ port);

            while (true){
                try{
                    sock = servsock.accept();
                    new Worker(sock,uidList).start();
                }catch (IOException ioe){System.out.println("Unable to start server");}
            }
        }catch (IOException ioe){
            System.out.println("Unable to start server");
            System.out.println(ioe.toString());
        }
    }
}
