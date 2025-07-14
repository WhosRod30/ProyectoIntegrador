/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import Controlador.LoginController;
import Vista.Login;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.*;

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
            // Styles
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            UIManager.put("Separator.stripeWidth", 3);
            UIManager.put("PasswordField.showRevealButton", true);
            UIManager.put("Component.arc", 18);
            UIManager.put("Panel.arc", 18);
            UIManager.put("Button.arc", 18);
            UIManager.put("TextComponent.arc", 14);

            // Nueva paleta de colores para sistema avícola
            Color colorPrimario = new Color(45, 90, 39); // verde avícola principal
            Color colorAcento = new Color(255, 140, 66); // naranja vibrante acento
            Color colorSecundario = new Color(74, 144, 164); // azul confianza
            Color colorFondo = new Color(248, 249, 250); // gris suave fondo
            Color colorTexto = new Color(44, 62, 80); // texto oscuro legible

            // Botones
            UIManager.put("Button.background", colorPrimario);
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.focusedBackground", colorPrimario);
            UIManager.put("Button.hoverBackground", colorAcento);
            UIManager.put("Button.selectedBackground", colorAcento);
            UIManager.put("Button.selectedForeground", colorPrimario);

            // Paneles y textos
            UIManager.put("Panel.background", colorFondo);
            UIManager.put("Label.foreground", colorTexto);
            UIManager.put("TextComponent.background", Color.WHITE);
            UIManager.put("TextComponent.foreground", colorTexto);
            UIManager.put("TextComponent.selectionBackground", new Color(45, 90, 39, 80));
            UIManager.put("TextComponent.selectionForeground", Color.WHITE);

            UIManager.put("Component.focusColor", colorPrimario);
            UIManager.put("Component.borderColor", new Color(45, 90, 39, 80));
            UIManager.put("Component.error.focusedBorderColor", new Color(231, 76, 60));
            UIManager.put("Component.success.focusedBorderColor", new Color(45, 90, 39));

            // Estilo para tablas
            UIManager.put("Table.background", Color.WHITE);
            UIManager.put("Table.foreground", colorTexto);
            UIManager.put("Table.selectionBackground", colorPrimario);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("Table.selectionInactiveBackground", new Color(200, 230, 210));
            UIManager.put("Table.selectionInactiveForeground", colorTexto);
            UIManager.put("Table.alternateRowColor", new Color(240, 245, 242));
            UIManager.put("Table.gridColor", new Color(220, 225, 222));
            UIManager.put("Table.cellFocusColor", colorAcento);
            UIManager.put("Table.rowHeight", 32);
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 15));
            UIManager.put("Table.selectionArc", 14);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 0));
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.fillsViewportHeight", true);
            UIManager.put("Table.paintOutsideAlternateRows", true);

            UIManager.put("TableHeader.background", colorPrimario);
            UIManager.put("TableHeader.foreground", Color.WHITE);
            UIManager.put("TableHeader.font", new Font("Segoe UI Semibold", Font.PLAIN, 15));
            UIManager.put("TableHeader.cellBorder", null);

            // TitleBar (FlatLaf)
            UIManager.put("TitlePane.background", colorPrimario);
            UIManager.put("TitlePane.foreground", Color.WHITE);
            UIManager.put("TitlePane.inactiveBackground", new Color(240, 245, 242));
            UIManager.put("TitlePane.inactiveForeground", new Color(120, 120, 120));
            UIManager.put("TitlePane.borderColor", colorPrimario);
            UIManager.put("TitlePane.centerTitle", true);
            UIManager.put("TitlePane.unifiedBackground", false);
            UIManager.put("TitlePane.buttonSize", new Dimension(24, 24));
            UIManager.put("TitlePane.titleMargins", new Insets(0, 8, 0, 8));
            UIManager.put("TitlePane.menuBarTitleGap", 0);
            UIManager.put("TitlePane.buttonMaximizedHeight", 24);
            UIManager.put("TitlePane.font", new Font("Segoe UI Semibold", Font.PLAIN, 15));

            // Vista
            login = new Login();
            // Controller
            LoginController controller = new LoginController(login);

        } catch (Exception e) {
        }
    }

}
