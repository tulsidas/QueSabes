package ar.com.jengibre.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.Keyboard.Event;
import playn.core.util.Clock;
import pythagoras.f.FloatMath;

public class QueSabes extends Game.Default {

   private static final int UPDATE_RATE = 33;

   private Clock.Source clock = new Clock.Source(UPDATE_RATE);

   private Sector norte, sur, este, oeste;

   public QueSabes() {
      super(UPDATE_RATE); // call update every 33ms (30 times per second)
   }

   @Override
   public void init() {
      norte = new Sector();
      sur = new Sector();
      este = new Sector();
      oeste = new Sector();

      Image bgImage = assets().getImageSync("images/bg.png");
      Image hulk = assets().getImageSync("images/hulk.png");

      Layer.HasSize lnorte = norte.layer();
      lnorte.setOrigin(lnorte.width() / 2, lnorte.height());
      lnorte.setRotation(FloatMath.PI);
      graphics().rootLayer().addAt(lnorte, graphics().width() / 2, 0);

      Layer.HasSize lsur = sur.layer();
      lsur.setOrigin(lsur.width() / 2, lsur.height());
      graphics().rootLayer().addAt(lsur, graphics().width() / 2, graphics().height());

      // ImageLayer este = graphics().createImageLayer(img);
      // este.setOrigin(este.width() / 2, este.height());
      // este.setRotation(FloatMath.PI / 2);
      // graphics().rootLayer().addAt(este, 0, graphics().height() / 2);
      //
      // ImageLayer oeste = graphics().createImageLayer(img);
      // oeste.setOrigin(oeste.width() / 2, oeste.height());
      // oeste.setRotation(-FloatMath.PI / 2);
      // graphics().rootLayer().addAt(oeste, graphics().width(),
      // graphics().height() / 2);

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

   }

   @Override
   public void update(int delta) {
      clock.update(delta);

      norte.update(delta);
      sur.update(delta);
      // este.update(delta);
      // oeste.update(delta);
   }

   @Override
   public void paint(float alpha) {
      clock.paint(alpha);

      norte.paint(clock);
      sur.paint(clock);
      // este.paint(clock);
      // oeste.paint(clock);
   }
}
