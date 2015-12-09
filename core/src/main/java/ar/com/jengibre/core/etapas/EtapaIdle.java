package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.util.Clock;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaIdle extends AbstractEtapa {

   public EtapaIdle(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgIdle));
   }

   @Override
   public void update(int delta) {
      // if (StartupLatch.empezoElJuego()) {
      // sector.jugandoOtros();
      // }
   }

   @Override
   public void doPaint(Clock clock) {
      // nada que hacer
   }

   @Override
   public void onPointerEnd(float x, float y) {
      sector.empezarJuego();
      // sector.ruleta();
   }
}