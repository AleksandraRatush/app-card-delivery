package ru.netology.selenide;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @Test
    public void cardDeliverySuccess()  {
        open("http://0.0.0.0:9999");
        $("[data-test-id='city'] .input__control")
                .setValue("Москва");
        $("div.input__popup .input__menu .menu-item__control")
                .shouldHave(Condition.exactText("Москва"))
                .click();
        String date  = LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] span input").click();
        $("[data-test-id='date'] span input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] span input").setValue(date);
        $("[data-test-id='date'] span").click();

        $("[data-test-id='name'] .input__control")
                .setValue("Иванова Александра");
        $("[data-test-id='phone'] .input__control")
                .setValue("+79272212612");


        $("[data-test-id='agreement'] span")
                .click();
        $("button.button")
                .click();
        $("[data-test-id='notification'] .notification__title" ).shouldBe(Condition.visible, Duration.ofSeconds(50))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='notification'] .notification__content" ).shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }



    @Test
    public void cardDeliveryComplexSuccess() {
        open("http://0.0.0.0:9999");
        $("[data-test-id='city'] .input__control")
                .setValue("Мо");
        $$("span.menu-item__control")
                .findBy(text("Москва")).click();
        $("[data-test-id='date'] span").click();
        long dateInMills  = Long.valueOf($("td.calendar__day_state_current").getAttribute("data-day"));
        LocalDate date = LocalDate.ofEpochDay(dateInMills / 86400000L);
        int currentMonth = date.getMonthValue();
        long selectedDateInMills =  date.plusDays(7).atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        String selectedDate = date.plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int nextMonth = date.plusDays(7).getMonthValue();
        if (nextMonth > currentMonth) {
           $("div.calendar__arrow_direction_right").click();
        }
        $("td[data-day='" + selectedDateInMills + "']").click();
        $("[data-test-id='name'] .input__control")
                .setValue("Иванова Александра");
        $("[data-test-id='phone'] .input__control")
                .setValue("+79272212612");
        $("[data-test-id='agreement'] span")
                .click();
        $("button.button")
                .click();
        $("[data-test-id='notification'] .notification__title" ).shouldBe(Condition.visible, Duration.ofSeconds(50))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='notification'] .notification__content" ).shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + selectedDate));

    }


}
