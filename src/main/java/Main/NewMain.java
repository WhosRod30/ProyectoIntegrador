package Main;
import Modelo.ControlConsumo;
import DAO.ConsumoDao;
import java.sql.Date;
import java.util.Calendar;
public class NewMain {
    public static void main(String[] args) {
        ControlConsumo consumo = new ControlConsumo();
        
        // Set values for the ControlConsumo object
        consumo.setCantidad(22);
        consumo.setGranja(1);
        consumo.setNumeroGalpon(1);
        consumo.setTipoAlimento("Alimento A"); // Set the type of food
        consumo.setObservaciones("Observaciones sobre el consumo"); // Set any observations
        
        // Set the current date
        consumo.setFecha(new Date(Calendar.getInstance().getTimeInMillis()));
        
        // Save the consumption data
        new ConsumoDao().guardarConsumo(consumo);
    }
}
