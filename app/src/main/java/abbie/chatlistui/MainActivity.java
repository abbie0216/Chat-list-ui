package abbie.chatlistui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView list;
    private TextView btnNewMsg;
    private TextView btnFriendLst;
    private ChatAdapter adapter = new ChatAdapter();
    private ArrayList<ChatItem> items = new ArrayList<>();
    private int numNewMsg = 0;
    private int numTemp = 1;
    private ObjectAnimator aniBtnShow, aniBtnHide;
    public static FriendLstDlg friendLstDlg;

    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 5000);
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
                btnNewMsg.setText((++numNewMsg) + " new message.");
                if (btnNewMsg.getVisibility() == View.INVISIBLE) {
                    aniBtnShow.start();
                }
            }
        }
    };

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this, "chatlist.db", null, 1);
        SPHelper.init(this);
        initStetho();
        initView();
        friendLstDlg = new FriendLstDlg(getBaseContext(), dbHelper);

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
                        if (btnNewMsg.getVisibility() == View.VISIBLE) {
                            aniBtnHide.start();
                        }
                        numNewMsg = 0;
                    }
                }
            }
        });

        btnNewMsg = (TextView) findViewById(R.id.btn_new_msg);
        btnNewMsg.setOnClickListener(this);

        btnFriendLst = (TextView) findViewById(R.id.btn_friend_list);
        btnFriendLst.setOnClickListener(this);

        aniBtnShow = ObjectAnimator.ofFloat(btnNewMsg, "alpha", 0, 1);
        aniBtnShow.setDuration(500);
        aniBtnShow.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                btnNewMsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                btnNewMsg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        aniBtnHide = ObjectAnimator.ofFloat(btnNewMsg, "alpha", 1, 0);
        aniBtnHide.setDuration(500);
        aniBtnHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnNewMsg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                btnNewMsg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        SPHelper.getInstance().putUserName("Abbie");
    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
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
                if (btnNewMsg.getVisibility() == View.VISIBLE) {
                    aniBtnHide.start();
                }
                numNewMsg = 0;
                break;
            case R.id.btn_friend_list:
                friendLstDlg.show();
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
                txtTitle = (TextView) itemView.findViewById(R.id.tv_title);
                txtContent = (TextView) itemView.findViewById(R.id.tv_content);
            }
        }
    }

    private class ChatItem {
        String title;
        String content;
    }
}
