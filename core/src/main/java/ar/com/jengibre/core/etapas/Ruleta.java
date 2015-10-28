package ar.com.jengibre.core.etapas;

import playn.core.util.Clock;
import ar.com.jengibre.core.Sector;

public class Ruleta extends Etapa {

   public Ruleta(Sector sector) {
      super(sector);

      // c.drawImage(bgRuleta, 0, 0);
      // c.drawImage(p1.image(), p1.tx(), p1.ty());
      // p1 = graphics().createImageLayer(QueSabes.p1);
      // anim.tweenXY(p1).from(100, 100).to(300,
      // 100).in(3000).easeInOut().then().tweenXY(p1).to(100, 100)
      // .then().repeat(p1);
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}