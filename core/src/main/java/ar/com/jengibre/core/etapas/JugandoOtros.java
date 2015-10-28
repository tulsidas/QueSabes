package ar.com.jengibre.core.etapas;

import ar.com.jengibre.core.Sector;

public class JugandoOtros extends Etapa {
   public JugandoOtros(Sector sector) {
      super(sector);
      // c.drawImage(bgIdle, 0, 0);
      // c.drawText("Partido en progreso, esperar", 300, 200);
   }

   public void doPaint(playn.core.util.Clock clock) {
   };

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}