package com.masha.yuhubrain;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masha.yuhubrain.data.GameLevel;

public class FirestoreRepository {
    FirebaseFirestore db =  FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //Получаем ссылку на документ уровня по имени
    public DocumentReference getLevel(String name){
        DocumentReference ref = db.collection("levels").document(name);
        return ref;
    }

    //Получаем ссылку на коллекцию уровней
    public CollectionReference getLevelCollection(){
        CollectionReference ref = db.collection("levels");
        return ref;
    }

    //Метод для загрузки изображения из Firebase Storage в ImageView.
    public void getImageIntoView(String link,final Context context,final ImageView target){
        if(link == null || link.isEmpty()) return;
        StorageReference ref = storage.getReference();
        StorageReference refLink = ref.child(link);
        refLink.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Glide.with(context).load(task.getResult()).into(target);
                }
            }
        });
    }
}
