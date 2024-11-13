package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


public class GetEventsCreatePullPointSubscriptionRequest implements OnvifRequest {

    public static final String TAG = GetEventsCreatePullPointSubscriptionRequest.class.getSimpleName();

    private static GetEventsCreatePullPointSubscriptionRequest instance;

    private GetEventsCreatePullPointSubscriptionRequest() {}

    public static synchronized GetEventsCreatePullPointSubscriptionRequest getInstance() {
        if (instance == null) {
            instance = new GetEventsCreatePullPointSubscriptionRequest();
        }
        return instance;
    }
    @Override
    public String getXml() {
        return "<CreatePullPointSubscription xmlns=\"http://www.onvif.org/ver10/events/wsdl/EventPortType/CreatePullPointSubscriptionRequest\"></CreatePullPointSubscription>";
    }
 
    @Override
    public OnvifType getType() {
        return OnvifType.GET_EVENTS_URI;
    }
}