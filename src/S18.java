import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class S18 {

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

    public static void registrarVisita(String seccion) {
        String url = driver.getCurrentUrl();
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        log("URL: " + url + " | Sección: " + seccion + " | Hora de visita: " + hora);
    }

    public static void clicJS(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
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

        // 1. Sección 1: Portada de Wikipedia
        driver.get("https://es.wikipedia.org/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("firstHeading")));
        registrarVisita("Portada");
        capturar("18_01_portada");
        WebElement logo = driver.findElement(By.className("mw-logo-icon"));
        capturarElemento(logo, "18_el_01_logo");

        // 2. Sección 2: Portal de la comunidad
        WebElement linkComunidad = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#n-portal a")));
        clicJS(linkComunidad);
        WebElement headingComunidad = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        registrarVisita("Portal de la comunidad");
        capturar("18_02_comunidad");
        capturarElemento(headingComunidad, "18_el_02_heading_comunidad");

        // 3. Sección 3: Ayuda
        WebElement linkAyuda = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#n-help a")));
        clicJS(linkAyuda);
        WebElement headingAyuda = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        registrarVisita("Ayuda");
        capturar("18_03_ayuda");
        capturarElemento(headingAyuda, "18_el_03_heading_ayuda");

        // 4. Sección 4: Cambios recientes
        WebElement linkCambios = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#n-recentchanges a")));
        clicJS(linkCambios);
        WebElement headingCambios = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        registrarVisita("Cambios recientes");
        capturar("18_04_cambios_recientes");
        capturarElemento(headingCambios, "18_el_04_heading_cambios");

        // 5. Sección 5: Página aleatoria
        WebElement linkAleatoria = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#n-randompage a")));
        clicJS(linkAleatoria);
        WebElement headingAleatoria = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        registrarVisita("Página aleatoria");
        capturar("18_05_aleatoria");
        capturarElemento(headingAleatoria, "18_el_05_heading_aleatoria");

        driver.quit();
    }
}
