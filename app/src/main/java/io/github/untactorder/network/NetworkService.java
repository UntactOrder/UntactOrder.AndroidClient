package io.github.untactorder.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 보내기
 * protected 주로 사용할 것!
 * ip, 포트번호 지정
 * @author 유채민
 */
public class NetworkService extends Service implements ApplicationLayer {
    String ip;
    Integer port;

    public NetworkService() {
    }

    public NetworkService(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //practice
    public void main(String[] args) {
        try {
            send("d");
            connectToServer(ip,port);
            recv();
        } catch (Exception e) {}
    }
    //

}