package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


public class GetEventsPullMessageRequest implements OnvifRequest {

    public static final String TAG = GetEventsPullMessageRequest.class.getSimpleName();

    private static GetEventsPullMessageRequest instance;

    private GetEventsPullMessageRequest() {}

    public static synchronized GetEventsPullMessageRequest getInstance() {
        if (instance == null) {
            instance = new GetEventsPullMessageRequest();
        }
        return instance;
    }

    @Override
    public String getXml() {
        return "<ContinuousMoveResponse xmlns=\"http://www.onvif.org/ver10/events/wsdl//onvif/Events/PullSubManager_20241105T090817Z_1 /PullMessagesRequest\"></ContinuousMoveResponse>";
    }
 
    @Override
    public OnvifType getType() {
        return OnvifType.GET_EVENTS_PULL_MESSAGE_URI;
    }
}