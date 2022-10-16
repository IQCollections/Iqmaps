package com.highiq.iqmaps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;

public class LoginTabFragment extends Fragment {

    private EditText email, pass;
    private TextView forgetPass;
    private Button login;
    private float v = 0;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflator.inflate(R.layout.fragment_login_tab, container, false);

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        forgetPass = root.findViewById(R.id.forget_pass);
        login = root.findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgetPass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgetPass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgetPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPass = encrypt(pass.getText().toString());
                if (userEmail.isEmpty() || userPass.isEmpty()) {


                } else {
                    mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Context context = view.getContext();
                                Toast.makeText(context, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser = mAuth.getCurrentUser();

                                Intent intent = new Intent(context,PermissionsActivity.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText(getContext(), "An Error has occurred, check your username and password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return root;
    }

    public String encrypt(String password){
        String result ="";
       try{
           MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
           digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

           StringBuffer MD5Hash = new StringBuffer();

           for (int i=0; i < messageDigest.length;i++){
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while(h.length() < 2){
                    h= "0" + h;
                }
                MD5Hash.append(h);
           }
           result = MD5Hash.toString();

       }catch (Exception e){

       }finally {
           return result;
       }


    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}
