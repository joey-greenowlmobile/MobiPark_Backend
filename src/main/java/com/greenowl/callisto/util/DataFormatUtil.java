package com.greenowl.callisto.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by greenowl on 15-09-17.
 */
public class DataFormatUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DataFormatUtil.class);

    private DataFormatUtil() {
    }

    /**
     * Converts a String in xml format to a JSON string.
     *
     * @param xml
     * @return
     * @throws JSONException
     */
    public static String getJsonFromXML(String xml) throws JSONException {
        LOG.debug("Attempting to convert xml string = {}", xml);
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj.toString();
    }

    /**
     * Converts a String in JSON format to XML String.
     *
     * @param json
     * @return
     * @throws JSONException
     */
    public static String getXMLFromJSON(String json) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        return XML.toString(jsonObj);
    }

}
