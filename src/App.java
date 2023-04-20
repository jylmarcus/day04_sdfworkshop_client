import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class App {
    public static void main(String[] args) throws NumberFormatException, UnknownHostException, IOException {
        String serverHost = args[0];
        String serverPort = args[1];

        // establish connection to server
        // start server first
        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));

        // setup console input from keyboard
        // variable to save keyboard input
        // variable to save msgReceived
        Console con = System.console();
        String keyboardInput = "";
        String msgReceived = "";

        // similar to server
        try (InputStream is = socket.getInputStream()){
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            try(OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                while(!keyboardInput.equals("close")) {
                    keyboardInput = con.readLine("Enter a command: \n");
                    
                    // send message to server
                    dos.writeUTF(keyboardInput);
                    dos.flush();

                    // receive message from server
                    msgReceived = dis.readUTF();
                    System.out.println(msgReceived);
                }

                dos.close();
                bos.close();
                os.close();

            } catch (EOFException ex) {
                ex.printStackTrace();
            }

            dis.close();
            bis.close();
            is.close();
            
        } catch (EOFException ex) {
            ex.printStackTrace();
        }

        socket.close();
    }
}
