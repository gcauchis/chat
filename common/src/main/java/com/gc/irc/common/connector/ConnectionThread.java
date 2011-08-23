package com.gc.irc.common.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.gc.irc.common.protocol.IRCMessage;


/**
 * The thread wich connects the client to the server, and manages the serialized objects wich are transmitted (they are defined in the com.irc.share.protocol package)
 * @author Colin
 *
 */
public class ConnectionThread extends Thread {
	private static final Logger logger = Logger.getLogger(ConnectionThread.class);

	private boolean connectedToServer = false;
	private boolean authenticated = false; // set to true when the server says that we are successfully logged in
	private boolean waitingForAuthentication = false; // set to true when we are waiting for the server to accept our login attempt
	private boolean manualDisconnection = false; // possibility to manually disconnect from the server
	private boolean serverDisconnection = false; // to try to log in automatically in case of a server disconnection followed by a reconnection
	private String serverName; //ip or name
	private int port;
	private InetAddress host = null;
	private Socket socket = null;
	private ObjectInputStream inObject;
	private ObjectOutputStream outObject;

	/**
	 * 
	 * @param serverName the ip address or name of the server
	 * If the server is on localhost, out an empty string
	 * @param port the port of the connexion
	 */
	public ConnectionThread( String serverName, int port ){
		
		this.serverName = serverName;
		this.port = port;
		
		if(serverName.isEmpty()){
			logger.info("The server parameters will be : name=localhost"+" port="+port);
		}
		else {
			logger.info("The server parameters will be : name="+serverName+" port="+port);
		}
		
		while(true) {
			logger.debug("Initialisation of the connection thread. Server name/ip : "+serverName+" port : "+port);
			
			try {
				if(!serverName.isEmpty())
					host = InetAddress.getByName( serverName );
				else
					host = InetAddress.getLocalHost();
				
				break;
				
			} catch (UnknownHostException e) {
				logger.error("Impossible to find the host");
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}

	}

	/**
	 * The thread infinite loop. Here, the client will try to connect to the server (opening a socket, and getting input and output streams).
	 * Then, the loop will wait for new serialized objects sent by the server, and execute the corresponding actions.
	 */
	@Override
	public void run() {	

		while(true){

			logger.info("Trying to connect");

			try {
				socket = new Socket(host, port);
				setConnectedToServer(true);
				manualDisconnection = false;
				

				logger.info("Socket successfully created.  Local port : "+socket.getLocalPort());

				/**
				 * Gestion par objet
				 */
				logger.debug("Trying to open streams...");
				outObject = new ObjectOutputStream(socket.getOutputStream());
				logger.debug("Output stream opened");
				inObject = new ObjectInputStream(socket.getInputStream());
				logger.debug("Input stream opened");

				while (true) {
					/**
					 * Reception du message.
					 */
					logger.debug("Waiting for an object message");
					IRCMessage messageObject = null;
					try {
						messageObject = IRCMessage.recevoirMessageObjetSocket(inObject);
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
						logger.error(e.getMessage());
						break;
					}
					catch (StreamCorruptedException e) {
						e.printStackTrace();
						logger.error(e.getMessage());
						break;
					}
					


					logger.debug("Message received : "+messageObject.getClass());
					if (messageObject == null){
						logger.error("Empty IRCMessage Object received");
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					else {
						// TODO : IMessageHandler
					}
				}
			} catch (IOException e) {
				setConnectedToServer(false);
				setWaitingForAuthentication(false);
				if(!manualDisconnection && isAuthenticated())
					setServerDisconnection(true);
				setAuthenticated(false);
				logger.error("The connection with the server failed "+e.getMessage());
			}

			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}		
		
		}

	}




	private void setConnectedToServer(boolean connectedToServer) {
		this.connectedToServer = connectedToServer;
	}

	/**
	 * Is the client currently connected on the server ? (the client may not be authenticated)
	 */
	public boolean isConnectedToServer() {
		return connectedToServer;
	}


	/**
	 * Send and IRC Message (will transmit a serialized object to the server)
	 * @param message the IRC message to send
	 */
	public void sendIRCMessage(IRCMessage message){
		try {
			synchronized (inObject) {
				synchronized (outObject) {
					message.envoyerMessageObjetSocket(outObject);
				}
			}
			
		}
		catch (SocketException e) {
			setConnectedToServer(false);
			setWaitingForAuthentication(false);
			if(!manualDisconnection && isAuthenticated())
				setServerDisconnection(true);
			setAuthenticated(false);
			logger.error("Socket error "+e.getMessage());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void setAuthenticated(boolean loggedIn) {
		this.authenticated = loggedIn;
	}

	/**
	 * Is the client currently authenticated on the server ?
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	
	/**
	 * disconnect the client from the server
	 * After that the client will automatically try to reconnect
	 */
	public void disconnect() {
		try {
			manualDisconnection = true;
			setServerDisconnection(false); // because it is a manual disconnection, from the client
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public void setWaitingForAuthentication(boolean waitingForAuthentication) {
		this.waitingForAuthentication = waitingForAuthentication;
	}

	public boolean isWaitingForAuthentication() {
		return waitingForAuthentication;
	}

	/**
	 * 
	 * @return the name or ip address of the remote server
	 */
	public String getServerName() {
		if(!serverName.isEmpty())
			return serverName;
		else return "localhost";
	}
	
	
	/**
	 * 
	 * @return the choosen port for the connexion with the server
	 */
	public int getPort() {
		return port;
	}

	public void setServerDisconnection(boolean serverDisconnection) {
		this.serverDisconnection = serverDisconnection;
	}

	public boolean isServerDisconnection() {
		return serverDisconnection;
	}

}
