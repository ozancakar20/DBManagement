/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Ozan
 */
public class Sutun {

    String ad;
    String tip;
    boolean notNull;
    String key;
    String defaultDeger;
    String autoInc;

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String isKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultDeger() {
        return defaultDeger;
    }

    public void setDefaultDeger(String defaultDeger) {
        this.defaultDeger = defaultDeger;
    }

    public String isAutoInc() {
        return autoInc;
    }

    public void setAutoInc(String autoInc) {
        this.autoInc = autoInc;
    }

}
