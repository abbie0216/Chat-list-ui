package abbie.chatlistui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Abbie on 2017/3/24.
 */

public class FriendLstDlg implements View.OnClickListener{

    PopupWindow window;
    View view;
    TextView btnAdd;
    TextView btnClose;
    int friendnum = 1;

    RecyclerView list;
    FriendLstAdapter adapter = new FriendLstAdapter();
    ArrayList<FriendItem> items = new ArrayList<>();
    ItemTouchHelper itemTouchHelper;

    public FriendLstDlg(Context ctx) {
        view = LayoutInflater.from(ctx).inflate(R.layout.dlg_friend_list, null);
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        window.setOutsideTouchable(false);

        list = (RecyclerView) view.findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(list);

        btnClose = (TextView) view.findViewById(R.id.btn_close);
        btnAdd = (TextView) view.findViewById(R.id.btn_add);
        btnClose.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        initData();
        window.showAtLocation(view, Gravity.TOP, 0, 0);
    }

    private void initData() {
        Log.d("Abbie", "init friend data.");
        for (int i = 0; i < 5; i++)
            items.add(new FriendItem("Friend #" + (friendnum++)));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Log.d("Abbie", "btn_add clicked.");
                items.add(new FriendItem("Friend #" + (friendnum++)));
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_close:
                Log.d("Abbie", "btn_close clicked.");
                if (window != null)
                    window.dismiss();
                break;
        }
    }

    public class FriendLstAdapter extends RecyclerView.Adapter<FriendLstAdapter.FriendLstViewHolder> implements ItemTouchHelperAdapter{

        @Override
        public FriendLstViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendLstViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_friend_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final FriendLstViewHolder holder, int position) {
            FriendItem item = items.get(position);
            holder.txtName.setText(item.name);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(items, fromPosition, toPosition);
            notifyItemMoved(fromPosition,toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {
            items.remove(position);
            notifyItemRemoved(position);
        }

        class FriendLstViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{

            TextView txtName;

            public FriendLstViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.txt_name);
            }

            @Override
            public void onItemSelected() {
            }

            @Override
            public void onItemClear() {
                itemView.setBackgroundColor(0);
            }
        }
    }

    class FriendItem {
        String name;

        FriendItem(String name) {
            this.name = name;
        }
    }
}
