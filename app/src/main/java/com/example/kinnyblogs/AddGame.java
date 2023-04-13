package com.example.kinnyblogs;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kinnyblogs.cloudanchor.CloudAnchorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddGame extends AppCompatActivity {

    private EditText title, desc, clue, arid, model;
    private Button saveBtn, arBtn, addClueBtn;
    private List<HashMap<String, Object>> clue_map = new ArrayList<HashMap<String, Object>>();



    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        title = findViewById(R.id.game_title);
        desc = findViewById(R.id.game_desc);
        clue = findViewById(R.id.game_clue);
        arid = findViewById(R.id.game_ar_id);
        model = findViewById(R.id.ar_model);


        saveBtn = findViewById(R.id.save_btn);
        arBtn = findViewById(R.id.ar_btn);
        addClueBtn = findViewById(R.id.add_clue);


        db = FirebaseFirestore.getInstance();

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            String newText = data.getStringExtra("anchor_id");

                            arid.setText(newText);
                        }
                    }
                });

        arBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddGame.this, CloudAnchorActivity.class);
                intent.putExtra("AR_TYPE", "HOST");
                intent.putExtra("MODEL", model.getText().toString());
                someActivityResultLauncher.launch(intent);
            }
        });

        addClueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String game_clue = clue.getText().toString();
                String game_arid = arid.getText().toString();

                HashMap<String, Object> clueSerial = new HashMap<String, Object>();

                clueSerial.put("clue_type", "text");
                clueSerial.put("clue_title", game_clue);
                clueSerial.put("clue_ARid", game_arid);
                clueSerial.put("clue_model", model.getText().toString());

                clue_map.add(clueSerial);


                clue.setText("");
                arid.setText("");
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String game_title = title.getText().toString();
                String game_desc = desc.getText().toString();
                String game_clue = clue.getText().toString();
                String game_arid = arid.getText().toString();

                saveToFirestore(game_title, game_desc, game_clue, game_arid);

                finish();

            }
        });



    }


    private void saveToFirestore(String title, String desc, String clue, String arid){
        if(!title.isEmpty()){
            HashMap<String, Object> map = new HashMap<>();
            map.put("game_name", title);
            map.put("game_desc", desc);

            /*HashMap<String, Object> clue_map = new HashMap<>();
            List<Map<String, Object>> clues = new ArrayList<Map<String, Object>>();

            clue_map.put("clue_type", "text");
            clue_map.put("clue_title", clue);
            clue_map.put("clue_ARid", arid);

            clues.add(clue_map);*/

            map.put("game_clues", clue_map);

            db.collection("Games").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddGame.this ,"Game Added !", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddGame.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}