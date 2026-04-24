package com.example.civildefence;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.civildefence.api.ApiClient;
import com.example.civildefence.models.Incident;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnSend;
    private SharedPreferences prefs;
    private int incidentId;
    private List<ChatMessage> messagesList;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        prefs = getSharedPreferences("civil_defense_prefs", MODE_PRIVATE);

        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);

        incidentId = getIntent().getIntExtra("incident_id", 0);

        messagesList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messagesList);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(chatAdapter);

        // Load incident info for chat header
        loadIncidentInfo();

        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });
    }

    private void loadIncidentInfo() {
        String token = "Bearer " + prefs.getString("access_token", "");

        ApiClient.getApiService(this).getIncidentById(token, incidentId)
                .enqueue(new Callback<Incident>() {
                    @Override
                    public void onResponse(Call<Incident> call, Response<Incident> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Incident inc = response.body();
                            setTitle("Chat - INC-" + inc.getId());

                            // Add system message
                            addSystemMessage("Connected to incident #" + inc.getId() +
                                    " - " + inc.getCategory());
                        }
                    }

                    @Override
                    public void onFailure(Call<Incident> call, Throwable t) {
                        setTitle("Chat - INC-" + incidentId);
                    }
                });
    }

    private void sendMessage(String message) {
        // In production, this would call WebSocket API
        // For now, simulate sending message
        String userEmail = prefs.getString("user_email", "User");
        addMessage(userEmail, message, System.currentTimeMillis());
        etMessage.setText("");

        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
    }

    private void addMessage(String sender, String content, long timestamp) {
        messagesList.add(new ChatMessage(sender, content, timestamp));
        chatAdapter.notifyItemInserted(messagesList.size() - 1);
        rvMessages.scrollToPosition(messagesList.size() - 1);
    }

    private void addSystemMessage(String content) {
        messagesList.add(new ChatMessage("System", content, System.currentTimeMillis()));
        chatAdapter.notifyItemInserted(messagesList.size() - 1);
    }

    // Chat Message Model
    public static class ChatMessage {
        String sender;
        String content;
        long timestamp;

        public ChatMessage(String sender, String content, long timestamp) {
            this.sender = sender;
            this.content = content;
            this.timestamp = timestamp;
        }
    }

    // Chat RecyclerView Adapter
    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

        private List<ChatMessage> messages;

        public ChatAdapter(List<ChatMessage> messages) {
            this.messages = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ChatMessage msg = messages.get(position);
            holder.tvSender.setText(msg.sender + ":");
            holder.tvContent.setText(msg.content);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSender, tvContent;

            ViewHolder(View itemView) {
                super(itemView);
                tvSender = itemView.findViewById(android.R.id.text1);
                tvContent = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}