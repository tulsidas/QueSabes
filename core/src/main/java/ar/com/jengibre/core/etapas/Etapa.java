package ar.com.jengibre.core.etapas;

import java.util.Random;

import playn.core.Layer;
import playn.core.util.Clock;
import tripleplay.anim.Animator;
import tripleplay.util.Randoms;
import ar.com.jengibre.core.Sector;

// Idle -> EsperandoOtros -> Ruleta -> Pregunta -> Bonus
// Idle -> EsperandoOtros -> JugandoOtros

public abstract class Etapa {
   protected Sector sector;

   protected Animator anim;

   protected Randoms rnd = Randoms.with(new Random());

   public Etapa(Sector sector) {
      this.sector = sector;
      anim = new Animator();
   }

   public abstract Layer.HasSize layer();

   public abstract void update(int delta);

   public abstract void doPaint(Clock clock);

   public void paint(Clock clock) {
      anim.paint(clock);

      doPaint(clock);
   }
}