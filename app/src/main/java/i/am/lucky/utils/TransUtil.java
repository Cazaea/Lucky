package i.am.lucky.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cazaea
 * @time 2017/11/28 10:41
 * @mail wistorm@sina.com
 */

public class TransUtil {

    /**
     * xml 转 map
     *
     * @param xmlStr
     * @return
     */
    public Map<String, String> xmlToMap(String xmlStr) {
        Map<String, String> map = new HashMap<>();
        try {
            SAXReader reader = new SAXReader();
            InputStream ins = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
            Document doc = reader.read(ins);
            Element root = doc.getRootElement();

            List<Element> list = root.elements();

            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
