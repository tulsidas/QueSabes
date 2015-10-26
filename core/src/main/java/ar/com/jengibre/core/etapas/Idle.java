package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgIdle;
import static ar.com.jengibre.core.QueSabes.hulk;
import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.util.Clock;
import ar.com.jengibre.core.Sector;

public class Idle extends Etapa {

   private boolean repaint = false;

   private long t;

   public Idle(Sector sector) {
      super(sector);
   }

   @Override
   public Image draw() {
      CanvasImage cImg = graphics().createImage(800, 400);
      Canvas c = cImg.canvas();

      c.drawImage(bgIdle, 0, 0);
      // c.setStrokeColor(Colors.RED);
      // c.setStrokeWidth(rnd.getInRange(2, 5));
      // c.drawLine(0, 0, cImg.width() / 2, cImg.height() / 2);
      // c.setStrokeColor(Colors.BLUE);
      // c.drawLine(cImg.width() / 2, cImg.height() / 2, cImg.width(), 0);
      // c.setFillColor(Colors.YELLOW);
      // c.fillCircle(rnd.getInRange(0, 800), rnd.getInRange(0, 400), 50);
      c.drawImage(hulk, rnd.getInRange(0, 800), rnd.getInRange(0, 400));

      return cImg;
   }

   @Override
   public void update(int delta) {
      // t += delta;
      // if (t > 10_000) {
      // t = 0;
      // repaint = true;
      // }
   }

   @Override
   public void doPaint(Clock clock) {
      if (repaint) {
         repaint();
         repaint = false;
      }
   }

   @Override
   public void clicked(float x, float y) {
      repaint = true;
   }
}