package abbie.chatlistui;

/**
 * Created by Abbie on 2017/5/8.
 */

public class FriendItem {
    private int id;
    private String name;
    private int order;

    FriendItem(int id, String name, int order) {
        this.id = id;
        this.name = name;
        this.order = order;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public int getOrder(){
        return order;
    }
}
