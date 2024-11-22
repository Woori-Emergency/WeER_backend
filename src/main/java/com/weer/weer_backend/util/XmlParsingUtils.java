package com.weer.weer_backend.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParsingUtils {

    public static String getTextContentSafely(Node item, String tagName) {
        NodeList nodeList = ((Element) item).getElementsByTagName(tagName);
        return (nodeList != null && nodeList.item(0) != null) ? nodeList.item(0).getTextContent() : null;
    }

    /**
     * 현재 <item> 노드에서 지정한 태그의 값을 Integer로 파싱합니다.
     *
     * @param node 현재 <item> 노드
     * @param tagName 가져올 태그 이름
     * @return Integer 값, 또는 null
     */
    public static Integer parseIntegerSafely(Node node, String tagName) {
        String textContent = getTextContentSafely(node, tagName);
        try {
            return (textContent != null && !textContent.trim().isEmpty()) ? Integer.parseInt(textContent.trim()) : null;
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse integer for tag: " + tagName + ", value: " + textContent);
            return null;
        }
    }

    /**
     * 현재 <item> 노드에서 지정한 태그의 값을 Boolean으로 파싱합니다.
     *
     * @param node 현재 <item> 노드
     * @param tagName 가져올 태그 이름
     * @return Boolean 값, 또는 null
     */
    public static Boolean parseBooleanSafely(Node node, String tagName) {
        String textContent = getTextContentSafely(node, tagName);
        if (textContent == null) return null;
        switch (textContent.trim().toUpperCase()) {
            case "Y":
                return true;
            case "N1":
                return false;
            default:
                return null;
        }
    }
}
