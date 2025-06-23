package be.teletask.onvif.requests;

import be.teletask.onvif.listeners.OnvifPTZStatusListener;
import be.teletask.onvif.models.OnvifType;

public class GetStatusRequest implements OnvifRequest {
    
    public static final String TAG = GetServicesRequest.class.getSimpleName();

    private final String token;
    private final OnvifPTZStatusListener listener;

    public GetStatusRequest(String profileToken, OnvifPTZStatusListener l) {
        super();
        this.token = profileToken;
        this.listener = l;
    }

    @Override
    public String getXml() {
        return "<GetStatus xmlns=\"http://www.onvif.org/ver10/ptz/wsdl\">"
                + "<ProfileToken>" + token + "</ProfileToken>"
                + "</GetStatus>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.GET_PTZ_STATUS;
    }

    public OnvifPTZStatusListener getListener() {
        return listener;
    }
}
