import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.util.List;

public class S9 {
    static WebDriver driver;

    public static void capturar(String nombre) throws Exception {
        File carpeta = new File("evidencias");
        if (!carpeta.exists()) carpeta.mkdirs();
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File origen = screenshot.getScreenshotAs(OutputType.FILE);
        File destino = new File("evidencias/" + nombre + ".png");
        FileHandler.copy(origen, destino);
    }

    public static void capturarElemento(WebElement elemento, String nombre) throws Exception {
        File carpeta = new File("evidencias");
        if (!carpeta.exists()) carpeta.mkdirs();
        File origen = elemento.getScreenshotAs(OutputType.FILE);
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

        log("=== EJERCICIO 9: Automation Exercise - Productos visibles ===");
        driver.get("https://automationexercise.com/products");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("features_items")));
        capturar("09_productos_pantalla_completa");

        List<WebElement> productos = driver.findElements(By.className("product-image-wrapper"));
        log("Productos detectados: " + productos.size());

        int contador = 0;
        for (WebElement producto : productos) {
            if (contador >= 3) break;
            capturarElemento(producto, "09_producto_individual_" + (contador + 1));
            contador++;
        }

        if (!productos.isEmpty()) {
            WebElement primerProducto = productos.get(0);
            WebElement nombreProducto = primerProducto.findElement(By.xpath(".//p"));
            WebElement precioProducto = primerProducto.findElement(By.xpath(".//h2"));
            WebElement imagenProducto = primerProducto.findElement(By.tagName("img"));

            capturarElemento(nombreProducto, "09_nombre_producto");
            capturarElemento(precioProducto, "09_precio_producto");
            capturarElemento(imagenProducto, "09_imagen_producto");

            log("Nombre del producto: " + nombreProducto.getText());
            log("Precio del producto: " + precioProducto.getText());
        }
        log("=== FIN EJERCICIO 9 ===");
        Thread.sleep(2000);
        driver.quit();
    }
}