package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


public class GetPTZContinuousMoveRequest implements OnvifRequest {

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