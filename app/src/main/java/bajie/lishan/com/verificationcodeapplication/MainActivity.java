package bajie.lishan.com.verificationcodeapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import bajie.lishan.com.verificationcodelibrary.VerificationCodeInput;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerificationCodeInput verificationCodeInput = findViewById(R.id.m_code_et);
        verificationCodeInput.setOnTextChangeListener(new VerificationCodeInput.Listener() {
            @Override
            public void onTextChange(String content) {
                Log.d("VINPUT onTextChange",content);
            }

            @Override
            public void onTextComplete(String content) {
                Log.d("VINPUT onTextComplete",content);
            }
        });
    }
}
