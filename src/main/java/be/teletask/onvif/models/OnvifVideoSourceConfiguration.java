package be.teletask.onvif.models;

public class OnvifVideoSourceConfiguration {
    //Constants
    public static final String TAG = OnvifDeviceInformation.class.getSimpleName();

    //Attributes

    //Constructors
    private boolean Rotation;
    private boolean Mirror;
    private boolean Flip;

    public boolean isRotation() {
        return Rotation;
    }

    public void setRotation(boolean rotation) {
        Rotation = rotation;
    }

    public boolean isMirror() {
        return Mirror;
    }

    public void setMirror(boolean mirror) {
        Mirror = mirror;
    }

    public boolean isFlip() {
        return Flip;
    }

    public void setFlip(boolean flip) {
        Flip = flip;
    }

    @Override
    public String toString() {
        return "OnvifVideoSourceConfiguration{" +
                "Rotation=" + Rotation +
                ", Mirror=" + Mirror +
                ", Flip=" + Flip +
                '}';
    }
}
