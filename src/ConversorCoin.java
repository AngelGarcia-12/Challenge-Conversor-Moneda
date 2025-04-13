import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.Scanner;

public class ConversorCoin {
    public static void createConnectionAPI() {
        HttpClient client = HttpClient.newBuilder()
            .version(Version.HTTP_1_1)
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
            .authenticator(Authenticator.getDefault())
            .build();
    }

    private void convertDollarToMXN() {
        return;
    }

    private void convertMXNToDollar() {
        return;
    }

    private void convertDollarToCoinBrazilian() {
        return;
    }

    private void convertCoinBrazilianToDollar() {
        return;
    }

    public void startMenu() {
        // Creacion de menu
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("**************************************************");
            System.out.println("*\t\tCONVERSOR DE MONEDAS\t\t *");
            System.out.println("* Elija una de las siguientes opciones:\t\t *");
            System.out.println("* 1) Conversion de 'Dolares' a 'Pesos Mexicanos' *");
            System.out.println("* 2) Conversion de 'Pesos Mexicanos' a 'Dolares' *");
            System.out.println("* 3) Conversion de 'Dolares' a 'Real Brasileño'\t *");
            System.out.println("* 4) Conversion de 'Real Brasileño' a 'Dolares'\t *");
            System.out.println("* 0) Salir\t\t\t\t\t *");
            System.out.println("*************************************************");
            System.out.print("\nOpcion: ");

            if(input.hasNextInt()) {
                int numOpcion = input.nextInt();

                switch (numOpcion) {
                    case 1:
                        // Conversion de Dolares a Pesos Mexicanos
                        convertDollarToMXN();
                        break;
                    case 2:
                        // Conversion de Pesos Mexicanos a Dolares
                        convertMXNToDollar();
                        break;
                    case 3:
                        // Conversion de Dolares a Real brasilenio
                        convertDollarToCoinBrazilian();
                        break;
                    case 4:
                        // Conversion de Real Brasilenio a Dolares
                        convertCoinBrazilianToDollar();
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
                    break;
                }
            }
            else {
                String opcion = input.nextLine();
                if(opcion.equalsIgnoreCase("salir")) {
                    break;
                }
            }
        }

        input.close();
    }
}
