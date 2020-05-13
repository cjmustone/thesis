package com.thesis.test;
//TODO: REAL INPUT VALIDATION
//TODO: RaspPi Link when creating - new class?

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class MyLoginActivity extends AppCompatActivity implements OnClickListener {
    private FirebaseAuth mAuth;
    private Button loginButton;
    private Button signUpButton;
    private EditText emailText;
    private EditText passText;
    private TextView signupText;
    private DatabaseReference mDatabase;
    String test = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);


        // Initialize all views, view = findViewById(R.id.VIEW_NAME
        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.buttonLogin);
        signUpButton = findViewById(R.id.buttonSignUp);
        emailText = findViewById(R.id.editEmail);
        passText = findViewById(R.id.editPassword);
        signupText = findViewById(R.id.signupText);
        signupText.setText(signupText.getText());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI(currentUser);
        } else {
            Log.d(test,"user was null, failed");
        }
    }

    private void signIn(String email, String password) {
        Log.d(test, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(test, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String token = FirebaseInstanceId.getInstance().getToken();
                            mDatabase.child("users").child(user.getUid()).child("token").setValue(token);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(test, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MyLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                      //  if (!task.isSuccessful()) {
                          //  mStatusTextView.setText(R.string.auth_failed);
                      //  }
                       // hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    //TODO: REAL INPUT VALIDATION
    //TODO: https://github.com/firebase/quickstart-android/blob/0d6a333f3d8574ebaf101ba4a4f43007797a8924/database/app/src/main/java/com/google/firebase/quickstart/database/java/SignInActivity.java#L158-L162
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(emailText.getText().toString())) {
            emailText.setError("Required");
            result = false;
        } else {
            emailText.setError(null);
        }

        if (TextUtils.isEmpty(passText.getText().toString())) {
            passText.setError("Required");
            result = false;
        } else {
            passText.setError(null);
        }

        return result;
    }


    public void signOut() {
        mAuth.signOut();
        updateUI(null);
    }


    public void updateUI(FirebaseUser curUser){
        if (curUser == null){
            Intent profileIntent = new Intent(this,  MyLoginActivity.class);
            startActivity(profileIntent);
            finish();
        } else {
            Intent profileIntent = new Intent(this,  MainActivity.class);
            //  profileIntent.putExtra("email",curUser.getEmail());
            startActivity(profileIntent);
            finish();
        }
    }


    /*
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }
    */

    @Override
    public void onClick(View v) {
        int clickedId = v.getId();

        if (clickedId == R.id.buttonLogin) {
            signIn(emailText.getText().toString(), passText.getText().toString());
        } else if (clickedId == R.id.buttonSignUp){
            //TODO: RaspPi Link when creating
            //createAccount(emailText.getText().toString(),passText.getText().toString());
            Intent profileIntent = new Intent(this,  SignUpActivity.class);
            startActivity(profileIntent);
            finish();
        }
    }
}
