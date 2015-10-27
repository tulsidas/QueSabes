package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import static ar.com.jengibre.core.QueSabes.btnEmpezar;
import playn.core.Canvas;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.StartupLatch;

public class Idle extends Etapa {

   public Idle(Sector sector) {
      super(sector);

      // TODO animar btnEmpezar y/o ponerle onda
   }

   @Override
   public void draw(Canvas c) {
      c.drawImage(bgIdle, 0, 0);
      c.drawImageCentered(btnEmpezar, 400, 200);

      /*
      if (listo) {
         if (StartupLatch.empezoElJuego()) {
            // sector.empezarJuego();
         }
         else {
            c.drawImageCentered(btnEsperando, 400, 200);
            c.drawText("" + StartupLatch.segundosHastaElComienzo(), 300, 300);
         }
      }
      else {
         if (StartupLatch.empezoElJuego()) {
            // TODO mostrar fondo de "juego en progreso"
         }
         else {
            c.drawImageCentered(btnEmpezar, 400, 200);
         }
      }
      */
   }

   @Override
   public void update(int delta) {
      if (StartupLatch.empezoElJuego()) {
         sector.jugandoOtros();
      }
   }

   @Override
   public void clicked(float x, float y) {
      sector.empezarJuego();
   }
}