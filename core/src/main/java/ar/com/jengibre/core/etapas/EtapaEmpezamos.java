package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaEmpezamos extends AbstractEtapa {

   public EtapaEmpezamos(Sector sector) {
      super(sector);

      GroupLayer group = graphics().createGroupLayer();
      layer.add(group);

      anim.flipbook(group, QueSabes.empezamos).then().action(() -> sector.ruleta());
   }

   @Override
   public void timeout() {
      // nada
   }
}