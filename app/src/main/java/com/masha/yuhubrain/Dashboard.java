package com.masha.yuhubrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.masha.yuhubrain.data.User;

//Login
public class Dashboard extends AppCompatActivity {
    Button callSingUp,login_btn;
    ImageView image;
    TextView logoText;
    TextInputLayout username,password;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        callSingUp = findViewById(R.id.singup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);



        callSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this,SingUp.class);

                Pair[] pairs = new Pair[6];

                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logoText,"logo_text");
                pairs[2] = new Pair<View,String>(username,"username_trans");
                pairs[3] = new Pair<View,String>(password,"password_trans");
                pairs[4] = new Pair<View,String>(login_btn,"btn_trans");
                pairs[5] = new Pair<View,String>(callSingUp,"login_singup_trans");

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this, pairs);
                    startActivity(intent, options.toBundle());
                }
                //startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем синглтон БД
                db = FirebaseFirestore.getInstance();
                Log.d("Dashboard",username.getEditText().getText().toString());
                //В случае если не введен логин, выводим диалоговое окно
                if(username.getEditText().getText().toString().length()==0){
                    new AlertDialog.Builder(Dashboard.this)
                            .setTitle("Не заполнены все поля")
                            .setMessage("Не указано имя пользователя")
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).create().show();
                }else {
                    //Получаем документ по имени пользователя
                    DocumentReference docRef = db.collection("users").document(username.getEditText().getText().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //Если пользователь найден, проверяем пароль
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                mAuth = FirebaseAuth.getInstance();
                                if (user != null) {
                                    mAuth.signInWithEmailAndPassword(user.getEmail(), password.getEditText().getText().toString()).addOnCompleteListener(Dashboard.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(Dashboard.this, MenuActivity.class);
                                                startActivity(intent);
                                            } else {
                                                new AlertDialog.Builder(Dashboard.this)
                                                        .setTitle("Неверный пароль")
                                                        .setMessage("Введен неверный пароль")
                                                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        }).create().show();
                                            }
                                        }

                                    });
                                } else {
                                    new AlertDialog.Builder(Dashboard.this)
                                            .setTitle("Пользователь не найден")
                                            .setMessage("Такого пользователя не существует")
                                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            }).create().show();
                                }
                            } else {
                                new AlertDialog.Builder(Dashboard.this)
                                        .setTitle("Пользователь не найден")
                                        .setMessage("Такого пользователя не существует")
                                        .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).create().show();
                            }
                        }
                    });
                }
            }
        });
    }


}
