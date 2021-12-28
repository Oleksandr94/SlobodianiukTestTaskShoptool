import driverManager.DriverManager;
import driverManager.Item;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.RandomPage;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.openqa.selenium.By.xpath;
import static org.testng.Assert.assertTrue;

public class BaseTest extends DriverManager {
    public static WebDriverWait wait;
    private static Actions actions;
    private Item item;

    @Test(priority = 1)
    @Parameters("numberOfItems") //
    public void checkNewAndOldPrices(@Optional("4") int numberOfItems) {
        driver.findElement(xpath("(//span[@class = 'site-menu__item'])[1]")).click();
        int maxPage = 3;
        Random randoms = new Random(); // get random number
        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> productPages = driver.findElements(xpath("//*[@class = 'pager__item j-catalog-pagination-btn']"));
            int randomPages = randoms.nextInt(maxPage) + 1;
            if (randomPages != 1) {
                productPages.get(randomPages - 2).click();
            }
            List<WebElement> productElems = driver.findElements(xpath("//div[@class=\"catalogCard-title\"]"));
            int maxProducts = productElems.size();// get the len of productElems
            int randomProduct = randoms.nextInt(maxProducts);// get random number
            productElems.get(randomProduct).click(); // Select the list item
            WebElement oldPriceElement = driver.findElement(xpath("//div[@class = 'product-price__old-price']"));
            WebElement newPriceElement = driver.findElement(xpath("//div[@class = 'product-price__item product-price__item--new']"));
            String oldPrice = oldPriceElement.getText();
            String newPrice = newPriceElement.getText();
            if (oldPrice != null && newPrice != null) ;
            {
                System.out.println("Old and new price displayed");
            }
            if (i < (numberOfItems - 1)) {
                if (randomPages != 1) {
                    driver.navigate().back();
                }
                driver.navigate().back();
            }
        }
    }

    @Test(priority = 2)
    public void checkThatThePriceHasChanged() {
        int numberOfItems = 3;
        Random randoms = new Random();
        driver.findElement(xpath("//a[@class = 'products-menu__button j-productsMenu-toggleButton']")).click();
        driver.findElement(xpath("(//img[@class = 'catalogCard-img'])[4]")).click();
        driver.findElement(xpath("(//img[@class = 'catalogCard-img'])[3]")).click();
        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> productPages = driver.findElements(xpath("//a[@class = 'pager__item j-catalog-pagination-btn']"));
            int randomPages = i + 1; //randoms.nextInt(maxPage) + 1;
            if (randomPages != 1) {  // Select the list item
                productPages.get(randomPages - 2).click();
            }
            Actions actions = new Actions(driver);
            List<WebElement> items = driver.findElements(xpath("//div[@class = 'catalogCard-image-i']"));
            int randomItemIndex = randoms.nextInt(items.size());
            actions.moveToElement(items.get(randomItemIndex)).perform();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.findElement(xpath("//a[@class = 'btn __special j-buy-button-add']/span")).click();
            actions.moveToElement(items.get(randomItemIndex)).perform();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
            driver.findElement(xpath("//div[@class ='popup-block']/a[@class ='popup-close']")).click();
            if (i < (numberOfItems - 2)) {
                if (randomPages != 1) {
                    driver.navigate().back();
                } else {
                    driver.navigate().refresh();
                    List<WebElement> cartItems = driver.findElements(By.xpath("//table[@class=\"cart-items\"]/tbody/tr"));
                    Assert.assertEquals(cartItems.size(), 3);
                    String total = driver.findElement(xpath("//div[@class='cart-footer-b cart-cost j-total-sum']")).getText();
                    System.out.println(total);
                    List<WebElement> delete = driver.findElements(xpath("//i[@class='icon-cart-remove']"));
                    RandomPage.randomElement(delete).click();
                    String newTotal = driver.findElement(xpath("//div[@class='cart-footer-b cart-cost j-total-sum']")).getText();
                    Assert.assertNotEquals(total, newTotal);
                }
            }
        }
    }


    @Test(priority = 3)
    @Parameters("ticket")
    public void checkThatNameProductContainsWord(@Optional("Витязь") String ticket) {
        List<WebElement> webElements = null;
        driver.findElement(xpath("//a[@class = 'products-menu__button j-productsMenu-toggleButton']")).click();
        driver.findElement(xpath("(//img[@class = 'catalogCard-img'])[4]")).click();
        driver.findElement(xpath("(//img[@class = 'catalogCard-img'])[5]")).click();
        webElements = driver.findElements(By.xpath(String.format("//div[@class ='catalogCard-title']/a", ticket)));
        if (webElements.size() > 0) {
            for (WebElement el : webElements) {
                String itemName = el.getText();
                Assert.assertTrue(itemName.contains(ticket));
            }
        }
    }


    @Test
    @Parameters("numberOfElements")
    public void calculationOfNewPriceRelativeToTheOldOne(@Optional("3") int numberOfElements) {
        List<Item> items = new LinkedList<>();
        String discountXpath = "//div[@class = 'productSticker-item __discount']//div[@class = 'productSticker-content']";
        driver.findElement(xpath("(//a[@class = 'site-menu__link'])[1]")).click();

        List<Item> randomItems = RandomPage.randomElementsNoRepeat(items, numberOfElements);
        for (Item item : randomItems) {
            double expectedPrice = item.getOldPrice() * (100 - item.getProductSticker()) / 100;
            double actualPrice = item.getNewPrice();
            String name = item.getName();
            Assert.assertEquals(expectedPrice, actualPrice, name);
        }

    }

}

