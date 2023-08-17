package pages;

import static org.assertj.core.api.Assertions.assertThat;

import annotations.Path;
import annotations.Template;
import annotations.UrlTemplates;
import exceptions.PathEmptyException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pageobject.AbsPageObject;

@UrlTemplates(
        templates = {@Template(name = "news", template = "/{1}/{2}"),
        @Template(name= "articles", template = "/{1}")}
)
public abstract class AbsBasePage<T> extends AbsPageObject {

    private String baseurl = System.getProperty("base.url");

    @FindBy(xpath = "//img[contains(@alt, \"Logo\")]//..")
    WebElement headerMain;

    @FindBy(xpath = "//div[@class=\"course-header2__title\"]")
    private WebElement header;

    public T pageHeaderShouldBeVisible() {
        assertThat(waiters.waitForElementVisible(header))
                .as("Header should be visible")
                .isTrue();

        return (T)this;
    }
    public T mainPageLoaded() {
        assertThat(waiters.waitForElementVisible(headerMain))
                .as("Header should be visible")
                .isTrue();

        return (T) this;
    }
    public T pageHeaderShouldBeSameAs(String header) {
        assertThat(this.header.getText())
                .as("Header should be {}", header)
                .isEqualTo(header);
        return (T) this;

    }
    public AbsBasePage(WebDriver driver) {
        super(driver);
    }

    private String getPath() throws PathEmptyException{
        Class<? extends AbsBasePage> clazz = this.getClass();

        if (clazz.isAnnotationPresent(Path.class)) {
            Path path = clazz.getAnnotation(Path.class);
            return path.value();
        }
        throw new PathEmptyException();
    }
    public T open() throws PathEmptyException {
        String path = getPath();
        String url = baseurl + getPath();

        driver.get(url);

        return (T) this;
    }

    public T open(String name, String[] params) {
        Class<? extends AbsBasePage> clazz = this.getClass();
        if(clazz.isAnnotationPresent(UrlTemplates.class)) {
            UrlTemplates urlTemplates = clazz.getAnnotation(UrlTemplates.class);
            Template[] templates = urlTemplates.templates();

            for(Template template: templates) {
                if (template.name().equals(name)) {
                    String urlTemplate = template.template();
                    for (int i = 0; i < params.length; i++) {
                        urlTemplate = urlTemplate.replace(String.format("{%d}", i + 1), params[i]);
                    }
                    driver.get(baseurl + urlTemplate);
                }
            }
        }
        return (T) this;
    }
}
