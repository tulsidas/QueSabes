package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.Personaje;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaTest extends AbstractEtapa {

   public EtapaTest(Sector sector) {
      super(sector);

      Personaje personaje = QueSabes.personajes.stream().filter((p) -> p.saluda() != null).findFirst().get();

      GroupLayer box = graphics().createGroupLayer();
      layer.addAt(box, 0, 0);
      anim.delay(1000).then().repeat(box)
            .flipbook(box, personaje.saluda());
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
   }
}