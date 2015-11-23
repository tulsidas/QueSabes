package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import pythagoras.f.Point;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaBonus extends AbstractEtapa {

   private ImageLayer pelota;

   private boolean tocoPelota = false;

   private float dx, dy; // la velocidad de la pelota

   public EtapaBonus(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgRuleta));

      pelota = graphics().createImageLayer(QueSabes.pelota);
      pelota.setInteractive(true);

      layer.add(graphics().createImageLayer(QueSabes.bgBonus));
      layer.addAt(pelota, 500, 400);

      // timeout que avanza el juego por si abandonan o tardan mucho
      // anim.delay(TIMEOUT).then().action(() -> timeout = true);
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
      pelota.setTranslation(pelota.tx() + dx, pelota.ty() + dy);

      // fue gol? se fue lejos?
      if (pelota.ty() < 0) {
         // TODO animar, etc
         sector.ruleta();
      }
   }

   @Override
   public void onPointerStart(float x, float y) {
      if (!tocoPelota) {
         tocoPelota = pelota == layer.hitTest(new Point(x, y));
      }
   }

   private static final float MAX = 25;

   @Override
   public void onPointerEnd(float x, float y) {
      if (tocoPelota) {
         dx = x - (pelota.tx() + pelota.width() / 2);
         dy = y - (pelota.ty() + pelota.height() / 2);

         // normalizo para que no viaje muy rápido la pelota
         if (Math.abs(dx) > MAX || Math.abs(dy) > MAX) {
            float factor = Math.max(Math.abs(dx), Math.abs(dy)) / MAX;

            dx /= factor;
            dy /= factor;
         }

         // ya no se puede volver a patear
         tocoPelota = false;
         pelota.setInteractive(false);
      }
   }
}