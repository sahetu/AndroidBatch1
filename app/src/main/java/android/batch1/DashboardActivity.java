package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //new CommonMethod(DashboardActivity.this,"Message");

        Bundle bundle = getIntent().getExtras();
        String sEmail = bundle.getString("EMAIL");
        String sPassword = bundle.getString("PASSWORD");
        Log.d("RESPONSE",sEmail+"\n"+sPassword);

        email = findViewById(R.id.dashboard_email);
        email.setText(sEmail);

    }
}