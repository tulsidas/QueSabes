package ar.com.jengibre.core.etapas;

import java.util.Random;

import playn.core.Canvas;
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

   public abstract void draw(Canvas canvas);

   public abstract void clicked(float x, float y);

   public abstract void update(int delta);

   public void paint(Clock clock) {
      anim.paint(clock);
   }
}