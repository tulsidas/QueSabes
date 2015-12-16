package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.ImageLayer;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.StartupLatch;

public class EtapaEsperandoOtros extends AbstractEtapa {
   private ImageLayer fondo;

   private ImageLayer reloj;

   private int cuantos;

   public EtapaEsperandoOtros(Sector sector, int cuantos) {
      super(sector);

      this.cuantos = cuantos;

      fondo = graphics().createImageLayer();
      layer.add(fondo);

      reloj = graphics().createImageLayer();
      layer.addAt(reloj, 760, 200);
   }

   public void sumoseUno() {
      cuantos++;
   }

   public void timeout() {
      // nada
   }

   @Override
   public void doUpdate(int delta) {
      fondo.setImage(QueSabes.bgEsperando.get(cuantos - 1));

      int falta = StartupLatch.counter() / 1000;
      reloj.setImage(QueSabes.reloj.get(falta));
   }
}