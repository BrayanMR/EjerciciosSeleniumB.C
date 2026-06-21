import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S1 {

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
    // es para poner los log
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

        driver.get("https://www.demoblaze.com/");
        capturar("01_inicio");

        WebElement producto = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(),'Samsung galaxy s6')]")
                )
        );
        capturar("02_tarjeta_producto");
        producto.click();
        log("2. Ingreso al detalle del producto");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Samsung galaxy s6')]")));


        WebElement botonAgregar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]")));
        log("3. Botón Add to cart encontrado");
        botonAgregar.click();
        log("4. Click en Add to cart");
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alerta = driver.switchTo().alert();
        alerta.accept();

        log("5. Alerta aceptada");
        driver.findElement(By.id("cartur")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'Samsung galaxy s6')]")));

        capturar("05_carrito");

        log("6. Carrito visualizado");

        driver.findElement(By.xpath("//button[contains(text(),'Place Order')]")).click();

        log("7. Formulario de compra abierto");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("name"))
        );

        driver.findElement(By.id("name")).sendKeys("Thomas");
        driver.findElement(By.id("country")).sendKeys("Colombia");
        driver.findElement(By.id("city")).sendKeys("Bogota");
        driver.findElement(By.id("card")).sendKeys("123456789");
        driver.findElement(By.id("month")).sendKeys("06");
        driver.findElement(By.id("year")).sendKeys("2026");

        log("8. Datos ingresados");
        driver.findElement(By.xpath("//button[contains(text(),'Purchase')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sweet-alert")));

        capturar("06_confirmacion_compra");
        log("9. Compra confirmada");

        driver.quit();
    }
}