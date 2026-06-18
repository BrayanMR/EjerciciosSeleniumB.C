import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class S6 {
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

        log("=== EJERCICIO 6: Automation Exercise - Contacto ===");
        driver.get("https://automationexercise.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
        capturar("06_contacto_home");

        driver.findElement(By.xpath("//a[contains(text(),'Contact us')]")).click();
        // Esperar a que el formulario esté presente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("contact-us-form")));
        capturar("06_contacto_pagina");

        //Localizar los campos dentro del formulario usando selectores CSS específicos
        WebElement campoNombre = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#contact-us-form input[name='name']")));
        WebElement campoEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#contact-us-form input[name='email']")));
        WebElement campoAsunto = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#contact-us-form input[name='subject']")));
        WebElement campoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#contact-us-form textarea#message")));
        WebElement campoArchivo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#contact-us-form input[name='upload_file']")));
        WebElement botonEnviar = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#contact-us-form input[type='submit']")));


        capturarElemento(campoNombre, "06_campo_nombre");
        capturarElemento(campoEmail, "06_campo_email");
        capturarElemento(campoAsunto, "06_campo_asunto");
        capturarElemento(campoMensaje, "06_campo_mensaje");
        capturarElemento(campoArchivo, "06_campo_archivo");
        capturarElemento(botonEnviar, "06_boton_enviar");

        String nombre = "Usuario Test";
        String email = "test@example.com";
        String asunto = "Consulta sobre productos";
        String mensaje = "Este es un mensaje de prueba.";

        campoNombre.sendKeys(nombre);
        log("Campo nombre: " + nombre);
        campoEmail.sendKeys(email);
        log("Campo email: " + email);
        campoAsunto.sendKeys(asunto);
        log("Campo asunto: " + asunto);
        campoMensaje.sendKeys(mensaje);
        log("Campo mensaje: " + mensaje);

        File archivoDummy = new File("archivo_prueba.txt");
        if (!archivoDummy.exists()) {
            archivoDummy.createNewFile();
        }
        campoArchivo.sendKeys(archivoDummy.getAbsolutePath());
        log("Archivo subido: " + archivoDummy.getAbsolutePath());

        capturar("06_contacto_formulario_lleno");

        botonEnviar.click();
        log("Click en Submit");

        Thread.sleep(1000);


        wait.until(ExpectedConditions.alertIsPresent());
        Alert alerta = driver.switchTo().alert();
        log("Alerta detectada: " + alerta.getText());
        alerta.accept();
        log("Alerta aceptada");


        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='status alert alert-success']")));
        capturar("06_contacto_mensaje_final");
        log("Mensaje final: " + driver.findElement(
                By.xpath("//div[@class='status alert alert-success']")).getText());
        log("FIN EJERCICIO 6");

        Thread.sleep(2000);
        driver.quit();
    }
}