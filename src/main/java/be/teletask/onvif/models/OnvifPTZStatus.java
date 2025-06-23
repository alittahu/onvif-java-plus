package be.teletask.onvif.models;

public class OnvifPTZStatus {

    public static final String TAG = OnvifPacket.class.getSimpleName();

    private double pan;
    private double tilt;
    private double zoom;

    public double getPan() {
        return pan;
    }

    public void setPan(double pan) {
        this.pan = pan;
    }

    public double getTilt() {
        return tilt;
    }

    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return "OnvifPTZStatus{" +
                "pan=" + pan +
                ", tilt=" + tilt +
                ", zoom=" + zoom +
                '}';
    }
}
