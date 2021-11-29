package io.github.untactorder.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 보내기
 * protected 주로 사용할 것!
 * ip, 포트번호 지정 o
 * @author 유채민
 */
public class NetworkService extends Service implements ApplicationLayer {
    private String ip;
    private Integer port;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected void setServerInfo(ServerInfo info) {
        ip = info.getIp(); port = info.getPort();
    }

    protected ServerInfo getServerInfo() {
        return new ServerInfo(ip, port);
    }

    //practice
    public void main(String[] args) {
        try {
            send("d");
            connectToServer(ip, port);
            recv();
        } catch (Exception e) {}
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("password", "123");
            jsonData.put("id", "123");
        } catch (JSONException e) {
            System.out.println("Wrong data");
        }
        String data = jsonData.toString();

    }
    //

}

class ServerInfo {
    private String ip;
    private Integer port;

    ServerInfo(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    String getIp() {
        return this.ip;
    }

    Integer getPort() {
        return this.port;
    }
}