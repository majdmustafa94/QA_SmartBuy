import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.time.Duration;

public class SmartBuy {
    public WebDriver driver;
    public int numbrtOftry = 100;
    SoftAssert softAssertp= new SoftAssert();
    public double final_price;

    @BeforeTest()
    public void This_is_before_test(){
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();

        driver.get("https://smartbuy-me.com/smartbuystore/ar");
        driver.findElement(By.xpath("/html/body/main/header/div[2]/div/div[2]/a")).click();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void Test_adding_item_SAMSUNG_50_inch(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.findElement(By.xpath("//*[@id=\"newtab-Featured\"]/div/div[1]/div/div/div/div[1]/div/div[1]/a")).click();

        for (int i=0; i<numbrtOftry; i++){
            driver.findElement(By.xpath("//*[@id=\"addToCartButton\"]")).click();

            String msg= driver.findElement(By.xpath("//*[@id=\"cboxLoadedContent\"]")).getText();

            if (msg.contains("Sorry")) {
                numbrtOftry = i;
                driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[1]")).click();
            }
            else {
                driver.findElement(By.xpath("//*[@id=\"addToCartLayer\"]/a[2]")).click();
            }
        }
    }

    @Test(priority = 2)
    public void we_need_to_check_the_correct_price(){
        String the_single_item_price=driver.findElement(By.xpath("/html/body/main/div[3]/div[1]/div[2]/div[1]/div/ul/table/tbody/tr/td/li[1]/div[4]")).getText();
        String[] the_price= the_single_item_price.split("JOD");
        String update_price= the_price[0].trim();
        this.final_price= Double.parseDouble(update_price);
        System.out.println(final_price);
    }

    @Test(priority = 3)
    public void check_total_price() {
        String total_price= driver.findElement(By.xpath("/html/body/main/div[3]/div[1]/div[2]/div[1]/div/ul/table/tbody/tr/td/li[1]/div[6]")).getText();
        String[] update_total_price=total_price.split("JOD");
        String update2_total_price= update_total_price[0].trim();
        String replace_price= update2_total_price.replace(",","");
        Double final_total_price=Double.parseDouble(replace_price);
        System.out.println(final_total_price);

        softAssertp.assertEquals(this.final_price*numbrtOftry,final_total_price,"check price");

        softAssertp.assertAll();
    }
}
