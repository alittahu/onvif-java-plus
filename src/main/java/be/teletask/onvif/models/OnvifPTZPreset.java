package be.teletask.onvif.models;

public class OnvifPTZPreset {
    private final String token;
    private final String name;

    public OnvifPTZPreset(String token, String name) {
        this.token = token;
        this.name = name;
    }

    public String getToken() { return token; }
    public String getName()  { return name; }

    @Override
    public String toString() {
        return "OnvifPTZPreset{token='" + token + "', name='" + name + "'}";
    }
}
