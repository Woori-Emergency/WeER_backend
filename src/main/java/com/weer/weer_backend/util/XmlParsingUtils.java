package com.weer.weer_backend.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlParsingUtils {

    /**
     * XML Document에서 지정한 태그 이름과 인덱스로 안전하게 텍스트 콘텐츠를 가져온다
     *
     * @param doc XML Document 객체
     * @param tagName 태그 이름
     * @param index 요소 인덱스
     * @return 요소의 텍스트 콘텐츠 또는 null (존재하지 않는 경우)
     */
    public static String getTextContentSafely(Document doc, String tagName, int index) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        return (nodeList != null && nodeList.item(index) != null) ? nodeList.item(index).getTextContent() : null;
    }

    /**
     * XML Document에서 지정한 태그 이름과 인덱스로 Integer 값을 안전하게 파싱
     *
     * @param doc XML Document 객체
     * @param tagName 태그 이름
     * @param index 요소 인덱스
     * @return Integer 값 또는 null (값이 없거나 파싱 불가한 경우)
     */
    public static Integer parseIntegerSafely(Document doc, String tagName, int index) {
        String textContent = getTextContentSafely(doc, tagName, index);
        return textContent != null ? Integer.parseInt(textContent) : null;
    }

    /**
     * XML Document에서 지정한 태그 이름과 인덱스로 Boolean 값을 안전하게 파싱
     * "Y"이면 true, "N1"이면 false로 변환하며, 그 외는 null로 처리
     *
     * @param doc XML Document 객체
     * @param tagName 태그 이름
     * @param index 요소 인덱스
     * @return Boolean 값 또는 null
     */
    public static Boolean parseBooleanSafely(Document doc, String tagName, int index) {
        String textContent = getTextContentSafely(doc, tagName, index);
        if ("Y".equalsIgnoreCase(textContent)) {
            return true;
        } else if ("N1".equalsIgnoreCase(textContent)) {
            return false;
        }
        return null; // "Y" 또는 "N1" 이외의 값은 null
    }
}
