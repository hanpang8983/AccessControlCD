package com.github.soukie.DACPolicy.values;

/**The Capability list class.
 * Created by qiyiy on 2016/1/5.
 */
public class CapabilityList {
    public boolean ownership;
    public boolean readable;
    public boolean writable;
    public boolean controllable;
    public boolean deletable;

    public CapabilityList(boolean ownership, boolean readable, boolean writable, boolean controllable, boolean deletable) {
        this.ownership = ownership;
        this.readable = readable;
        this.writable = writable;
        this.controllable = controllable;
        this.deletable = deletable;
    }

    /**
     * The method to get capability string from capability list
     * @param capabilityList: capability list
     * @return capability string likes "__w_d", "orwcd" or "_rwcd"
     */
    public static String capabilityListToString(CapabilityList capabilityList) {
        return (capabilityList.ownership ? "o" : "_") +
                (capabilityList.readable ? "r" : "_") +
                (capabilityList.writable ? "w" : "_") +
                (capabilityList.controllable ? "c" : "_") +
                (capabilityList.deletable ? "d" : "_");
    }

    public static CapabilityList capabilityStringToList(String capabilityString) {
        return new CapabilityList(capabilityString.charAt(0) == 'o',
                capabilityString.charAt(1) == 'r',
                capabilityString.charAt(2) == 'w',
                capabilityString.charAt(3) == 'c',
                capabilityString.charAt(4) == 'd');
    }
}
