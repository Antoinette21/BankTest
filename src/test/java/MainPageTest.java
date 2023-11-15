import org.apache.commons.lang3.Range;
import org.openqa.selenium.By;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class MainPageTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeTest
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "D:\\chrome\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.bspb.ru/");
        wait = new WebDriverWait(driver, 15);
    }
//тест на сравнение заголовка сайта
    @Test
    public void testTitle() {
        String responseTitleSite = driver.getTitle();
        String titleSite = "Банк Санкт-Петербург – Официальный сайт | Банк для частных клиентов и для бизнеса | Банк для частных клиентов и для бизнеса";
        Assert.assertEquals(responseTitleSite, titleSite);
    }

    //тест - переход на страницу "Связь с банком"
    @Test
    public void testCommunicationWithTheBank() {
        WebElement communicationButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"1\"]/div/div/div[3]/a")));

        Assert.assertTrue("Кнопка 'Связь с банком' не найдена", communicationButton.isDisplayed());

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", communicationButton);

        wait.until(ExpectedConditions.urlContains("https://www.bspb.ru/feedback"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("После нажатия на кнопку 'Связь с банком', ожидался переход на другую страницу",
                currentUrl.contains("https://www.bspb.ru/feedback"));
    }

//тест - Переход на страницу авторизации пользователя
    @Test
    public void testClickToSignIn() {

        WebElement signInButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"app-wrapper\"]/div[2]/div[1]/div/div/a")));

        Assert.assertTrue("Кнопка 'Войти' не найдена", signInButton.isDisplayed());

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", signInButton);
        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());

        driver.switchTo().window(browserTabs.get(1));
        wait.until(ExpectedConditions.urlContains("https://i.bspb.ru/auth"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("После нажатия на кнопку 'Войти', ожидался переход на другую страницу",
                currentUrl.contains("https://i.bspb.ru/auth"));
    }

    //тест - переход на страницу финансовые рынки
    @Test
    public void testNavigationToFinance() {

        WebElement financialMarketsLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"app-wrapper\"]/div[2]/div[1]/div/div/div/nav/a[5]")));

        Assert.assertNotNull("Ссылка на 'Финансовые рынки' не найдена", financialMarketsLink);

        financialMarketsLink.click();

        wait.until(ExpectedConditions.urlContains("https://www.bspb.ru/finance"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("После перехода на страницу 'Финансовые рынки', ожидался другой URL",
                currentUrl.contains("https://www.bspb.ru/finance"));
    }

    //тест - Переход на соц.страницу банка в ВК
    @Test
    public void testClickToVK() {

        WebElement clickToVK = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/div[4]/div/div/div[4]/div[2]/div/a[2]")));

        Assert.assertNotNull("Ссылка на 'VK' не найдена", clickToVK);

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", clickToVK);

        List<String> browserTabs = new ArrayList<String>(driver.getWindowHandles());

        driver.switchTo().window(browserTabs.get(1));
        wait.until(ExpectedConditions.urlContains("https://vk.com/bspb"));

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue("После перехода на сайт 'VK', ожидался другой URL",
                currentUrl.contains("https://vk.com/bspb"));
    }



    //тест - Проверка информации о курсах валюты при продаже
    @Test
    public void testCurrencyExchangeInformation() {
        WebElement eurElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/main/div/div[5]/div/div/div[1]/table/tbody/tr[1]/td[2]")));
        WebElement usdElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/main/div/div[5]/div/div/div[1]/table/tbody/tr[2]/td[2]")));

        String usdCurrent = usdElement.getText();
        double valueUsdCurrent = Double.parseDouble(usdCurrent);
        String eurCurrent = eurElement.getText();
        double valueEurCurrent = Double.parseDouble(eurCurrent);

        Assert.assertFalse("Информация о курсе продажи USD отсутствует", usdCurrent.isEmpty());
        Assert.assertFalse("Информация о курсе продажи EUR отсутствует", eurCurrent.isEmpty());

        Range<Double> rangeUsd = Range.between(85.0, 95.0);
        Range<Double> rangeEur = Range.between(95.0, 105.0);

        Assert.assertTrue("Текущие значение USD входит в диапазон",rangeUsd.contains(valueUsdCurrent) );
        Assert.assertTrue("Текущие значение EUR входит в диапазон", rangeEur.contains(valueEurCurrent));

    }


    @AfterTest
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

}
