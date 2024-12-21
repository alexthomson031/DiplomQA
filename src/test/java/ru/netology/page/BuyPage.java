package ru.netology.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BuyPage {
    private SelenideElement numberCardField = $("input.input__control");
    private SelenideElement monthField = $$("input.input__control").get(1);
    private SelenideElement yearField = $$("input.input__control").get(2);
    private SelenideElement ownerField = $$("input.input__control").get(3);
    private SelenideElement codeField = $$("input.input__control").get(4);
    private SelenideElement successNotification = $("notification.notification_status_ok.notification__title");
    private SelenideElement buttonError = $("button.icon.icon_theme_alfa-on-color");
    private SelenideElement error = $("notification.notification_status_error");
    private SelenideElement button = $$("button").findBy(Condition.text("Продолжить"));

    public void notificationPage(DataHelper.PurchaseInfo purchaseInfo) {
        numberCardField.setValue(purchaseInfo.getCardNumber());
        monthField.setValue(purchaseInfo.getMonth());
        yearField.setValue(purchaseInfo.getYear());
        ownerField.setValue(purchaseInfo.getOwner());
        codeField.setValue(purchaseInfo.getCode());
        button.click();
    }

    public void checkSuccessNotification() {
        $(".notification_status_ok").shouldBe(Condition.visible, Duration.ofMillis(15000));
    }

    public void checkAllFieldsAreRequired() {
        $$(".input__sub").shouldHave(CollectionCondition.size(5)).shouldHave(CollectionCondition.texts("Поле обязательно для заполнения"));
    }

    public void checkInvalidCvc() {
        $(".input__sub").shouldHave(exactText("Значение поля должно содержать 3 цифры")).shouldBe(visible);
    }

    public void checkInvalidDataName() {
        $(".input__sub").shouldHave(exactText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    public void checkRequiredField() {
        $(".input__sub").shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    public void checkShortName() {
        $(".input__sub").shouldHave(exactText("Значение поля должно содержать больше одной буквы")).shouldBe(visible);
    }

    public void checkLongName() {
        $(".input__sub").shouldHave(exactText("Значение поля не может содержать более 100 символов")).shouldBe(visible);
    }

    public void checkInvalidName() {
        $(".input__sub").shouldHave(exactText("Введите полное имя и фамилию")).shouldBe(visible);
    }

    public void checkExpiredDate() {
        $(".input__sub").shouldHave(exactText("Истёк срок действия карты")).shouldBe(visible);
    }

    public void checkInvalidDate() {
        $(".input__sub").shouldHave(exactText("Неверно указан срок действия карты")).shouldBe(visible);
    }

    public void checkInvalidFormat() {
        $(".input__sub").shouldHave(exactText("Неверный формат")).shouldBe(visible);
    }

    public void checkDeclineNotification() {
        $(".notification__content").shouldHave(exactText("Ошибка! Банк отказал в проведении операции.")).shouldBe(visible, Duration.ofSeconds(15));
    }

    public void checkDeclineNotificationSql() {
        $("notification_status_error").shouldBe(Condition.visible, Duration.ofMillis(15000));
    }
}