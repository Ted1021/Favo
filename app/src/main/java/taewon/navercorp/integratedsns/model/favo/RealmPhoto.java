package taewon.navercorp.integratedsns.model.favo;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by USER on 2017-11-22.
 */

@SuppressWarnings("serial")
public class RealmPhoto extends RealmObject implements Serializable{

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
