import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.util.List;

public class S5 {
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

    public static void log(String mensaje) {
        System.out.println(mensaje);
    }

    public static void main(String[] args) throws Exception {
        driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();

        log("=== EJERCICIO 5: Demoblaze - Carrito con 2 productos ===");
        driver.get("https://www.demoblaze.com/");
        // Esperar a que haya al menos un producto
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".card-block .card-title a")));
        capturar("05_demoblaze_inicio");

        List<WebElement> productos = driver.findElements(By.cssSelector(".card-block .card-title a"));
        if (productos.size() < 2) {
            log("No hay suficientes productos. Encontrados: " + productos.size());
            driver.quit();
            return;
        }

        //PRIMER PRODUCTO
        productos.get(0).click();
        // Esperar que cargue la página de detalle (el nombre del producto es un buen indicador)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("name")));
        capturar("05_producto1_detalle");

        WebElement tarjeta1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("more-information")));
        capturarElemento(tarjeta1, "05_tarjeta_producto1");

        WebElement addToCart1 = driver.findElement(By.xpath("//a[contains(text(),'Add to cart')]"));
        capturarElemento(addToCart1, "05_boton_add_to_cart1");
        addToCart1.click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alerta = driver.switchTo().alert();
        log("Alerta producto1: " + alerta.getText());
        alerta.accept();

        // Volver a la página principal
        driver.get("https://www.demoblaze.com/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".card-block .card-title a")));
        capturar("05_demoblaze_inicio_despues_producto1");

        //SEGUNDO PRODUCTO
        productos = driver.findElements(By.cssSelector(".card-block .card-title a"));
        if (productos.size() < 2) {
            log("No hay suficientes productos después de recargar.");
            driver.quit();
            return;
        }
        productos.get(1).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("name")));
        capturar("05_producto2_detalle");

        WebElement tarjeta2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("more-information")));
        capturarElemento(tarjeta2, "05_tarjeta_producto2");

        WebElement addToCart2 = driver.findElement(By.xpath("//a[contains(text(),'Add to cart')]"));
        capturarElemento(addToCart2, "05_boton_add_to_cart2");
        addToCart2.click();
        wait.until(ExpectedConditions.alertIsPresent());
        alerta = driver.switchTo().alert();
        log("Alerta producto2: " + alerta.getText());
        alerta.accept();

        // Ir al carrito
        driver.get("https://www.demoblaze.com/cart.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
        capturar("05_carrito_dos_productos");

        WebElement total = driver.findElement(By.id("totalp"));
        capturarElemento(total, "05_total_carrito");
        log("Total del carrito: " + total.getText());
        log("FIN EJERCICIO 5");

        Thread.sleep(2000);
        driver.quit();
    }
}