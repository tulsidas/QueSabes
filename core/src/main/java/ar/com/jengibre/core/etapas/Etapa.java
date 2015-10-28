package ar.com.jengibre.core.etapas;

import java.util.Random;

import playn.core.GroupLayer;
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

   protected GroupLayer layer;

   public Etapa(Sector sector) {
      this.sector = sector;
      this.layer = sector.layer();
      layer.removeAll(); // TODO chequear

      anim = new Animator();
   }

   public abstract void clicked(float x, float y);

   public abstract void update(int delta);

   public void paint(Clock clock) {
      anim.paint(clock);
   }

   public abstract void doPaint(Clock clock);

}