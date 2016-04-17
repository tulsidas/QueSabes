package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;

import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import ar.com.jengibre.core.Personaje;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.util._Action;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EtapaRuleta extends AbstractEtapa {

   Map<Personaje, ImageLayer> map = Maps.newHashMap();

   GroupLayer flipbookGroup; // aca va el flipbook

   Personaje elegido = null;

   public EtapaRuleta(Sector sector) {
      super(sector);

      ImageLayer fondo = graphics().createImageLayer(QueSabes.bgRuleta);
      fondo.setDepth(Short.MIN_VALUE);
      layer.add(fondo);

      flipbookGroup = graphics().createGroupLayer();

      final int TIME = 500;
      List<ImageLayer> images = Lists.newArrayList();
      int numPersonajes = QueSabes.personajes.size();
      for (int i = 0; i < numPersonajes; i++) {
         Personaje pje = QueSabes.personajes.get(i);

         Image color = pje.imgRuleta();
         Image bn = pje.imgRuletaBN();

         ImageLayer img = graphics().createImageLayer(color);
         img.setOrigin(img.width() / 2, img.height() / 2);

         map.put(pje, img);
         images.add(img);

         // offscreen para que no moleste
         layer.addAt(img, -Sector.WIDTH, Sector.HEIGHT / 2);

         anim.delay(i * TIME).then().repeat(img).tweenScale(img).from(0.4F).to(1).in(TIME).easeOut().then()
               .tweenScale(img).to(0.4F).in(TIME).easeIn().then().delay((numPersonajes - 2) * TIME);

         anim.delay(i * TIME).then().repeat(img).add(new _Action(() -> {
            img.setDepth(50);
            img.setImage(color);
         })).then().tweenX(img).from(Sector.WIDTH).to(Sector.WIDTH / 2).in(TIME).then().tweenX(img).to(0)
               .in(TIME).then()
               // viaja por atrás
               .add(new _Action(() -> {
                  img.setDepth(0);
                  img.setImage(bn);
               })).then().tweenX(img).to(Sector.WIDTH).in((numPersonajes - 2) * TIME);

         anim.delay(i * TIME).then().repeat(img).tweenAlpha(img).from(0.5F).to(1).in(TIME).easeOut().then()
               .tweenAlpha(img).to(0.5F).in(TIME).easeIn().then().delay((numPersonajes - 2) * TIME);
      }
   }

   @Override
   public void timeout() {
      touchEnd(0, 0);
   }

   @Override
   public void touchEnd(float x, float y) {
      if (elegido == null) {
         QueSabes.tic.play();

         // obtengo de los que NO salieron aún para el sector
         elegido = rnd.pick(map.keySet().stream().filter(p -> !sector.personajesQueSalieron().contains(p))
               .iterator(), null);

         // lo agrego al sector para que no vuelva a salir en otra ronda
         sector.salioPersonaje(elegido);

         anim.clear();

         final int TIME_OUT = 3000;

         for (Map.Entry<Personaje, ImageLayer> entry : map.entrySet()) {
            ImageLayer img = entry.getValue();

            if (entry.getKey() == elegido) {
               // pongo la color
               img.setImage(elegido.imgRuleta());

               anim.tweenAlpha(img).to(1).in(TIME_OUT).easeOut();
               anim.delay(TIME_OUT / 2).then().tweenScale(img).to(1).in(TIME_OUT).easeOutElastic();
               anim.delay(TIME_OUT / 2).then().tweenX(img).to(Sector.WIDTH / 2).easeOutElastic().in(TIME_OUT);
            }
            else {
               anim.tweenAlpha(img).to(0).in(TIME_OUT / 2).easeOut();
               anim.delay(TIME_OUT).then().destroy(img);
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
