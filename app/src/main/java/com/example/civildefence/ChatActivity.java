package com.example.civildefence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnSend, btnAttach;
    private ArrayList<String> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        btnAttach = findViewById(R.id.btn_attach);

        rvMessages.setLayoutManager(new LinearLayoutManager(this));

        Toast.makeText(this,
                "Connecting to WebSocket for real-time team chat\nIncident: #INC-001",
                Toast.LENGTH_SHORT).show();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (!message.isEmpty()) {
                    Toast.makeText(ChatActivity.this,
                            "API: POST /incidents/{id}/messages\nMessage sent: \"" + message + "\"",
                            Toast.LENGTH_SHORT).show();
                    etMessage.setText("");
                }
            }
        });

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatActivity.this,
                        "Attaching media to chat message",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}