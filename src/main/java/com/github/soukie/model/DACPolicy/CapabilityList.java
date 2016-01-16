package com.github.soukie.model.DACPolicy;

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
        return (capabilityList.ownership ? "o" : "-") +
                (capabilityList.readable ? "r" : "-") +
                (capabilityList.writable ? "w" : "-") +
                (capabilityList.controllable ? "c" : "-") +
                (capabilityList.deletable ? "d" : "-");
    }

    public static CapabilityList capabilityStringToList(String capabilityString) {
        return new CapabilityList(capabilityString.charAt(0) == 'o',
                capabilityString.charAt(1) == 'r',
                capabilityString.charAt(2) == 'w',
                capabilityString.charAt(3) == 'c',
                capabilityString.charAt(4) == 'd');
    }
    public static int capabilityStringToIntValue(String capabilityString) {
        return (capabilityString.charAt(0) == 'o' ? 16 : 0) +
                (capabilityString.charAt(1) == 'r' ? 8 : 0) +
                (capabilityString.charAt(2) == 'w' ? 4 : 0) +
                (capabilityString.charAt(3) == 'c' ? 2 : 0) +
                (capabilityString.charAt(4) == 'd' ? 1 : 0);
    }

}
