package ar.com.jengibre.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import playn.core.Game;
import playn.core.Image;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.util.Clock;
import pythagoras.f.FloatMath;
import pythagoras.f.Point;

public class QueSabes extends Game.Default {

   private static final int UPDATE_RATE = 33;

   private Clock.Source clock = new Clock.Source(UPDATE_RATE);

   private Sector norte, sur, este, oeste;

   public static Image bgImage, bgIdle, hulk;

   public QueSabes() {
      super(UPDATE_RATE); // call update every 33ms (30 times per second)
   }

   @Override
   public void init() {
      graphics().rootLayer().removeAll();

      bgImage = assets().getImageSync("images/bg.png");
      bgIdle = assets().getImageSync("images/idle.png");
      hulk = assets().getImageSync("images/hulk.png");

      norte = new Sector();
      sur = new Sector();
      este = new Sector();
      oeste = new Sector();

      float gw = graphics().width();
      float gh = graphics().height();

      Layer.HasSize lnorte = norte.layer();
      lnorte.setRotation(FloatMath.PI);
      graphics().rootLayer().addAt(lnorte, gw, gh / 2);

      Layer.HasSize loeste = oeste.layer();
      loeste.setRotation(FloatMath.PI / 2);
      graphics().rootLayer().addAt(loeste, gw / 2, 0);

      Layer.HasSize lsur = sur.layer();
      graphics().rootLayer().addAt(lsur, 0, gh / 2);

      Layer.HasSize leste = este.layer();
      leste.setRotation(FloatMath.PI * 1.5F);
      graphics().rootLayer().addAt(leste, gw / 2, gh);

      // RELOAD HOOK
      keyboard().setListener(new Keyboard.Adapter() {
         @Override
         public void onKeyUp(Event event) {
            if (event.key() == Key.R) {
               System.out.println("reload!");
               init();
            }
         }
      });
      // RELOAD HOOK

      pointer().setListener(new Pointer.Adapter() {
         @Override
         public void onPointerEnd(Pointer.Event event) {
            float x = event.x();
            float y = event.y();
            Sector sector;

            if (x >= y) {
               if (Math.abs(x - graphics().width() / 2) >= Math.abs(y - graphics().height() / 2)) {
                  sector = este;
               }
               else {
                  sector = norte;
               }
            }
            else {
               if (Math.abs(x - graphics().width() / 2) >= Math.abs(y - graphics().height() / 2)) {
                  sector = oeste;
               }
               else {
                  sector = sur;
               }
            }

            Layer layer = sector.layer();
            Point tf = layer.transform().inverseTransform(new Point(x, y), new Point());
            sector.clicked(tf.x, tf.y);
         }
      });
   }

   @Override
   public void update(int delta) {
      clock.update(delta);

      norte.update(delta);
      sur.update(delta);
      este.update(delta);
      oeste.update(delta);
   }

   @Override
   public void paint(float alpha) {
      clock.paint(alpha);

      norte.paint(clock);
      sur.paint(clock);
      este.paint(clock);
      oeste.paint(clock);
   }
}
