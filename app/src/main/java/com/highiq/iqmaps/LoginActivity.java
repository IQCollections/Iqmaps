package com.highiq.iqmaps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gsi;
    private GoogleSignInClient gsc;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton google;
    private float v = 0;
    private Button btnRegister, btnLogin;
    private EditText edtLoginUser, edtLoginPassword, edtRegisterEmail, edtRegisterUser, edtRegisterPass1, edtRegisterPass2;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            tabLayout = findViewById(R.id.tab_layout);
            viewPager = findViewById(R.id.view_pager);
            //  google = findViewById(R.id.fab_google);
            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);
            edtLoginUser = findViewById(R.id.email);
            edtLoginPassword = findViewById(R.id.pass);
            edtRegisterUser = findViewById(R.id.username);
            edtRegisterEmail = findViewById(R.id.email2);
            edtRegisterPass1 = findViewById(R.id.pass2);
            edtRegisterPass2 = findViewById(R.id.confirm_pass2);
            gsi = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            gsc = GoogleSignIn.getClient(LoginActivity.this, gsi);

            tabLayout.addTab(tabLayout.newTab().setText("Login"));
            tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            // google.setTranslationY(300);
            tabLayout.setTranslationY(300);


            //    google.setAlpha(v);
            tabLayout.setAlpha(v);
            //startActivity(new Intent(this,LoginTabFragment.class));

            //  google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
            //   google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
            //  google.setOnClickListener(new View.OnClickListener() {
            //     @Override
            //     public void onClick(View view) {
            //         googleSignIn();
            //     }
            // });

        }


        public void toaster(String message){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


        public void googleSignIn(){
            Intent signInIntent  = gsc.getSignInIntent();
            startActivityForResult(signInIntent,1000);


        }
        public void navFromGoogle(){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==1000){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                try{
                    task.getResult(ApiException.class);
                    navFromGoogle();
                } catch (ApiException e) {
                    Toast.makeText(this, "An error has occurred"+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }

        }
    }