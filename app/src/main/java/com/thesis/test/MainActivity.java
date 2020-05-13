package com.thesis.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(this);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        TextView textView = findViewById(R.id.textView);
        TextView standByText = findViewById(R.id.standBy);
        textView.setText(textView.getText());
        standByText.setText(standByText.getText());

        //startFragment();
       // getToken();
    }

    private void startFragment() {
        Fragment fragment = new Serial();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }

    private void getToken(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mdata= databaseRef.child("users").child("Demo");
        mdata.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
    }


    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, MyLoginActivity.class));
    }
}
