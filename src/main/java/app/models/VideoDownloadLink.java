package app.models;

import app.utils.Downloader;
import org.openqa.selenium.WebElement;

public class VideoDownloadLink{
    public String format;
    public String quality;
    private String link;

    public VideoDownloadLink(WebElement link){
        format = link.getAttribute("data-type").replace("dash","No Audio");
        quality = link.getAttribute("data-quality");
        this.link = link.getAttribute("href");
    }

    public String getFormat(){
        return format;
    }

    public String getQuality(){
        return quality;
    }

    public String getLink(){
        return link;
    }

    public String toString(){
        return (format + "(" + quality + ")");// -> " + Downloader.getInstance(link).getSize());
    }
}