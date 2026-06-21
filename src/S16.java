import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class S16 {

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

        String tema = "Java";

        // 1. Abrir Wikipedia en español
        driver.get("https://es.wikipedia.org/");
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("search")));
        searchInput.sendKeys(tema);

        // Capturar la búsqueda
        capturar("16_busqueda");

        // Buscar
        driver.findElement(By.name("search")).submit();

        // 2. Esperar al encabezado del artículo (resultado)
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        Thread.sleep(2000); // Esperar renderizado

        log("Tema consultado: " + tema);
        log("Título obtenido: " + heading.getText());

        // Capturar el resultado y el encabezado del artículo
        capturar("16_resultado");
        capturar("16_encabezado_articulo");

        // Capturar individualmente el título y el primer párrafo
        capturarElemento(heading, "16_titulo");

        WebElement primerParrafo = driver.findElement(By.cssSelector(".mw-parser-output p"));
        capturarElemento(primerParrafo, "16_primer_parrafo");

        // Capturar individualmente la tabla lateral (infobox) si existe
        List<WebElement> infoboxList = driver.findElements(By.cssSelector("table.infobox"));
        if (!infoboxList.isEmpty()) {
            capturarElemento(infoboxList.get(0), "16_tabla_lateral");
        }

        driver.quit();
    }
}
