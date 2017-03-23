package abbie.chatlistui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list)
    RecyclerView list;
    ArrayList<ChatItem> items;
    Handler handler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 2000);
            Log.d("Abbie","task run..");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(task, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(task);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{

        public ChatViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChatViewHolder(getLayoutInflater().inflate(R.layout.view_chat_item,parent));
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    class ChatItem{
        int type;

    }
}
