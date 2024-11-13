package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


public class GetPTZContinuousMoveRequest implements OnvifRequest {
    private static GetPTZContinuousMoveRequest instance;

    private GetPTZContinuousMoveRequest() {}

    public static synchronized GetPTZContinuousMoveRequest getInstance() {
        if (instance == null) {
            instance = new GetPTZContinuousMoveRequest();
        }
        return instance;
    }

    public static final String TAG = GetPTZContinuousMoveRequest.class.getSimpleName();
    @Override
    public String getXml() {
        return "<ContinuousMoveResponse xmlns=\"http://www.onvif.org/ver20/ptz/wsdl\"></ContinuousMoveResponse>";
    }
 
    @Override
    public OnvifType getType() {
        return OnvifType.GET_PTZ_URI;
    }
}