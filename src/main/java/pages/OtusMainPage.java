package pages;

import static org.assertj.core.api.Assertions.assertThat;

import annotations.Path;
import org.openqa.selenium.By;
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

    private String courseTileSelector =
            "//main//section[.//h2[contains(text(), \"Авторские онлайн‑курсы для профессионалов\")]]";
    private String courseListSelector = "//a[contains(@href,'https://otus.ru/lessons/') and not(@class)]//div//h5"; // :is(h5)

    private String courseDateStart = courseListSelector + "//..//..//div//span"; // //h5//..//..//span[1]//span[1]
    public List<String> coursesListTemplate = Arrays.asList(
            "Системный аналитик и бизнес-аналитик",
            "Системный аналитик. Team Lead",
            "Углубленное изучение языка Java",
            "Специализация Machine Learning",
            "Специализация Системный аналитик",
            "Специализация Python",
            "Специализация iOS Developer",
            "Специализация Java-разработчик",
            "Специализация QA Automation Engineer",
            "Специализация Fullstack Developer",
            "Специализация С++",
            "Специализация Android",
            "Специализация Administrator Linux",
            "Специализация С#",
            "Специализация сетевой инженер",
            "C# Developer. Professional",
            "Руководитель поддержки пользователей в IT",
            "Symfony Framework");

    public ArrayList getListCourses() {
        List<WebElement> courses = $$(courseListSelector);
        System.out.println(courses);
        for (WebElement el:courses) {
            System.out.println(el.getCssValue(el.getText()));
        }
        ArrayList listResult = new ArrayList<String>();
        for(WebElement course: courses) {
            String courseName = course.getText();
            listResult.add(courseName);
        }
        return listResult;
    }
    public OtusMainPage pageListCoursesShouldBeVisible() {
        List<WebElement> courses = $$(courseListSelector);
        System.out.println("всего курсов "+courses.size());
        long actualCoursesList = courses.stream()
                .filter((WebElement coursesList) -> waiters.waitForElementVisible(coursesList)).count();
        assertThat(actualCoursesList).as("number of courses not 18").isEqualTo(courses.size());
        return this;
    }

    public String getCourseThumbsTitle(int index) {
        List<WebElement> courses = $$(courseListSelector);
        return courses.get(--index).getText();
    }
    public CoursePage clickCourseThumbsByTitle(String title) {
        List<WebElement> tiles = $$(courseListSelector);
        List<WebElement> course = tiles.stream()
                .filter((WebElement element) -> element.getText().equals(title))
                .collect(Collectors.toList());
        assertThat(course)
                .as("Size of tiles list should be 1")
                .hasSize(1);
        course.get(0).click();

        return new CoursePage(driver);
    }
    public String getEarlyStartedCourseName() {
        List<WebElement> coursesDates = $$(courseDateStart);

        List<WebElement> result = new ArrayList<>(Stream.concat(coursesDates.stream()
                        .filter((WebElement element) -> element.getText().startsWith("С ")), coursesDates.stream()
                        .filter((WebElement element) -> element.getText().startsWith("В ")))
                .collect(Collectors.toList()));

        ArrayList<LocalDate> dateList = new ArrayList<>();

        for (WebElement element : result) {
            ArrayList<String> date = new ArrayList<String>(Arrays.asList(element.getText().substring(2).split(" ")));
            String dateCourse = date.get(0) + " " + date.get(1) + " 2023";

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
                LocalDate date1 = LocalDate.parse(dateCourse, formatter);
                System.out.println(date1);
                dateList.add(date1);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            } catch (DateTimeParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
        LocalDate minDate = dateList.stream()
                .reduce((a, b) -> a.isBefore(b) ? a : b)
                .get();
        System.out.println("min start date is " + minDate.format(DateTimeFormatter.ofPattern("d MMMM")));
        String minCourseDate =  minDate.format(DateTimeFormatter.ofPattern("d MMMM"));

        List<WebElement> courses = driver.findElements(By.xpath(
                "//span[contains(text(), \"" + minCourseDate + "\")]//..//..//h5"));
        return courses.get(0).getText();

    }
}
