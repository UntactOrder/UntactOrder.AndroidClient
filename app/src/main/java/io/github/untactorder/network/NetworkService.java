package io.github.untactorder.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 보내기
 * protected 주로 사용할 것!
 * ip, 포트번호 지정 o
 * @author 유채민
 * activity -> service : ip/port/tablecheck return값 넘기기
 */
public class NetworkService extends Service implements ApplicationLayer {
    private static final String TAG = "NetworkService";
    public static String IP;
    public static Integer PORT;
    public static Integer TABLE_NAME;
    public static String PASSWORD;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() 호출됨.");
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy() 호출됨.");
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() 호출됨.");
        if(intent == null) {
            return Service.START_STICKY;
        } else {
            Intent showintent = new Intent();
            String command = intent.getStringExtra("command");
            switch(command) {
                case "tableCheck":  {
                    tableName = Integer.parseInt(intent.getStringExtra("tableName"));
                    try {
                        String check = tableCheck(tableName,ip,port);
                        showintent.putExtra("tableCheck",check);
                    } catch(IOException e) {}
                    break;
                }
                case "sign_up": {
                    int id = Integer.parseInt(intent.getStringExtra("id"));
                    String pw = intent.getStringExtra("pw");
                    try{
                        String signUp = signUp(id,pw);
                        showintent.putExtra("sign_up",signUp);
                    } catch(IOException e) {}
                    break;
                }
                case "sign_in": {
                    int id = Integer.parseInt(intent.getStringExtra("id"));
                    String pw = intent.getStringExtra("pw");
                    try{
                        String signIn = signIn(id,pw);
                        showintent.putExtra("sign_in",signIn);
                    } catch(IOException e) {}
                    break;
                }
                case "getMenuList": {
                    try{
                        String getMenuList = getMenuList();
                        showintent.putExtra("getMenuList",getMenuList);
                    } catch(IOException e) {}
                    break;
                }
                case "putNewOrder": {
                    String order = intent.getStringExtra("order");
                    try{
                        String putNewOrder = putNewOrder(order);
                        showintent.putExtra("putNewOrder",putNewOrder);
                    } catch(IOException e) {}
                    break;
                }
                case "getOrderList": {
                    tableName = Integer.parseInt(intent.getStringExtra("tableName"));
                    try{
                        String getOrderList = getOrderList(tableName);
                        showintent.putExtra("getOrderList",getOrderList);
                    } catch(IOException e) {}
                    break;
                }
            }
            startActivity(showintent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

}
