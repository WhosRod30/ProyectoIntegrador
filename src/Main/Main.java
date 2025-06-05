/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import Controlador.LoginController;
import Vista.Login;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

/**
 *
 * @author franc
 */
public class Main {

     public static Login login;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Styles
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 15);
            UIManager.put("Button.borderColor", "#B5E0FF");
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("Separator.stripeWidth", 3);
            UIManager.put("PasswordField.showRevealButton", true);
            //Vista
            login = new Login();
            //Controller
            LoginController controller = new LoginController(login);
            
        } catch (Exception e) {
        }
    }
    
}
