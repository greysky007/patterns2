package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//input[@name = 'login']").setValue(registeredUser.getLogin());
        $x("//input[@name = 'password']").setValue(registeredUser.getPassword());
       $x("//span[@class = 'button__text']").click();
        $("[id=\"root\"]").shouldHave(Condition.text("  Личный кабинет")).click();
        // TODO: добавить логику теста, в рамках которого будет выполнена попытка входа в личный кабинет с учётными
        //  данными зарегистрированного активного пользователя, для заполнения полей формы используйте
        //  пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        Configuration.holdBrowserOpen = true;
        var notRegisteredUser = getUser("active");
        $x("//input[@name = 'login']").setValue(notRegisteredUser.getLogin());
        $x("//input[@name = 'password']").setValue(notRegisteredUser.getPassword());
        $x("//span[@class = 'button__text']").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет
        //  незарегистрированного пользователя, для заполнения полей формы используйте пользователя notRegisteredUser
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//input[@name = 'login']").setValue(blockedUser.getLogin());
        $x("//input[@name = 'password']").setValue(blockedUser.getPassword());
        $x("//span[@class = 'button__text']").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));

        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет,
        //  заблокированного пользователя, для заполнения полей формы используйте пользователя blockedUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//input[@name = 'login']").setValue(wrongLogin);
        $x("//input[@name = 'password']").setValue(registeredUser.getPassword());
        $x("//span[@class = 'button__text']").click();
        $("[data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  логином, для заполнения поля формы "Логин" используйте переменную wrongLogin,
        //  "Пароль" - пользователя registeredUser
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        Configuration.holdBrowserOpen = true;
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//input[@name = 'login']").setValue(registeredUser.getLogin());
        $x("//input[@name = 'password']").setValue(wrongPassword);
        $x("//span[@class = 'button__text']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(15)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
        // TODO: добавить логику теста в рамках которого будет выполнена попытка входа в личный кабинет с неверным
        //  паролем, для заполнения поля формы "Логин" используйте пользователя registeredUser,
        //  "Пароль" - переменную wrongPassword
    }
}
