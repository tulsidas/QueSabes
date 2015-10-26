package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import static ar.com.jengibre.core.QueSabes.hulk;
import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import ar.com.jengibre.core.Sector;

public class Idle extends Etapa {

   private float hulkx, hulky;

   private int t = 0;

   private int segundos = 0;

   public Idle(Sector sector) {
      super(sector);
   }

   @Override
   public Image draw() {
      CanvasImage cImg = graphics().createImage(800, 400);
      Canvas c = cImg.canvas();

      c.drawImage(bgIdle, 0, 0);
      c.drawImage(hulk, hulkx, hulky);
      c.drawText("" + segundos, 300, 300);

      return cImg;
   }

   @Override
   public void update(int delta) {
      t += delta;
      if (t > 1000) {
         segundos++;
         t = 0;
      }
   }

   @Override
   public void clicked(float x, float y) {
      hulkx = rnd.getInRange(0, 800);
      hulky = rnd.getInRange(0, 400);
   }
}