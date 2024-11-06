//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif;

public class OnvifXMLBuilder {
    public static final String TAG = OnvifXMLBuilder.class.getSimpleName();

    public OnvifXMLBuilder() {
    }

    public static String getSoapHeader() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" ><soap:Body>";
    }

    public static String getEnvelopeEnd() {
        return "</soap:Body></soap:Envelope>";
    }

    public static String getDiscoverySoapHeader() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" xmlns:tns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\"><soap:Header><wsa:Action>http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action><wsa:MessageID>urn:uuid:%s</wsa:MessageID>\n<wsa:To>urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To>\n</soap:Header><soap:Body>";
    }

    public static String getDiscoverySoapBody() {
        return "<tns:Probe/>";
    }

    public static String getContinuousMoveBody(String profileToken, double panSpeed, double tiltSpeed, double zoomSpeed) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                "xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\" " +
                "xmlns:tt=\"http://www.onvif.org/ver10/schema\">" +
                "<s:Body>" +
                "<tptz:ContinuousMove>" +
                "<tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>" +
                "<tptz:Velocity>" +
                "<tt:PanTilt x=\"" + panSpeed + "\" y=\"" + tiltSpeed + "\" />" +
                "<tt:Zoom x=\"" + zoomSpeed + "\" />" +
                "</tptz:Velocity>" +
                "</tptz:ContinuousMove>" +
                "</s:Body>" +
                "</s:Envelope>";
    }

    public static String getStopBody(String profileToken, Boolean panTilt, Boolean zoom) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" ")
                .append("xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\">")
                .append("<s:Body>")
                .append("<tptz:Stop>")
                .append("<tptz:ProfileToken>").append(profileToken).append("</tptz:ProfileToken>");

        // 添加可选参数
        if (panTilt != null) {
            sb.append("<tptz:PanTilt>").append(panTilt).append("</tptz:PanTilt>");
        }
        if (zoom != null) {
            sb.append("<tptz:Zoom>").append(zoom).append("</tptz:Zoom>");
        }

        sb.append("</tptz:Stop>")
                .append("</s:Body>")
                .append("</s:Envelope>");

        return sb.toString();
    }

    public static String getPullMessagesBody(String timeout, int messageLimit) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" ")
                .append("xmlns:wse=\"http://www.w3.org/2005/08/addressing\" ")
                .append("xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">")
                .append("<s:Body>")
                .append("<PullMessages xmlns=\"http://www.onvif.org/ver10/events/wsdl\">")
                .append("<Timeout>").append(timeout).append("</Timeout>")
                .append("<MessageLimit>").append(messageLimit).append("</MessageLimit>")
                .append("</PullMessages>")
                .append("</s:Body>")
                .append("</s:Envelope>");

        return sb.toString();
    }



}
