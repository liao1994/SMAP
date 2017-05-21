package dk.group2.smap.shinemyroom;

import android.content.Context;

public abstract class BaseHueControl implements IHueControl{
    protected Context c;
    public BaseHueControl(Context c){

        this.c = c;
    }

}
