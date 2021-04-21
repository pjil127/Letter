package com.example.letter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

public class ReplyActivity extends AppCompatActivity {

    public static final String TAG = "ReplyActivity";
    private TextView tvReplyContent;
    private Button btnSend;
    private String letterId;
    ParseObject object;
    ParseObject reply = new ParseObject("Reply");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        letterId = getIntent().getStringExtra("REPLY_LETTER_ID");
        tvReplyContent = findViewById(R.id.tvReplyContent);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyContent = tvReplyContent.getText().toString();
                if(replyContent.isEmpty()){
                    Toast.makeText( ReplyActivity.this,"Reply cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Letter");

                reply.put("user", ParseUser.getCurrentUser());
                reply.put("content", replyContent);
                reply.put("report", false);

                query.getInBackground(letterId, (object, error) -> {
                    if (error == null) {
                        reply.put("letter", object);
                    }
                    else {
                        // something went wrong
                        Toast.makeText(ReplyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                reply.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!=null){
                            Log.e(TAG,"Error while saving",e);
                            Toast.makeText(ReplyActivity.this,"Error while saving",Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG,"Post save was successful");
                        tvReplyContent.setText("");
                    }
                });
            }
        });
    }
}