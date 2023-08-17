package pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import waiters.Waiters;

import java.util.List;

public abstract class AbsPageObject<T> {
    protected WebDriver driver;
    protected Actions actions;
    protected Waiters waiters;
    public AbsPageObject(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.waiters = new Waiters(driver);

        PageFactory.initElements(driver, this);
    }

    public WebElement $(By locator) {
        return driver.findElement(locator);
    }

    public WebElement $(String locator) {
        return driver.findElement(locatorAnalyzer(locator));
    }

    public List<WebElement> $$(By locator) {
        return  driver.findElements(locator);
    }
    public List<WebElement> $$(String locator) {
        return driver.findElements(locatorAnalyzer(locator));
    }
    private By locatorAnalyzer(String locator) {
        if(locator.startsWith("/")) {
            return By.xpath(locator);
        } else {
            return By.cssSelector(locator);
        }
    }
}
