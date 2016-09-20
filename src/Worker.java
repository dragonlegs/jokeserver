import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Hemanth on 9/18/2016.
 */
public class Worker extends Thread {
    Socket sock;
    Worker(Socket s,HashMap<String,Integer> al ) {sock =s;collect=al; }
    HashMap<String,Integer> collect = new HashMap<String,Integer>();
    Boolean Joke = true;

    public void run(){
        PrintStream out = null;
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintStream(sock.getOutputStream());
            try{
                String name;
                name = in.readLine();
                System.out.println("Request From: " + name);
                System.out.println("Sending value "+name+Joke.toString());
                collect.put(name+Joke.toString(),0);
                printList(collect);
                out.println("Send things over\n");
                //out.println(null);
            }catch (IOException ioe){System.out.println("Server Read Error");}
            sock.close();
        }catch (IOException x){
            System.out.println(x);

        }


    }

    /**
     * Toggles between Joke and Proverb
     */
    public void setJoke(){
        Joke = !Joke;
        if (Joke) {
            System.out.println("Enabled Joke State");
        }else{
            System.out.println("Enabled Proverb State");
        }

    }
    public void printList(HashMap<String,Integer> al){
        for (String key : al.keySeta()){
            System.out.println(key + " : " + al.get(key));
        }

    }

}
