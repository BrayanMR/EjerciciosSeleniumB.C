import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.util.List;

public class S8 {
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

        log("=== EJERCICIO 8: Demoblaze - Categoría Phones ===");
        driver.get("https://www.demoblaze.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contcont")));
        capturar("08_categoria_inicio");

        WebElement categoriaPhones = driver.findElement(By.xpath("//a[contains(text(),'Phones')]"));
        capturarElemento(categoriaPhones, "08_categoria_phones_link");
        categoriaPhones.click();
        log("Categoría seleccionada: Phones");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card-block")));
        capturar("08_categoria_phones_productos");

        List<WebElement> productos = driver.findElements(By.cssSelector(".card-block .card-title a"));
        log("Cantidad de productos visibles: " + productos.size());

        if (!productos.isEmpty()) {
            WebElement tarjeta = productos.get(0).findElement(By.xpath("./ancestor::div[@class='card-block']"));
            capturarElemento(tarjeta, "08_tarjeta_producto_phones");
        }
        log("=== FIN EJERCICIO 8 ===");
        Thread.sleep(2000);
        driver.quit();
    }
}