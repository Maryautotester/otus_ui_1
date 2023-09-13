package factory;

import exceptions.BrowserNotSupportedException;
import factory.impl.ChromeWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class FactoryDriver implements IFactoryDriver {
    private final static String BROWSER_NAME = System.getProperty("browser", "chrome");

    @Override
    public EventFiringWebDriver getDriver() throws BrowserNotSupportedException {

        switch (BROWSER_NAME) {
            case "chrome": {
                ChromeWebDriver chromeWebDriver = new ChromeWebDriver();
                chromeWebDriver.downloadLocalWebDriver(BROWSER_NAME);
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
