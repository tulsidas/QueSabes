package ar.com.jengibre.core.etapas;

import java.util.Random;

import playn.core.GroupLayer;
import playn.core.util.Clock;
import tripleplay.anim.Animator;
import tripleplay.util.Randoms;
import ar.com.jengibre.core.Sector;

// Idle -> EsperandoOtros -> [Ruleta -> Pregunta -> Bonus]* -> Fin
// Idle -> EsperandoOtros -> JugandoOtros

public abstract class AbstractEtapa {
   protected Sector sector;

   protected Animator anim;

   protected Randoms rnd = Randoms.with(new Random());

   protected GroupLayer layer;

   public AbstractEtapa(Sector sector) {
      this.sector = sector;
      this.layer = sector.layer();
      layer.removeAll(); // TODO chequear

      anim = new Animator();
   }

   public abstract void update(int delta);

   public void paint(Clock clock) {
      anim.paint(clock);
      
      doPaint(clock);
   }

   public abstract void doPaint(Clock clock);

   public void onPointerStart(float x, float y) {
      // por default NOOP
   }

   public void onPointerEnd(float x, float y) {
      // por default NOOP
   }
}