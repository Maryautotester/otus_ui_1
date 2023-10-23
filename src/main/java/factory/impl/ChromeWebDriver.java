package factory.impl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class ChromeWebDriver implements IDriver {

    @Override
    public WebDriver newDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--no-first-run");
        // chromeOptions.addArguments("--enable-extensions");
        chromeOptions.addArguments("--homepage=about:blank");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        chromeOptions.setCapability(CapabilityType.VERSION, System.getProperty("browser.version", "115.0"));
        chromeOptions.setCapability(CapabilityType.BROWSER_NAME, System.getProperty("browser", "chrome"));
        chromeOptions.setCapability("enableVNC", Boolean.parseBoolean(System.getProperty("enableVNC", "true")));

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
        chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);


        URL remoteURL = getRemoteUrl();
        System.out.println("remoteUrl = " + remoteURL);
        if (remoteURL == null) {
            return new ChromeDriver(chromeOptions);
        } else {
            return new RemoteWebDriver(getRemoteUrl(), chromeOptions);
        }
    }
    URL getRemoteUrl() {
        try {
            return new URL(System.getProperty("webdriver.remote.url"));
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
