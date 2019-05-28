package chat;

import GUI.CaptureView;
import GUI.ChatView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.IntStream;

public class Server {
    // Properties
    private String serverName;
    // Sockets
    private Socket myService;
    private ServerSocket serviceSocket;
    
    private OutputStream outputStream;
    private InputStream inputStream;
    
    private DataOutputStream outputData;
    private DataInputStream inputData;
    
    private static final char match = ' ';
    private boolean option = true;
    private ChatView chatView;
    
    public Server(ChatView chatView) {
        this.chatView = chatView;
    }
    
    public String getUsername() {
        return serverName;
    }
    
        private static Long unique(String input) {
        char match = ' ';
        return IntStream.range(0, input.length())
                .map(input::charAt)
                .filter(e -> e != match)
                .distinct()
                .count();
    }

    
    public void SetConnection(int port) {
        try {
            serviceSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            myService = serviceSocket.accept();
            Thread th1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (option) {
                        ListenData(chatView);
                    }
                }
            });
            th1.start();
            System.out.println("Successfully connected");
        } catch (IOException ex) {
           System.err.println("ERROR: connection error");
           System.exit(0);
        }
    }
    
    public void ListenData(ChatView chatView) {
        try {
            inputStream = myService.getInputStream();
            String input= new DataInputStream(inputStream).readUTF();
            Long inputData=IntStream.range(0, input.length())
                .map(input::charAt)
                .filter(e -> e != match)
                .distinct()
                .count();
                
            
            chatView.AddRemoteMessage(inputData.toString());
        } catch (IOException ex) {
            System.err.println("ERROR: error listening data");
        }
    }
    
    public void SendMessage(String msg) {
        try {
            outputStream = myService.getOutputStream();
            outputData = new DataOutputStream(outputStream);
            outputData.writeUTF(msg);
            outputData.flush();
        } catch (IOException ex) {
            System.err.println("ERROR: error sending data");
        }
    }
    
    public void SetServerProperties(String name) {
        serverName = name;
    }
    
    public void CloseConnection() {
        try {
            outputData.close();
            inputData.close();
            serviceSocket.close();
            myService.close();
        } catch (IOException ex) {
            System.err.println("ERROR: error closing connection");
        }
    }
    
    public static void main(String [] args) {
        ChatView chatView = new ChatView();
        Server srv = new Server(chatView);
        
        CaptureView captureView = new CaptureView(chatView, true);
        captureView.SetTitleText("Server login");
        captureView.SetIpField("This computer");
        captureView.SetPortField(5555);
        captureView.SetIpEnable(false);
        captureView.setVisible(true);
        
        String srvName = captureView.GetUsername();
        srv.SetServerProperties(srvName);
        
        int portMachine = captureView.GetPort();
        srv.SetConnection(portMachine);
        
        chatView.SetServer(srv);
        chatView.SetUsername(srvName);
        
        chatView.setVisible(true); 
    }
}