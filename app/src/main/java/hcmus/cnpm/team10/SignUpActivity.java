package hcmus.cnpm.team10;

import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.utils.bar_handler.ActionBarQuickSetUp;
import hcmus.cnpm.team10.utils.bar_handler.BasicBarHandler;

import android.os.Bundle;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpActionBar("SignUp");


    }

    private void setUpActionBar(String title){
        int backImgId = R.drawable.ic_launcher_background;
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
}
