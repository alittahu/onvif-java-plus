package be.teletask.onvif.parsers;

import be.teletask.onvif.models.OnvifPTZStatus;
import be.teletask.onvif.responses.OnvifResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

public class GetStatusParser extends OnvifParser<OnvifPTZStatus> {

    //Constants
    public static final String TAG = GetStatusParser.class.getSimpleName();

    private static final String KEY_POSITION = "Position";
    private static final String KEY_POSITION_PANTILT = "PanTilt";
    private static final String KEY_POSITION_ZOOM = "Zoom";

    @Override
    public OnvifPTZStatus parse(OnvifResponse response) {

        OnvifPTZStatus status = new OnvifPTZStatus();
        try {
            getXpp().setInput(new StringReader(response.getXml()));
            eventType = getXpp().getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals(KEY_POSITION_PANTILT)) {
                    for (int i = 0; i < getXpp().getAttributeCount(); i++) {
                        if (getXpp().getAttributeName(i).equals("x")) {
                            status.setPan(Double.parseDouble(getXpp().getAttributeValue(i)));
                        } else if (getXpp().getAttributeName(i).equals("y")) {
                            status.setTilt(Double.parseDouble(getXpp().getAttributeValue(i)));
                        }
                    }
                } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals(KEY_POSITION_ZOOM)) {
                    for (int i = 0; i < getXpp().getAttributeCount(); i++) {
                        if (getXpp().getAttributeName(i).equals("x")) {
                            status.setZoom(Double.parseDouble(getXpp().getAttributeValue(i)));
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals(KEY_POSITION)) {
                    return status;
                }

                eventType = getXpp().next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
