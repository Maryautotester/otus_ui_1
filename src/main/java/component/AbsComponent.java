package component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageobject.AbsPageObject;
import waiters.Waiters;

public class AbsComponent<T> extends AbsPageObject<T> {

    //{
    //    Waiters.waitForCondition(ExpectedConditions.visibilityOfElementLocated(getComponentLocator()));
    //}
    public AbsComponent(WebDriver driver) {
        super(driver);
    }

    protected WebElement getComponentEntity() {
        return null;
    }
}
