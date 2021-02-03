/*********************************************************************
Purpose/Description: Write a program in Java to implement an efficient function.
********************************************************************/
package serverclient;

public class Server {
    myPort port = new myPort();
    public int portNumber = port.getValue();

    Thread alphaChat;
    ChatroomAlpha alpha = new ChatroomAlpha(portNumber);

    public ChatroomAlpha getAlpha() {
        return alpha;
    }

    void runServer() {
        Thread alphaChat = new Thread(alpha);
        alphaChat.start();
    }

    public static void main(String[] args) {
        new Server().runServer();
    }
}
