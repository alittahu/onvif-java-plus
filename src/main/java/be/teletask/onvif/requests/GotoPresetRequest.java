package be.teletask.onvif.requests;

import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.OnvifType;

public class GotoPresetRequest implements OnvifRequest {

    private final String profileToken;
    private final String presetToken;
    private final OnvifResponseListener listener; // optional

    public GotoPresetRequest(String profileToken, String presetToken) {
        this(profileToken, presetToken, null);
    }

    public GotoPresetRequest(String profileToken, String presetToken, OnvifResponseListener listener) {
        this.profileToken = profileToken;
        this.presetToken = presetToken;
        this.listener = listener;
    }

    @Override
    public String getXml() {
        // Speed is optional. Most cameras are fine without it.
        return "<GotoPreset xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\">" +
                "<ProfileToken>" + profileToken + "</ProfileToken>" +
                "<PresetToken>" + presetToken + "</PresetToken>" +
                "</GotoPreset>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.GOTO_PRESET;
    }

    public String getProfileToken() { return profileToken; }
    public String getPresetToken()  { return presetToken; }
    public OnvifResponseListener getListener() { return listener; }
}
