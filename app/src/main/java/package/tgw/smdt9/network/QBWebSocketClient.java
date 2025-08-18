package com.dpdtech.application.network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import android.util.Log;
import java.net.URI;

public class QBWebSocketClient extends WebSocketClient {

    private final MessageListener messageListener;

    public QBWebSocketClient(String serverUri, MessageListener messageListener) {
        super(URI.create(serverUri));
        this.messageListener = messageListener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // When WebSocket connection is opened
        Log.i("SYED_onOpen", String.valueOf(handshakedata));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // When WebSocket connection is closed
        Log.i("SYED_onClose", reason);
    }

    @Override
    public void onMessage(String message) {
        // When a message is received
        if (message != null) {
            messageListener.onMessage(message);
        } else {
            messageListener.onMessage("");
        }
    }

    @Override
    public void onError(Exception ex) {
        // When an error occurs
        Log.i("SYED_ERROR", String.valueOf(ex));
    }

    public void sendMessage(String message) {
        send(message);
    }
    public void sendScanMessage(String message) {
        send(message);
    }

    // Listener interface for message handling
    public interface MessageListener {
        void onMessage(String message);
    }


}
