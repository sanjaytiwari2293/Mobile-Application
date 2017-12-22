package com.example.sanja.tripbookfinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    EditText editTextFName;
    EditText editTextLName;
    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPass;
    EditText editTextRepeatPass;
    RadioGroup radioGroup;
    RadioButton radioButtonMale;
    RadioButton radioButtonFemale;
    Button buttonCancel;
    Button buttonSignup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String gender = null;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        setTitle("SignUp");

        mAuth = FirebaseAuth.getInstance();

        editTextFName = (EditText)findViewById(R.id.editTextFirstName);
        editTextLName = (EditText)findViewById(R.id.editText2LName);
        editTextUserName = (EditText)findViewById(R.id.editTextUsername);
        editTextEmail = (EditText)findViewById(R.id.editTextEmailSignup);
        editTextPass = (EditText)findViewById(R.id.editTextPassSignup);
        editTextRepeatPass = (EditText)findViewById(R.id.editTextRepeatPass);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioButtonMale = (RadioButton)findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton)findViewById(R.id.radioButtonFemale);
        buttonCancel = (Button)findViewById(R.id.buttonCancelSignup);
        buttonSignup = (Button)findViewById(R.id.buttonSignupSignup);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = editTextFName.getText().toString();
                String lname = editTextLName.getText().toString();
                String uname = editTextUserName.getText().toString();
                String email = editTextEmail.getText().toString();
                String pass = editTextPass.getText().toString();
                String rpass = editTextRepeatPass.getText().toString();

                int rId = radioGroup.getCheckedRadioButtonId();
                if (rId ==R.id.radioButtonMale){
                    gender ="Male";
                }else{
                    gender ="Female";
                }

                if(fname.length()==0 | lname.length()==0 | uname.length()==0 | email.length()==0 | pass.length()==0 |
                        rpass.length()==0 | rId== -1 | !(pass.equals(rpass)) | !(email.contains("@"))){

                    Toast.makeText(SignupActivity.this, "Enter correct details", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("Success","");
                    signUpFunction(fname, lname, uname, email, pass, rpass, gender);

                }

            }
        });

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> userArrayList = new ArrayList<User>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);
                    userArrayList.add(user);

                }
                Log.d("demo","datasnapshot UserRef"+dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    private void signUpFunction(final String fname, final String lname, final String uname, final String email, final String pass, String rpass, final String gender) {

        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fname+" "+lname).build();
                    user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(SignupActivity.this, "Successfully added the user", Toast.LENGTH_SHORT).show();

                                User user1 = new User();
                                user1.setFname(fname);
                                user1.setLname(lname);
                                user1.setGender(gender);
                                user1.setUname(uname);
                                user1.setEmail(email);
                                user1.setPassword(pass);

                                DatabaseReference mSubUsersRef = mUserRef.push();
                                String key = mSubUsersRef.getKey();
                                user1.setUid(key);
                                Log.d("KEY ",""+mSubUsersRef.getKey());
                                mSubUsersRef.setValue(user1);


                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });

                }
                else {

                    Toast.makeText(SignupActivity.this, "Error occured. Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
