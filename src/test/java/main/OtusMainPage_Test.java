package main;

import annotations.Driver;
import exceptions.PathEmptyException;
import extensions.UIExtensions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.OtusMainPage;

@ExtendWith(UIExtensions.class)
public class OtusMainPage_Test {
    @Driver
    public WebDriver driver;
    @Test
    @DisplayName("Выбор самого позднего курса")
    public void clickOnLatestCourse() throws PathEmptyException {
        OtusMainPage mainPage = new OtusMainPage(driver)
                .open()
                .mainPageLoaded()
                .pageListCoursesShouldBeVisible();
        String course = mainPage.getCourseNameByStartDate(false);
        System.out.println("Позже всех стартует курс " + course);

        mainPage.clickCourseThumbsByTitle(course)
                .pageHeaderShouldBeSameAs(course.replaceAll("Специализация сетевой инженер", "Network Engineer"));
    }
}
