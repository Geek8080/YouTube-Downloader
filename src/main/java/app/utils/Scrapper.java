package app.utils;

import app.controllers.HomeController;
import app.models.VideoDownloadLink;
import javafx.scene.image.Image;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Scrapper {

    public static ArrayList<VideoDownloadLink> scrapeYouTube(String url){
        System.setProperty("phantomjs.binary.path", "phantomjs.exe");
        WebDriver page = new PhantomJSDriver();

        String downloadURL = "http://en.savefrom.net";
        page.get(downloadURL);
        try {
            new WebDriverWait(page, 20).until(ExpectedConditions.presenceOfElementLocated((By.id("sf_url"))));
            System.out.println("Page loaded Successfully");
        }catch (Exception ex){
            javax.swing.JOptionPane.showMessageDialog(null, "The network seems to be to slow...");
            return null;
        }
        page.findElement(By.id("sf_url")).sendKeys(url + Keys.ENTER);
        try {
            new WebDriverWait(page, 20).until(ExpectedConditions.presenceOfElementLocated(By.className("info-box")));
            new WebDriverWait(page, 20).until(
                    webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        }catch (Exception ex){
            javax.swing.JOptionPane.showMessageDialog(null, "The network seems to be to slow...");
            return null;
        }
        //Content loaded on the page
        //Code to download if more than one option is available
            WebElement btn = page.findElement(By.cssSelector("div.drop-down-box"));
            Actions dropDownOpen = new Actions(page);
            dropDownOpen.moveToElement(btn).click().build().perform();


        WebElement formatList = page.findElement(By.cssSelector("div.list"));
        List<WebElement> link_groups = formatList.findElements(By.cssSelector("div.link-group"));

        ArrayList<VideoDownloadLink> Videos = new ArrayList<>();
        for (WebElement link_group : link_groups){
            List<WebElement> links = link_group.findElements(By.tagName("a"));
            //System.out.println(links.size() + " Links in this group");
            for(WebElement link: links){
                Videos.add(new VideoDownloadLink(link));
            }
        }

        String title = page.findElement(By.cssSelector("div.row:nth-child(1)")).getText();
        HomeController.videoName = title;
        //String duration = page.findElement(By.className("meta")).findElement(By.className("row duration")).getText();
        //HomeController.dur = duration;
        //System.out.println("\n\n\n" + duration);
        WebElement thumbNail = page.findElement(By.className("thumb"));
        try {
            URL URL = new URL(thumbNail.getAttribute("src"));
            System.out.println(URL);
            BufferedImage img = ImageIO.read(URL);
            String ext = getExt(thumbNail.getAttribute("src"));
            File imageFile = new File("Images/img." + ext);
            if(imageFile.exists()){
                imageFile.delete();
            }
            if(!imageFile.getParentFile().exists())
                if(!imageFile.getParentFile().mkdirs())
                    System.out.println("Couldn't make directories...");
            ImageIO.write(img, getExt(thumbNail.getAttribute("src")), imageFile);
            imageFile.deleteOnExit();
            HomeController.iconLocation = imageFile.getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        }


        page.close();

        return Videos;
    }

    public static String getExt(String src) {
        String res = "";
        res = src.substring(src.length()-4);
        if (res.charAt(0)=='.')
            res = res.substring(1);
        return res;
    }

}
