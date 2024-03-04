package android.batch1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.io.File;

public class RazorpayDemoActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    Button payNow;
    EditText amount;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_demo);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        amount = findViewById(R.id.razorpay_amount);
        payNow = findViewById(R.id.razorpay_pay);

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.getText().toString().trim().equals("")) {
                    amount.setError("Amount Required");
                } else if (amount.getText().toString().trim().equals("0")) {
                    amount.setError("Valid Amount Required");
                } else {
                    startPayment();
                }
            }
        });

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Purchase Deal From " + getResources().getString(R.string.app_name));
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Integer.parseInt(amount.getText().toString()) * 100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", sp.getString(ConstantSp.EMAIL, ""));
            preFill.put("contact", sp.getString(ConstantSp.CONTACT, ""));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(RazorpayDemoActivity.this);
            builder.setTitle("Payment Successfully");
            builder.setMessage("Transaction Id : "+s);
            builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    onBackPressed();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(RazorpayDemoActivity.this);
            builder.setTitle("Payment Failed");
            builder.setMessage(paymentData.getData().toString());
            builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    onBackPressed();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }
    }
}