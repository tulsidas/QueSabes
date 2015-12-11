package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.ImageLayer;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaEsperandoOtros extends AbstractEtapa {
   private ImageLayer il;

   private int cuantos;

   public EtapaEsperandoOtros(Sector sector, int cuantos) {
      super(sector);

      this.cuantos = cuantos;

      il = graphics().createImageLayer();
      layer.add(il);

      updateImage();
   }

   public void sumoseUno() {
      cuantos++;
      updateImage();
   }

   public void timeout() {
      // nada
   }

   @Override
   public void doUpdate(int delta) {
      // TODO actualizar y mostrar cuenta regresiva
   }

   private void updateImage() {
      il.setImage(QueSabes.esperando.get(cuantos - 1));
   }
}