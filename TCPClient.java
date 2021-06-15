
import java.net.*;
import java.io.*;
public class TCPClient {
    public static void main(String[] args) throws Exception {
        try{
            Socket socket=new Socket("127.0.0.1",8888);
            DataInputStream inStream=new DataInputStream(socket.getInputStream());
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String clientMessage="",serverMessage="";
            while(!clientMessage.equals("bye")){
                serverMessage=inStream.readUTF();
                System.out.println(serverMessage);
                clientMessage=br.readLine();
                outStream.writeUTF(clientMessage);
                outStream.flush();
                serverMessage=inStream.readUTF();
                System.out.println(serverMessage);
                if(clientMessage.equals("s"))
                {
                    clientMessage=br.readLine();
                    outStream.writeUTF(clientMessage);
                    outStream.flush();

                    serverMessage=inStream.readUTF();
                    System.out.println(serverMessage);
                }
                else if(clientMessage.equals("r"))
                {
                    if(!serverMessage.equals("Your File Does not exists"))
                    {
                        String client = socket.getLocalSocketAddress().toString();
                        String file = client + "_127.0.0.1.txt";
                        file = file.replace("/",":");

                        String[] arrOfStr = file.split(":", -3);
                        file = arrOfStr[1] + "_" + arrOfStr[2];
                        File f = new File(file);
                        if(f.exists())
                        {
                            FileWriter fr = new FileWriter(f);
                            fr.write(serverMessage);
                            fr.close();
                        }
                        else
                        {
                            f.createNewFile();
                            FileWriter fr = new FileWriter(f);
                            fr.write(serverMessage);
                            fr.close();
                        }
                    }

                }
                else
                {
                    break;
                }
            }
            outStream.close();
            outStream.close();
            socket.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
