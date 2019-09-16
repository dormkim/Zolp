package com.example.zolup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InputPointInfo extends AppCompatActivity {

    private TextView marker_name, address;
    private Button check_button;
    private EditText in_name, in_tag, in_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputpointinfo);

        marker_name = findViewById(R.id.marker_name);
        address = findViewById(R.id.address);
        check_button = findViewById(R.id.finish_button);
        in_name = findViewById(R.id.name_info);
        in_tag = findViewById(R.id.tag_info);
        in_main = findViewById(R.id.main_info);

        Intent intent = getIntent();
        String name = intent.getStringExtra("marker_name");
        String add = intent.getStringExtra("address");

        marker_name.setText(name);
        address.setText(add);

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InputPointInfo.this, "결과 : " + in_name.getText() + " " + in_tag.getText() + " " + in_main.getText(), Toast.LENGTH_LONG).show();
                //DB에 올리는 과정 필요//
                finish();
            }
        });
    }
}
