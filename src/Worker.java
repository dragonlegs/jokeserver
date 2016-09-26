import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

/**
 * Created by Hemanth on 9/18/2016.
 */
public class Worker extends Thread {
    Socket sock;
    Worker(Socket s,HashMap<String,Integer> al,Status status,HashMap<String,Integer> wl,Boolean secondary ) {sock =s;collect=al;Joke = status; uidCycle = wl;this.secondary = secondary; }
    HashMap<String,Integer> collect = new HashMap<String,Integer>();
    HashMap<String,Integer> uidCycle = new HashMap<String,Integer>();
    Status Joke;
    Boolean secondary;

    public void run(){
        PrintStream out = null;
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintStream(sock.getOutputStream());
            try{
                String name;
                name = in.readLine();
                //System.out.println("Request From: " + name);
                //System.out.println("Sending value "+name+Joke.getJoke());
                //collect.put(name+Joke.toString(),0);
                printList(collect);
                //out.println("Send things over\n");
                //setJoke();
                //out.println(null);
                if (checkNew(name+Joke.getJoke())) {
                    String data = getData(Joke, name);
                    out.print(data);
                }
            }catch (IOException ioe){System.out.println("Server Read Error");}
            sock.close();
        }catch (IOException x){
            System.out.println(x);

        }

    }

    /**
     * Toggles between Joke and Proverb
     */
//    public void setJoke(){
//        //Boolean local_value = JokeServer.STATUS;
//        JokeServer.setJoke();
//    }

    /**
     * For troublshooting purposes print current list
     * @param al
     */
    public void printList(HashMap<String,Integer> al){
        for (String key : al.keySet()){
            System.out.println(key + " : " + al.get(key));
        }

    }

    /**
     * Chooser between getting joke or proverb
     * @param status
     * @param UUID
     * @return data
     */
    public String getData(Status status,String UUID){
        if (status.getJoke()){
            System.out.println("Getting Joke");
            return getJoke(UUID);

        }else{
            System.out.println("Getting Proverb");
            return getProverb(UUID);
        }

    }
    //Jokes are from onelinefun.com

    /**
     * Chooses the correct joke from random seed
     * @param UUID
     * @return Sends Joke to client
     */
    public String getJoke(String UUID){
        String dataString;
        String unique = UUID+"true";
        int selection = collect.get(unique);
        if (selection == 5){
            collect.put((UUID+"true"),0);
            selection = 0;
        }
        List<Integer> messages = Arrays.asList(0, 1, 2, 3, 4);
        messages = randomSeed(messages,(UUID+"true"));
        //Collections.shuffle(messages, new Random(unique.hashCode()));
        //System.out.println(messages);
        String user = UUID.split(":")[1];
        System.out.println("Current UUID: " + UUID + " Joke Selection : " + selection);
        selection = messages.get(selection);
        String serverTag = serverTagId(secondary);
        String defaultString = serverTag + "JA " + user + " : Life is all about perspective. The sinking of the Titanic was a miracle to the lobsters in the ship's kitchen.";
        switch (selection){
            case 0:
                dataString = defaultString;
                break;
            case 1:
                dataString = serverTag + "JB " + user + " : Today a man knocked on my door and asked for a small donation towards the local swimming pool. I gave him a glass of water.";
                break;
            case 2:
                dataString = serverTag + "JC "+ user + " : When I call a family meeting I turn off the house wifi and wait for them all to come running.";
                break;
            case 3:
                dataString = serverTag + "JD " + user + " : I refused to believe my road worker father was stealing from his job, but when I got home all the signs were there.";
                break;
            case 4:
                dataString = serverTag + "JE " + user + " : I recently decided to sell my vacuum cleaner as all it was doing was gathering dust.";
                break;
            default:
                System.out.println("Default");
                dataString = defaultString;
                collect.put(UUID+"true",0);

    }

        collect.put(UUID+"true",collect.get(UUID+"true")+1);
        return dataString;
    }
    //Proverbs from http://www.u.arizona.edu/~rchaves/deepthought.html

    /**
     * Chooses Joke and proverb
     * @param UUID Unique String to store for random list
     * @return dataString String of Proverb
     */
    public String getProverb(String UUID){
        String dataString ;
        String unique = UUID+"false";
        int selection = collect.get(unique);
        if (selection == 5){
            collect.put((UUID+"false"),0);
            selection = 0;
        }
        List<Integer> messages = Arrays.asList(0, 1, 2, 3, 4);
        //Get Seed Random List for random
        messages = randomSeed(messages,(UUID+"false"));
        //Collections.shuffle(messages, new Random(unique.hashCode()));
        //System.out.println(messages);
        String user = UUID.split(":")[1];
        selection = messages.get(selection);
        String serverTag = serverTagId(secondary);
        String defaultString = serverTag + "PA " + user + " : Those who say it can't be done are usually interrupted by others doing it.";
        switch (selection){
            case 0:
                dataString = defaultString;
                break;
            case 1:
                dataString = serverTag + "PB " + user + " : A smile is an inexpensive way to change your looks.";
                break;
            case 2:
                dataString = serverTag + "PC " + user + " : The pursuit of happiness is the chase of a life time.";
                break;
            case 3:
                dataString = serverTag + "PD " + user + " : Every man dies; but not every man really lives.";
                break;
            case 4:
                dataString = serverTag + "PE " + user + " : Better to understand little than to misunderstand a lot.";
                break;
            default:
                dataString = defaultString;
                collect.put(UUID+"false",0);

        }
        collect.put(UUID+"false",collect.get(UUID+"false")+1);
        return dataString;
    }

    /**
     * Checks if UUID(name) is found in List
     * @param name Unique String
     * @return Boolean if new UUID
     */
    public Boolean checkNew(String name){
        if (collect.get(name) != null){
            System.out.println("Old UUID :" + name);
            return true;
        }else{
            collect.put(name,0);
            return true;
        }

    }

    /**
     *Generate random list of jokes and proverb for each client.
     * @param list List that stores Random
     * @param UID
     * @return
     */
    public List<Integer> randomSeed(List<Integer> list,String UID){
        //If value is found in list
        if (uidCycle.get(UID) != null){
           // System.out.println("Old Cycle UUID: " + UID);
            //System.out.println("Old Cycle Key: " + uidCycle.get(UID) );
            //System.out.println("Current Cycle Zero " + uidCycle.get(UID));
            //If we reach end of joke/proverb cycle with 5 jokes/proverbs
            if (uidCycle.get(UID) % 5 == 0){
                //System.out.println("Current Cycle Zero " + uidCycle.get(UID));
                //So benchmark that falls under
                int real = uidCycle.get(UID)-1;
                //System.out.println("Lower by one "+real);
                //Generate seed by getting benchmark that substracting reminder from the value but make sure it falls
                //under the previous benchmark
                int seed = real - (real % 5);
                //System.out.println("Seed is " + seed);

                Collections.shuffle(list, new Random((UID+seed).hashCode()));
            //Generate the benchmark by subtracting the reminder from the value
            }else{
                int seed = uidCycle.get(UID) - (uidCycle.get(UID) % 5);
                //System.out.println("Current Cycle Less " + uidCycle.get(UID));
                //System.out.println("Seed is " +seed);

                Collections.shuffle(list, new Random((UID+seed).hashCode()));
            }
            uidCycle.put(UID, uidCycle.get(UID)+1);
         //If value not found add to list ,recursive
        }else{
            //System.out.println("New Cycle");
            uidCycle.put(UID,1);
            return randomSeed(list , UID);
        }

        return list;
    }

    /**
     * Tag to append to Joke/proverb output
     * @param check
     * @return String Tag
     */
    public String serverTagId(Boolean check){
        if (check){
            return "<s2> ";
        }else{
            return "<s1> ";
        }
    }

}
