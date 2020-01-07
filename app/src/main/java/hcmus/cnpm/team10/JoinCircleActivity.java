package hcmus.cnpm.team10;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.circle.Circle;
import hcmus.cnpm.team10.user.CurrentUser;
import hcmus.cnpm.team10.utils.api.APIService;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class JoinCircleActivity extends AppCompatActivity {

    private String      mPreviousCid = null;
    private Circle      mTempCircle  = null;
    private AlertDialog mWaitDialog  = null;

    private final static int GET_CIRCLE_INFORMATION_REQUEST = 1001;

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
                Toast.makeText(getBaseContext(), "Join Circle success", Toast.LENGTH_LONG).show();
                dialog.cancel();

                Intent data = new Intent();
                String circleId = getCircleIdFromEditText();

                data.putExtra("circleId", circleId);
                data.putExtra("circle", mTempCircle);

                mTempCircle.addUser(CurrentUser.getInstance());


                JoinCircleActivity.this.setResult(RESULT_OK, data);
                JoinCircleActivity.this.finish();




            }

            if(dialog.isShowing())
                dialog.cancel();
        }
    }

    private String getCircleIdFromEditText() {
        EditText editText = findViewById(R.id.edit_text_circle_id);
        return editText.getText().toString();
    }

    private JoinCircleReceiver mReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_circle);

        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setTitle("Download Circle Information");
        builder.setMessage("Please wait");
        mWaitDialog = builder.create();

        mReceiver = new JoinCircleReceiver();
        Button btnJoin = findViewById(R.id.btn_join_circle);

        btnJoin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String circleId = getCircleIdFromEditText();

                        HashMap<String, String> parameters = new HashMap<>(2);
                        parameters.put("cid", circleId);
                        parameters.put("uid", CurrentUser.getUserId());

                        Intent intent = APIService.createIntent(
                                getBaseContext(),
                                Constants.JOIN_CIRCLE_ACTION,
                                Constants.JOIN_CIRCLE_QUERY,
                                parameters
                        );
                        startService(intent);
                    }
                }
        );

        registerReceiver(mReceiver, mReceiver.filter);

        final EditText editTextCircleId = findViewById(R.id.edit_text_circle_id);
        editTextCircleId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cid = getCircleIdFromEditText();

                if(cid.length() != 7)
                    return;

                if(mPreviousCid == null || !editTextCircleId.getText().toString().equals(mPreviousCid)){
                        Intent intent = new Intent(getBaseContext(), GetCircleInformationActivity.class);
                        intent.putExtra("cid", cid);
                        startActivityForResult(intent, GET_CIRCLE_INFORMATION_REQUEST);
//                        mWaitDialog.show();
                        mPreviousCid = cid;
                    }
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_CIRCLE_INFORMATION_REQUEST && resultCode == RESULT_OK){
            mTempCircle = (Circle) data.getSerializableExtra("circle");

            EditText editTextCircleName = findViewById(R.id.edit_text_circle_name);
            editTextCircleName.setText(mTempCircle.getName());
            editTextCircleName.setEnabled(false);

            Button btnJoin = findViewById(R.id.btn_join_circle);
            Log.i(this.getClass().toString(), data.toString());
            boolean isJoined = data.getBooleanExtra("joined", true);
            btnJoin.setEnabled(!isJoined);

            if(mWaitDialog.isShowing())
                mWaitDialog.hide();

        }

    }
}
