package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import pythagoras.f.Point;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaBonus extends AbstractEtapa {

   private boolean tocoPelota = false;

   private ImageLayer arco, pelota;

   private GroupLayer arqueritoLayer, pelotaLayer;

   private float dx, dy; // la velocidad de la pelota

   public EtapaBonus(Sector sector) {
      super(sector);

      layer.add(graphics().createImageLayer(QueSabes.bgArquerito));

      arqueritoLayer = graphics().createGroupLayer();
      layer.add(arqueritoLayer);
      // custom Flip
      arco = graphics().createImageLayer();
      arqueritoLayer.add(arco);
      anim.repeat(arqueritoLayer).flipbook(arqueritoLayer, QueSabes.arquerito); // add(new ObservableFlip(image, QueSabes.arquerito));
      // custom Flip

      pelota = graphics().createImageLayer(QueSabes.pelota0);
      pelota.setInteractive(true);
      layer.addAt(pelota, 400, 400);

      pelotaLayer = graphics().createGroupLayer();
      layer.addAt(pelotaLayer, 400, 400);

      // timeout que avanza el juego por si abandonan o tardan mucho
      // anim.delay(TIMEOUT).then().action(() -> timeout = true);
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
      pelotaLayer.setTranslation(pelotaLayer.tx() + dx, pelotaLayer.ty() + dy);

      // fue gol? se fue lejos?
      if (pelotaLayer.tx() < 0 || pelotaLayer.tx() > Sector.WIDTH || pelotaLayer.ty() > Sector.HEIGHT) {
         System.out.println("se fue al cazzo");
         layer.destroyAll();
      }
      // else if (pelotaLayer.ty() < 0) {
      // System.out.println("¿gol?");
      // layer.destroyAll();
      // }
      else if (pelotaLayer.ty() < 220) {
         dx = 0;
         dy = 0;
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

         // angulo = (float) Math.atan2(dy, dx);

         // ya no se puede volver a patear
         tocoPelota = false;
         pelota.destroy();

         // la pelota ahora gira en direccion a donde fue pateada
         // pelotaLayer.setRotation(angulo);
         anim.repeat(pelotaLayer).flipbook(pelotaLayer, QueSabes.pelota);
      }
   }
}