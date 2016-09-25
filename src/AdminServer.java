import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Hemanth on 9/23/16.
 */
public class AdminServer extends Thread {
    private static Socket sock;
    private Status status;
    private int port;
    AdminServer(Status status,int port){this.status = status;this.port = port;}
    @Override
    public void run(){

        int q_len = 6;
        try{
            ServerSocket adminsock = new ServerSocket(port,q_len);
            System.out.println("Starting Admin Server PORT:" + port);

//Server Forever listing to port for Admin Connection
            while (true) {
                sock = adminsock.accept();
                //
                //When Admin connects handle it
                new AdminMode (sock,status).start();


            }
        }catch (Exception e){System.out.println(e);}
    }
}
