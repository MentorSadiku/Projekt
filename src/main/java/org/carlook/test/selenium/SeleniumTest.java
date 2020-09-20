package org.carlook.test.selenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
public class SeleniumTest {
    

        private WebDriver driver = null;

        @Before
        public void setUpClass(){
            System.setProperty("webdriver.gecko.driver","C:\\Users\\mento\\Downloads\\geckodriver.exe");
            File pathBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
            FirefoxBinary firefoxBinary = new FirefoxBinary(pathBinary);
            DesiredCapabilities desired = DesiredCapabilities.firefox();
            FirefoxOptions options = new FirefoxOptions();
            desired.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options.setBinary(firefoxBinary));
            driver = new FirefoxDriver(options);
        }

        @Test
        public void startWebDriver() throws InterruptedException {

            //Öffne Seite
            driver.get("http://localhost:8080/Projekt_war_exploded/#main");

            //Fullsize
            driver.manage().window().maximize();

            //Click on Button "Menü"
            driver.findElement(By.xpath("//*[@id=\"Projektwarexploded-4492806\"]/div/div[2]/div[1]/div/div/div[3]/div/div[3]/div/span")).click();

            //Click on Button "Login"
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/span[1]/span")).click();

            //Daten eingeben
            //driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys("Gustav");
            driver.findElement(By.xpath("//*[@id=\"gwt-uid-3\"]")).sendKeys("max@web.de");
            driver.findElement(By.xpath("//*[@id=\"gwt-uid-5\"]")).sendKeys("123");
            //driver.findElement(By.xpath("//*[@id=\"passwort2\"]")).sendKeys("123");

            //Click on Button "Login"
            driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[5]/div/div[2]/div/div[5]/div/div[1]/div")).click();

            //Registrieren button drücken
            //driver.findElement(By.xpath("//*[@id=\"Projektwarexploded-4492806\"]/div/div[2]/div[5]/div/div[2]/div/div[15]/div/div[1]/div")).click();

            //Ok Button clicken
            //driver.findElement(By.xpath("//*[@id=\"Projektwarexploded-4492806-overlays\"]/div[2]/div/div/div[3]/div/div/div[3]/div")).click();
            TimeUnit.SECONDS.sleep(3);
            assertEquals("http://localhost:8080/Projekt_war_exploded/#!main",driver.getCurrentUrl());
        }
        @After
        public void tearDownClass() {
            driver.quit();
        }


}
