package io.github.untactorder.network;

import android.app.Service;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * <세션계층>
 * 소켓생성 o
 * 서버연결 o
 * ip, port저장 -> NetworkService
 * send o
 * receive o
 * @author 유채민
 */
public interface SessionLayer {
    Socket clientSocket = new Socket();

    default void connect(String ip, Integer port) {
        if (ip != null && port != null) {
            try{
                System.out.println("<client> connecting to: "+ip+":"+port);
                clientSocket.connect(new InetSocketAddress(ip, port));
                System.out.println("<client> connected to server.");
            } catch (IOException e){
                e.printStackTrace();
                System.out.println("<client> connection failed.");
            }
        } else {
            System.out.println("<client> server address is not composed.");
        }
    }

    default void send(String data) throws IOException {
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(output));
        out.println(data);
        out.flush();
    }

    default String recv() throws IOException {
        InputStream input = clientSocket.getInputStream();
        while (input.available() == 0);
        byte[] byteArray = new byte[input.available()];
        int size = input.read(byteArray);
        if (size == -1) {
            throw new IOException("Error: recv() error! (wrong byte array size)");
        }
        String recvmsg = new String(byteArray, 0, size, StandardCharsets.UTF_8); // JSON -> String
        System.out.println("receive: "+ recvmsg);
        return recvmsg;
    }
}
