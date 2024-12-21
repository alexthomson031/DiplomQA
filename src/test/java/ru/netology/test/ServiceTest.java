package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.DbUtils;
import ru.netology.page.StartPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class ServiceTest {
    private StartPage startPage;

    @BeforeEach
    void setUp() {
        DbUtils.clearTables();
        startPage = open("http://localhost:8080", StartPage.class);
    }

    //passed
    @Test
    void buyInPaymentGate() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    //passed
    @Test
    void buyInPaymentGateWithNameInLatinLetters() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidNameInLatinLetters(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    //failed DANGER банк одобрил!!!!
    @Test
    void notBuyInPaymentGateWithDeclinedCardNumber() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getDeclinedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkDeclineNotification();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }

    //сервис принимает для оплаты карту "9999 9999 9999 9999" спустя 15 сек ожид
    @Test
    void notBuyInPaymentGateWithInvalidCardNumber() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getInvalidCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkDeclineNotification();
    }

    //passed
    @Test
    void notBuyInPaymentGateWithShortCardNumber() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getShortCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidFormat();
    }

    //пустое поле "карта"
    @Test
    void notBuyInPaymentGateWithEmptyCardNumber() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(null, getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkRequiredField();
    }

    ////неверно указал месяц(00) + год(next) сервис принимает
    @Test
    void notBuyInPaymentGateWithInvalidMonth() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), "00", getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidDate();
    }

    ////неверно указал месяц(13) + год(прошлый)
    @Test
    void notBuyInPaymentGateWithNonExistingMonth() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), "13", getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidDate();
    }

    //неверно указал месяц(прошлый) + год(прошлый)
    @Test
    void notBuyInPaymentGateWithExpiredMonth() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getLastMonth(), getCurrentYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidDate();
    }

    // отправил пустое поле "месяц"
    @Test
    void notBuyInPaymentGateWithEmptyMonth() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), null, getNextYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkRequiredField();
    }

    //passed
    @Test
    void notBuyInPaymentGateWithExpiredYear() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getLastYear(), getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkExpiredDate();
    }

    //отправил пустое поле "год"
    @Test
    void notBuyInPaymentGateWithEmptyYear() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), null, getValidName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkRequiredField();
    }

    //Element not found {.input__sub} владелец может содержать 1 имя Русск
    @Test
    void notBuyInPaymentGateWithOnlyName() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidName();
    }

    //Element not found {.input__sub} владелец может содержать 1 имя Латин
    @Test
    void notBuyInPaymentGateWithOnlyNameInLatinLetters() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyNameInLatinLetters(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidName();
    }

    //Element not found {.input__sub} владелец может содержать 1 фамилию Русск
    @Test
    void notBuyInPaymentGateWithOnlySurname() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlySurname(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidName();
    }

    //Element not found {.input__sub} владелец может содержать 1 фамилию Латин
    @Test
    void notBuyInPaymentGateWithOnlySurnameInLatinLetters() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlySurnameInLatinLetters(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidName();
    }

    //Element not found {.input__sub} владелец 3000 символов
    @Test
    void notBuyInPaymentGateWithTooLongName() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getTooLongName(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkLongName();
    }

    //failed Element not found {.input__sub} владелец цифрами
    @Test
    void notBuyInPaymentGateWithDigitsInName() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithNumbers(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidDataName();
    }


    //Element not found {.input__sub} "владелец" с одним символом и выдает "успешно"
    @Test
    void notBuyInPaymentGateWithTooShortName() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithOneLetter(), getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkShortName();
    }

    //passed
    @Test
    void notBuyInPaymentGateWithEmptyName() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), null, getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkRequiredField();
    }

    //пропущен владелец
    @Test
    void notBuyInPaymentGateWithSpaceInsteadOfName() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), " ", getValidCvc());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidDataName();
    }

    //1 ссв
    @Test
    void notBuyInPaymentGateWithOneDigitInCvc() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithOneDigit());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidCvc();
    }

    //2 ссв
    @Test
    void notBuyInPaymentGateWithTwoDigitsInCvc() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithTwoDigits());
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkInvalidCvc();
    }

    //passed
    @Test
    void notBuyInPaymentGateWithEmptyCvc() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), null);
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
    }

    //failed
    @Test
    void notBuyInPaymentGateWithAllEmptyFields() {
        DataHelper.PurchaseInfo purchaseInfo = new DataHelper.PurchaseInfo(null, null, null, null, null);
        val buyPage = startPage.buyPage();
        buyPage.notificationPage(purchaseInfo);
        buyPage.checkAllFieldsAreRequired();
    }
}