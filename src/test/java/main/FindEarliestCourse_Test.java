package main;

import annotations.Driver;
import exceptions.PathEmptyException;
import extensions.UIExtensions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import pages.OtusMainPage;

@ExtendWith(UIExtensions.class)
@Execution(ExecutionMode.CONCURRENT)
public class FindEarliestCourse_Test {
    @Driver
    public WebDriver driver;


    @Test
    @DisplayName("Выбор самого раннего курса")
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

}
