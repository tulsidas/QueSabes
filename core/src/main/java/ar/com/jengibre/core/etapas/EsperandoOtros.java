package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import static ar.com.jengibre.core.QueSabes.btnEsperando;
import playn.core.Canvas;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.StartupLatch;

public class EsperandoOtros extends Etapa {
   public EsperandoOtros(Sector sector) {
      super(sector);
   }

   @Override
   public void draw(Canvas c) {
      c.drawImage(bgIdle, 0, 0);

      c.drawImageCentered(btnEsperando, 400, 200);
      c.drawText("" + StartupLatch.segundosHastaElComienzo(), 300, 300);
   }

   @Override
   public void update(int delta) {
      if (StartupLatch.empezoElJuego()) {
         sector.empezoJuego();
      }
   }

   @Override
   public void clicked(float x, float y) {
   }
}