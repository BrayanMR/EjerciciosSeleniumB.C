import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class S10 {
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

        log("=== EJERCICIO 10: Automation Exercise - Registro ===");
        driver.get("https://automationexercise.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        capturar("10_registro_home");

        WebElement campoNombre = driver.findElement(By.xpath("//input[@data-qa='signup-name']"));
        WebElement campoCorreo = driver.findElement(By.xpath("//input[@data-qa='signup-email']"));
        WebElement botonSignup = driver.findElement(By.xpath("//button[@data-qa='signup-button']"));

        capturarElemento(campoNombre, "10_campo_nombre");
        capturarElemento(campoCorreo, "10_campo_correo");
        capturarElemento(botonSignup, "10_boton_signup");

        String nombre = "TestUser" + System.currentTimeMillis();
        String correo = "test" + System.currentTimeMillis() + "@example.com";
        campoNombre.sendKeys(nombre);
        campoCorreo.sendKeys(correo);
        log("Nombre ingresado: " + nombre);
        log("Correo ingresado: " + correo);

        capturar("10_registro_formulario_lleno");
        botonSignup.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(),'Enter Account Information')]")));
        capturar("10_registro_pagina_siguiente");
        log("Proceso de registro iniciado, se cargó la página de datos adicionales.");
        log("=== FIN EJERCICIO 10 ===");

        Thread.sleep(2000);
        driver.quit();
    }
}