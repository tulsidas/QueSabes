package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.ImageLayer;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.util._Action;

public class EtapaIdle extends AbstractEtapa {

   public EtapaIdle(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgIdle));

      ImageLayer boton = graphics().createImageLayer(QueSabes.botonUp);
      layer.addAt(boton, 20, 200);

      anim.repeat(boton).delay(500).then().add(new _Action(() -> boton.setImage(QueSabes.botonDn))).then()
            .delay(500).then().add(new _Action(() -> boton.setImage(QueSabes.botonUp))).then().delay(1500)
            .then().shake(boton);
   }

   @Override
   public void timeout() {
      // nada
   }

   @Override
   public void onPointerEnd(float x, float y) {
      sector.empezarJuego();
   }
}