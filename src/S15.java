import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S15 {

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

    public static void log(String mensaje) {
        System.out.println("[LOG] " + mensaje);
    }

    public static void main(String[] args) throws Exception {

        driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();

        driver.get("https://opensource-demo.orangehrmlive.com/");

        log("1.Página de login cargada");

        capturar("01_login");

        WebElement usuario = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("username"))
        );

        usuario.sendKeys("Admin");

        WebElement clave = driver.findElement(By.name("password"));
        clave.sendKeys("admin123");

        capturar("02_datos_ingresados");

        WebElement botonLogin = driver.findElement(By.xpath("//button[@type='submit']"));

        capturar("03_boton_login");

        botonLogin.click();

        log("2.Credenciales enviadas");

        WebElement tituloDashboard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h6[text()='Dashboard']")
                )
        );

        capturar("04_panel_principal");

        log("3.Autenticación exitosa");

        capturar("05_titulo_dashboard");

        WebElement menuLateral = driver.findElement(By.className("oxd-sidepanel"));

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].style.border='3px solid red'", menuLateral);

        capturar("06_menu_lateral");

        log("4.Panel cargado correctamente");

        driver.quit();

        log("5.Navegador cerrado");
    }
}