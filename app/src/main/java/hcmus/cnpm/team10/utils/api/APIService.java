package hcmus.cnpm.team10.utils.api;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class APIService extends IntentService {


    public APIService() {

        super("API_SERVICE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(this.getClass().toString(), "onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String query = intent.getStringExtra("query");
        HashMap<String, String> parameters = (HashMap) intent.getSerializableExtra("parameters");

        HttpHandler handler = new HttpHandler();
        String result = Constants.EMPTY_TOKEN;
        try {
            result = handler.getStringFromQuery(query, parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == null)
            result = Constants.EMPTY_TOKEN;

        Intent ans_intent = new Intent(intent.getAction());
        ans_intent.putExtra("root_query", query);
        ans_intent.putExtra("result", result);
        Log.i(this.getClass().toString(), "Run Success");

        sendBroadcast(ans_intent);
    }



    public static Intent createIntent(Context context, String action, String query, HashMap<String, String> parameters){
        Intent intent = new Intent(context, APIService.class);
        intent.setAction(action);
        intent.putExtra("query", query);
        intent.putExtra("parameters", parameters);

        return intent;

    }
}
