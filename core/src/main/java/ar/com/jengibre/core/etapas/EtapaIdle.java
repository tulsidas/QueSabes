package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.util.Clock;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.StartupLatch;

public class EtapaIdle extends AbstractEtapa {

   public EtapaIdle(Sector sector) {
      super(sector);

      CanvasImage cImg = graphics().createImage(Sector.WIDTH, Sector.HEIGHT);
      cImg.canvas().drawImage(bgIdle, 0, 0);
      cImg.canvas().drawText("Tocá para empezar a jugar", 500, 200);

      layer.add(graphics().createImageLayer(cImg));
   }

   @Override
   public void update(int delta) {
      if (StartupLatch.empezoElJuego()) {
         sector.jugandoOtros();
      }
   }

   @Override
   public void doPaint(Clock clock) {
      // nada que hacer
   }

   @Override
   public void onPointerEnd(float x, float y) {
      sector.empezarJuego();
   }
}