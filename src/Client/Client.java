package Client;

import Server.ChatServerInterface;

import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Assignment 2A
 * Created by Peonsson & roppe546 on 2015-09-26.
 */

public class Client extends UnicastRemoteObject implements Notifiable {

    private ChatServerInterface chatServerInterface;

    /**
     * Sole constructor.
     * @param s
     * @throws RemoteException
     */
    public Client(ChatServerInterface s) throws RemoteException {

        super();
        this.chatServerInterface = s;
    }

    public static void main(String[] args) {
       System.out.println("Client starting!");

       try {
           /**
            * Clients connect to the RMI service.
            */
           String local = "localhost";
           String url = "rmi://" + local + "/chatserver";
           ChatServerInterface chatServer = (ChatServerInterface) Naming.lookup(url);
           Client c = new Client(chatServer);
           chatServer.register(c);

           /**
            * Clients sends calls methods as messages to the server.
            * With simple error handling.
            */
           String msg;
           Scanner scan = new Scanner(System.in);
           while(true) {

               Date currentTime = new Date();
               SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
               msg = scan.nextLine();
               String time = "[" + ft.format(currentTime) + "]";

               if(msg.startsWith("/help")) {

                   System.out.println(time + chatServer.getHelp());

               } else if(msg.startsWith("/nick")) {

                   String[] parts = msg.split(" ");

                   if(parts.length != 2) {
                       System.out.println(time + "[Server]: Invalid username. Use /nick <username>");
                       continue;
                   }

                   System.out.println(time + chatServer.setNickname(c, parts[1]));

               } else if(msg.startsWith("/quit")) {

                   chatServer.deRegister(c);
                   System.out.println("Halting execution..");
                   System.exit(0);

               } else if(msg.startsWith("/who")) {

                   System.out.println(time + "[Server]: " + chatServer.getOnlineClients());

               } else if(msg.startsWith("/")) {

                   System.out.println(time + "[Server]: No such command. Use /help to see available commands.");

               } else {
                   try {
                       chatServer.broadcast(c, msg);
                   } catch(ConnectException e) {
                       System.err.println("Cannot connect to server!");
                       System.err.println("Halting execution..");
                       System.exit(0);
                   }
               }
           }

       } catch (RemoteException e) {
           e.printStackTrace();
       } catch (MalformedURLException e) {
           e.printStackTrace();
       } catch (NotBoundException e) {
           e.printStackTrace();
       } catch (NoSuchElementException e) {

       }
    }

    /**
     * Clients receive messages from the server.
     * @param msg
     * @throws RemoteException
     */
    @Override
    public void sendMessage(String msg) throws RemoteException {

        Date currentTime = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        String time = "[" + ft.format(currentTime) + "]";

        System.out.println(time + msg);
    }

    /**
     * Checks if the current client is alive.
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean isAlive() throws RemoteException {
        return true;
    }
}