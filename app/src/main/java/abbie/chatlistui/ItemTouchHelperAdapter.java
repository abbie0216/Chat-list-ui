package abbie.chatlistui;

/**
 * Created by Abbie on 2017/3/24.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
