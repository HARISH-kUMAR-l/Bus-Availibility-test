import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.*;
import java.time.Duration;
import java.util.NoSuchElementException;

import static java.time.Month.valueOf;

public class BusCountAutomation {
    public static void main(String[] args) throws IOException, InterruptedException {
        String inputFilePath = "C:\\Users\\rh725\\Downloads\\input.csv";
        String outputFilePath = "C:\\Users\\rh725\\Downloads\\output.csv";

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\rh725\\Desktop\\Resource\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 45);

        driver.get("https://www.redbus.in");

        List<String[]> inputRecords = readCSV(inputFilePath);
        List<String[]> limitedRecords = inputRecords.subList(0, 1);

        List<String[]> outputRecords = new ArrayList<>();
        outputRecords.add(new String[]{"From", "To", "Date", "4-Star Count", "AC Count", "Non-AC Count", "Highest Price", "Lowest Price"});

        for (String[] record : limitedRecords) {
            try {
                String fromCity = record[0];
                String toCity = record[1];
                String dateToClick = record[2];

                WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("src")));
                fromInput.clear();
                fromInput.sendKeys(fromCity);
                Thread.sleep(1000);
                fromInput.sendKeys(Keys.ENTER);

                WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("dest")));
                toInput.clear();
                toInput.sendKeys(toCity);
                Thread.sleep(1000);
                toInput.sendKeys(Keys.ENTER);
                Thread.sleep(5000);

                driver.findElement(By.xpath("//span[@class='dateText']")).click();

                String[] dateParts = dateToClick.split("-");
                String day = dateParts[0];
                String month = dateParts[1];
                String year = dateParts[2];

                WebElement calendarElement = driver.findElement(By.cssSelector(".hrJoeL"));
                WebDriverWait wait2 = new WebDriverWait(driver, 5);
                WebElement dayDiv = wait2.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath(".//div[span[text()='" + day + "']]"))
                );
                dayDiv.click();

                Thread.sleep(5000);
                driver.findElement(By.id("search_button")).click();

                WebElement acCheck = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//label[@for='bt_AC'])[1]")));
                acCheck.click();
                WebElement noAcCheck = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//label[@for='bt_NONAC'])[1]")));
                noAcCheck.click();

                List<WebElement> ratingElements = driver.findElements(By.cssSelector(".rating-sec .rating"));
                int fourStarCount = 0;

                for (WebElement el : ratingElements) {
                    String ratingText = el.getText().trim();
                    try {
                        double rating = Double.parseDouble(ratingText);
                        if (rating >= 4.0) {
                            fourStarCount++;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }

                int acCount = driver.findElements(By.xpath("//*[contains(text(),'AC') and contains(@class,'bus-type')]"))
                        .size();
                int nonAcCount = driver.findElements(By.xpath("//*[contains(@class,'bus-type') and not(contains(text(),'AC'))]"))
                        .size();

                List<WebElement> prices = driver.findElements(By.cssSelector(".fare span"));
                List<Integer> priceList = new ArrayList<>();
                for (WebElement price : prices) {
                    try {
                        priceList.add(Integer.parseInt(price.getText().replace("\u20B9", "").trim()));
                    } catch (Exception ignored) {
                    }
                }
                int highestPrice = priceList.stream().max(Integer::compareTo).orElse(0);
                int lowestPrice = priceList.stream().min(Integer::compareTo).orElse(0);

                outputRecords.add(new String[]{
                        fromCity, toCity, dateToClick,
                        String.valueOf(fourStarCount),
                        String.valueOf(acCount),
                        String.valueOf(nonAcCount),
                        String.valueOf(highestPrice),
                        String.valueOf(lowestPrice)
                });
            } catch (Exception e) {
                System.out.println("Error processing route: " + Arrays.toString(record));
                e.printStackTrace();
            }
        }

        writeCSV(outputFilePath, outputRecords);
        driver.quit();
        System.out.println("Results written to " + outputFilePath);
    }

    private static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        }
        return data;
    }

    private static void writeCSV(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }
}

