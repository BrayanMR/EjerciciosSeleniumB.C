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

        private static boolean logInicializado = false;

    public static void log(String mensaje) {
        System.out.println("[LOG] " + mensaje);
        try {
            java.io.File folder = new java.io.File("logs");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            try (java.io.FileWriter fw = new java.io.FileWriter("logs/ejercicios.logs", true)) {
                if (!logInicializado) {
                    logInicializado = true;
                    String className = Thread.currentThread().getStackTrace()[2].getClassName();
                    if (className.contains(".")) {
                        className = className.substring(className.lastIndexOf('.') + 1);
                    }
                    fw.write("\n ===========================\n");
                    fw.write(java.time.LocalDateTime.now() + " Inicio de prueba - " + className + "\n");
                    
                    // Hook para cerrar e imprimir el pie de página de log automáticamente al finalizar el programa
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try (java.io.FileWriter fwExit = new java.io.FileWriter("logs/ejercicios.logs", true)) {
                            fwExit.write(java.time.LocalDateTime.now() + "\n");
                            fwExit.write("============================================== \n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }));
                }
                fw.write(mensaje + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        usuario.sendKeys("Greinly");

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