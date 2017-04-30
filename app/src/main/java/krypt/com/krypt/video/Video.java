package krypt.com.krypt.video;

import android.support.annotation.NonNull;

/**
 * Created by hackean on 4/28/17.
 */

public class Video implements Comparable<Video>{

    private int serialNumber;
    private String path;

    public Video(String path, int serialNumber) {
        this.path = path;
        this.serialNumber = serialNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public int compareTo(@NonNull Video video) {
        if (this.getSerialNumber() > video.getSerialNumber()){
            return 1;
        } else if (this.getSerialNumber() < video.getSerialNumber()){
            return -1;
        } else {
            return 0;
        }
    }
}
