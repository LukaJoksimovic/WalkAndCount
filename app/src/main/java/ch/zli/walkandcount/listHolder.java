package ch.zli.walkandcount;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class listHolder {

    private static listHolder lh_instance = null;

    List<LatLng> positions = new ArrayList<>();

    private listHolder() {

    }

    public static listHolder getInstance() {
        if(lh_instance == null) {
            lh_instance = new listHolder();
        }
        return lh_instance;
    }

    public void fillList(List<LatLng> tmp) {
        positions.addAll(tmp);
    }

}
