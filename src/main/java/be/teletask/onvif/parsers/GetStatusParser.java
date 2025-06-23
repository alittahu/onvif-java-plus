package be.teletask.onvif.parsers;

import be.teletask.onvif.models.OnvifPTZStatus;
import be.teletask.onvif.responses.OnvifResponse;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

public class GetStatusParser extends OnvifParser<OnvifPTZStatus> {

    @Override
    public OnvifPTZStatus parse(OnvifResponse response) {
        OnvifPTZStatus st = new OnvifPTZStatus();
        try {
            getXpp().setInput(new StringReader(response.getXml()));
            int evt = getXpp().getEventType();
            while (evt != XmlPullParser.END_DOCUMENT) {
                if (evt == XmlPullParser.START_TAG) switch (getXpp().getName()) {
                    case "PanTilt":
                        st.setPan(Double.parseDouble(getXpp().getAttributeValue(null, "x")));
                        st.setTilt(Double.parseDouble(getXpp().getAttributeValue(null, "y")));
                        break;
                    case "Zoom":
                        st.setZoom(Double.parseDouble(getXpp().getAttributeValue(null, "x")));
                        break;
                }
                evt = getXpp().next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }
}
