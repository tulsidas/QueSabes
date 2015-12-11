package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;

import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import pythagoras.f.Circle;
import pythagoras.f.Point;
import pythagoras.f.Vector;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.util.ObservableFlip;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class EtapaBonus extends AbstractEtapa {

   private boolean tocoPelota = false;

   private boolean pateo = false;

   private ImageLayer arco, pelota;

   private GroupLayer arqueritoLayer, pelotaLayer;

   private float dx, dy; // la velocidad de la pelota

   private float pw2; // pelota.width / 2

   private ObservableFlip arcoFlip;

   // //////////////////////////////////////////////////////

   public EtapaBonus(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgArquerito));

      arqueritoLayer = graphics().createGroupLayer();
      layer.add(arqueritoLayer);

      arco = graphics().createImageLayer();
      arcoFlip = anim.repeat(arqueritoLayer).add(new ObservableFlip(arco, QueSabes.arquerito));
      arqueritoLayer.add(arco);

      pelota = graphics().createImageLayer(QueSabes.pelota0);
      pelota.setInteractive(true);
      pw2 = pelota.width() / 2;

      pelotaLayer = graphics().createGroupLayer();
      pelotaLayer.add(pelota);
      layer.addAt(pelotaLayer, 400, 400);

      ImageLayer bonus = graphics().createImageLayer(QueSabes.bonus);
      layer.addAt(bonus, 0, Sector.HEIGHT);
      anim.tweenY(bonus).to(0).in(400).easeOut().then().delay(400).then().tweenY(bonus).to(-Sector.HEIGHT)
            .in(400).easeIn();
   }

   @Override
   public void doPaint(Clock clock) {
   }

   private boolean checkCollision(Circle c) {
      float px = pelotaLayer.tx() + pw2;
      float py = pelotaLayer.ty() + pw2;

      // oc = arco
      Vector dc = new Vector(px - c.x, py - c.y);
      float dist2 = dc.x * dc.x + dc.y * dc.y;

      if (dist2 < (c.radius + pw2) * (c.radius + pw2)) {
         // Compute dot = dc.x*vx+dcy*vy and dot = dot/dist2
         float dot = dc.x * dx + dc.y * dy;
         dot = dot / dist2;

         // Update vx = vx - 2*dot*dc.x, vy = vy - 2*dot*dc.y
         dx = dx - 2 * dot * dc.x;
         dy = dy - 2 * dot * dc.y;

         pelotaLayer.setTranslation(pelotaLayer.tx() + dx, pelotaLayer.ty() + dy);

         return true;
      }
      else {
         return false;
      }
   }

   @Override
   public void doUpdate(int delta) {

      List<Circle> circles = Lists.newArrayList();
      int frame = arcoFlip.getFrame();
      // palo izq
      if (frame < 11) {
         circles.add(new Circle(328, 210, 20));
         circles.add(new Circle(338, 180, 20));
      }
      else {
         circles.add(new Circle(325, 210, 20));
         circles.add(new Circle(325, 180, 20));
      }

      // palo der
      if (frame < 11) {
         circles.add(new Circle(645, 180, 20));
         circles.add(new Circle(655, 210, 17));
      }
      else {
         circles.add(new Circle(638, 180, 20));
         circles.add(new Circle(645, 210, 15));
      }

      circles.addAll(arquero.get(frame));

      // arquero
      for (Circle cir : circles) {
         if (checkCollision(cir)) {
            QueSabes.palo.play();

            break; // una colisión a lo sumo
         }
      }

      if (dx != 0 && dy != 0) {
         pelotaLayer.setTranslation(pelotaLayer.tx() + dx, pelotaLayer.ty() + dy);

         float px = pelotaLayer.tx();
         float py = pelotaLayer.ty();

         // fue gol? se fue lejos?
         if (px < 0 || px > Sector.WIDTH || py > Sector.HEIGHT || py < 0) {
            fuera();
         }
         else if (py < 160 && px > 330 && px - pelota.width() < 630) {
            gol();
         }
      }
   }

   @Override
   public void timeout() {
      if (!pateo) {
         // XXX revisar que no esté cortando la jugada ni que se superponga
         fuera();
      }
   }

   @Override
   public void onPointerStart(float x, float y) {
      if (!pateo) {
         tocoPelota = pelota == layer.hitTest(new Point(x, y));
      }
   }

   private static final float MAX = 25;

   @Override
   public void onPointerEnd(float x, float y) {
      if (tocoPelota && !pateo) {
         dx = x - (pelotaLayer.tx() + pw2);
         dy = y - (pelotaLayer.ty() + pw2);

         // normalizo para que no viaje muy rápido la pelota
         if (Math.abs(dx) > MAX || Math.abs(dy) > MAX) {
            float factor = Math.max(Math.abs(dx), Math.abs(dy)) / MAX;

            dx /= factor;
            dy /= factor;
         }

         // ya no se puede volver a patear
         pelota.setInteractive(false);
         pateo = true;

         QueSabes.tuc.play();

         // la pelota ahora gira en direccion a donde fue pateada
         // float angulo = (float) Math.atan2(dy, dx);
         // pelota.setRotation(angulo);
         anim.repeat(pelotaLayer).flipbook(pelotaLayer, QueSabes.pelota);
      }
   }

   private void fuera() {
      dx = 0;
      dy = 0;

      QueSabes.nogol.play();

      arcoFlip.handle().cancel();

      ImageLayer pulgares = graphics().createImageLayer(QueSabes.pulgarAbajo);
      pulgares.setOrigin(pulgares.width() / 2, pulgares.height() / 2);
      layer.addAt(pulgares, Sector.WIDTH / 2, Sector.HEIGHT / 2);

      anim.tweenScale(pulgares).from(0).to(1).in(2000).easeOutElastic();
      anim.tweenRotation(pulgares).from(0).to(-0.5F).in(500).easeOut().then().tweenRotation(pulgares)
            .to(0.5F).in(500).easeOut().then().tweenRotation(pulgares).to(0).in(500).easeOut();

      anim.delay(3500).then().action(() -> sector.finArquerito(false));
   }

   private void gol() {
      dx = 0;
      dy = 0;

      if (rnd.getBoolean()) {
         QueSabes.gol1.play();
      }
      else {
         QueSabes.gol2.play();
      }

      arcoFlip.handle().cancel();

      ImageLayer gol = graphics().createImageLayer(QueSabes.gol);
      gol.setOrigin(gol.width() / 2, gol.height() / 2);
      layer.addAt(gol, Sector.WIDTH / 2, Sector.HEIGHT / 2);
      ImageLayer medalla = graphics().createImageLayer(QueSabes.medalla);
      medalla.setOrigin(medalla.width() / 2, medalla.height() / 2);

      anim.tweenScale(gol).from(0).to(0.8F).in(2000).easeOutElastic().then()
            .addAt(layer, medalla, Sector.WIDTH / 2, Sector.HEIGHT / 2).then().tweenScale(medalla).from(2)
            .to(0.6F).in(500).easeOutBack();

      // con medalla
      anim.delay(3500).then().action(() -> sector.finArquerito(true));
   }

   private final Map<Integer, List<Circle>> arquero = ImmutableMap
         .<Integer, List<Circle>> builder()
         .put(0,
               ImmutableList.of(new Circle(475, 176, 14), new Circle(473, 198, 13), new Circle(472, 220, 13)))
         .put(1,
               ImmutableList.of(new Circle(464, 176, 14), new Circle(460, 198, 13), new Circle(461, 220, 13)))
         .put(2,
               ImmutableList.of(new Circle(470, 187, 14), new Circle(452, 204, 13), new Circle(435, 220, 13)))
         .put(3,
               ImmutableList.of(new Circle(460, 177, 14), new Circle(440, 192, 13), new Circle(420, 205, 13)))
         .put(4,
               ImmutableList.of(new Circle(459, 168, 14), new Circle(437, 181, 13), new Circle(415, 192, 13)))
         .put(5,
               ImmutableList.of(new Circle(459, 153, 14), new Circle(434, 162, 13), new Circle(410, 168, 13)))
         .put(6,
               ImmutableList.of(new Circle(459, 168, 14), new Circle(437, 181, 13), new Circle(415, 192, 13)))
         .put(7,
               ImmutableList.of(new Circle(460, 177, 14), new Circle(440, 192, 13), new Circle(420, 205, 13)))
         .put(8,
               ImmutableList.of(new Circle(470, 187, 14), new Circle(452, 204, 13), new Circle(435, 220, 13)))
         .put(9,
               ImmutableList.of(new Circle(464, 176, 14), new Circle(460, 198, 13), new Circle(461, 220, 13)))
         .put(10,
               ImmutableList.of(new Circle(475, 176, 14), new Circle(473, 198, 13), new Circle(472, 220, 13)))
         .put(11,
               ImmutableList.of(new Circle(500, 159, 14), new Circle(499, 184, 13), new Circle(498, 208, 12)))
         .put(12,
               ImmutableList.of(new Circle(515, 160, 14), new Circle(520, 186, 13), new Circle(530, 208, 12)))
         .put(13,
               ImmutableList.of(new Circle(522, 163, 14), new Circle(531, 188, 13), new Circle(547, 208, 12)))
         .put(14,
               ImmutableList.of(new Circle(530, 165, 14), new Circle(545, 190, 13), new Circle(565, 208, 12)))
         .put(15,
               ImmutableList.of(new Circle(548, 173, 14), new Circle(565, 195, 13), new Circle(586, 208, 12)))
         .put(16,
               ImmutableList.of(new Circle(550, 173, 14), new Circle(570, 195, 13), new Circle(595, 208, 12)))
         .put(17,
               ImmutableList.of(new Circle(555, 173, 15), new Circle(575, 195, 13), new Circle(600, 208, 12)))
         .put(18,
               ImmutableList.of(new Circle(550, 173, 14), new Circle(570, 195, 13), new Circle(595, 208, 12)))
         .put(19,
               ImmutableList.of(new Circle(548, 173, 14), new Circle(565, 195, 13), new Circle(586, 208, 12)))
         .put(20,
               ImmutableList.of(new Circle(530, 165, 14), new Circle(545, 190, 13), new Circle(565, 208, 12)))
         .put(21,
               ImmutableList.of(new Circle(522, 163, 14), new Circle(531, 188, 13), new Circle(547, 208, 12)))
         .put(22,
               ImmutableList.of(new Circle(515, 160, 14), new Circle(520, 186, 13), new Circle(530, 208, 12)))
         .build();
}