package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import static com.example.project.R.id.registerACT;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "project";
    Button login;
    EditText Lemail, Lpass;
    TextView registerBtn, forgetBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
   //private int counter = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(Tag,"OnCreate Called");
        login=findViewById(R.id.btnlogin);
        Lemail=findViewById(R.id.lo_Email);
        Lpass=findViewById(R.id.lo_pass);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.registerACT);
        forgetBtn = findViewById(R.id.forgetPass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Lemail.getText().toString().trim();
                String pass = Lpass.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Lemail.setError("الرجاء إدخال إيميل فعال");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    Lpass.setError("الرجاء ادخال كلمة مرور");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "نعتذر حدث خطأ, يرجى إعاده المحاولة لاحقا"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passresetDialog = new AlertDialog.Builder(v.getContext());
                passresetDialog.setTitle("إعاده تعيين كلمة المرور؟");
                passresetDialog.setMessage("ادخل إيميلك للإستراد كلمة المرور");
                passresetDialog.setView(resetMail);
                passresetDialog.setPositiveButton("أعاده تعيين", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "تم إرسال رابط إعاده التعيين إلى جهازك", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "حدث خطأ لم يتم إرسال رابط إعاده التعيين" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        
                    }
                });
                passresetDialog.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passresetDialog.create().show();
            }
        });

     registerBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             startActivity(new Intent(getApplicationContext(), registration.class));
         }
     });
    }
    /*public void login(){
        String user=Lemail.getText().toString().trim();
        String pass=password.getText().toString().trim();
        if(user.equals("rab")&&pass.equals("1234")){
            Intent home_in = new Intent(MainActivity.this, Home.class);
            startActivity(home_in);
        }
        else {
            counter-- ;
            Toast.makeText(this,"كلمة السر غير صحيحة",Toast.LENGTH_LONG).show();
            if ( counter==0){
                Toast.makeText(this,"انتهى عدد المحاولات! حاول لفي وقت أخر",Toast.LENGTH_LONG).show();
                login.setEnabled(false);
            }
        }
    } */


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Tag,"OnStart Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Tag,"onResume Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Tag,"onStop Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Tag,"onPause Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Tag,"onDestroy Called");
    }
}