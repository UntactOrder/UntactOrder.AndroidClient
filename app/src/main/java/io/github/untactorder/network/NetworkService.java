package io.github.untactorder.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NetworkService extends Service {
    public NetworkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}