package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;

public class GetPTZStopRequest implements OnvifRequest {

    private static GetPTZStopRequest instance;

    private GetPTZStopRequest() {}

    public static synchronized GetPTZStopRequest getInstance() {
        if (instance == null) {
            instance = new GetPTZStopRequest();
        }
        return instance;
    }

    public static final String TAG = GetPTZStopRequest.class.getSimpleName();

    @Override
    public String getXml() {
        return "<StopResponse xmlns=\"http://www.onvif.org/ver20/ptz/wsdl/Stop\"></StopResponse>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.GET_PTZ_URI;
    }
}
