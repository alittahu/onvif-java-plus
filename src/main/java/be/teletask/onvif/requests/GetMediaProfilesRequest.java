package be.teletask.onvif.requests;

import be.teletask.onvif.models.OnvifType;


/**
 * Created by Tomas Verhelst on 04/09/2018.
 * Copyright (c) 2018 TELETASK BVBA. All rights reserved.
 */
public class GetMediaProfilesRequest implements OnvifRequest {

    //Constants
    public static final String TAG = GetMediaProfilesRequest.class.getSimpleName();

    @Override
    public String getXml() {
        return "<GetProfiles xmlns=\"http://www.onvif.org/ver10/media/wsdl\"/>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.GET_MEDIA_PROFILES;
    }

}
