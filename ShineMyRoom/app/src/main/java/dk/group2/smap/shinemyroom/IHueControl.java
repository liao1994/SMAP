package dk.group2.smap.shinemyroom;

/**
 * Created by liao on 20-05-2017.
 */
public interface IHueControl{
    void setGroupLight(int groupId, boolean state);
    void getGroupDetails(int groupId);
    void getGroupDetails_V2(int groupId, final onGroupResponseListener listner1);

        void setOnGroupResponseListener(onGroupResponseListener listner);
    interface onGroupResponseListener {
        void onGroupResult(String response);
    }
    //more stuff later
}

