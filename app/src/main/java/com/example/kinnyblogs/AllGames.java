package com.example.kinnyblogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kinnyblogs.cloudanchor.CloudAnchorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.javagl.obj.Obj;

public class AllGames extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private MyAdapter adpater;
    private List<Model> list;
    private ProgressBar pb;
    private FloatingActionButton btn;
    private SwipeRefreshLayout swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_games);

        recyclerView = findViewById(R.id.recyclerview);

        pb = findViewById(R.id.progressBar);
        btn = findViewById(R.id.floatingActionButton);
        swipe = findViewById(R.id.swipeRefreshLayout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllGames.this, AddGame.class));
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adpater = new MyAdapter(this, list);

        adpater.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<HashMap<String, Object>> clues) {
                // Handle item click event here
                Intent intent = new Intent(AllGames.this, ClueActivity.class);
                intent.putExtra("CLUE_DATA", clues);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adpater);



        showData();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                showData();
            }
        });


    }

    private void showData(){
        pb.setVisibility(View.VISIBLE);

        db.collection("Games").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                for(DocumentSnapshot snapshot:task.getResult()){
                    Model model = new Model(snapshot.getString("game_name"), snapshot.getString("game_desc"), (ArrayList<HashMap<String, Object>>) snapshot.get("game_clues"));
                    list.add(model);


                }

                adpater.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AllGames.this, "Something is not right :(", Toast.LENGTH_SHORT).show();
            }
        });

        pb.setVisibility(View.INVISIBLE);
    }
}