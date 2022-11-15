package com.highiq.iqmaps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;

public class SignUpTabFragment extends Fragment {
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseUser uid;
    private DatabaseReference dbRef;
    private EditText edtLoginUser, edtLoginPassword, edtRegisterEmail, edtRegisterUser, edtRegisterPass1, edtRegisterPass2;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflator.inflate(R.layout.fragment_sign_up_tab, container, false);
        mAuth = FirebaseAuth.getInstance();
        edtRegisterEmail = root.findViewById(R.id.email2);
        edtRegisterUser = root.findViewById(R.id.username);
        edtRegisterPass1 = root.findViewById(R.id.pass2);
        edtRegisterPass2 = root.findViewById(R.id.confirm_pass2);
        btnRegister = root.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        edtRegisterEmail = root.findViewById(R.id.email2);
        edtRegisterUser = root.findViewById(R.id.username);
        edtRegisterPass1 = root.findViewById(R.id.pass2);
        edtRegisterPass2 = root.findViewById(R.id.confirm_pass2);
        btnRegister = root.findViewById(R.id.btnRegister);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Settings");
        uid = FirebaseAuth.getInstance().getCurrentUser();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity lg = new LoginActivity();
                String userEmail = edtRegisterEmail.getText().toString();
                String userPass = encrypt(edtRegisterPass1.getText().toString());
                String userPasscon = encrypt(edtRegisterPass2.getText().toString());

                if (userEmail.isEmpty() || userPass.isEmpty() || (!userPass.equals(userPasscon))) {
                    Toast.makeText(getActivity(), "You cannot leave the fields blank/Check if your passwords match", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                SettingsClass settingsClass = new SettingsClass("", "", "Kms", "Light Mode", "");
                                dbRef.child(uid.getUid()).setValue(settingsClass);
                                Toast.makeText(getActivity(), "Succesfull Sign Up!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "An error has occured" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


        return root;
    }

    public void register() {
        LoginActivity lg = new LoginActivity();
        String userEmail = edtRegisterEmail.getText().toString();
        String userPass = edtRegisterUser.getText().toString();
        String userPasscon = edtRegisterPass2.getText().toString();
        if (userEmail.isEmpty() || userPass.isEmpty() || (!userPass.equals(userPasscon))) {

        } else {

            mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {


                        lg.toaster("You may now sign in");

                    } else {
                        lg.toaster("An Error has occurred " + task.getException());
                    }
                }
            });

        }


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


}