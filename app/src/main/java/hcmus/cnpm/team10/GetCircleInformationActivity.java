package hcmus.cnpm.team10;

import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.circle.Circle;
import hcmus.cnpm.team10.user.CurrentUser;
import hcmus.cnpm.team10.utils.api.APIService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GetCircleInformationActivity extends AppCompatActivity {

    public class Receiver extends BroadcastReceiver{

        private final IntentFilter filter;

        Receiver(){
            filter = new IntentFilter(Constants.DOWNLOAD_CIRCLE_INFO_ACTION);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int result_code = RESULT_CANCELED;
            Intent output_intent = null;
            String result = intent.getStringExtra("result");

            try {
                JSONObject data = new JSONObject(result);
                Circle c = new Circle(data);
                output_intent = new Intent();
                output_intent.putExtra("circle", c);
                Log.i(this.getClass().toString(), result);
                boolean isJoined = data.getInt("joined") == 1;
                output_intent.putExtra("joined", isJoined);
                result_code = RESULT_OK;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                GetCircleInformationActivity.this.setResult(result_code, output_intent);
                GetCircleInformationActivity.this.finish();
            }
        }
    }

    private Receiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent input_intent = getIntent();
        String cid = input_intent.getStringExtra("cid");

        mReceiver = new Receiver();
        registerReceiver(mReceiver, mReceiver.filter);


        HashMap<String, String> parameters = new HashMap<>(2);
        parameters.put("cid", cid);
        parameters.put("uid", CurrentUser.getUserId());

        Intent service_intent = APIService.createIntent(
                getBaseContext(),
                Constants.DOWNLOAD_CIRCLE_INFO_ACTION,
                Constants.DOWNLOAD_CIRCLE_INFO_QUERY,
                parameters
        );
        startService(service_intent);


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(mReceiver, mReceiver.filter);
    }
}
