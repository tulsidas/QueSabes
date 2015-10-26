package ar.com.jengibre.core.etapas;

import playn.core.Layer;
import playn.core.util.Clock;
import ar.com.jengibre.core.Sector;

public class JugandoOtros extends Etapa {
   public JugandoOtros(Sector sector) {
      super(sector);
   }

   @Override
   public Layer.HasSize layer() {
      return null;
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void doPaint(Clock clock) {
   }
}