import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class S17 {

    static WebDriver driver;

    public static String obtenerFechaHora() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    public static void capturar(String nombre) throws Exception {
        File carpeta = new File("evidencias");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File origen = screenshot.getScreenshotAs(OutputType.FILE);
        String ruta = "evidencias/" + obtenerFechaHora() + "_" + nombre + ".png";
        File destino = new File(ruta);
        FileHandler.copy(origen, destino);
        log("Evidencia creada en: " + destino.getAbsolutePath().replace("\\", "/"));
    }

    public static void capturarElemento(WebElement elemento, String nombre) throws Exception {
        File carpeta = new File("evidencias");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        File origen = elemento.getScreenshotAs(OutputType.FILE);
        String ruta = "evidencias/" + obtenerFechaHora() + "_" + nombre + ".png";
        File destino = new File(ruta);
        FileHandler.copy(origen, destino);
        log("Evidencia creada en: " + destino.getAbsolutePath().replace("\\", "/"));
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

        // 1. Etapa 1: Página inicial
        driver.get("https://www.demoblaze.com/");
        Thread.sleep(1000);
        capturar("17_01_inicio");

        // Elemento individual 1: Botón de categoría Laptops
        WebElement linkLaptops = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[@onclick=\"byCat('notebook')\"]"))
        );
        capturarElemento(linkLaptops, "17_el_01_categoria_laptops");

        // 2. Etapa 2: Categoría Laptops
        linkLaptops.click();
        Thread.sleep(2000);
        capturar("17_02_laptops");

        // Elemento individual 2: Enlace del producto MacBook air
        WebElement linkMacbook = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'MacBook air')]"))
        );
        capturarElemento(linkMacbook, "17_el_02_enlace_producto");

        // 3. Etapa 3: Detalle de producto
        linkMacbook.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'MacBook air')]")));
        capturar("17_03_detalle_producto");

        // Elemento individual 3: Botón Add to cart
        WebElement botonAgregar = driver.findElement(By.xpath("//a[contains(text(),'Add to cart')]"));
        capturarElemento(botonAgregar, "17_el_03_boton_agregar");

        // Agregar al carrito y aceptar alerta
        botonAgregar.click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alerta = driver.switchTo().alert();
        alerta.accept();

        // Elemento individual 4: Enlace del menú Cart
        WebElement linkMenuCart = driver.findElement(By.id("cartur"));
        capturarElemento(linkMenuCart, "17_el_04_menu_cart");

        // 4. Etapa 4: Carrito
        linkMenuCart.click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
        Thread.sleep(2000);
        capturar("17_04_carrito");

        // Elemento individual 5: Botón Place Order
        WebElement botonPlaceOrder = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(),'Place Order')]"))
        );
        capturarElemento(botonPlaceOrder, "17_el_05_boton_place_order");

        // 5. Etapa 5: Formulario de compra
        botonPlaceOrder.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("orderModal")));
        Thread.sleep(1000);
        capturar("17_05_formulario_compra");

        driver.quit();
    }
}
