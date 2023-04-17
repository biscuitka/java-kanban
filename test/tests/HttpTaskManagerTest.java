package tests;

import api.HttpTaskManager;
import api.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{
    KVServer kvServer;

    @BeforeEach
    void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager("http://localhost:8078/");
    }

    @AfterEach
    void stopServer(){
        kvServer.stop();
    }


}