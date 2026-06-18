import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class S4 {
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();

        log("=== EJERCICIO 4: Wikipedia ===");
        driver.get("https://es.wikipedia.org/");

        // Esperar a que el campo de búsqueda esté presente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchInput")));
        log("Campo de búsqueda encontrado.");

        WebElement searchBox = driver.findElement(By.id("searchInput"));
        capturar("04_wikipedia_portada");
        capturarElemento(searchBox, "04_campo_busqueda");

        // Escribir y enviar con Enter
        searchBox.clear();
        searchBox.sendKeys("Selenium");
        searchBox.sendKeys(Keys.ENTER);

        // Esperar el título del artículo
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstHeading")));
        capturar("04_resultado_busqueda");

        WebElement title = driver.findElement(By.id("firstHeading"));
        capturarElemento(title, "04_titulo_articulo");

        log("Palabra buscada: Selenium");
        log("URL cargada: " + driver.getCurrentUrl());
        log("Resultado encontrado: " + title.getText());
        log("=== FIN EJERCICIO 4 ===");

        Thread.sleep(2000);
        driver.quit();
    }
}