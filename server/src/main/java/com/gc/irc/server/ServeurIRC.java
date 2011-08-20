package com.gc.irc.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.persistance.PersiteUsers;
import com.gc.irc.server.thread.ThreadGestionClientIRC;
import com.gc.irc.server.thread.ThreadServeurIRC;

import java.lang.management.*;
import javax.management.*;

/**
 * Main class.
 * 
 * Start the server.
 * Start the ThreadPull.
 * 
 * When a Client is connected a Thread is start to discuss with.
 * @author gcauchis
 *
 */
public class ServeurIRC {
	private final static Logger logger = Logger.getLogger(ServeurIRC.class);
	private static int nbThreadServeur = Integer.parseInt(ServerConf.getConfProperty("nbThread", "1"));
	private static ServerSocket serverSocket = null;
	private int port = 1973;
	private List<ThreadGestionClientIRC> clientConnecter = Collections.synchronizedList(new ArrayList<ThreadGestionClientIRC>());
	private List<ThreadServeurIRC> pullThreadServeur = Collections.synchronizedList(new ArrayList<ThreadServeurIRC>());
	private Map<Integer, IRCUser> listUserById = Collections.synchronizedMap(new HashMap<Integer, IRCUser>());
	private Map<Integer, ThreadGestionClientIRC> listThreadClientByIdUser = Collections.synchronizedMap(new HashMap<Integer, ThreadGestionClientIRC>());
	
	private static String messageAcceuil = "Welcome on our server.";
	/**
	 * Used to change the welcoming message.
	 * @param messageAcceuil The new message.
	 */
	public static void setMessageAcceuil(String messageAcceuil) {
		ServeurIRC.messageAcceuil = messageAcceuil;
	}
	
	/**
	 * Get the welcome message.
	 * @return The welcome message.
	 */
	public static String getMessageAcceuil() {
		return messageAcceuil;
	}
	
	/**
	 * Builder, Initialize the server.
	 * The port is 1973.
	 */
	public ServeurIRC() {
		initServeur();
	}

	/**
	 * Builder, Initialize the server.
	 * The port is given.
	 * @param port New port.
	 */
	public ServeurIRC(int port) {
		if(this.port != port){
			setPort(port);
		}
		initServeur();
		
	}
	
	/**
	 * Initialize the server.
	 */
	private void initServeur(){
		logger.info("Initialise server.");
		/**
		 * Listen the designed Port
		 */
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.fatal("Impossible to open the socket.");
			e.printStackTrace();
			System.exit(-1);
		}
		logger.info("Server initialize. Listen port "+port);
		
		
		/**
		 * Start thread server pull. The first thread is registered as a MBean.
		 */
		
		// Get the Platform MBean Server
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		
		// Construct the ObjectName for the MBean we will register
		ObjectName name = null;
		try {
			name = new ObjectName("com.irc.server.thread:type=ThreadServeurIRC");
		} catch (MalformedObjectNameException e) {
			logger.fatal("Malformed Object Name (JMX MBean Server).");
			e.printStackTrace();
			System.exit(-1);
		} catch (NullPointerException e) {
			logger.fatal("Can not register the object ThreadServerIRC (JMX MBean Server).");
			e.printStackTrace();
		}
		
		//Create the Thread
		ThreadServeurIRC threadServer = new ThreadServeurIRC(this);
		threadServer.start();
		
		// Register the Hello World MBean
		try {
			mbs.registerMBean(threadServer, name);
		} catch (InstanceAlreadyExistsException e) {
			logger.fatal("Instance Already Exists (JMX MBean Server).");
			e.printStackTrace();
			System.exit(-1);
		} catch (MBeanRegistrationException e) {
			logger.fatal("Unable to register MBean (JMX MBean Server).");
			e.printStackTrace();
			System.exit(-1);
		} catch (NotCompliantMBeanException e) {
			logger.fatal("Not Compliant MBean (JMX MBean Server).");
			e.printStackTrace();
			System.exit(-1);
		}
		
		for (int i = 1 ; i < nbThreadServeur ; i++){
			threadServer = new ThreadServeurIRC(this);
			threadServer.start();
			pullThreadServeur.add(threadServer);
		}
		

	}
	
	/**
	 * Change the listening port
	 * 
	 * Don't forget to use initServer() after use this method.
	 * @param port New Listening Port.
	 */
	public void setPort(int port) {
		this.port = port;
		logger.debug("Nouveau port : "+port);
	}

	/**
	 * Main
	 * 
	 * Start server.
	 * 
	 * Wait for new Client.
	 * @param args
	 */
	public static void main(String[] args) {
		ServeurIRC serveur = null;
		if( args.length>=1 ){
			serveur = new ServeurIRC(Integer.parseInt(args[0]));
		} else {
			serveur = new ServeurIRC();
		}
		while(true){
			serveur.attenteClient();
		}
	}
	
	/**
	 * Wait for new client.
	 * When a client connect to the sever stat a Thred fot him.
	 */
	private void attenteClient() {
		Socket clientSocket = null;
		try {
			logger.debug("Wait for a client");
			clientSocket = serverSocket.accept();
			logger.debug("Client "+ clientSocket.getInetAddress()+" is connected");
		} catch (IOException e) {
			logger.warn("Timeout or Connection error.");
			e.printStackTrace();
			return;
		}
		Thread thread = new ThreadGestionClientIRC(clientSocket, this);
		logger.debug("End Client's Thread Initialization.");
        thread.start();
	}
	
	/**
	 * Add the login client to the Client's list.
	 * @param client New Client
	 */
	public void nouveauClientConnecter(ThreadGestionClientIRC client){
		logger.debug("Add a new Connected Client : "+client.getUser().getNickName());
		synchronized (clientConnecter) {
			synchronized (listUserById) {
				synchronized (listThreadClientByIdUser) {
					logger.debug("Add to clientConnecter");
					clientConnecter.add(client);
					logger.debug("Add to listThreadClientByIdUser");
					listThreadClientByIdUser.put(client.getUser().getId(),client);
					logger.debug("Add to listUserById");
					listUserById.put(client.getUser().getId(),client.getUser());
				}
			}
		}
	
		/**
		 * Persist the change.
		 */
		PersiteUsers.persistListUser(getAllUsers());
	}
	
	/**
	 * Delete the deconnected Client.
	 * @param client Deconnected Client.
	 */
	public void deconnectionClient(ThreadGestionClientIRC client){
		logger.debug("Delete the deconnected Client : "+client.getUser().getNickName());
		synchronized (clientConnecter) {
			synchronized (listUserById) {
				synchronized (listThreadClientByIdUser) {
					logger.debug("Remove from list clientConnecter");
					clientConnecter.remove(client);
					logger.debug("Remove from listUserConnectedById");
					listUserById.remove(client.getUser().getId());
					logger.debug("Remove from lisThreadClientByIdUser");
					listThreadClientByIdUser.remove(client.getUser().getId());
				}	
			}
		}
		
		/**
		 * Persist the change.
		 */
		PersiteUsers.persistListUser(getAllUsers());
	}
	
	/**
	 * Get the Thread list of connected client.
	 * @return Client's thread list.
	 */
	public List<ThreadGestionClientIRC> getClientConnecter() {
		return clientConnecter;
	}
	
	/**
	 * Get the users Connected list.
	 * @return The list of all the connected users.
	 */
	public ArrayList<IRCUser> getAllUsers() {
		ArrayList<IRCUser> list = null;
		synchronized (listUserById) {
			list = new ArrayList<IRCUser>(listUserById.values());
		}
		return list;
	}
	
	/**
	 * Get the user demand if he is connected.
	 * @param id User's Id.
	 * @return The User selected or null if not find.
	 */
	public IRCUser getUser(int id) {
		return listUserById.get(id);
	}
	
	/**
	 * Get the thread of a selected user.
	 * @param id User's Id.
	 * @return The Designed User's Thread.
	 */
	public ThreadGestionClientIRC getThreadOfUser(int id) {
		return listThreadClientByIdUser.get(id);
	}
	
	/**
	 * Finalize the Server.
	 */
	public void finalizeClass(){
		for (ThreadServeurIRC thread : pullThreadServeur) {
			thread.finalizeClass();
		}
		
		for (ThreadGestionClientIRC thread : clientConnecter) {
			thread.finalizeClass();
		}
		try {
			super.finalize();
		} catch (Throwable e) {
			logger.warn("Problem when finalize the Server");
			e.printStackTrace();
		}
	}
	
}
