package loginseguro;

import vista.VentanaLogin;

public class LoginSeguro {

    public static void main(String[] args) {
        // Creamos una instancia de nuestra ventana de login y la hacemos visible
        VentanaLogin ventana = new VentanaLogin();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null); // Esto centra la ventana en la pantalla
    }
}
