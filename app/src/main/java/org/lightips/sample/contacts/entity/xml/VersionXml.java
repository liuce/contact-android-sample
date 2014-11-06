package org.lightips.sample.contacts.entity.xml;

import org.lightips.common.http.xml.XmlResult;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by liuce on 10/29/14.
 */
@Root(name="version")
public class VersionXml {
    @Element(required=false, name = "versionCode")
    private String versionCode;
    @Element(required=false, name = "versionName")
    private String versionName;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public static class VersionXmlResult extends XmlResult{

        @ElementList(required = false, name = "data")
        List<VersionXml> data;

        public List<VersionXml> getData() {
            return data;
        }

        public void setData(List<VersionXml> data) {
            this.data = data;
        }
    }
}
