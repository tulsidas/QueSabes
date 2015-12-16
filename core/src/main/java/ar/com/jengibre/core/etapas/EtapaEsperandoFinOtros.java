package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaEsperandoFinOtros extends AbstractEtapa {
   public EtapaEsperandoFinOtros(Sector sector) {
      super(sector);

      GroupLayer group = graphics().createGroupLayer();
      layer.add(group);

      anim.repeat(group).flipbook(group, QueSabes.bgEsperandoFin);
   }

   public void timeout() {
      // nada
   }
}