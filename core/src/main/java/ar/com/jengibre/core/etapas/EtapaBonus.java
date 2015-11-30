package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;

import java.util.List;
import java.util.Map;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import pythagoras.f.Circle;
import pythagoras.f.Point;
import pythagoras.f.Vector;
import pythagoras.f.Vectors;
import tripleplay.util.Colors;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;
import ar.com.jengibre.core.util.ObservableFlip;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class EtapaBonus extends AbstractEtapa {

   private boolean tocoPelota = false;

   private ImageLayer arco, pelota;

   private GroupLayer arqueritoLayer, pelotaLayer;

   private float dx, dy; // la velocidad de la pelota

   private float pw2; // pelota.width / 2

   private ObservableFlip arcoFlip;

   // //////////////////////////////////////////////////////

   private CanvasImage bb;

   public EtapaBonus(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgArquerito));

      arqueritoLayer = graphics().createGroupLayer();
      layer.add(arqueritoLayer);

      arco = graphics().createImageLayer();
      // QueSabes.arquerito.frames.apply(17, arco);
      arcoFlip = anim.repeat(arqueritoLayer).add(new ObservableFlip(arco, QueSabes.arquerito));
      arqueritoLayer.add(arco);

      pelota = graphics().createImageLayer(QueSabes.pelota0);
      pelota.setInteractive(true);
      pw2 = pelota.width() / 2;

      pelotaLayer = graphics().createGroupLayer();
      pelotaLayer.add(pelota);
      layer.addAt(pelotaLayer, 400, 400);

      // timeout que avanza el juego por si abandonan o tardan mucho
      // anim.delay(TIMEOUT).then().action(() -> timeout = true);
      bb = graphics().createImage(Sector.WIDTH, Sector.HEIGHT);
      layer.add(graphics().createImageLayer(bb));
   }

   @Override
   public void doPaint(Clock clock) {
      Canvas c = bb.canvas();
      c.clear();

      c.setStrokeWidth(2);

      List<Circle> circles = Lists.newArrayList();
      int frame = arcoFlip.getFrame();
      c.setFillColor(Colors.YELLOW);
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
         c.fillCircle(cir.x, cir.y, cir.radius);

         if (checkCollision(cir)) {
            break;
         }
      }

      // pelota
      // c.setFillColor(Colors.GREEN);
      // c.fillCircle(pelotaLayer.tx() + pw2, pelotaLayer.ty() + pw2, pw2);
   }

   private boolean checkCollision(Circle c) {
      float px = pelotaLayer.tx() + pw2;
      float py = pelotaLayer.ty() + pw2;

      double distance = ((c.x - px) * (c.x - px)) + ((c.y - py) * (c.y - py));
      if (distance < (c.radius + pw2) * (c.radius + pw2)) {
         // balls have collided

         float collisionPointX = ((c.x * pw2) + (px * c.radius)) / (c.radius + pw2);
         float collisionPointY = ((c.y * pw2) + (py * c.radius)) / (c.radius + pw2);

         bb.canvas().setFillColor(Colors.BLACK);
         bb.canvas().fillCircle(collisionPointX, collisionPointY, 3);

         float cmass = 5;
         float pmass = 1;
         float cspeedx = 1;
         float cspeedy = 1;

         float newVelX2 = (dx * (pmass - cmass) + (2 * cmass * cspeedx)) / (cmass + pmass);
         float newVelY2 = (dy * (pmass - cmass) + (2 * cmass * cspeedy)) / (cmass + pmass);

         dx = newVelX2;
         dy = newVelY2;

         pelotaLayer.setTranslation(pelotaLayer.tx() + dx, pelotaLayer.ty() + dy);

         return true;
      }
      else {
         return false;
      }
   }

   @Override
   public void update(int delta) {
      if (dx != 0 && dy != 0) {
         pelotaLayer.setTranslation(pelotaLayer.tx() + dx, pelotaLayer.ty() + dy);

         // if (bbCollision()) {
         // System.out.println("colisión!");
         // dx = 0;
         // dy = 0;
         // }

         /*
         // fue gol? se fue lejos?
         if (pelotaLayer.tx() < 0 || pelotaLayer.tx() > Sector.WIDTH || pelotaLayer.ty() > Sector.HEIGHT) {
            sector.ruleta();
         }
         // else if (pelotaLayer.ty() < 0) {
         // System.out.println("¿gol?");
         // }
         else if (pelotaLayer.ty() < 220) {
            dx = 0;
            dy = 0;
            // sector.ruleta();
            sector.arquerito();
         }
         */

         // y entre 220 y 160 -> gol
         // x entre 300 y 600 -> gol

         // if (pelotaLayer.ty() < 160) {
         // dx = 0;
         // dy = 0;
         // }
         // else if (pelotaLayer.tx() < -pelota.width() || pelotaLayer.tx() >
         // Sector.WIDTH) {
         // dx = 0;
         // dy = 0;
         // }
      }
   }

   @Override
   public void onPointerStart(float x, float y) {
      if (!tocoPelota) {
         tocoPelota = pelota == layer.hitTest(new Point(x, y));
      }
   }

   private static final float MAX = 25 - 20; // FIXME

   @Override
   public void onPointerEnd(float x, float y) {
      if (tocoPelota) {
         dx = x - (pelotaLayer.tx() + pw2);
         dy = y - (pelotaLayer.ty() + pw2);

         // normalizo para que no viaje muy rápido la pelota
         if (Math.abs(dx) > MAX || Math.abs(dy) > MAX) {
            float factor = Math.max(Math.abs(dx), Math.abs(dy)) / MAX;

            dx /= factor;
            dy /= factor;
         }

         // ya no se puede volver a patear
         tocoPelota = false;
         // pelota.destroy();

         // la pelota ahora gira en direccion a donde fue pateada
         // float angulo = (float) Math.atan2(dy, dx);
         // pelota.setRotation(angulo);
         anim.repeat(pelotaLayer).flipbook(pelotaLayer, QueSabes.pelota);
      }
   }

   Map<Integer, List<Circle>> arquero = ImmutableMap
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