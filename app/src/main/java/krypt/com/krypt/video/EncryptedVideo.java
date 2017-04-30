package krypt.com.krypt.video;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hackean on 4/29/17.
 */

public class EncryptedVideo extends RealmObject {

    @PrimaryKey
    private Long objectId;

    private String originalPath;

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getOriginalPath() {
        return originalPath;
    }
}
