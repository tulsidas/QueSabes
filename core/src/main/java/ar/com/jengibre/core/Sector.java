package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.EsperandoOtros;
import ar.com.jengibre.core.etapas.Etapa;
import ar.com.jengibre.core.etapas.Idle;
import ar.com.jengibre.core.etapas.JugandoOtros;
import ar.com.jengibre.core.etapas.Ruleta;

public class Sector {

   private Etapa etapa;

   private GroupLayer layer;

   public static final int WIDTH = 800;

   public static final int HEIGHT = 400;

   public Sector() {
      layer = graphics().createGroupLayer();

      etapa = new Idle(this);
      // etapa = new Ruleta(this);
   }

   public GroupLayer layer() {
      return layer;
   }

   public void clicked(float x, float y) {
      etapa.clicked(x, y);
   }

   public void update(int delta) {
      etapa.update(delta);
   }

   public void paint(Clock clock) {
      etapa.paint(clock);
   }

   /**
    * Idle -> EsperandoOtros
    */
   public void empezarJuego() {
      StartupLatch.sectorListoParaEmpezar();
      etapa = new EsperandoOtros(this);
   }

   /**
    * EsperandoOtros -> Ruleta
    */
   public void empezoJuego() {
      etapa = new Ruleta(this);
   }

   /**
    * EsperandoOtros -> JugandoOtros
    */
   public void jugandoOtros() {
      etapa = new JugandoOtros(this);
   }
}