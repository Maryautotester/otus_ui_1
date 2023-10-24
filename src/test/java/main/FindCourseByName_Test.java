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
public class FindCourseByName_Test {
    @Driver
    public WebDriver driver;


    @Test
    @DisplayName("Выбор курса по имени - Специализация Python")
    public void filterCourseTest() throws PathEmptyException {
        OtusMainPage mainPage = new OtusMainPage(driver);
        mainPage.open()
                .mainPageLoaded()
                .pageListCoursesShouldBeVisible()
                .clickCourseThumbsByTitle("Специализация Python");
    }
}
