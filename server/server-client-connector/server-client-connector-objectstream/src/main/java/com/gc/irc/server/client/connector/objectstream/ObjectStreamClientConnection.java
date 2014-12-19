package com.gc.irc.server.client.connector.objectstream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.server.client.connector.AbstractClientSocketConnection;

/**
 * Communication between the Client and the server using an {@link java.io.ObjectOutputStream}.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class ObjectStreamClientConnection extends AbstractClientSocketConnection<ObjectInputStream, ObjectOutputStream> {


    /**
     * Builder who initialize the TCP connection.
     *
     * @param clientSocket
     *            Client's Socket.
     */
    public ObjectStreamClientConnection(final Socket clientSocket) {
        super(clientSocket);
    }


	/** {@inheritDoc} */
	@Override
	protected ObjectInputStream build(InputStream inputStream) throws IOException {
		return new ObjectInputStream(inputStream);
	}

	/** {@inheritDoc} */
	@Override
	protected ObjectOutputStream build(OutputStream outputStream) throws IOException {
		return new ObjectOutputStream(outputStream);
	}

	/** {@inheritDoc} */
	@Override
	protected Message receiveMessage(ObjectInputStream inputStream)
			throws ClassNotFoundException, IOException {
		return IOStreamUtils.receiveMessage(inputStream);
	}

	/** {@inheritDoc} */
	@Override
	protected void send(ObjectOutputStream outputStream, Message message)
			throws IOException {
		IOStreamUtils.sendMessage(outputStream, message);
	}

}
