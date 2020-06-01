package com.masha.yuhubrain;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.masha.yuhubrain.data.GameLevel;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {
    private MutableLiveData<List<GameLevel>> levels = new MutableLiveData<>();
    private FirestoreRepository repository = new FirestoreRepository();

    //Получаем все уровни из БД
    public MutableLiveData<List<GameLevel>> getLevels() {
        if(levels.getValue() == null){
            levels.postValue(new ArrayList<GameLevel>());
            repository.getLevelCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                    List<GameLevel> gameLevels = new ArrayList<>();
                    for(DocumentSnapshot snapshot : documentSnapshots){
                        gameLevels.add(snapshot.toObject(GameLevel.class));
                    }
                    levels.postValue(gameLevels);
                }
            });
        }
        return levels;
    }

    //Устанавливаем список уровней
    public void setLevels() {
        repository.getLevelCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
                List<GameLevel> gameLevels = new ArrayList<>();
                for(DocumentSnapshot snapshot : documentSnapshots){
                    gameLevels.add(snapshot.toObject(GameLevel.class));
                }
                levels.postValue(gameLevels);
            }
        });
    }
}
