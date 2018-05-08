package android.carrier.net.elastos.codepumpkin.Bean;

import android.app.Application;

import org.elastos.carrier.Carrier;

public class SysApp extends Application {


    String userID = "";
    String userAddr = "";
    Carrier carrierInst = null;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public Carrier getCarrier() {
        return carrierInst;
    }

    public void setCarrier(Carrier inst) {
        carrierInst = inst;
    }


}
