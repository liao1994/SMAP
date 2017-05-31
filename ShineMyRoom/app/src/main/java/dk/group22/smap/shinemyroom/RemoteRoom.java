package dk.group22.smap.shinemyroom;

import java.util.ArrayList;

import dk.group22.smap.shinemyroom.generated.Group;
import dk.group22.smap.shinemyroom.generated.Light;
import dk.group22.smap.shinemyroom.generated.State;


public class RemoteRoom {

    private ArrayList<Light> lightArrayList;
    private Group group;

    public RemoteRoom(ArrayList<Light> lightArrayList, Group group) {

        this.lightArrayList = lightArrayList;
        this.group = group;
    }



    public ArrayList<Light> getLightArrayList() {
        return lightArrayList;
    }

    public void setLightArrayList(ArrayList<Light> lightArrayList) {
        this.lightArrayList = lightArrayList;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    public void setLightStateForGroup(boolean state){
        for(Light light : lightArrayList)
        {
            State lightState = light.getState();
            lightState.setOn(state);
            light.setState(lightState);
        }
        State groupState = group.getState();
        groupState.setOn(state);
        group.setState(groupState);
    }

    public Boolean getLightStateForGroup() {
        for(Light light : lightArrayList)
        {
            if(light.getState().getOn())
                return true;
        }
        return false;
    }

}
