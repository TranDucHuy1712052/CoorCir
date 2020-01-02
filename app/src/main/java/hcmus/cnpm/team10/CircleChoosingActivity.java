package hcmus.cnpm.team10;

import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.circle.Circle;
import hcmus.cnpm.team10.circle.CircleListAdapter;
import hcmus.cnpm.team10.user.User;
import hcmus.cnpm.team10.utils.api.APIService;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CircleChoosingActivity extends AppCompatActivity {

    public class CircleDownloadReceiver extends BroadcastReceiver{

        IntentFilter filter;
        AlertDialog  dialog;

        public CircleDownloadReceiver(){
            filter = new IntentFilter(Constants.DOWNLOAD_CIRCLE_ACTION);

            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(CircleChoosingActivity.this);
            dialogBuilder.setTitle("Circle Download").setMessage("Please wait....");
            dialog = dialogBuilder.create();
        }


        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("result");
            dialog.setMessage("Download success, loading...");
            try {
                JSONArray array = new JSONArray(result);
                Log.d(this.getClass().toString(), String.valueOf(array.length()));
                for(int i = 0; i < array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    Circle c = new Circle(obj);
                    mAdapter.addCircle(c);
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.setMessage("Error in loading...");
            }
            finally {
                dialog.cancel();
            }

        }

    }

    private CircleListAdapter mAdapter;
    private CircleDownloadReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_choosing);


        setupCircleListView();

        // Register Receiver before send intent
        mReceiver = new CircleDownloadReceiver();
        registerReceiver(mReceiver, mReceiver.filter);

        Intent input_intent = getIntent();
        // Call download
        HashMap<String, String> parameters = new HashMap<>(1);
        parameters.put("uid", input_intent.getStringExtra("uid"));
        Intent intent = APIService.createIntent(getBaseContext(), Constants.DOWNLOAD_CIRCLE_ACTION,
                Constants.DOWNLOAD_CIRCLE_QUERY, parameters);
        startService(intent);
        mReceiver.dialog.show();


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

    private void setupCircleListView(){
        ListView listView = findViewById(R.id.list_view_circles);
        // ArrayList<Circle> circles = createDebugCircleList();
         mAdapter = new CircleListAdapter(getBaseContext(), R.layout.list_item_circle, new ArrayList<Circle>());

        listView.setAdapter(mAdapter);

    }


    private ArrayList<Circle> createDebugCircleList(){
        ArrayList<Circle> circles = new ArrayList<>();
        Circle c1 = new Circle(
                "111",
                "HCMUS_Student",
                1,
                null,
                null
        );

        for (int i = 0; i < 5; i++)
            c1.addUser(new User());

        circles.add(c1);

        Circle c2 = new Circle(
                "112",
                "NMCNPM_Team10",
                1,
                null,
                null
        );
        circles.add(c2);



        return circles;

    }


}
