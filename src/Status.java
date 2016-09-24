/**
 * Created by Hemanth on 9/23/16.
 */
public class Status extends Thread {
    static Boolean status = true;

    @Override
    public void run (){
        System.out.println("Current Joke Status" + printJoke());
        while (true) {

        }

    }

    public void setJoke(){
        status = !status;
        System.out.println(printJoke());

    }
    public Boolean getJoke(){
        return status;
    }

     public String printJoke(){
         if (status){
             return "Current Joke State: Joke";
         }else{
             return "Current Joke State: Proverb";
         }
     }


    @Override
    public String toString() {
        if (status){
            return "Joke";
        }else{
            return "Proverb";
        }
    }
}
