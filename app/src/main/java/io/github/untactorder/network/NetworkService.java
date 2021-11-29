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

    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null) {
            return Service.START_STICKY;
        } else {

        }
        return super.onStartCommand(intent, flags, startId);
    }

}
