package be.teletask.onvif.parsers;

import be.teletask.onvif.models.OnvifVideoSourceConfiguration;
import be.teletask.onvif.responses.OnvifResponse;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

public class GetVideoSourceConfigurationsParser extends OnvifParser<OnvifVideoSourceConfiguration> {
    private static final String KEY_ROTATION = "Rotation";
    private static final String KEY_MIRROR   = "Mirror";
    private static final String KEY_FLIP     = "Flip";

    @Override
    public OnvifVideoSourceConfiguration parse(OnvifResponse response) {
        OnvifVideoSourceConfiguration vsc = new OnvifVideoSourceConfiguration();

        try {
            getXpp().setInput(new StringReader(response.getXml()));
            int eventType = getXpp().getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = getXpp().getName();
                    switch (name) {
                        case KEY_ROTATION:
                            getXpp().next();
                            vsc.setRotation("true".equalsIgnoreCase(getXpp().getText()));
                            break;
                        case KEY_MIRROR:
                            getXpp().next();
                            vsc.setMirror("true".equalsIgnoreCase(getXpp().getText()));
                            break;
                        case KEY_FLIP:
                            getXpp().next();
                            vsc.setFlip("true".equalsIgnoreCase(getXpp().getText()));
                            break;
                    }
                }
                eventType = getXpp().next();
            }
        } catch (Exception e) {
            // log or rethrow as needed
            e.printStackTrace();
        }

        return vsc;
    }
}
