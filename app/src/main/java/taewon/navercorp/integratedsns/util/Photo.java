package taewon.navercorp.integratedsns.util;

import java.io.Serializable;

/**
 * Created by tedkim on 2017. 11. 19..
 */
@SuppressWarnings("serial")
public class Photo implements Serializable {

    private Integer height;
    private String src;
    private Integer width;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
