package be.teletask.onvif.util;

import be.teletask.onvif.responses.OnvifResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author：chenbojian
 * @ClassName：OnvifRespsonAnalyzeUtils
 * @Date：11/8/2024 10:13 AM
 * @desc： 用于onvif协议各类xml响应体的解析
 */
public class OnvifResponsesAnalyzeUtils {
    public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


    /**
     * 解析出PullMessage方法响应中的值
     *
     * @param onvifResponseXml pullMessage的xml响应体
     * @return 所有值的Map
     */
    public static Map<String, String> getPullMessageValues(String onvifResponseXml) {
        Map<String, String> valuesMap = new HashMap<>();
        try {
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new java.io.ByteArrayInputStream(onvifResponseXml.getBytes("UTF-8")));

            NodeList dataNodes = document.getElementsByTagNameNS("http://www.onvif.org/ver10/schema", "Data");
            for (int i = 0; i < dataNodes.getLength(); i++) {
                Element dataElement = (Element) dataNodes.item(i);
                NodeList simpleItems = dataElement.getElementsByTagNameNS("http://www.onvif.org/ver10/schema", "SimpleItem");

                for (int j = 0; j < simpleItems.getLength(); j++) {
                    Element simpleItem = (Element) simpleItems.item(j);
                    String name = simpleItem.getAttribute("Name");
                    String value = simpleItem.getAttribute("Value");
                    valuesMap.put(name, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valuesMap;
    }

}
