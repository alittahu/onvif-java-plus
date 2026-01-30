package be.teletask.onvif.parsers;

import be.teletask.onvif.models.OnvifPTZPreset;
import be.teletask.onvif.responses.OnvifResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GetPresetsParser {

    public List<OnvifPTZPreset> parse(OnvifResponse response) {
        List<OnvifPTZPreset> presets = new ArrayList<>();
        if (response == null || response.getXml() == null) return presets;

        try {
            XmlPullParserFactory f = XmlPullParserFactory.newInstance();
            f.setNamespaceAware(true);
            XmlPullParser xpp = f.newPullParser();
            xpp.setInput(new StringReader(response.getXml()));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && "Preset".equals(xpp.getName())) {
                    String token = getAttrAnyNs(xpp, "token");
                    String name = null;

                    int presetDepth = xpp.getDepth();

                    // Walk inside <Preset> ... </Preset>
                    while (!(eventType == XmlPullParser.END_TAG
                            && xpp.getDepth() == presetDepth
                            && "Preset".equals(xpp.getName()))) {

                        eventType = xpp.next();

                        if (eventType == XmlPullParser.START_TAG && "Name".equals(xpp.getName())) {
                            // next() -> TEXT
                            if (xpp.next() == XmlPullParser.TEXT) {
                                name = xpp.getText();
                            }
                        }
                    }

                    presets.add(new OnvifPTZPreset(token, name));
                }

                eventType = xpp.next();
            }

        } catch (Exception ignored) {
            // keep presets empty on parse failures
        }

        return presets;
    }

    private static String getAttrAnyNs(XmlPullParser xpp, String attrLocalName) {
        // Some cameras return attributes with ns, so scan all attrs.
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            String n = xpp.getAttributeName(i);
            if (attrLocalName.equals(n)) {
                return xpp.getAttributeValue(i);
            }
        }
        // fallback: common weird casing
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            String n = xpp.getAttributeName(i);
            if (attrLocalName.equalsIgnoreCase(n)) {
                return xpp.getAttributeValue(i);
            }
        }
        return null;
    }
}
