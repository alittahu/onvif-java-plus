package be.teletask.onvif.requests;

import be.teletask.onvif.listeners.OnvifPTZPresetsListener;
import be.teletask.onvif.models.OnvifType;

public class GetPresetsRequest implements OnvifRequest {

    private final String profileToken;
    private final OnvifPTZPresetsListener listener;

    public GetPresetsRequest(String profileToken, OnvifPTZPresetsListener listener) {
        this.profileToken = profileToken;
        this.listener = listener;
    }

    @Override
    public String getXml() {
        return "<GetPresets xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\">" +
                "<ProfileToken>" + profileToken + "</ProfileToken>" +
                "</GetPresets>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.GET_PRESETS;
    }

    public String getProfileToken() { return profileToken; }
    public OnvifPTZPresetsListener getListener() { return listener; }
}
