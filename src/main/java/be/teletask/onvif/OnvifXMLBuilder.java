//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package be.teletask.onvif;

import java.util.UUID;

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

    public static String getCreatePullPointSubscriptionBody(String filterExpression,String subscriptionPolicyUrl) {
        StringBuilder body = new StringBuilder();
        body.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
                .append("<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" ")
                .append("xmlns:a=\"http://www.w3.org/2005/08/addressing\">")
                .append("<s:Header>")
                .append("<a:Action s:mustUnderstand=\"1\">http://www.onvif.org/ver10/events/wsdl/EventPortType/CreatePullPointSubscriptionRequest</a:Action>")
                .append("<a:ReplyTo><a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo>")
                .append("<a:To s:mustUnderstand=\"1\">"+subscriptionPolicyUrl+"</a:To>")
                .append("</s:Header>")
                .append("<s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">")
                .append("<CreatePullPointSubscription xmlns=\"http://www.onvif.org/ver10/events/wsdl\">")
                .append("<Filter>")
                .append("<wsnt:TopicExpression Dialect=\"http://www.onvif.org/ver10/tev/topicExpression/ConcreteSet\" ")
                .append("xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" ")
                .append("xmlns:tns1=\"http://www.onvif.org/ver10/topics\" ")
                .append("xmlns:yy=\"http://custom/topics\" ")
                .append("xmlns:tt=\"http://www.onvif.org/ver10/schema\">")
                .append(filterExpression)
                .append("</wsnt:TopicExpression>")
                .append("</Filter>")
                .append("</CreatePullPointSubscription>")
                .append("</s:Body>")
                .append("</s:Envelope>");

        return body.toString();
    }

    public static String getUnsubscribeBody(String subscriptionPolicyUrl) {
        String messageID= UUID.randomUUID().toString();
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                "xmlns:a=\"http://www.w3.org/2005/08/addressing\">" +
                "<s:Header>" +
                "<a:Action s:mustUnderstand=\"1\">http://docs.oasis-open.org/wsn/bw-2/SubscriptionManager/UnsubscribeRequest</a:Action>" +
                "<a:MessageID>urn:uuid:" + messageID + "</a:MessageID>" +
                "<a:ReplyTo>" +
                "<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>" +
                "</a:ReplyTo>" +
                "<a:To s:mustUnderstand=\"1\">" + subscriptionPolicyUrl + "</a:To>" +
                "</s:Header>" +
                "<s:Body xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" +
                "<Unsubscribe xmlns=\"http://docs.oasis-open.org/wsn/b-2\" />" +
                "</s:Body>" +
                "</s:Envelope>";
    }



}
