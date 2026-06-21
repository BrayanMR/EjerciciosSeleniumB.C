import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class S15 {

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

        // 1. Abrir OrangeHRM
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        Thread.sleep(2000);

        // Capturar pantalla de login
        capturar("15_login");

        // Localizar campos
        WebElement campoUsuario = driver.findElement(By.name("username"));
        WebElement campoContrasena = driver.findElement(By.name("password"));
        WebElement botonLogin = driver.findElement(By.xpath("//button[@type='submit']"));

        // Ingresar credenciales válidas
        campoUsuario.sendKeys("Admin");
        campoContrasena.sendKeys("admin123");

        // Capturar pantalla con datos ingresados
        capturar("15_datos_ingresados");

        // Capturar individualmente el botón login
        capturarElemento(botonLogin, "15_boton_login");

        // Hacer clic en login e iniciar sesión
        botonLogin.click();

        // Esperar a que se cargue el panel principal (Dashboard)
        WebElement tituloDashboard = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h6[contains(.,'Dashboard')]"))
        );
        WebElement menuLateral = driver.findElement(By.className("oxd-sidepanel"));

        log("Autenticación exitosa.");
        log("Panel principal cargado.");

        // Capturar pantalla del panel principal
        capturar("15_panel_principal");

        // Capturar individualmente el título del dashboard y el menú lateral
        capturarElemento(tituloDashboard, "15_titulo_dashboard");
        capturarElemento(menuLateral, "15_menu_lateral");

        driver.quit();
    }
}
