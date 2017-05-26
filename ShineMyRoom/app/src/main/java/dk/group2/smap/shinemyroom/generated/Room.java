package dk.group2.smap.shinemyroom.generated;

import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;

/**
 * Created by liao on 23-05-2017.
 */

public class Room {

    public Room(){
    }
    private PHGroup phGroup;
    private ArrayList<PHLight> lights;


    public PHGroup getPhGroup() {
        return phGroup;
    }

    public void setPhGroup(PHGroup phGroup) {
        this.phGroup = phGroup;
    }

    public ArrayList<PHLight> getLights() {
        return lights;
    }

    public void setLights(ArrayList<PHLight> lights) {
        this.lights = lights;
    }
}
