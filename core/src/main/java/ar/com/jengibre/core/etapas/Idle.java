package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import static ar.com.jengibre.core.QueSabes.btnEmpezar;
import static ar.com.jengibre.core.QueSabes.btnEsperando;
import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Path;
import tripleplay.util.Colors;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.StartupLatch;

public class Idle extends Etapa {

   private boolean listo;

   public Idle(Sector sector) {
      super(sector);

      // TODO animar btnEmpezar y/o ponerle onda
   }

   @Override
   public Image draw() {
      CanvasImage cImg = graphics().createImage(800, 400);
      Canvas c = cImg.canvas();

      c.drawImage(bgIdle, 0, 0);
      if (listo) {
         if (StartupLatch.empezoElJuego()) {
            sector.empezarJuego();
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

      return cImg;
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
      if (!listo) {
         listo = true;

         StartupLatch.sectorListoParaEmpezar();
      }
   }
}