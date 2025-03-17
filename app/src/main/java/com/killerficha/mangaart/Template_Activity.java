package com.killerficha.mangaart;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Template_Activity extends AppCompatActivity {
    Templates t;
    ImageButton iixii;
    ImageButton iiixii;
    ImageButton ivxii;
    EditText rowss;
    EditText coalss;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_template);
        iixii = findViewById(R.id.twoxtwo);
        iiixii = findViewById(R.id.threextwo);
        ivxii = findViewById(R.id.fourxtwo);
        rowss = findViewById(R.id.row);
        coalss = findViewById(R.id.coal);


        iixii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setTMode(Templates.templ.TWO_X_TWO);
            }
        });
        iiixii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setTMode(Templates.templ.THREE_X_TWO);
            }
        });
        ivxii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setTMode(Templates.templ.FOUR_X_TWO);
            }
        });
        if(rowss.getText() != null && coalss.getText() != null){
            t.setTMode(Templates.templ.CUSTOM);
//            t.setRows((int)rowss.getText());
        }




    }
}