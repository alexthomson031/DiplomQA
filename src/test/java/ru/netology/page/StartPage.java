package ru.netology.page;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$$;

public class StartPage {
    public BuyPage buyPage() {
        $$("button button_size_m button_theme_alfa-on-white").find(exactText("Купить")).click();
        return new BuyPage();
    }

    public BuyCreditPage buyCreditPage() {
        $$("[button button_view_extra button_size_m button_theme_alfa-on-white]").find(exactText("Купить в кредит")).click();
        return new BuyCreditPage();
    }
}
