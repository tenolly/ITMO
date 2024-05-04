package client.managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang3.SerializationUtils;

import utils.request.CommandRequest;

public class DataManager implements AutoCloseable {
    private SocketChannel clientChannel = SocketChannel.open();

    private String host;
    private int port;

    public DataManager(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    public CommandRequest sendRequest(CommandRequest request) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        
        byte[] arr = SerializationUtils.serialize(request);

        buffer.putInt(arr.length);
        buffer.put(arr);
        buffer.flip();

        clientChannel.write(buffer);
        buffer.clear();
        
        clientChannel.read(buffer);
        buffer.flip();

        return (CommandRequest) SerializationUtils.deserialize(buffer.array());
    }

    public boolean is_available() {
        try {
            sendRequest(new CommandRequest("ping"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void connect() throws IOException {
        clientChannel.connect(new InetSocketAddress(host, port));   
    }

    public void reconnect() throws IOException {
        this.clientChannel = SocketChannel.open();
        connect();
    }

    @Override
    public void close() throws Exception {
        clientChannel.close();
    }
}
