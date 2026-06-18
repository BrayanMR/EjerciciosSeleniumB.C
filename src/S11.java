import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.util.List;

public class S11 {

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

        driver.get("https://www.demoblaze.com/");
        capturar("11_pagina_inicial");

        WebElement botonCart = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cartur")));
        capturarElemento(botonCart, "11_boton_cart");

        botonCart.click();
        wait.until(ExpectedConditions.urlContains("cart.html"));
        capturar("11_ingreso_al_carrito");

        WebElement tablaCarrito = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table-responsive")));
        capturarElemento(tablaCarrito, "11_tabla_carrito");

        Thread.sleep(3000);
        capturar("11_carrito_vacio");

        WebElement tbody = driver.findElement(By.id("tbodyid"));
        List<WebElement> filas = tbody.findElements(By.tagName("tr"));

        if (filas.isEmpty()) {
            log("No se encontraron productos agregados.");
        }

        driver.quit();
    }
}