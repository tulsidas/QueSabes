package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import static pythagoras.f.FloatMath.PI;

import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import ar.com.jengibre.core.Personaje;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EtapaRuleta extends AbstractEtapa {

   Map<Personaje, ImageLayer> map = Maps.newHashMap();

   GroupLayer flipbookGroup; // aca va el flipbook

   Personaje elegido = null;

   final int DELAY = 350;

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
   }

   @Override
   public void timeout() {
      onPointerEnd(0, 0);
   }

   @Override
   public void onPointerEnd(float x, float y) {
      if (elegido == null) {
         // obtengo de los que NO salieron aún para el sector
         elegido = rnd.pick(map.keySet().stream().filter(p -> !sector.personajesQueSalieron().contains(p))
               .iterator(), null);

         // lo agrego al sector para que no vuelva a salir en otra ronda
         sector.salioPersonaje(elegido);

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

         anim.play(elegido.soundSaluda());
         anim.destroy(map.get(elegido)).then().add(layer, flipbookGroup).then()
               .flipbook(flipbookGroup, elegido.fbSaluda()).then().delay(500).then().action(() -> {
                  sector.pregunta(elegido);
               });
      }
   }
}