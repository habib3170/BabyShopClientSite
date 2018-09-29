package com.habib4990gmail.babyshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.habib4990gmail.babyshop.common.Common;
import com.habib4990gmail.babyshop.model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPhone.getText().toString().isEmpty() || edtName.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fild is Empty !", Toast.LENGTH_SHORT).show();
                }else {
                    if (Common.isConnectedToInterner(getBaseContext())) {
                        final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                        mDialog.setMessage("Please Waiting");
                        mDialog.show();

                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //Check if already user phone
                                if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, "Phone Number already register", Toast.LENGTH_SHORT).show();
                                } else {
                                    mDialog.dismiss();
                                    User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                    table_user.child(edtPhone.getText().toString()).setValue(user);
                                    Toast.makeText(SignUpActivity.this, "Sign Up Succesfully !", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        Toast.makeText(SignUpActivity.this, "Please check your connection !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }
}
