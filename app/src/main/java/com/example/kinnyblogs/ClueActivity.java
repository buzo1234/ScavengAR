package com.example.kinnyblogs;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kinnyblogs.cloudanchor.CloudAnchorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClueActivity extends AppCompatActivity {

    TextView clue, clue_no;
    Button arbtn;

    private int count = 0, k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue);

        Intent intent = getIntent();
        ArrayList<HashMap<String, Object>> clues = (ArrayList<HashMap<String,Object>>)  intent.getSerializableExtra("CLUE_DATA");

        clue = findViewById(R.id.clue);
        clue_no = findViewById(R.id.textView5);
        arbtn = findViewById(R.id.button);
        clue_no.setText("Clue " + (k+1));
        clue.setText(clues.get(k).get("clue_title").toString());

        count = clues.size();

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            count = count - 1;
                            k = k+1;
                            if(count > 0){
                                clue.setText(clues.get(k).get("clue_title").toString());
                                clue_no.setText("Clue " + (k+1));
                            }
                            else{
                                clue.setText("Anchor was resolved");
                                clue_no.setText("Game Won!!");
                                arbtn.setVisibility(View.INVISIBLE);
                            }

                        }
                    }
                });

        arbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClueActivity.this, CloudAnchorActivity.class);
                intent.putExtra("AR_TYPE", "RESOLVE");
                intent.putExtra("ANCHOR_ID", clues.get(k).get("clue_ARid").toString());
                intent.putExtra("MODEL",  clues.get(k).get("clue_model").toString());
                someActivityResultLauncher.launch(intent);
            }
        });
    }
}