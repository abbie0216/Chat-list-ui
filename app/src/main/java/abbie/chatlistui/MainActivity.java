package abbie.chatlistui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView list;
    TextView btnNewMsg;
    ChatAdapter adapter = new ChatAdapter();
    ArrayList<ChatItem> items = new ArrayList<>();
    int numNewMsg = 0;
    int numTemp = 1;

    Handler handler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000 * getRandom(1, 5));
            if (items.size() == 99)
                items.remove(0);
            ChatItem item = new ChatItem();
            item.title = "member";
            item.content = "chat content num " + (numTemp++);
            items.add(item);
            adapter.notifyDataSetChanged();
            if (!list.canScrollVertically(1)) {
                Log.d("Abbie", "Get new msg. Already at bottom of list.");
                list.scrollToPosition(items.size() - 1);
            } else {
                Log.d("Abbie", "Get new msg. Not at bottom of list.");
                btnNewMsg.setText((++numNewMsg) + " 則新訊息");
                btnNewMsg.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        list = (RecyclerView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { //停止滾動
                    if (recyclerView.canScrollVertically(1)) {
                        Log.d("Abbie", "Not at bottom of list.");
                    } else {
                        Log.d("Abbie", "Already at bottom of list.");
                        btnNewMsg.setVisibility(View.INVISIBLE);
                        numNewMsg = 0;
                    }
                }
            }
        });
        btnNewMsg = (TextView) findViewById(R.id.btn_new_msg);
        btnNewMsg.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_msg:
                list.scrollToPosition(items.size() - 1);
                btnNewMsg.setVisibility(View.INVISIBLE);
                numNewMsg = 0;
                break;
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {
            ChatItem item = items.get(position);
            holder.txtTitle.setText(item.title);
            holder.txtContent.setText(item.content);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle;
            TextView txtContent;

            public ChatViewHolder(View itemView) {
                super(itemView);
                txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
                txtContent = (TextView) itemView.findViewById(R.id.txt_content);
            }
        }
    }

    private class ChatItem {
        String title;
        String content;
    }

    private int getRandom(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1) + min));
    }
}
