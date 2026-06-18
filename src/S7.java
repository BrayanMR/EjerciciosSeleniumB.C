import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class S7 {
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

        log("=== EJERCICIO 7: Demoblaze - Menú principal ===");
        driver.get("https://www.demoblaze.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contcont")));
        capturar("07_menu_inicio");

        String[] opciones = {"Home", "Contact", "About us", "Cart", "Log in"};
        By[] localizadores = {
                By.xpath("//a[contains(text(),'Home')]"),
                By.xpath("//a[contains(text(),'Contact')]"),
                By.xpath("//a[contains(text(),'About us')]"),
                By.xpath("//a[contains(text(),'Cart')]"),
                By.xpath("//a[contains(text(),'Log in')]")
        };

        for (int i = 0; i < opciones.length; i++) {
            WebElement opcion = driver.findElement(localizadores[i]);
            capturarElemento(opcion, "07_menu_" + opciones[i].toLowerCase().replace(" ", "_"));
            opcion.click();
            log("Clic en: " + opciones[i]);

            if (opciones[i].equals("Home")) {
                capturar("07_menu_home_seleccionado");
            } else if (opciones[i].equals("Contact")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("exampleModalLabel")));
                capturar("07_menu_contact_seleccionado");
                driver.findElement(By.xpath("//button[contains(text(),'Close')]")).click();
            } else if (opciones[i].equals("About us")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("videoModalLabel")));
                capturar("07_menu_aboutus_seleccionado");
                driver.findElement(By.xpath("//button[contains(text(),'Close')]")).click();
            } else if (opciones[i].equals("Cart")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
                capturar("07_menu_cart_seleccionado");
                driver.navigate().back();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("contcont")));
            } else if (opciones[i].equals("Log in")) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logInModalLabel")));
                capturar("07_menu_login_seleccionado");
                driver.findElement(By.xpath("//button[contains(text(),'Close')]")).click();
            }
            Thread.sleep(500);
        }
        log("=== FIN EJERCICIO 7 ===");
        Thread.sleep(2000);
        driver.quit();
    }
}