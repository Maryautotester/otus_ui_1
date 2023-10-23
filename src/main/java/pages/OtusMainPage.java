package pages;

import static org.assertj.core.api.Assertions.assertThat;

import annotations.Path;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/")

public class OtusMainPage extends AbsBasePage<OtusMainPage> {

    public OtusMainPage(WebDriver driver) {
        super(driver);
    }

    private String courseListSelector = "//a[contains(@href,'https://otus.ru/lessons/') and not(@class)]//div//h5"; // :is(h5)

    private String courseDateStart = courseListSelector + "//..//..//div//span"; // //h5//..//..//span[1]//span[1]

    @Step("Получение списка курсов")
    public ArrayList getListCourses() {
        List<WebElement> courses = $$(courseListSelector);
        ArrayList listResult = new ArrayList<String>();
        for(WebElement course: courses) {
            String courseName = course.getText();
            listResult.add(courseName);
        }
        return listResult;
    }

    @Step("Страница выбора курсов загружена")
    public OtusMainPage pageListCoursesShouldBeVisible() {
        List<WebElement> courses = $$(courseListSelector);
        long actualCoursesList = courses.stream()
                .filter((WebElement coursesList) -> waiters.waitForElementVisible(coursesList))
                .count();
        assertThat(actualCoursesList).as("number of courses not 18").isEqualTo(courses.size());
        return this;
    }
    @Step("Кликнуть по плитке курса по названию")
    public CoursePage clickCourseThumbsByTitle(String title) {
        List<WebElement> tiles = $$(courseListSelector);
        List<WebElement> course = tiles.stream()
                .filter((WebElement element) -> element.getText().equals(title))
                .collect(Collectors.toList());
        assertThat(course)
                .as("Size of tiles list should be 1")
                .hasSize(1);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", course.get(0));
        course.get(0).click();

        return new CoursePage(driver);
    }
    @Step("Получение названия курса по дате")
    public String getCourseNameByStartDate(boolean isEarliest) {
        List<WebElement> coursesDates = $$(courseDateStart);

        List<WebElement> result = new ArrayList<>(Stream.concat(coursesDates.stream()
                        .filter((WebElement element) -> element.getText().startsWith("С ")), coursesDates.stream()
                        .filter((WebElement element) -> element.getText().startsWith("В ")))
                .collect(Collectors.toList()));

        ArrayList<LocalDate> dateList = new ArrayList<>();

        for (WebElement element : result) {
            ArrayList<String> date = new ArrayList<>(Arrays.asList(element.getText().substring(2).split(" ")));
            String dateCourse = date.get(0) + " " + date.get(1) + " 2023";

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
                LocalDate date1 = LocalDate.parse(dateCourse, formatter);
                dateList.add(date1);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            } catch (DateTimeParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
        String selectCourseDate;
        LocalDate selectDate;
        if(isEarliest) {
            selectDate = dateList.stream()
                    .reduce((a, b) -> a.isBefore(b) ? a : b)
                    .get();
            System.out.println("min start date is " + selectDate.format(DateTimeFormatter.ofPattern("d MMMM")));
        } else {
            selectDate = dateList.stream()
                    .reduce((a, b) -> a.isAfter(b) ? a : b)
                    .get();
            System.out.println("max start date is " + selectDate.format(DateTimeFormatter.ofPattern("d MMMM")));
        }
        selectCourseDate = selectDate.format(DateTimeFormatter.ofPattern("d MMMM"));

        List<WebElement> courses = driver.findElements(By.xpath(
                "//span[contains(text(), \"" + selectCourseDate + "\")]//..//..//h5"));
        return courses.get(0).getText();

    }
}
