
import java.net.*;
import java.io.*;
public class MultithreadedSocketServer {
    private static int active = 0;

    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server=new ServerSocket(8888);
            int counter=0;
            System.out.println("Server Started ....");

            System.out.println("Waiting for clients on port 8888");

            while(true){
                counter++;

                Socket serverClient=server.accept();  //server accept the client connection request
                active++;
                String addres = serverClient.getRemoteSocketAddress().toString();
                System.out.println("Got Connection from "+addres);
                System.out.println("Active Connections: "+active);
                ServerClientThread sct = new ServerClientThread(serverClient,counter); //send  the request to a separate thread
                sct.start();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    void setActive()
    {
        active--;
        if(active == 0)
        {
            System.out.println("Waiting for clients on port 8888");
        }
    }
}
