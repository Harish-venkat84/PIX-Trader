package com.trader.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TraderUtils {

    public static WebDriver driver;

    // return the class name as a string
    public String exceptionClassNameAndMessage(Exception exceptionObj) {

        return " | Default error message --> "+exceptionObj.getMessage()
                + " | Method implementation class --> "+exceptionObj.getStackTrace()[0]
                + " | Method extends class --> "+exceptionObj.getStackTrace()[1];
    }

    // this method is user for browser launch
    // parameter ==> (browser -> name), (url -> application url)
    public static WebDriver launchBrowser(String browser, String url, String headLess) {

        try {

            if (browser.equalsIgnoreCase("Chrome")) {

                driver = new ChromeDriver(options(headLess));

            } else if (browser.equalsIgnoreCase("Edge")) {

                driver = new EdgeDriver();

            } else if (browser.equalsIgnoreCase("Firefox")) {

                driver = new FirefoxDriver();
            }

            driver.manage().window().maximize();

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            driver.get(url);

//            pageObjectManager = new PageObjectManager(driver);

        }catch(Exception exception) {

            System.out.println("problem on launching browser"
                    + " | Default error message --> "+exception.getMessage()
                    + " | Method implementation class --> "+exception.getStackTrace()[0]
                    + " | Method extends class --> "+exception.getStackTrace()[1]);
        }

        return driver;
    }


    static ChromeOptions option;
    static Map<String, Integer> contentSettings = new HashMap<>();
    static Map<String, Object> profile = new HashMap<>();
    static Map<String, Object> prefs = new HashMap<>();
    private static ChromeOptions options(String headLess){

        option = new ChromeOptions();

        if (headLess.equalsIgnoreCase("HeadLess")) {

            option.addArguments("--headless=new");
        }

        contentSettings.put("media_stream", 2);
        profile.put("managed_default_content_settings", contentSettings);
        prefs.put("profile", profile);
        option.setExperimentalOption("prefs", prefs);

        return option;
    }


    static WebDriverWait wait;
    public WebDriverWait waitObj(){

        return wait == null ? new WebDriverWait(driver, Duration.ofSeconds(20)) : wait;
    }

    public void sendKeys(WebElement element, String text){

        waitObj().until(elementToBeClickable(element)).sendKeys(text);
    }

    public void clickElement(WebElement element){

        waitObj().until(elementToBeClickable(element)).click();
    }

    public void sleep(long time){

        try{ Thread.sleep(time); }catch (Exception exception){ exceptionClassNameAndMessage(exception); }
    }

    static ObjectMapper mapper = new ObjectMapper();
    @SuppressWarnings(value = "unchecked")
    public Map<Object, Object> jsonReader(String filePath) {

        Map<Object, Object> json = null;
        
        try{

            json = mapper.readValue(Paths.get(System.getProperty("user.dir")+filePath).toFile(), Map.class);
            

        }catch (Exception ex) {

            System.out.println("problem on jsonRead method");
        }

        return json;
    }

    public void jsonPayloadWriter(Object object, String filePath) {

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(System.getProperty("user.dir") + filePath), object);
        }catch (Exception ex){

            System.out.println("problem on jsonPayloadWriter method");
        }
    }

    public Map<String, String> filePaths(){

        Map<String, String> files = new HashMap<>();

        files.put("Login", "/applicationData/Login.json");
        files.put("Screenshots", "/screenshots");

        return files;
    }

    public void createNewFolder(){

        File newDirectory = new File(System.getProperty("user.dir") + File.separator + filePaths().get("Screenshots"));

        if(!newDirectory.exists()){

        newDirectory.mkdir();
        }

    }

    public void deleteFolder(){

        try {

            File directory = new File(System.getProperty("user.dir") + File.separator + filePaths().get("Screenshots"));

            if (!directory.exists()) {
                directory.delete();
            }
        }catch (Exception exception){

            System.out.println(exceptionClassNameAndMessage(exception));
        }
    }

}