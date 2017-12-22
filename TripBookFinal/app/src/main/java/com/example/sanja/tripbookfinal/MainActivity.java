package com.example.sanja.tripbookfinal;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText emailLogin;
    EditText passLogin;
    Button buttonLogin;
    Button buttonSignup;

    ArrayList<User> userArrayList=new ArrayList<>();

    final String User = "details";
    SignInButton googleSignIn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    LocationManager locationManager;
    LocationListener locationListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserRef = mRootRef.child("User");
    GoogleApiClient mGoogleApiClient;
    final int RC_SIGN_IN = 107;


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Summer Trip");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        emailLogin = (EditText)findViewById(R.id.editTextEmail);
        passLogin = (EditText)findViewById(R.id.editTextPass);
        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonSignup = (Button)findViewById(R.id.buttonSignUp);

        mAuth = FirebaseAuth.getInstance();

        googleSignIn = (SignInButton)findViewById(R.id.googleSignIn);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList = new ArrayList<User>();

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

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(MainActivity.this, "Cannot connect. Try again later", Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){

                    Intent intent = new Intent(MainActivity.this, UserAccountActivity.class);
                    startActivity(intent);
                }

            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailLogin.getText().toString();
                String password = passLogin.getText().toString();

                if(email.length()==0 | password.length()==0){

                    Toast.makeText(MainActivity.this, "Enter correct details", Toast.LENGTH_SHORT).show();
                }
                else{

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                User user = getUserFullDetails(email);   //method call
                                if(user!= null){
                                    Intent intent = new Intent(MainActivity.this, UserAccountActivity.class);
                                    intent.putExtra(User, user);
                                    startActivity(intent);
                                }

                            }
                            else {
                                Toast.makeText(MainActivity.this, "Enter Correct Details", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }


            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d("demo", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("demo", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*updateUI(user);*/
                            Log.d("demo","inside1");
                            Intent intent = new Intent(MainActivity.this,UserAccountActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("demo", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            /*updateUI(null);*/
                        }

                        // ...
                    }
                });

    }

    private User getUserFullDetails(final String email) {

        for(int i=0;i<userArrayList.size();i++){
            User user = userArrayList.get(i);
            if(user.getEmail().equals(email)){
                Log.d("demo","CONTAINS USER DETAILS");
                return user;
            }
            else {
                continue;
            }

        }
        return null;
    }
}
