import api.HttpTaskServer;
import api.KVServer;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;



public class Main {

    public static void main(String[] args) throws IOException {


        KVServer kvServer = new KVServer();
        kvServer.start();
        TaskManager manager = Managers.getHttpTaskManager("http://localhost:8078/");
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();

       // kvServer.stop();
       // httpTaskServer.stop();


    }
}
