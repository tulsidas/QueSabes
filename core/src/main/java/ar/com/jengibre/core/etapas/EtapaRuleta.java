package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import static pythagoras.f.FloatMath.PI;

import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.Personaje;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EtapaRuleta extends AbstractEtapa {

   static final int TIMEOUT = 10_000;

   Map<Personaje, ImageLayer> map = Maps.newHashMap();

   GroupLayer flipbookGroup; // aca va el flipbook

   Personaje elegido = null;

   boolean timeout = false;

   final int DELAY = 250;

   public EtapaRuleta(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgRuleta));

      flipbookGroup = graphics().createGroupLayer();

      List<ImageLayer> images = Lists.newArrayList();
      for (Personaje pje : QueSabes.personajes) {
         ImageLayer img = graphics().createImageLayer(pje.imgRuleta());
         images.add(img);

         map.put(pje, img);
      }

      final float ANGLE = PI * 2 / images.size();
      for (int i = 0; i < images.size(); i++) {
         ImageLayer img = images.get(i);

         img.setTranslation(img.width() / 2, -img.height());
         img.setOrigin(img.width() / 2, -img.height());
         img.setRotation(ANGLE * i);
         layer.add(img);

         anim.repeat(img).tweenRotation(img).to(2 * PI + img.rotation()).in(DELAY * images.size());
      }

      // timeout que avanza el juego por si abandonan o tardan mucho
      // anim.delay(TIMEOUT).then().action(() -> timeout = true);
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
      if (elegido == null) {
         // elegido = rnd.pick(map.keySet(), null);
         elegido = map.keySet().stream().filter((p) -> p.saluda() != null).findFirst().get();

         anim.clear();

         // el elegido tiene que terminar en 0
         // el resto donde corresponda
         float rotation = map.get(elegido).rotation();

         float hasta2PI = rotation > 2 * PI ? 4 * PI - rotation : 2 * PI - rotation;

         // 2PI -> DELAY * personajes.size()
         float duration = hasta2PI * DELAY * map.size() / (2 * PI);

         for (Map.Entry<Personaje, ImageLayer> entry : map.entrySet()) {
            Personaje p = entry.getKey();
            ImageLayer img = entry.getValue();

            anim.tweenRotation(img).to(img.rotation() + hasta2PI).in(duration).easeOutBack();

            if (p != elegido) {
               anim.tweenAlpha(img).to(0).in(duration).easeOut().then().destroy(img);
            }

         }

         anim.addBarrier(500);

         anim.play(QueSabes.saluda);
         anim.destroy(map.get(elegido)).then().add(layer, flipbookGroup).then()
               .flipbook(flipbookGroup, elegido.saluda()).then().delay(500).then().action(() -> {
                  // destruyo todo lo agregado en esta etapa
                     layer.destroyAll();

                     sector.pregunta(elegido);
                  });
      }
   }
}