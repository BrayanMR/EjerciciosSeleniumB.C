import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class S13 {

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

    public static void log(String mensaje) {
        System.out.println(mensaje);
    }

    public static void main(String[] args) throws Exception {
        driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();

        driver.get("https://automationexercise.com/products");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'All Products')]")));

        capturar("13_pantalla_busqueda");

        WebElement campoBusqueda = driver.findElement(By.id("search_product"));
        capturarElemento(campoBusqueda, "13_campo_busqueda");

        campoBusqueda.sendKeys("ProductoXYZ123");
        driver.findElement(By.id("submit_search")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Searched Products')]")));
        Thread.sleep(2000);

        capturar("13_resultado_obtenido");

        WebElement zonaResultados = driver.findElement(By.xpath("//div[@class='features_items']"));
        capturarElemento(zonaResultados, "13_zona_resultados");

        List<WebElement> productos = driver.findElements(By.cssSelector(".product-image-wrapper"));
        if (productos.isEmpty()) {
            log("La búsqueda no devolvió coincidencias visibles.");
        }

        driver.quit();
    }
}