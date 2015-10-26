package ar.com.jengibre.core;

import playn.core.Layer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.Etapa;
import ar.com.jengibre.core.etapas.Idle;

public class Sector {

   private Etapa etapa;

   public Sector() {
      etapa = new Idle(this);
   }

   public Layer.HasSize layer() {
      // private CanvasImage cImg;
      // cImg = graphics().createImage(800, 400);
      // cImg.canvas().drawImage(bgImage, 0, 0);
      // cImg.canvas().setStrokeColor(Colors.RED);
      // cImg.canvas().setStrokeWidth(rnd.getInRange(2, 5));
      // cImg.canvas().drawLine(0, 0, cImg.width() / 2, cImg.height() / 2);
      // cImg.canvas().setStrokeColor(Colors.BLUE);
      // cImg.canvas().drawLine(cImg.width() / 2, cImg.height() / 2,
      // cImg.width(), 0);
      // cImg.canvas().setFillColor(Colors.YELLOW);
      // cImg.canvas().fillCircle(rnd.getInRange(0, 800), rnd.getInRange(0,
      // 400), 50);
      // cImg.canvas().drawImage(hulk, rnd.getInRange(0, 800), rnd.getInRange(0,
      // 400));
      // return graphics().createImageLayer(cImg);

      return etapa.layer();
   }

   public void update(int delta) {
      etapa.update(delta);
   }

   public void paint(Clock clock) {
      etapa.paint(clock);
   }
}