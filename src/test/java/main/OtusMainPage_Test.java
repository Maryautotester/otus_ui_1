package main;

import annotations.Driver;
import exceptions.PathEmptyException;
import extensions.UIExtensions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

import pages.OtusMainPage;

@ExtendWith(UIExtensions.class)
public class OtusMainPage_Test {
    @Driver
    public WebDriver driver;

    @Test
    public void filterCourseTest() throws PathEmptyException {
        OtusMainPage mainPage = new OtusMainPage(driver);
        mainPage.open()
                .mainPageLoaded()
                .pageListCoursesShouldBeVisible()
                .clickCourseThumbsByTitle("Специализация Python");
    }
    @Test
    public void clickOnEarliestCourse() throws PathEmptyException {
        OtusMainPage mainPage = new OtusMainPage(driver)
                .open()
                .mainPageLoaded()
                .pageListCoursesShouldBeVisible();
        System.out.println(mainPage.getListCourses());
        String earliestCourse = mainPage.getCourseNameByStartDate(true);
        System.out.println("Раньше всех стартует курс " + earliestCourse);

        mainPage.clickCourseThumbsByTitle(earliestCourse)
                .pageHeaderShouldBeSameAs(earliestCourse);
    }

    @Test
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
