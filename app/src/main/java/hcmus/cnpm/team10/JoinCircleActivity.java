package hcmus.cnpm.team10;

import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.user.CurrentUser;
import hcmus.cnpm.team10.utils.api.APIService;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class JoinCircleActivity extends AppCompatActivity {

    public class JoinCircleReceiver extends BroadcastReceiver {
        private final AlertDialog  dialog;
        private final IntentFilter filter;

        private JoinCircleReceiver(){
            filter = new IntentFilter(Constants.JOIN_CIRCLE_ACTION);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(JoinCircleActivity.this);
            dialogBuilder.setTitle("Join circle status").setMessage("Please wait....");
            dialog = dialogBuilder.create();
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(this.getClass().toString(), "OnReceive");

            String result = intent.getStringExtra("result");
            if (result.equals(hcmus.cnpm.team10.utils.api.Constants.EMPTY_TOKEN)
                    || result.equals("-1"))
                Toast.makeText(getBaseContext(), "Join fail", Toast.LENGTH_LONG).show();
            else{
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
                CurrentUser.setUserId(result);
                dialog.cancel();
            }

            if(dialog.isShowing())
                dialog.cancel();
        }
    }

    private JoinCircleReceiver mReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);


        mReceiver = new JoinCircleReceiver();
        Button btnJoin = findViewById(R.id.btn_join_circle);

        btnJoin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = findViewById(R.id.edit_text_circle_id);
                        String circleId = editText.getText().toString();

                        HashMap<String, String> parameters = new HashMap<>(2);
                        parameters.put("cid", circleId);
                        parameters.put("uid", CurrentUser.getUserId());

                        Intent intent = APIService.createIntent(
                                getBaseContext(),
                                Constants.JOIN_CIRCLE_ACTION,
                                Constants.JOIN_CIRCLE_QUERY,
                                null
                        );
                        startService(intent);
                    }
                }
        );

        registerReceiver(mReceiver, mReceiver.filter);


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
