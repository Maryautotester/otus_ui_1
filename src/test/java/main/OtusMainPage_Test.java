package main;

import annotations.Driver;
import exceptions.PathEmptyException;
import extensions.UIExtensions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import pages.OtusMainPage;

import java.text.ParseException;

@ExtendWith(UIExtensions.class)
public class OtusMainPage_Test {
    @Driver
    public WebDriver driver;

    @Test
    public void testCoursesList() throws PathEmptyException, ParseException {
        OtusMainPage mainPage = new OtusMainPage(driver)
                .open()
                .mainPageLoaded()
                .pageListCoursesShouldBeVisible();
        System.out.println(mainPage.getListCourses());
        //        Assertions.assertThatList(mainPage.getListCourses())
        //                .as("List of courses is the same")
        //                .isEqualTo(mainPage.coursesListTemplate);

        System.out.println("Раньше всех стартует курс " + mainPage.getEarlyStartedCourseName());

        String title = mainPage.getCourseThumbsTitle(3);
        System.out.println(title);
        mainPage.clickCourseThumbsByTitle(title)
                .pageHeaderShouldBeSameAs(title)
                .returnMainPage();

        mainPage.mainPageLoaded();

    }
}
