package factory;

import exceptions.BrowserNotSupportedException;
import org.openqa.selenium.WebDriver;

public interface IFactoryDriver {
    WebDriver getDriver() throws BrowserNotSupportedException;
}
