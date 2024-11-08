package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


public class GetPTZStopRequest implements OnvifRequest {

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