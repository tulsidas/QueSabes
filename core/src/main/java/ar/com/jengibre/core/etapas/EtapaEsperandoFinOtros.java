package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaEsperandoFinOtros extends AbstractEtapa {
   public EtapaEsperandoFinOtros(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgEsperandoFin));
   }

   public void timeout() {
      // nada
   }
}