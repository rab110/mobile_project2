package com.example.project;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.TextUtilsCompat;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity {
    EditText Rusername, Rpass, Rpass2, email ;
    Button register ;
    TextView login ;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStor;
    String user_ID;
    public static final String TAG = "TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Rusername = findViewById(R.id.re_user);
        Rpass = findViewById(R.id.re_pass);
        Rpass2 = findViewById(R.id.re_pass2);
        email = findViewById(R.id.Email);
        register = findViewById(R.id.btn_create);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        login = findViewById(R.id.loginACTV);
        fStor = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Femail = email.getText().toString().trim();
                String Fpass = Rpass.getText().toString().trim();
                String Fpass2 = Rpass2.getText().toString().trim();
                String fname = Rusername.getText().toString();

                if (TextUtils.isEmpty(Femail)){
                    email.setError("الرجاء إدخال إيميل فعال");
                    return;
                }
                if (TextUtils.isEmpty(Fpass)){
                    Rpass.setError("الرجاء ادخال كلمة مرور");
                    return;
                }
                if(Fpass.length() < 8){
                    Rpass.setError("يجب على كلمة المرور ان تكون مكونه من 8 خانات او أكثر");
                    return;
                }
                if (Fpass.equals(Fpass2)) {

                } else {
                    Rpass2.setError("الرجاء إدخال كلمة سر مطابقة");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(Femail,Fpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(registration.this, "تم إنشاء الحساب", Toast.LENGTH_SHORT).show();
                            user_ID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStor.collection("users").document(user_ID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",Rusername);
                            user.put("Email", email);
                            /* db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "onSuccess: تم إنشاء الحساب "+ user_ID+documentReference.getId());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "حدث خطأ في العملية",e);
                                }
                            }); */

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: تم إنشاء الحساب لـ"+ user_ID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        } else {
                            Toast.makeText(registration.this, "نعتذر حدث خطأ, يرجى إعاده المحاولة لاحقا"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


} }
