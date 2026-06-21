import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S12 {

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

    public static void capturarPantallaRobot(String nombre) {
        try {
            java.awt.Robot robot = new java.awt.Robot();
            java.awt.image.BufferedImage screenShot = robot.createScreenCapture(
                    new java.awt.Rectangle(java.awt.Toolkit.getDefaultToolkit().getScreenSize())
            );
            File carpeta = new File("evidencias");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            javax.imageio.ImageIO.write(screenShot, "png", new File("evidencias/" + nombre + ".png"));
        } catch (Exception e) {
            System.out.println("Error al capturar con Robot: " + e.getMessage());
        }
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

        driver.get("https://www.demoblaze.com/");
        WebElement producto = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Samsung galaxy s6')]"))
        );
        producto.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Samsung galaxy s6')]")));
        capturar("12_producto");

        WebElement botonAgregar = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]"))
        );
        botonAgregar.click();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alertaConfirmacion = driver.switchTo().alert();
        alertaConfirmacion.accept();

        driver.findElement(By.id("cartur")).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
        capturar("12_carrito");

        WebElement botonPlaceOrder = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'Place Order')]"))
        );
        capturarElemento(botonPlaceOrder, "12_boton_place_order");

        botonPlaceOrder.click();

        WebElement formularioModal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='orderModal']//div[@class='modal-content']"))
        );
        capturar("12_formulario_compra");
        capturarElemento(formularioModal, "12_formulario");

        WebElement botonPurchase = driver.findElement(By.xpath("//button[contains(text(),'Purchase')]"));
        botonPurchase.click();

        wait.until(ExpectedConditions.alertIsPresent());
        Thread.sleep(1000);
        capturarPantallaRobot("12_alerta_generada");
        capturarPantallaRobot("12_alerta");

        Alert alertaError = driver.switchTo().alert();
        alertaError.accept();

        log("Intento fallido de compra.");

        driver.quit();
    }
}