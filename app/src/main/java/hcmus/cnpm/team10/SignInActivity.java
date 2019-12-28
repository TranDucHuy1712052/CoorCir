package hcmus.cnpm.team10;

import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.utils.api.APIService;
import hcmus.cnpm.team10.utils.api.HttpHandler;

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

import java.io.IOException;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {


    public class LoginQueryReceiver extends BroadcastReceiver{
        private final AlertDialog dialog;
        private final IntentFilter filter;

        private LoginQueryReceiver(){
            filter = new IntentFilter(Constants.LOGIN_ACTION);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignInActivity.this);
            dialogBuilder.setTitle("Login status").setMessage("Please wait....");
            dialog = dialogBuilder.create();
        }


        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(this.getClass().toString(), "OnReceive");

            String result = intent.getStringExtra("result");
            if (result.equals(hcmus.cnpm.team10.utils.api.Constants.EMPTY_TOKEN)
            || result.equals("-1"))
                Toast.makeText(getBaseContext(), "Login fail", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            dialog.cancel();
        }
    }

    private LoginQueryReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        Button btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                        startActivity(intent);

                    }
                }
        );

        mReceiver = new LoginQueryReceiver();
        registerReceiver(mReceiver, mReceiver.filter);


        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HttpHandler handler = new HttpHandler();

                        HashMap<String, String> parameters = new HashMap<>(2);
                        parameters.put("usr", getUsername());
                        parameters.put("p", getPasword());

//                        Intent intent = new Intent(getBaseContext(), APIService.class);
//                        intent.setAction(Constants.LOGIN_ACTION);
//                        intent.putExtra("query", Constants.LOGIN_QUERY);
//                        intent.putExtra("parameters", parameters);

                        Intent intent = APIService.createIntent(getBaseContext(), Constants.LOGIN_ACTION,
                                Constants.LOGIN_QUERY, parameters);
                        startService(intent);
                        mReceiver.dialog.show();

                    }
                }
        );

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

    private String getUsername(){
        EditText editText = findViewById(R.id.edit_text_username);
        return editText.getText().toString();
    }

    private String getPasword(){
        EditText editText = findViewById(R.id.edit_text_password);
        return editText.getText().toString();
    }
}
