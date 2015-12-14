package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaMedallero extends AbstractEtapa {
   public EtapaMedallero(Sector sector, int medallas) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgMedallero));
   }

   public void timeout() {
      // volvemos a empezar
      sector.reset();
   }
}