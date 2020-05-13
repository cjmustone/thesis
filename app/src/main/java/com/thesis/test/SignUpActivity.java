package com.thesis.test;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignUpActivity extends AppCompatActivity implements OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String test = "test";
    private EditText userEmail;
    private EditText userPass;
    private EditText userPassConfirm;
    private EditText wifiName;
    private String deviceMAC;
    private Button finishButton;
    private String bssid;

    //TODO: SET ON CLICK LISTENER AND CREATE ONCLICK METHOD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.emailSignUp);
        userPass = findViewById(R.id.passSignUp);
        userPassConfirm = findViewById(R.id.passSignUpConf);
        finishButton = findViewById(R.id.butFinish);
        wifiName = findViewById(R.id.wifiName);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        deviceMAC = "PLACEHOLDER";
        finishButton.setOnClickListener(this);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifi = wifiManager.getConnectionInfo();
        bssid = wifi.getBSSID();
        Log.d(test, "bssid: " + bssid);
    }

    //TODO: RaspPi Link when creating
    private void createAccount(String email, String password) {
        Log.d(test, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(test, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            String token = FirebaseInstanceId.getInstance().getToken();

                            assert user != null;
                            writeNewUser(user.getUid(),user.getDisplayName(),user.getEmail(),deviceMAC,bssid,token);

                            // TODO save user.getUid() to variable to give to pi in order for it to know where to store alert
                            updateUI(user);
                            //TODO GO TO MAIN ACTIVITY
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(test, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    //TODO: REAL INPUT VALIDATION
    //TODO: https://github.com/firebase/quickstart-android/blob/0d6a333f3d8574ebaf101ba4a4f43007797a8924/database/app/src/main/java/com/google/firebase/quickstart/database/java/SignInActivity.java#L158-L162
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(userEmail.getText().toString())) {
            userEmail.setError("Required");
            result = false;
        } else {
            userEmail.setError(null);
        }

        if (TextUtils.isEmpty(userPass.getText().toString())) {
            userPass.setError("Required");
            result = false;
        } else if (!userPass.getText().toString().equals(userPassConfirm.getText().toString())){
            Toast.makeText(SignUpActivity.this, "Passwords don't match",
                    Toast.LENGTH_SHORT).show();
            userPass.setError("passwords don't match");
            result = false;

        } else {
            userPass.setError(null);
        }

        return result;
    }

    private void writeNewUser(String userId, String name, String email, String mac, String bssid, String token) {
        User user = new User(name, email);
        //TODO: SYNC WITH PI VIA SERIAL CONNNECTION Activit
        //TODO: Return Data, load data to database
        mDatabase.child("users").child(userId).child("Email").setValue(email);
        mDatabase.child("users").child(userId).child("Device").setValue(mac);
        mDatabase.child("users").child(userId).child("token").setValue(token);
        mDatabase.child("users").child(userId).child("bssid").setValue(bssid);
    }

    public void updateUI(FirebaseUser curUser){
        if (curUser == null){
            Intent profileIntent = new Intent(this,  SignUpActivity.class);
            startActivity(profileIntent);
            finish();
        } else {
            Intent profileIntent = new Intent(this,  MainActivity.class);
            profileIntent.putExtra("email",curUser.getEmail());
            startActivity(profileIntent);
            finish();
        }
    }



    //TODO need button to set onclick listener to
    @Override
    public void onClick(View v) {
        int clickedId = v.getId();

        if (clickedId == R.id.butFinish){
            createAccount(userEmail.getText().toString(),userPass.getText().toString());
            //Intent profileIntent = new Intent(this,  MainActivity.class);
            //startActivity(profileIntent);
            //finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

