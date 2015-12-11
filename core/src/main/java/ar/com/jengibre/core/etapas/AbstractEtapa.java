package ar.com.jengibre.core.etapas;

import java.util.Random;

import playn.core.GroupLayer;
import playn.core.util.Clock;
import tripleplay.anim.Animator;
import tripleplay.util.Randoms;
import ar.com.jengibre.core.Sector;

public abstract class AbstractEtapa {

   // tiempo máximo que lo dejo estar en una etapa si no hace nada
   protected static final int TIMEOUT = 10_000;

   protected Sector sector;

   protected Animator anim;
   
   private Animator timeoutAnim;

   protected Randoms rnd = Randoms.with(new Random());

   protected GroupLayer layer;

   private boolean timeout = false;

   public AbstractEtapa(Sector sector) {
      this.sector = sector;
      this.layer = sector.layer();
      layer.removeAll(); // TODO chequear

      anim = new Animator();

      // timeout que avanza el juego por si abandonan o tardan mucho 
      // (en otro Animator para que no moleste al del juego)
      timeoutAnim = new Animator();
      timeoutAnim.delay(TIMEOUT).then().action(() -> {
         timeout = true;
      });
   }

   public final void update(int delta) {
      if (timeout) {
         // hay que llamarlo desde update, desde paint se rompe todo
         timeout();

         timeout = false;
      }

      doUpdate(delta);
   }

   public abstract void timeout();

   public void paint(Clock clock) {
      anim.paint(clock);
      timeoutAnim.paint(clock);

      doPaint(clock);
   }

   public void doUpdate(int delta) {
      // por default NOOP
   }

   public void doPaint(Clock clock) {
      // por default NOOP
   }

   public void onPointerStart(float x, float y) {
      // por default NOOP
   }

   public void onPointerEnd(float x, float y) {
      // por default NOOP
   }
}