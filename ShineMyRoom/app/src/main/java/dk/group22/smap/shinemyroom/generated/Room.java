package dk.group22.smap.shinemyroom.generated;

import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;


public class Room {

    public Room(PHGroup phGroup, ArrayList<PHLight> lights){
        this.phGroup = phGroup;
        this.lights = lights;
    }
    private PHGroup phGroup;
    private ArrayList<PHLight> lights;
    private Boolean on;
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

    public Boolean getOn() {
        for(PHLight light : lights)
        {
            if(light.getLastKnownLightState().isOn())
                return true;
        }
        return false;
    }
    public void setOn(boolean state){
        for(PHLight light : lights)
        {
            light.getLastKnownLightState().setOn(state);
            //lastKnownLightState.setOn(state);
            //PHLightState phLightState = new PHLightState();
            //phLightState.setOn(state);
            //if(light.setLastKnownLightState(phLightState);
        }
    }
}
