
import java.util.*;
import java.io.*;
import java.net.*;

public class Server implements Runnable {
    
    private Socket socket;
    
    public static void main(String[] args) {
        
    }
    
    public Server(Socket socket) {
        this.socket = socket;
        System.out.println("********************");
        System.out.println("Connection!");
        System.out.println("********************");
        System.out.println("From: " + socket.getInetAddress());
        System.out.println("Port: " + socket.getLocalPort());
        System.out.println("********************");
    }
    
    public void run() {
        String request = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            line = in.readLine();
            if (line != null)
                request += line;
            processRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
                       
        }
    }
    
    public void processRequest(String req) {
        req = req.replace("GET ", "");
        req = req.replace(" HTTP/1.1", "");
        respond(req);
        
    }
    
    public void respond(String file) {
        String fullDirectory;
        if (file.equals("/"))
            fullDirectory = System.getProperty("user.dir") + "/web/index.html";
        else
            fullDirectory = System.getProperty("user.dir") + "/web" + file;
        PrintWriter out = null;
        try {
            out  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-type: text/html\r\n\r\n");
            File output = new File(fullDirectory);
            if (!output.exists() || output.isDirectory()) {
                out.print("404 Not Found \n\n");
                out.flush();
                out.close();
            } else if (output.exists() && !output.isDirectory()) {
                String line;
                String fileContents = "";
                BufferedReader in = new BufferedReader(new FileReader(fullDirectory));
                while ((line = in.readLine()) != null)  {
                    out.print(line);
                }
                out.flush();
                out.close();
                in.close();
            }
            
        } catch (Exception e){
            out.print("500 Internal Service Error \n\n");
            out.flush();
            out.close();
        }
        
    }
}