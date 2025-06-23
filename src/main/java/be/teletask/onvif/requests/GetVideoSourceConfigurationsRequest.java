package be.teletask.onvif.requests;

import be.teletask.onvif.listeners.OnvifVideoSourceConfigurationListener;
import be.teletask.onvif.models.OnvifType;

public class GetVideoSourceConfigurationsRequest implements OnvifRequest {
    //Constants
    public static final String TAG = GetDeviceInformationRequest.class.getSimpleName();

    //Attributes
    private final OnvifVideoSourceConfigurationListener listener;

    //Constructors
    public GetVideoSourceConfigurationsRequest(OnvifVideoSourceConfigurationListener listener) {
        super();
        this.listener = listener;
    }

    public OnvifVideoSourceConfigurationListener getListener() {
        return listener;
    }

    @Override
    public String getXml() {
        return "<GetVideoSourceConfigurations xmlns=\"http://www.onvif.org/ver10/media/wsdl\"/>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.GET_VIDEO_CONFIG;
    }
}
