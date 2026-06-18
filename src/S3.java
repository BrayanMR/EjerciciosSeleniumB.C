import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S3{

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

        log("1. Navegador iniciado");

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        log("2. Página de login cargada");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.name("username")
                )
        );

        capturar("01_login_inicial");
        WebElement usuario = driver.findElement(By.name("username"));

        log("3. Campo usuario encontrado");

        capturar("02_campo_usuario");

        usuario.sendKeys("FRANCISCO");

        WebElement password = driver.findElement(By.name("password"));

        log("4. Campo contraseña encontrado");

        capturar("03_campo_password");

        password.sendKeys("45664366");

        capturar("04_formulario_diligenciado");

        log("5. Datos ingresados en el formulario");

        WebElement botonLogin = driver.findElement(
                By.xpath("//button[@type='submit']")
        );

        log("6. Botón login encontrado");
        botonLogin.click();

        log("7. Intento de login realizado");

        WebElement mensajeError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//p[contains(@class,'oxd-alert-content-text')]")
                )
        );

        log("8. Error mostrado: " + mensajeError.getText());
        capturar("06_mensaje_error");

        driver.quit();

        log("9. Navegador cerrado");
    }
}