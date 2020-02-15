package com.thesis.test;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

       // getToken();
    }

    private void getToken(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mdata= databaseRef.child("users").child("Demo");
        mdata.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
    }


    @Override
    public void onClick(View v) {
        MyLoginActivity mla = new MyLoginActivity();
        mla.signOut();
    }
}
