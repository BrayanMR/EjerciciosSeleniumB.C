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

    private static boolean logInicializado = false;

    public static void log(String mensaje) {
        System.out.println("[LOG] " + mensaje);

        try {
            File folder = new File("logs");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            try (java.io.FileWriter fw =
                         new java.io.FileWriter("logs/ejercicios.logs", true)) {

                if (!logInicializado) {

                    logInicializado = true;

                    String className =
                            Thread.currentThread().getStackTrace()[2].getClassName();

                    if (className.contains(".")) {
                        className =
                                className.substring(className.lastIndexOf('.') + 1);
                    }

                    fw.write("\n===========================\n");
                    fw.write(java.time.LocalDateTime.now()
                            + " Inicio de prueba - "
                            + className + "\n");

                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        try (java.io.FileWriter fwExit =
                                     new java.io.FileWriter("logs/ejercicios.logs", true)) {

                            fwExit.write(java.time.LocalDateTime.now() + "\n");
                            fwExit.write("==============================================\n");

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

    public static void cerrarModal(By botonCerrar, WebDriverWait wait) {

        wait.until(ExpectedConditions.elementToBeClickable(botonCerrar));

        WebElement boton = driver.findElement(botonCerrar);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", boton);

        wait.until(ExpectedConditions.invisibilityOf(boton));
    }

    public static void main(String[] args) throws Exception {

        driver = new ChromeDriver();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();

        log("=== EJERCICIO 7: Demoblaze - Menú principal ===");

        driver.get("https://www.demoblaze.com/");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("contcont")
                )
        );

        capturar("07_menu_inicio");

        String[] opciones = {
                "Home",
                "Contact",
                "About us",
                "Cart",
                "Log in"
        };

        By[] localizadores = {
                By.xpath("//a[contains(text(),'Home')]"),
                By.xpath("//a[contains(text(),'Contact')]"),
                By.xpath("//a[contains(text(),'About us')]"),
                By.xpath("//a[contains(text(),'Cart')]"),
                By.xpath("//a[contains(text(),'Log in')]")
        };

        for (int i = 0; i < opciones.length; i++) {

            WebElement opcion =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    localizadores[i]
                            )
                    );

            capturarElemento(
                    opcion,
                    "07_menu_" +
                            opciones[i].toLowerCase().replace(" ", "_")
            );

            opcion.click();

            log("Clic en: " + opciones[i]);

            switch (opciones[i]) {

                case "Home":

                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("contcont")
                            )
                    );

                    capturar("07_menu_home_seleccionado");
                    break;

                case "Contact":

                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("exampleModal")
                            )
                    );

                    capturar("07_menu_contact_seleccionado");

                    cerrarModal(
                            By.xpath(
                                    "//div[@id='exampleModal']//button[contains(text(),'Close')]"
                            ),
                            wait
                    );

                    break;

                case "About us":

                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("videoModal")
                            )
                    );

                    capturar("07_menu_aboutus_seleccionado");

                    cerrarModal(
                            By.xpath(
                                    "//div[@id='videoModal']//button[contains(text(),'Close')]"
                            ),
                            wait
                    );

                    break;

                case "Cart":

                    wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    By.id("tbodyid")
                            )
                    );

                    capturar("07_menu_cart_seleccionado");

                    driver.navigate().back();

                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("contcont")
                            )
                    );

                    break;

                case "Log in":

                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    By.id("login2")
                            )
                    );

                    capturar("07_menu_login_seleccionado");

                    cerrarModal(
                            By.xpath(
                                    "//div[@id='logInModal']//button[contains(text(),'Close')]"
                            ),
                            wait
                    );

                    break;
            }

            Thread.sleep(1000);
        }

        log("=== FIN EJERCICIO 7 ===");

        Thread.sleep(3000);

        driver.quit();
    }
}