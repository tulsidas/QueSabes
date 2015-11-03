package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import static pythagoras.f.FloatMath.PI;

import java.util.ArrayList;
import java.util.List;

import playn.core.ImageLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaRuleta extends AbstractEtapa {

   static final int TIMEOUT = 10_000;

   List<ImageLayer> personajes;

   int elegido = -1;

   boolean timeout = false;

   public EtapaRuleta(Sector sector) {
      super(sector);

      personajes = new ArrayList<>();
      personajes.add(graphics().createImageLayer(QueSabes.p1));
      personajes.add(graphics().createImageLayer(QueSabes.p2));
      personajes.add(graphics().createImageLayer(QueSabes.p3));
      personajes.add(graphics().createImageLayer(QueSabes.p4));
      personajes.add(graphics().createImageLayer(QueSabes.p5));
      personajes.add(graphics().createImageLayer(QueSabes.p6));
      personajes.add(graphics().createImageLayer(QueSabes.p7));
      personajes.add(graphics().createImageLayer(QueSabes.p8));
      personajes.add(graphics().createImageLayer(QueSabes.p9));
      personajes.add(graphics().createImageLayer(QueSabes.p10));
      personajes.add(graphics().createImageLayer(QueSabes.p11));
      personajes.add(graphics().createImageLayer(QueSabes.p12));

      final int DELAY = 250;

      layer.add(graphics().createImageLayer(QueSabes.bgRuleta));
      for (int i = 0; i < personajes.size(); i++) {
         ImageLayer p = personajes.get(i);

         p.setTranslation(550, 550);
         p.setOrigin(p.width() / 2, -p.height());
         layer.add(p);

         anim.delay(i * DELAY).then().repeat(p).tweenRotation(p).to(2 * PI + p.rotation())
               .in(DELAY * personajes.size());
      }

      // timeout que avanza el juego por si abandonan o tardan mucho
      anim.delay(TIMEOUT).then().action(() -> timeout = true);
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
      if (timeout) {
         onPointerEnd(0, 0);
         timeout = false;
      }
   }

   @Override
   public void onPointerEnd(float x, float y) {
      if (elegido == -1) {
         elegido = rnd.getInRange(0, personajes.size());

         anim.clear();

         // el elegido tiene que terminar en PI
         // el resto donde corresponda
         float rotation = personajes.get(elegido).rotation();
         float hastaPi = rotation < PI ? PI - rotation : 3 * PI - rotation;

         float cantPersonajes = personajes.size();
         float duration = 2400;

         for (int i = 0; i < cantPersonajes; i++) {
            ImageLayer p = personajes.get(i);

            anim.tweenRotation(p).to(p.rotation() + hastaPi).in(duration).easeOutBack();

            if (i != elegido) {
               anim.tweenAlpha(p).to(0).in(duration).easeOut();
            }
         }

         // FIXME animar el elegido, etc

         anim.delay(duration + 1000).then().action(() -> sector.pregunta(elegido));
      }
   }
}