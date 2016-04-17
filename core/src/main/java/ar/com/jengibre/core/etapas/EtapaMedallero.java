package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.ImageLayer;
import tripleplay.anim.AnimBuilder;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaMedallero extends AbstractEtapa {
   public EtapaMedallero(Sector sector, int medallas) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgMedallero));

      ImageLayer medalla = graphics().createImageLayer(QueSabes.medalla);
      layer.addAt(medalla, 120, 160);

      ImageLayer cuantas = graphics().createImageLayer(QueSabes.numMedallas.get(0));
      cuantas.setOrigin(cuantas.image().width() / 2, cuantas.image().height() / 2);

      AnimBuilder animb = anim.tweenScale(medalla).from(2).to(0.8f).in(400).easeOut().then().delay(300)
            .then().addAt(layer, cuantas, 570, 210).then();

      // valores iniciales
      int shake = 300;
      int delay = 200;

      for (int i = 1; i <= medallas; i++) {
         final int ufa = i - 1;
         animb = animb.action(() -> cuantas.setImage(QueSabes.numMedallas.get(ufa)))
               .then().play(QueSabes.sndMedalla)
               .then().shake(cuantas).in(Math.max(shake, 100)).then().delay(Math.max(delay, 50)).then();

         shake -= 30;
         delay -= 20;
      }

      animb.delay(500).then().addAt(layer, graphics().createImageLayer(QueSabes.gracias), 375, 280).then()
            .delay(5000).then().action(() -> sector.reset());
   }

   public void timeout() {
      // nada, la animacion se encarga de resetear
   }
}
