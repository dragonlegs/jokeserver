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

    /**
     * Toogle for current mode
     */
    public void setJoke(){
        status = !status;
        System.out.println(printJoke());

    }

    /**
     *
     * @return  Boolean State
     */
    public Boolean getJoke(){
        return status;
    }

    /**
     *
     * @return Pretty print of current State
     */
     public String printJoke(){
         if (status){
             return "Current Joke State: Joke";
         }else{
             return "Current Joke State: Proverb";
         }
     }

    /**
     * rewrote to get string of the boolean value
     * @return
     */
    @Override
    public String toString() {
        if (status){
            return "Joke";
        }else{
            return "Proverb";
        }
    }
}
