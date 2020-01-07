package hcmus.cnpm.team10;

import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.utils.api.APIService;
import hcmus.cnpm.team10.utils.bar_handler.ActionBarQuickSetUp;
import hcmus.cnpm.team10.utils.bar_handler.BasicBarHandler;

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
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    public class SignUpReceiver extends BroadcastReceiver {
        private final AlertDialog  dialog;
        private final IntentFilter filter;

        private SignUpReceiver(){
            filter = new IntentFilter(Constants.SIGNUP_ACTION);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
            dialogBuilder.setTitle("Signup status").setMessage("Please wait....");
            dialog = dialogBuilder.create();
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(this.getClass().toString(), "OnReceive");

            String result = intent.getStringExtra("result");
            if (result.equals(hcmus.cnpm.team10.utils.api.Constants.EMPTY_TOKEN)
                    || result.equals("-1")){
                Toast.makeText(getBaseContext(), "Signup fail", Toast.LENGTH_LONG).show();
                Log.e(this.getClass().toString(), result);
            }
            else
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            dialog.cancel();
        }
    }

    private SignUpReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpActionBar("SignUp");

        // Register receiver to catch finish signup
        mReceiver = new SignUpReceiver();
        registerReceiver(mReceiver, mReceiver.filter);

        // Register event listener for btn
        Button btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // Quick check
                        // Check Radio
                        RadioButton radioBtn = findViewById(R.id.radio_btn_agree_cond);
                        if(!radioBtn.isChecked()){
                            Toast.makeText(getBaseContext(), "You haven't agree to our term and service yet", Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Check password
                        String retypePassword = getStringFromEditText(R.id.edit_text_password_retype);
                        String password = getStringFromEditText(R.id.edit_text_password);
                        if (!retypePassword.equals(password)){
                            Toast.makeText(getBaseContext(), "Retype password incorrect", Toast.LENGTH_LONG).show();
                            return;
                        }


                        // Generate parameters for server
                        String username = getStringFromEditText(R.id.edit_text_username);
                        String phone = getStringFromEditText(R.id.edit_text_phone);
                        String fullname = getStringFromEditText(R.id.edit_text_fullname);
//                        fullname = fullname.replace(" ", "+");

                        HashMap<String, String> parameters = new HashMap<>(4);
                        parameters.put("fn", fullname);
                        parameters.put("phn", phone);
                        parameters.put("usr", username);
                        parameters.put("p", password);

                        if(!isValidParameters(parameters))
                            return;

                        // Send request to server
                        Intent intent = APIService.createIntent(
                                /*context=*/getBaseContext(),
                                /*action=*/Constants.SIGNUP_ACTION,
                                /*query=*/Constants.SIGNUP_QUERY,
                                parameters
                        );
                        startService(intent);

                        mReceiver.dialog.show();
                    }

                    private boolean isValidParameters(HashMap<String, String> parameters){
                        // Make sure no empty string
                        for(String value : parameters.values())
                            if(value.length() == 0){
                                Toast.makeText(getBaseContext(), "Make sure you fill all field", Toast.LENGTH_LONG).show();
                                return false;
                            }

                        // Check maximum length of query
                        if(parameters.get("p").length() > 45){
                            Toast.makeText(getBaseContext(), "Password is too long", Toast.LENGTH_LONG).show();
                            return false;
                        }


                        return true;
                    }
                }
        );


    }

    private void setUpActionBar(String title){
        int backImgId = android.R.drawable.ic_menu_revert;
        View.OnClickListener leftListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };

        BasicBarHandler handler = new BasicBarHandler(
                title,
                backImgId,
                leftListener
        );

        ActionBarQuickSetUp.setUpBar(SignUpActivity.this, handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(this.getClass().toString(), "onPause");
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(this.getClass().toString(), "onPostResume");
        registerReceiver(mReceiver, mReceiver.filter);
    }

    private String getStringFromEditText(int id){
        EditText editText = findViewById(id);
        return editText.getText().toString();
    }

}
