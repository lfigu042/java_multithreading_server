/*********************************************************************
Purpose/Description: Write a program in Java to implement an efficient function.
********************************************************************/
package serverclient;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatroomAlpha implements Runnable {
    
    //List to hold references of objects created
    public ArrayList<Thread> clientList = new ArrayList<>();
    private ArrayList<PrintWriter> clientOutputStreams = new ArrayList();
    private ArrayList<Echoer> echoerList = new ArrayList<>();
//    Server server;
    myPort port = new myPort();
    public int portNumber = port.getValue();

    public ChatroomAlpha(int portNumber){
        this.portNumber = portNumber;
    }

    public void run() {

        //Create ServerSocket and Client Socket
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            //Read data from client
            System.out.println("Alpha Server Running on (" + portNumber + ")...");
            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                Echoer echoer = new Echoer(socket, writer);
                Thread client = new Thread(echoer);
                client.start();
                System.out.println("Thread Name: " + client.getName() + " connected... (" + portNumber + ")");

                // Add objects to list
                echoerList.add(echoer);
                clientOutputStreams.add(writer);
                clientList.add(client);

                //Check Network status
                checkOnNetwork();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Server exception " + e.getMessage());
        }
    }

    public void boardCast(String echo) {
        synchronized (this) {
            try {
                for (PrintWriter writer : clientOutputStreams) {
                    writer.println(echo);
                    writer.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    

    /**
     * METHOD WILL CHECK IF THE CLIENT IS IN THE SYSTEM. IT WILL ALSO REMOVE THE
     * PRINTWRITER REFERENCES AFTER A CONNECTION IS CLOSED.
     */
    void checkOnNetwork() {
        try {
            System.out.println("--------------------------------------");
            System.out.println("ALPHA SERVER REPORT (PORT " + portNumber + ")");
            System.out.println("----------Thread Report---------------");
            for (Thread client : clientList) {
                System.out.println("Running Threads: " + client.getName() + "\n"
                        + "Thread State: " + client.getState());
            }
            System.out.println("----------Ending Thread Report---------------");
            System.out.println("");
            System.out.println("----------PrintWriter Report---------------");
            for (PrintWriter writer : clientOutputStreams) {
                System.out.println(writer.toString());
            }
            System.out.println("----------Ending PrintWriter Report---------------");
            System.out.println("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Thread> getClientList() {
        return clientList;
    }

    //INNER CLIENT/ECHOER CLASS-------------------------------------------------
    public class Echoer extends Thread {
        
        //Inner class variables
        private Socket socket;
        BufferedReader input;
        PrintWriter output;
        Scanner scanner;
        private boolean isStillConnected = true;

        public PrintWriter getOutput() {
            return output;
        }

        public Echoer(Socket socket, PrintWriter writer) {
            this.output = writer;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //Making connection streams
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //output = new PrintWriter(socket.getOutputStream(), true);
                scanner = new Scanner(System.in);

                //Get user connection ID / User name
                String userName = getUserName();
                boardCast(userName + " has joined the chatroom! (" + portNumber + ")");

                //Set the name of the thread to user selected name
                Thread.currentThread().setName(userName);
                System.out.println("Client Connected: " + Thread.currentThread().getName());
                
                String echoString = "";
                while (true & echoString != null) {
                    if (echoString.equalsIgnoreCase("exit") || echoString.equalsIgnoreCase("stop") || echoString.equalsIgnoreCase("leave")){
                        
                        break;
                    }
                    echoString = input.readLine();
                    output.println(">>>");
                    boardCast(Thread.currentThread().getName() + "(PORT " + portNumber + ") : " + echoString);
                    System.out.println(Thread.currentThread().getName() + " : " + echoString);
                }
                //Message if the user leaves or closes connection and set the connection to flase
                System.out.println(Thread.currentThread().getName() + " : Has left the chatroom (" + portNumber + ")");
                boardCast(Thread.currentThread().getName() + " : Has left the chatroom (" + portNumber + ")");
                this.isStillConnected = false;

            } catch (IOException e) {
                System.out.println("Client Disconnected: " + Thread.currentThread().getName());
                boardCast("Client Disconnected: " + Thread.currentThread().getName());
                this.isStillConnected = false;
            } finally {
                try {
                    socket.close();
                    input.close();
                    output.close();
                } catch (IOException e) {
                    //Oh, well!
                }
            }
        }

        public String getUserName() throws IOException {
            String userName = "";
            while(userName.isEmpty()){
                output.println("Please Enter a User ID");
                userName = input.readLine();
            }
            return userName;
        }

        public boolean isIsStillConnected() {
            return isStillConnected;
        }

    }

}
