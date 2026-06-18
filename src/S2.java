import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class S2{

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
        String productoBuscar = "Blue Top";

        driver.get("https://automationexercise.com/");

        log("1. Página principal cargada");

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Products')]"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'All Products')]")));
        WebElement campoBusqueda = driver.findElement(By.id("search_product"));

        log("2. Campo de búsqueda encontrado");
        capturar("1 campo_busqueda");

        campoBusqueda.sendKeys(productoBuscar);
        WebElement botonBuscar = driver.findElement(By.id("submit_search"));
        botonBuscar.click();

        log("3. Búsqueda ejecutada");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Searched Products')]")));

        capturar("2_resultado_busqueda");

        List<WebElement> resultados = driver.findElements(
                By.cssSelector(".product-image-wrapper")
        );

        if (resultados.size() > 0) {

            log("4. Resultados encontrados: " + resultados.size());
            capturar("3.tarjeta_producto");
            log("6. Se capturó un producto encontrado");
        } else {
            log("7. No se encontraron resultados");}
        driver.quit();
        log("8. Navegador cerrado");
    }
}