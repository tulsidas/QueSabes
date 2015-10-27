package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import playn.core.Canvas;
import ar.com.jengibre.core.Sector;

public class JugandoOtros extends Etapa {
   public JugandoOtros(Sector sector) {
      super(sector);
   }

   @Override
   public void draw(Canvas c) {
      c.drawImage(bgIdle, 0, 0);
      c.drawText("Partido en progreso, esperar", 300, 200);
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}