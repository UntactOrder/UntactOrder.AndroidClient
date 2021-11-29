package io.github.untactorder.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import io.github.untactorder.data.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public enum RequestType {TableCheck, SignUp, SignIn, GetMenuList, PutNewOrder, GetOrderList}

    public static List<String> RESULT_ARRAY = Collections.synchronizedList(new ArrayList<>());

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
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            Intent resultIntent = new Intent();
            RequestType command = (RequestType) intent.getSerializableExtra("command");
            switch (command) {
                case TableCheck: {
                    try {
                        String check = tableCheck(
                                Customer.getId(),Customer.getIp(),Customer.getPort());
                        Customer.setStatus(check);
                        //resultIntent.putExtra("tableCheck", check);
                    } catch (IOException e) {

                    }
                    break;
                }
                case SignUp: {
                    try {
                        String result = signUp(Customer.getId(),Customer.getPw());
                        //resultIntent.putExtra("sign_up", result);
                    } catch(IOException e) {

                    }
                    break;
                }
                case SignIn: {
                    String pw = intent.getStringExtra("pw");
                    try {
                        String result = signIn(Customer.getId(),pw);
                        //resultIntent.putExtra("sign_in", result);
                    } catch (IOException e) {

                    }
                    break;
                }
                case GetMenuList: {
                    try {
                        String getMenuList = getMenuList();
                        resultIntent.putExtra("getMenuList",getMenuList);
                    } catch (IOException e) {

                    }
                    break;
                }
                case PutNewOrder: {
                    String order = intent.getStringExtra("order");
                    try {
                        String putNewOrder = putNewOrder(order);
                        resultIntent.putExtra("putNewOrder",putNewOrder);
                    } catch (IOException e) {

                    }
                    break;
                }
                case GetOrderList: {
                    try {
                        Map<String, Object> getOrderList = getOrderList(Customer.getId());

                    } catch (IOException e) {

                    }
                    break;
                }
            }
            //startActivity(resultIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
