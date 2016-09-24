import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.ParseException;


/**
 * Created by Hemanth on 9/23/16.
 */
public class AdminMode extends Thread {
    private Status status;
    private Socket sock;

    AdminMode(Socket sock, Status status) {
        this.status = status;
        this.sock = sock;
    }

    @Override
    public void run() {
        System.out.println("Admin Connected");
        PrintStream out = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintStream(sock.getOutputStream());
            try {
                String name;
                name = in.readLine();
                System.out.println("Request From Admin: " + name);
                try {
                    evaluteRequest(Integer.parseInt(name),out);
                }catch (NumberFormatException ioe){out.println("Invalid Numerical Choice");}


            } catch (IOException ioe) {
                System.out.println("Server Read Error");
            }

            sock.close();
        } catch (IOException x) {
            System.out.println(x);

        }
    }

    public void evaluteRequest(int check,PrintStream output){
        switch(check){
            case 0:
                System.out.println("Admin Selection Change Server State");
                status.setJoke();
                output.println("Current Status of Joke State: " + status.toString());
                break;
            case 1:
                System.out.println("Admin: Send Server State " + status.toString());
                output.println(status.toString());
                break;
            case 2:
                System.out.println("Admin: Shutdown Initiated");
                output.println("Shutdown Initiated");
                System.exit(0);
                break;
            default :
                System.out.println("Admin: Not a valid option");
                output.println("Not a valid option");
        }

    }


}
