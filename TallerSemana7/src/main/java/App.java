

import java.util.concurrent.*;
import model.*;
import controller.*;
/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        var task = new controlador("C:/Users/Usuario/Documents/TallerSemana7/TallerSemana7/src/main/resources/weatherHistory.csv");
        //var thread = new Thread(task);
        //executor.execute(task);
        /*thread.start();
        thread.join();*/
        Future<ClimaB> future= executor.submit(task);
        var resutl =future.get();
        System.out.println(resutl);
        executor.shutdown();
       /* if(executor.awaitTermination(10, TimeUnit.SECONDS)){
            System.out.println(task);
        }else{
            System.out.println("tiempo finalizado ");
        }
*/





    }
}