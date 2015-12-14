package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.StartupLatch;

public class EtapaEsperandoOtros extends AbstractEtapa {
   private ImageLayer il;

   private CanvasImage img;

   private int cuantos;

   public EtapaEsperandoOtros(Sector sector, int cuantos) {
      super(sector);

      this.cuantos = cuantos;

      img = graphics().createImage(Sector.WIDTH, Sector.HEIGHT);

      il = graphics().createImageLayer(img);
      layer.add(il);
   }

   public void sumoseUno() {
      cuantos++;
   }

   public void timeout() {
      // nada
   }

   @Override
   public void doUpdate(int delta) {
      int falta = StartupLatch.counter();

      Canvas c = img.canvas();
      c.clear();
      c.drawImage(QueSabes.bgEsperando.get(cuantos - 1), 0, 0);
      c.drawText("" + falta, 100, 100);
      // FIXME cambiar por imagenes con numeritos
   }
}