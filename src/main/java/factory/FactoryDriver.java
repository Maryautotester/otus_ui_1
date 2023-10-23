package factory;

import exceptions.BrowserNotSupportedException;
import factory.impl.ChromeWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class FactoryDriver implements IFactoryDriver {
    private final static String BROWSER_NAME = System.getProperty("browser", "chrome");
    private final static String REMOTE_URL = System.getProperty("webdriver.remote.url");


    @Override
    public EventFiringWebDriver getDriver() throws BrowserNotSupportedException {


        switch (BROWSER_NAME) {
            case "chrome": {
                ChromeWebDriver chromeWebDriver = new ChromeWebDriver();
                if(REMOTE_URL == null) {
                    chromeWebDriver.downloadLocalWebDriver(BROWSER_NAME);
                }
                WebDriverManager.getInstance(BROWSER_NAME).setup();
                return new EventFiringWebDriver(chromeWebDriver.newDriver());
            }
            default:
                try {
                    throw new BrowserNotSupportedException(BROWSER_NAME);
                } catch (BrowserNotSupportedException ex) {
                    ex.printStackTrace();
                    return null;
                }

        }

    }
}
