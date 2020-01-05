package com.example.zolup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private String my_id;
    private ArrayList<MapInfo> mapInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrivateMapActivity.class);
                intent.putExtra("id_info", my_id);
                intent.putExtra("mapinfos", mapInfos);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShareMapActivity.class);
                intent.putExtra("id_info", my_id);
                intent.putExtra("mapinfos", mapInfos);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        my_id = getIntent().getStringExtra("id_info");
        mapInfos.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("devices").document(my_id);

        //로그인한 사람의 정보를 저장
        db.collection("staypoints")
            .whereEqualTo("userId", ref)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MapInfo m = new MapInfo();

                            if(document.getString("name").equals("")){
                                m.setName("내용없음");
                            }
                            else{
                                m.setName(document.getString("name"));
                            }
                            m.setDocu_id(document.getId());
                            m.setContent(document.getString("content"));
                            m.setLatitude(document.getGeoPoint("coordinate").getLatitude());
                            m.setLongitude(document.getGeoPoint("coordinate").getLongitude());
                            m.setCheck_share(document.getBoolean("isShared"));
                            m.setDate(document.getDate("date"));
                            m.setMy_id(my_id);
                            mapInfos.add(m);
                        }
                    }
                }
            });

        //본인 외 다른사용자들의 정보를 저장
        db.collection("staypoints")
            .whereGreaterThan("userId", ref)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MapInfo m = new MapInfo();
                            m.setContent(document.getString("content"));
                            m.setLatitude(document.getGeoPoint("coordinate").getLatitude());
                            m.setLongitude(document.getGeoPoint("coordinate").getLongitude());
                            m.setCheck_share(document.getBoolean("isShared"));
                            m.setName(document.getString("name"));
                            m.setDate(document.getDate("date"));
                            m.setMy_id("-1");
                            mapInfos.add(m);
                        }
                    }
                }
            });

        //본인 외 다른사용자들의 정보를 저장
        db.collection("staypoints")
            .whereLessThan("userId", ref)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            MapInfo m = new MapInfo();
                            m.setContent(document.getString("content"));
                            m.setLatitude(document.getGeoPoint("coordinate").getLatitude());
                            m.setLongitude(document.getGeoPoint("coordinate").getLongitude());
                            m.setCheck_share(document.getBoolean("isShared"));
                            m.setName(document.getString("name"));
                            m.setDate(document.getDate("date"));
                            m.setMy_id("-1");
                            mapInfos.add(m);
                        }
                    }
                }
            });
    }

}
