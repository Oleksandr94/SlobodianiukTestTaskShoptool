package pages;

import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class RandomPage {

    private static Random random;

    public static <T> List<T> randomElementsNoRepeat(List<T> givenList, int numberOfElements) {
        List<T> newElements = new LinkedList<>();
        random = new Random();
        if (givenList.size() < numberOfElements) numberOfElements = givenList.size();
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = random.nextInt(givenList.size());
            newElements.add(givenList.get(randomIndex));
            givenList.remove(randomIndex);
        }
        return newElements;
    }

    public static WebElement randomElement(List<WebElement> givenList) {
        random = new Random();
        return givenList.get(random.nextInt(givenList.size()));
    }

}
