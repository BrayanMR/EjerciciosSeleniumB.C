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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();

        log("=== EJERCICIO 10: Automation Exercise - Registro ===");
        driver.get("https://automationexercise.com/");


        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        Thread.sleep(2000);
        capturar("10_registro_home");


        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Signup / Login')]")));
        capturarElemento(loginLink, "10_enlace_login");
        loginLink.click();
        log("Clic en 'Signup / Login'");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'New User Signup!')]")));
        capturar("10_pagina_login");

        // Localizar campos de registro
        WebElement campoNombre = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@data-qa='signup-name']")));
        WebElement campoCorreo = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@data-qa='signup-email']")));
        WebElement botonSignup = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-qa='signup-button']")));

        capturarElemento(campoNombre, "10_campo_nombre");
        capturarElemento(campoCorreo, "10_campo_correo");
        capturarElemento(botonSignup, "10_boton_signup");

        String nombre = "TestUser" + System.currentTimeMillis();
        String correo = "test" + System.currentTimeMillis() + "@example.com";
        campoNombre.sendKeys(nombre);
        campoCorreo.sendKeys(correo);
        log("Nombre: " + nombre);
        log("Correo: " + correo);

        capturar("10_registro_formulario_lleno");
        botonSignup.click();
        log("Clic en Signup");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='password']")));
        log("Página de registro de cuenta cargada.");

        // Capturar la página de información de cuenta (con el título correcto)
        capturar("10_registro_pagina_siguiente");
        log("Registro exitoso. Página de información de cuenta cargada.");


        try {
            WebElement titulo = driver.findElement(By.xpath("//b[contains(text(),'Enter Account Information')]"));
            capturarElemento(titulo, "10_titulo_cuenta");
            log("Título encontrado: " + titulo.getText());
        } catch (Exception e) {
            log("No se encontró el título exacto, pero el formulario sí cargó.");
        }

        log("=== FIN EJERCICIO 10 ===");
        Thread.sleep(2000);
        driver.quit();
    }
}