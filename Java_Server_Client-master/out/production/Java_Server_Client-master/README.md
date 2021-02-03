# Java_Server_Client

<strong>In this assignment, you will implement a simple chat room. This will require a server
program.</strong>

<h1>1 Problem Specication</h1>
The server is multi-threaded. It maintains a list (a ArrayList will work well) of all the active
connections. It will listen on a port for a new connection. You have the authority to choose
the port number and design appropriate protocol for client-server communication. When one
comes in, it spawns a thread to handle the connection, and the main thread resumes listening.

The spawned thread sends out a message asking for an ID name on the socket. When the
client replies, the connection information is added to the master list of chat room occupants
(so there is a shared ArrayList of PrintWriter references). From that point on, anything
that is typed by the client is relayed to all chat room occupants (including the client), with
the ID identifying the client. 

Of course if null is read from the client, or the special value
LOGOUT then the client has disconnected and you should remove the connection from the
shared ArrayList and then exit the thread gracefully (i.e. stop the innite loop in the run
method). Don't worry too much about error checking. However, you will need to be careful
to synchronize access to the array list using locks, since otherwise you might delete an entry
while another thread is trying to broadcast to everyone, potentially resulting in an out-of-
bounds ArrayList access or worse. 

You can test the server by starting it and connecting via
several telnet windows. (You can turn the echo on for the telnet windows to see what you
are typing.)Broadcast a message when the client joins the chatroom, and then broadcast a mes-
sage when the client leaves the chatroom (in the nally block, after adjusting the ArrayList,
but immediately prior to terminating the run method).

<strong>Bonus Part</strong><br>
You should be able to have multiple chat rooms simultaneously. Also, the server should
make sure that there are no two clients with the same ID name in a chatroom at any given
time. If a client wants to join with an ID acquired by another chatroom occupant, the server
has to notify the client to choose a dierent ID.
