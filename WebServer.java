import java.io.*;
import java.net.*;
import java.util.*;


public class WebServer {
    public static void main(String[] args){
        try {
            ServerSocket ss = new ServerSocket(8080);
            while (true) {
                Thread conn = new Thread(new Server(ss.accept()));
                conn.start();
            }
         } catch (IOException e) {
                e.printStackTrace();
         }
    }
}