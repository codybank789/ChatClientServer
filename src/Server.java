import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by muffy on 12/03/2017.
 */
public class Server {
    private int portNumber;
    private boolean listening;

    public Server(int portNumber, boolean listening) {
        this.portNumber = portNumber;
        this.listening = listening;
    }

    public void runServer() throws IOException {
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            while (listening) {
                ServerThread serverThread = new ServerThread(serverSocket.accept());
                serverThread.start();
            }
        } catch (IOException e) {
            System.err.println("Cannot listen");
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.err.println("Usage: java Server <portNumber>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        Server server = new Server(portNumber, true);
        server.runServer();


    }
}
