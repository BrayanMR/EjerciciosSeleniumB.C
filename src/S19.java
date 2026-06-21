import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S19 {

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

        // 1. Navegar a un producto
        driver.get("https://www.demoblaze.com/");
        WebElement producto = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Samsung galaxy s6')]"))
        );
        producto.click();

        WebElement tituloProducto = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Samsung galaxy s6')]"))
        );

        // Capturar antes de la acción
        capturar("19_antes_de_accion");

        // Capturar individualmente el producto seleccionado y el botón de agregar al carrito
        WebElement botonAgregar = driver.findElement(By.xpath("//a[contains(text(),'Add to cart')]"));
        capturarElemento(tituloProducto, "19_producto_seleccionado");
        capturarElemento(botonAgregar, "19_boton_alerta");

        // Realizar acción que genera la alerta (clic)
        botonAgregar.click();

        // Esperar a que la alerta esté presente
        wait.until(ExpectedConditions.alertIsPresent());
        Thread.sleep(1000); // Dar tiempo para que se pinte la alerta

        // Capturar después del clic (pantalla completa con la alerta nativa del navegador)
        capturarPantallaRobot("19_despues_del_clic");

        // Leer alerta, loggear y aceptar
        Alert alerta = driver.switchTo().alert();
        log("Texto de la alerta: " + alerta.getText());
        alerta.accept();

        // Capturar estado posterior a la alerta
        Thread.sleep(1000);
        capturar("19_estado_posterior_alerta");

        driver.quit();
    }
}
