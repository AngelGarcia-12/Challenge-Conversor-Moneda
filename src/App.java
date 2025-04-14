import controller.ConversorService;

public class App {
    public static void main(String[] args) throws Exception {
        // Singlenton pattern
        ConversorService myConversor = new ConversorService();
        myConversor.startMenu();
    }
}
