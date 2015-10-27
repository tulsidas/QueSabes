package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgRuleta;
import playn.core.Canvas;
import ar.com.jengibre.core.Sector;

public class Ruleta extends Etapa {
   public Ruleta(Sector sector) {
      super(sector);
   }

   @Override
   public void draw(Canvas c) {
      c.drawImage(bgRuleta, 0, 0);
      c.drawText("RULETA", 300, 200);
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}