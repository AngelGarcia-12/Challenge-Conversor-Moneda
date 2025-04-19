package controller;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

// Importaciones de mis packages
import model.EnvLoader;
import model.ConversorCurrency;

public class ConversorService {
    private String createConnectionAPI() {
        // Extraer las variables de entorno para crear el endpoint de la API
        final Map<String, String> env = EnvLoader.loadEnv(".env");
        final String apiBaseUrl = env.get("API_URL");
        final String apiKey = env.get("API_KEY");
        final String baseCurrency = env.get("BASE_CURRENCY");

        // Construccion de la URL
        final String endpointURL = String.format("%s/%s/latest/%s", apiBaseUrl, apiKey, baseCurrency);
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointURL))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //! System.out.println("Codigo de respuesta: " + response.statusCode());
            //! System.out.println("Respuesta:\n" + response.body());

            return response.body();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    private Map<String, Double> getExchangeRates() {
        try{
            String responseJson = createConnectionAPI();
            Gson gson = new Gson();
    
            // Deserializar la respuesta JSON a un objeto
            ConversorCurrency conversor = gson.fromJson(responseJson, ConversorCurrency.class);
    
            return conversor.getConversionRates();
        }
        catch(Exception e) {
            System.out.println("No fue posible establecer conexion con la API");

            return null;
        }
    }

    private void showRates() {
        Map<String, Double> rates = getExchangeRates();

        if(rates != null) {
            System.out.println("\n--- Todas las tasas ---");
            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            System.out.println();
        }
        else {
            System.out.println("No se pudo obtener las tasas de cambio");
        }
    }

    private void convertAnyCurrency(double input, String currentCurrency, String convertCurrency) {
        Map<String, Double> rates = getExchangeRates();

        if(rates != null) {
            System.out.println("┌-----------------------------------------------┐");
            System.out.println(String.format("| El valor actual de %s es %.2f: ", convertCurrency, rates.get(convertCurrency)) + "\t\t|");
            double total = input * rates.get(convertCurrency);
            System.out.println(String.format("|\t %.2f %s son %.2f %s \t\t|", input, currentCurrency, total, convertCurrency));
            System.out.println("└-----------------------------------------------┘");
        }
        else {
            System.out.println("No se pudo obtener las tasas de cambio");
        }
    }

    private void desconvertAnyCurrency(double input, String currentCurrency, String convertCurrency) {
        Map<String, Double> rates = getExchangeRates();

        if(rates != null) {
            System.out.println("┌-----------------------------------------------┐");
            System.out.println(String.format("| El valor actual de %s es %f: ", convertCurrency, rates.get(convertCurrency)) + "  |");
            double total = input / rates.get(currentCurrency);
            System.out.println(String.format("|\t %.2f %s son %.2f %s \t\t|", input, currentCurrency, total, convertCurrency));
            System.out.println("└-----------------------------------------------┘");
        }
        else {
            System.out.println("No se pudo obtener las tasas de cambio");
        }
    }

    private void searchRate(String input) {
        Map<String, Double> rates = getExchangeRates();

        if(rates != null) {
            input = input.toUpperCase();
            if(rates.containsKey(input)) {
                System.out.println("┌-----------------------------------------------┐");
                System.out.println(String.format("| El valor actual de %s es %f: ", input, rates.get(input)) + "\t\t|");
                System.out.println("└-----------------------------------------------┘");
                System.out.println();
            }
            else {
                System.out.println("No se pudo obtener las tasas de cambio");
            }
        }
        else {
            System.out.println("No se pudo obtener las tasas de cambio");
        }
    }

    private void convertDollarToMXN(double input) {
        final String currentCurrency = "USD";
        final String convertCurrency = "MXN";
        convertAnyCurrency(input, currentCurrency, convertCurrency);
    }

    private void convertMXNToDollar(double input) {
        final String currentCurrency = "MXN";
        final String convertCurrency = "USD";
        desconvertAnyCurrency(input, currentCurrency, convertCurrency);
    }

    private void convertDollarToCoinBrazilian(double input) {
        final String currentCurrency = "USD";
        final String convertCurrency = "BRL";
        convertAnyCurrency(input, currentCurrency, convertCurrency);
    }

    private void convertCoinBrazilianToDollar(double input) {
        final String currentCurrency = "BRL";
        final String convertCurrency = "USD";
        desconvertAnyCurrency(input, currentCurrency, convertCurrency);
    }

    private void convertMxnToBrl(double input) {
        final String currentCurrency = "MXN";
        final String convertCurrency = "BRL";
        convertAnyCurrency(input, currentCurrency, convertCurrency);
    }

    private void convertBrlToMxn(double input) {
        final String currentCurrency = "BRL";
        final String convertCurrency = "MXN";
        desconvertAnyCurrency(input, currentCurrency, convertCurrency);
    }

    public void startMenu() {
        // Creacion de menu
        Scanner inputOption = new Scanner(System.in);
        Scanner inputConversion = new Scanner(System.in);
        while(true) {
            System.out.println("*********************************************************");
            System.out.println("*\t\tCONVERSOR DE MONEDAS\t\t\t*");
            System.out.println("* Elija una de las siguientes opciones:\t\t\t*");
            System.out.println("* 1) Conversion de 'Dolares' a 'Pesos Mexicanos'\t*");
            System.out.println("* 2) Conversion de 'Pesos Mexicanos' a 'Dolares'\t*");
            System.out.println("* 3) Conversion de 'Dolares' a 'Real Brasileño'\t\t*");
            System.out.println("* 4) Conversion de 'Real Brasileño' a 'Dolares'\t\t*");
            System.out.println("* 5) Conversion de 'Pesos Mexicanos' a 'Real Brasileño' *");
            System.out.println("* 6) Conversion de 'Real Brasileño' a 'Pesos Mexicanos' *");
            System.out.println("* 7) Ver valores actuales de monedas\t\t\t*");
            System.out.println("* 8) Buscar alguna tasa para ver su valor actual\t*");
            System.out.println("* 0) Salir\t\t\t\t\t\t*");
            System.out.println("*********************************************************");
            System.out.print("\nOpcion: ");

            if(inputOption.hasNextInt()) {
                int numOpcion = inputOption.nextInt();

                switch (numOpcion) {
                    case 1:
                        // Conversion de Dolares a Pesos Mexicanos
                        System.out.print("Elija la cantidad a convertir: $");
                        if(inputConversion.hasNextDouble()) {
                            double currency = inputConversion.nextDouble();
                            while(currency < 0) {
                                System.out.println("La cantidad ingresada debe ser una cantidad positiva");
                                System.out.print("Elija la cantidad a convertir: $");
                                currency = inputConversion.nextDouble();
                            }
                            convertDollarToMXN(currency);
                        }
                        else {
                            System.out.println("Por favor elija una cantidad real");
                        }
                        break;
                    case 2:
                        // Conversion de Pesos Mexicanos a Dolares
                        System.out.print("Elija la cantidad a convertir: ");
                        if(inputConversion.hasNextDouble()) {
                            double currency = inputConversion.nextDouble();
                            while(currency < 0) {
                                System.out.println("La cantidad ingresada debe ser una cantidad positiva");
                                System.out.print("Elija la cantidad a convertir: $");
                                currency = inputConversion.nextDouble();
                            }
                            convertMXNToDollar(currency);
                        }
                        else {
                            System.out.println("Por favor elija una cantidad real");
                        }
                        break;
                    case 3:
                        // Conversion de Dolares a Real brasilenio
                        System.out.print("Elija la cantidad a convertir: ");
                        if(inputConversion.hasNextDouble()) {
                            double currency = inputConversion.nextDouble();
                            while(currency < 0) {
                                System.out.println("La cantidad ingresada debe ser una cantidad positiva");
                                System.out.print("Elija la cantidad a convertir: $");
                                currency = inputConversion.nextDouble();
                            }
                            convertDollarToCoinBrazilian(currency);
                        }
                        else {
                            System.out.println("Por favor elija una cantidad real");
                        }
                        break;
                    case 4:
                        // Conversion de Real Brasilenio a Dolares
                        System.out.print("Elija la cantidad a convertir: ");
                        if(inputConversion.hasNextDouble()) {
                            double currency = inputConversion.nextDouble();
                            while(currency < 0) {
                                System.out.println("La cantidad ingresada debe ser una cantidad positiva");
                                System.out.print("Elija la cantidad a convertir: $");
                                currency = inputConversion.nextDouble();
                            }
                            convertCoinBrazilianToDollar(currency);
                        }
                        else {
                            System.out.println("Por favor elija una cantidad real");
                        }
                        break;
                    case 5:
                        System.out.print("Elija la cantidad a convertir: ");
                        if(inputConversion.hasNextDouble()) {
                            double currency = inputConversion.nextDouble();
                            while(currency < 0) {
                                System.out.println("La cantidad ingresada debe ser una cantidad positiva");
                                System.out.print("Elija la cantidad a convertir: $");
                                currency = inputConversion.nextDouble();
                            }
                            convertMxnToBrl(currency);
                        }
                        else {
                            System.out.println("Por favor elija una cantidad real");
                        }
                        break;
                    case 6:
                        System.out.print("Elija la cantidad a convertir: ");
                        if(inputConversion.hasNextDouble()) {
                            double currency = inputConversion.nextDouble();
                            while(currency < 0) {
                                System.out.println("La cantidad ingresada debe ser una cantidad positiva");
                                System.out.print("Elija la cantidad a convertir: $");
                                currency = inputConversion.nextDouble();
                            }
                            convertBrlToMxn(currency);
                        }
                        else {
                            System.out.println("Por favor elija una cantidad real");
                        }
                        break;
                    case 7:
                        showRates();
                        break;
                    case 8:
                        inputOption.nextLine();
                        System.out.print("Introduzca el tipo de moneda a buscar: ");
                        String rate = inputOption.nextLine();
                        searchRate(rate);
                        break;
                    case 0:
                        // Opcion Salir
                        break;
                    default:
                        // Opcion invalida
                        System.out.println("Opcion no valida, por favor ingrese una opcion valida");
                        break;
                }

                // Opcion Salir
                if(numOpcion == 0) {
                    System.out.println("Gracias por usar este programa!");
                    break;
                }
            }
            else {
                String opcion = inputOption.nextLine();
                if(opcion.equalsIgnoreCase("salir")) {
                    System.out.println("Gracias por usar este programa!");
                    break;
                }
                else {
                    System.out.println("Opcion no valida, por favor ingrese una opcion valida");
                }
            }

            // Poner en pausa la informacion hasta que el usuario presione enter para continuar
            System.out.println("Presiona Enter para continuar...");
            inputOption.nextLine();
            inputOption.nextLine();
        }

        inputOption.close();
        inputConversion.close();
    }
}
