package dk.group2.smap.shinemyroom;

/**
 * Created by liao on 25-05-2017.
 */
public interface callbacks{
    public void onResult(String response);
    public void setOnResponseListener();
    public interface onResponseListener {
        public void returnResult(String response);
    }
}
