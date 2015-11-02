package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.AbstractEtapa;
import ar.com.jengibre.core.etapas.EtapaEsperandoOtros;
import ar.com.jengibre.core.etapas.EtapaJugandoOtros;
import ar.com.jengibre.core.etapas.EtapaPregunta;
import ar.com.jengibre.core.etapas.EtapaRuleta;

public class Sector {

   private AbstractEtapa etapa;

   private GroupLayer layer;

   public static final int WIDTH = 1080;

   public static final int HEIGHT = 540;

   public Sector() {
      layer = graphics().createGroupLayer();

      // etapa = new EtapaIdle(this);
      // etapa = new EtapaRuleta(this);
      etapa = new EtapaPregunta(this, 1);
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
      etapa = new EtapaEsperandoOtros(this);
   }

   /**
    * EsperandoOtros -> Ruleta
    */
   public void empezoJuego() {
      etapa = new EtapaRuleta(this);
   }

   /**
    * EsperandoOtros -> JugandoOtros
    */
   public void jugandoOtros() {
      etapa = new EtapaJugandoOtros(this);
   }

   /**
    * Ruleta -> Pregunta
    */
   public void mostrarPregunta(int personaje) {
      etapa = new EtapaPregunta(this, personaje);
   }
}