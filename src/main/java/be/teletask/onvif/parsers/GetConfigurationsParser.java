package be.teletask.onvif.parsers;

import be.teletask.onvif.models.OnvifConfiguration;
import be.teletask.onvif.responses.OnvifResponse;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GetConfigurationsParser extends OnvifParser<List<OnvifConfiguration>> {

    //Constants
    public static final String TAG = GetConfigurationsParser.class.getSimpleName();

    private static final String KEY_PTZ_CONFIGURATION = "PTZConfiguration";
    private static final String ATTR_TOKEN = "token";
    private static final String ATTR_NAME = "Name";
    private static final String KEY_PANTILT_LIMITS = "PanTiltLimits";
    private static final String KEY_ZOOM_LIMITS = "ZoomLimits";

    @Override
    public List<OnvifConfiguration> parse(OnvifResponse response) {

        List<OnvifConfiguration> configurations = new ArrayList<>();
        OnvifConfiguration config = null;
        try {
            getXpp().setInput(new StringReader(response.getXml()));
            eventType = getXpp().getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals(KEY_PTZ_CONFIGURATION)) {

                    config = new OnvifConfiguration();
                    String token = getXpp().getAttributeValue(null, ATTR_TOKEN);
                    getXpp().nextTag();
                    if (getXpp().getName().equals(ATTR_NAME)) {
                        getXpp().next();
                        String name = getXpp().getText();
                        config.setToken(token);
                        config.setName(name);
                    }
                } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals(KEY_PANTILT_LIMITS)) {

                    while(true) {
                        eventType = getXpp().next();
                        // System.out.println("EventType [" + eventType + "], Name:" + getXpp().getName());
                        if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals(KEY_PANTILT_LIMITS)) {
                            break;
                        }

                        if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("XRange")) {

                            while (true) {
                                if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals("XRange")) {
                                    break;
                                }
                                eventType = getXpp().next();
                                if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Min")) {
                                    eventType = getXpp().next();
                                    String text = getXpp().getText();
                                    config.setMinPan(text);
                                } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Max")) {
                                    eventType = getXpp().next();
                                    String text = getXpp().getText();
                                    config.setMaxPan(text);
                                }
                            }

                        } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("YRange")) {

                            while (true) {
                                if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals("YRange")) {
                                    break;
                                }
                                eventType = getXpp().next();
                                if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Min")) {
                                    eventType = getXpp().next();
                                    String text = getXpp().getText();
                                    config.setMinTilt(text);
                                } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Max")) {
                                    eventType = getXpp().next();
                                    String text = getXpp().getText();
                                    config.setMaxTilt(text);
                                }
                            }
                        }

                    }

                } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals(KEY_ZOOM_LIMITS)) {

                    while(true) {
                        eventType = getXpp().next();
                        if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals(KEY_ZOOM_LIMITS)) {
                            break;
                        }

                        if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("XRange")) {

                            while (true) {
                                if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals("XRange")) {
                                    break;
                                }
                                eventType = getXpp().next();
                                if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Min")) {
                                    eventType = getXpp().next();
                                    String text = getXpp().getText();
                                    config.setMinZoom(text);
                                } else if (eventType == XmlPullParser.START_TAG && getXpp().getName().equals("Max")) {
                                    eventType = getXpp().next();
                                    String text = getXpp().getText();
                                    config.setMaxZoom(text);
                                }
                            }

                        }

                    }

                } else if (eventType == XmlPullParser.END_TAG && getXpp().getName().equals(KEY_PTZ_CONFIGURATION)) {
                    configurations.add(config);
                }

                eventType = getXpp().next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return configurations;
    }

    public static void main(String[] args) {
        String s = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:SOAP-ENC=\"http://www.w3.org/2003/05/soap-encoding\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:c14n=\"http://www.w3.org/2001/10/xml-exc-c14n#\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsa5=\"http://www.w3.org/2005/08/addressing\" xmlns:tt=\"http://www.onvif.org/ver10/schema\" xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\" xmlns:ter=\"http://www.onvif.org/ver10/error\">\n" +
                "   <SOAP-ENV:Header/>\n" +
                "   <SOAP-ENV:Body>\n" +
                "      <tptz:GetConfigurationsResponse>\n" +
                "         <tptz:PTZConfiguration token=\"0\">\n" +
                "            <tt:Name>user0</tt:Name>\n" +
                "            <tt:UseCount>2</tt:UseCount>\n" +
                "            <tt:NodeToken>1</tt:NodeToken>\n" +
                "            <tt:DefaultAbsolutePantTiltPositionSpace>http://www.onvif.org/ver10/tptz/PanTiltSpaces/PositionGenericSpace</tt:DefaultAbsolutePantTiltPositionSpace>\n" +
                "            <tt:DefaultAbsoluteZoomPositionSpace>http://www.onvif.org/ver10/tptz/ZoomSpaces/PositionGenericSpace</tt:DefaultAbsoluteZoomPositionSpace>\n" +
                "            <tt:DefaultRelativePanTiltTranslationSpace>http://www.onvif.org/ver10/tptz/PanTiltSpaces/TranslationGenericSpace</tt:DefaultRelativePanTiltTranslationSpace>\n" +
                "            <tt:DefaultRelativeZoomTranslationSpace>http://www.onvif.org/ver10/tptz/ZoomSpaces/TranslationGenericSpace</tt:DefaultRelativeZoomTranslationSpace>\n" +
                "            <tt:DefaultContinuousPanTiltVelocitySpace>http://www.onvif.org/ver10/tptz/PanTiltSpaces/VelocityGenericSpace</tt:DefaultContinuousPanTiltVelocitySpace>\n" +
                "            <tt:DefaultContinuousZoomVelocitySpace>http://www.onvif.org/ver10/tptz/ZoomSpaces/VelocityGenericSpace</tt:DefaultContinuousZoomVelocitySpace>\n" +
                "            <tt:DefaultPTZSpeed>\n" +
                "               <tt:PanTilt space=\"http://www.onvif.org/ver10/tptz/PanTiltSpaces/GenericSpeedSpace\" y=\"1\" x=\"1\"/>\n" +
                "               <tt:Zoom space=\"http://www.onvif.org/ver10/tptz/ZoomSpaces/ZoomGenericSpeedSpace\" x=\"1\"/>\n" +
                "            </tt:DefaultPTZSpeed>\n" +
                "            <tt:DefaultPTZTimeout>PT2147S</tt:DefaultPTZTimeout>\n" +
                "            <tt:PanTiltLimits>\n" +
                "               <tt:Range>\n" +
                "                  <tt:URI>http://www.onvif.org/ver10/tptz/PanTiltSpaces/PositionGenericSpace</tt:URI>\n" +
                "                  <tt:XRange>\n" +
                "                     <tt:Min>-1</tt:Min>\n" +
                "                     <tt:Max>1</tt:Max>\n" +
                "                  </tt:XRange>\n" +
                "                  <tt:YRange>\n" +
                "                     <tt:Min>-1</tt:Min>\n" +
                "                     <tt:Max>1</tt:Max>\n" +
                "                  </tt:YRange>\n" +
                "               </tt:Range>\n" +
                "            </tt:PanTiltLimits>\n" +
                "            <tt:ZoomLimits>\n" +
                "               <tt:Range>\n" +
                "                  <tt:URI>http://www.onvif.org/ver10/tptz/ZoomSpaces/PositionGenericSpace</tt:URI>\n" +
                "                  <tt:XRange>\n" +
                "                     <tt:Min>0</tt:Min>\n" +
                "                     <tt:Max>1</tt:Max>\n" +
                "                  </tt:XRange>\n" +
                "               </tt:Range>\n" +
                "            </tt:ZoomLimits>\n" +
                "         </tptz:PTZConfiguration>\n" +
                "         <tptz:PTZConfiguration token=\"1\">\n" +
                "            <tt:Name>user1</tt:Name>\n" +
                "            <tt:UseCount>2</tt:UseCount>\n" +
                "            <tt:NodeToken>1</tt:NodeToken>\n" +
                "            <tt:DefaultAbsolutePantTiltPositionSpace>http://www.onvif.org/ver10/tptz/PanTiltSpaces/PositionGenericSpace</tt:DefaultAbsolutePantTiltPositionSpace>\n" +
                "            <tt:DefaultAbsoluteZoomPositionSpace>http://www.onvif.org/ver10/tptz/ZoomSpaces/PositionGenericSpace</tt:DefaultAbsoluteZoomPositionSpace>\n" +
                "            <tt:DefaultRelativePanTiltTranslationSpace>http://www.onvif.org/ver10/tptz/PanTiltSpaces/TranslationGenericSpace</tt:DefaultRelativePanTiltTranslationSpace>\n" +
                "            <tt:DefaultRelativeZoomTranslationSpace>http://www.onvif.org/ver10/tptz/ZoomSpaces/TranslationGenericSpace</tt:DefaultRelativeZoomTranslationSpace>\n" +
                "            <tt:DefaultContinuousPanTiltVelocitySpace>http://www.onvif.org/ver10/tptz/PanTiltSpaces/VelocityGenericSpace</tt:DefaultContinuousPanTiltVelocitySpace>\n" +
                "            <tt:DefaultContinuousZoomVelocitySpace>http://www.onvif.org/ver10/tptz/ZoomSpaces/VelocityGenericSpace</tt:DefaultContinuousZoomVelocitySpace>\n" +
                "            <tt:DefaultPTZSpeed>\n" +
                "               <tt:PanTilt space=\"http://www.onvif.org/ver10/tptz/PanTiltSpaces/GenericSpeedSpace\" y=\"1\" x=\"1\"/>\n" +
                "               <tt:Zoom space=\"http://www.onvif.org/ver10/tptz/ZoomSpaces/ZoomGenericSpeedSpace\" x=\"1\"/>\n" +
                "            </tt:DefaultPTZSpeed>\n" +
                "            <tt:DefaultPTZTimeout>PT2147S</tt:DefaultPTZTimeout>\n" +
                "            <tt:PanTiltLimits>\n" +
                "               <tt:Range>\n" +
                "                  <tt:URI>http://www.onvif.org/ver10/tptz/PanTiltSpaces/PositionGenericSpace</tt:URI>\n" +
                "                  <tt:XRange>\n" +
                "                     <tt:Min>-0.5</tt:Min>\n" +
                "                     <tt:Max>0.9</tt:Max>\n" +
                "                  </tt:XRange>\n" +
                "                  <tt:YRange>\n" +
                "                     <tt:Min>-1</tt:Min>\n" +
                "                     <tt:Max>0.8</tt:Max>\n" +
                "                  </tt:YRange>\n" +
                "               </tt:Range>\n" +
                "            </tt:PanTiltLimits>\n" +
                "            <tt:ZoomLimits>\n" +
                "               <tt:Range>\n" +
                "                  <tt:URI>http://www.onvif.org/ver10/tptz/ZoomSpaces/PositionGenericSpace</tt:URI>\n" +
                "                  <tt:XRange>\n" +
                "                     <tt:Min>0.5</tt:Min>\n" +
                "                     <tt:Max>1</tt:Max>\n" +
                "                  </tt:XRange>\n" +
                "               </tt:Range>\n" +
                "            </tt:ZoomLimits>\n" +
                "         </tptz:PTZConfiguration>\n" +
                "      </tptz:GetConfigurationsResponse>\n" +
                "   </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        GetConfigurationsParser parser = new GetConfigurationsParser();
        OnvifResponse response = new OnvifResponse(s);
        List<OnvifConfiguration> list = parser.parse(response);
        System.out.println(list);

    }

}
