import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.util.Scanner;

class ServerClientThread extends Thread {
    Socket serverClient;

    ServerClientThread(Socket inSocket,int counter){
        serverClient = inSocket;

    }
    public void run(){
        try{
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            String clientMessage="", serverMessage="";
            while(!clientMessage.equals("bye")){
                String msg = "Press s to save Data /n Press r to read Data /n any other to exit";
                outStream.writeUTF(msg);
                //outStream.flush();
                clientMessage=inStream.readUTF();
                if(clientMessage.equals("s"))
                {
                    serverMessage = "Enter Data";
                    outStream.writeUTF(serverMessage);
                    outStream.flush();
                    clientMessage=inStream.readUTF();
                    String address = serverClient.getRemoteSocketAddress().toString();
                    String file = address+".txt";
                    file =file.replace("/",":");
                    String[] arrOfStr = file.split(":", -3);
                    file = arrOfStr[1]+"_"+arrOfStr[2];

                    System.out.println(file);
                    File f = new File(file);

                    if(f.exists())
                    {

                        FileWriter fr = new FileWriter(f, true);
                        fr.write(" "+clientMessage);
                        fr.close();

                    }
                    else
                    {

                        f.createNewFile();
                        FileWriter fr = new FileWriter(f, true);
                        fr.write(clientMessage);
                        fr.close();
                    }
                    msg = "Data Stored Successfully";
                    outStream.writeUTF(msg);
                    outStream.flush();
                }
                else if(clientMessage.equals("r"))
                {

                    String address = serverClient.getRemoteSocketAddress().toString();
                    String file = address+".txt";
                    file =file.replace("/",":");
                    String[] arrOfStr = file.split(":", -3);
                    file = arrOfStr[1]+"_"+arrOfStr[2];
                    System.out.println(file);
                    File f = new File(file);
                    if(f.exists())
                    {
                        Scanner myReader = new Scanner(f);
                        String data = "";
                        while(myReader.hasNextLine())
                        {
                            String line = myReader.nextLine();
                            System.out.println("Line ----- "+line);
                            data = data + " " + line;
                        }
                        System.out.println("Data ------ "+data);
                        outStream.writeUTF(data);
                        outStream.flush();
                    }
                    else
                    {
                        String msg1 = "Your File Does not exists";
                        outStream.writeUTF(msg1);
                        outStream.flush();
                    }


                }
                else
                {
                    System.out.println(clientMessage);
                    String msg1 = "Connection closed";
                    outStream.writeUTF(msg1);
                    outStream.flush();
                    break;
                }

            }
            inStream.close();
            outStream.close();
            serverClient.close();
        }catch(Exception ex){
            System.out.println(ex.getStackTrace());
        }finally{
            System.out.println(serverClient.getRemoteSocketAddress() + " exit ");
            MultithreadedSocketServer obj = new MultithreadedSocketServer();
            obj.setActive();
        }
    }
}
