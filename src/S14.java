import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S14 {

    static WebDriver driver;

    public static void capturar(String nombre) throws Exception {
        File carpeta = new File("evidencias");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File origen = screenshot.getScreenshotAs(OutputType.FILE);
        File destino = new File("evidencias/" + nombre + ".png");
        FileHandler.copy(origen, destino);
    }

    public static void capturarElemento(WebElement elemento, String nombre) throws Exception {
        File carpeta = new File("evidencias");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        File origen = elemento.getScreenshotAs(OutputType.FILE);
        File destino = new File("evidencias/" + nombre + ".png");
        FileHandler.copy(origen, destino);
    }

    public static void log(String mensaje) {
        System.out.println(mensaje);
    }

    public static void main(String[] args) throws Exception {
        driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        Thread.sleep(2000);

        capturar("14_pantalla_completa_login");

        WebElement logo = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@alt='company-branding']")));
        WebElement campoUsuario = driver.findElement(By.name("username"));
        WebElement campoContrasena = driver.findElement(By.name("password"));
        WebElement botonLogin = driver.findElement(By.xpath("//button[@type='submit']"));
        WebElement enlaceRecuperacion = driver.findElement(By.className("orangehrm-login-forgot-header"));

        capturarElemento(logo, "14_logo");
        capturarElemento(campoUsuario, "14_campo_usuario");
        capturarElemento(campoContrasena, "14_campo_contraseña");
        capturarElemento(botonLogin, "14_boton_login");
        capturarElemento(enlaceRecuperacion, "14_enlace_recuperacion");

        if (logo.isDisplayed()) {
            log("Logo encontrado.");
        }
        if (campoUsuario.isDisplayed()) {
            log("Campo usuario encontrado.");
        }
        if (campoContrasena.isDisplayed()) {
            log("Campo contraseña encontrado.");
        }
        if (botonLogin.isDisplayed()) {
            log("Botón login encontrado.");
        }
        if (enlaceRecuperacion.isDisplayed()) {
            log("Enlace de recuperación encontrado.");
        }

        campoUsuario.sendKeys("Admin");
        campoContrasena.sendKeys("admin123");
        botonLogin.click();

        wait.until(ExpectedConditions.urlContains("dashboard"));
        Thread.sleep(2000);

        driver.quit();
    }
}