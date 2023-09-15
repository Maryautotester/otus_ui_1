package pages;

import static org.assertj.core.api.Assertions.assertThat;

import annotations.Path;
import exceptions.PathEmptyException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageobject.AbsPageObject;

public abstract class AbsBasePage<T> extends AbsPageObject<T> {

    private String baseurl = System.getProperty("base.url");
    @FindBy(xpath = "//img[contains(@alt, \"Logo\")]//..")
    WebElement headerMain;

    @FindBy(xpath = "//h1")
    private WebElement header;

    public T mainPageLoaded() {
        assertThat(waiters.waitForElementVisible(headerMain))
                .as("Header should be visible")
                .isTrue();

        return (T) this;
    }
    public T pageHeaderShouldBeSameAs(String header) {
        assertThat(this.header.getText())
                .as("Header should be %s", header)
                .contains(header.replace("Специализация ", ""));
        return (T) this;

    }
    public AbsBasePage(WebDriver driver) {
        super(driver);
    }

    private String getPath() throws PathEmptyException{
        Path path = getClass().getAnnotation(Path.class);
        if (path != null){
            return path.value();
        }
        throw new PathEmptyException();
    }

    public T open() throws PathEmptyException {
        String url = baseurl + getPath();
        driver.get(url);
        return (T) this;
    }
}
