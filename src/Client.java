import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by muffy on 11/03/2017.
 */
public class Client
{
    private String hostName;
    private int portNumber;


    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    class Listener extends Thread {
        public boolean isRunning = true;
        BufferedReader in;

        public Listener(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            super.run();

            while(isRunning) {
                try {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void runClient() {
        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            Listener listener = new Listener(in);
            listener.start();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
            listener.isRunning = false;
        } catch (UnknownHostException e) {
            System.err.println("Dont know " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Cant connect to " + hostName);
            System.exit(1);
        }

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java Client <hostName> <portNumber>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        Client one = new Client(hostName, portNumber);
        one.runClient();


    }
}
