package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import ar.com.jengibre.core.etapas.AbstractEtapa;
import ar.com.jengibre.core.etapas.EtapaBonus;
import ar.com.jengibre.core.etapas.EtapaEsperandoOtros;
import ar.com.jengibre.core.etapas.EtapaIdle;
import ar.com.jengibre.core.etapas.EtapaPregunta;
import ar.com.jengibre.core.etapas.EtapaRuleta;

public class Sector {

   private AbstractEtapa etapa;

   private GroupLayer layer;

   public static final int WIDTH = 960;

   public static final int HEIGHT = 540;

   private String nombre;

   // cuantas rondas (ruleta->pregunta->arquerito) completó
   private int rondas;

   private int medallas;

   public Sector(String nombre) {
      this.nombre = nombre;
      reload();
   }

   public void reload() {
      if (layer == null) {
         layer = graphics().createGroupLayer(WIDTH, HEIGHT);
      }
      else {
         layer.removeAll();
      }

      rondas = 0;
      medallas = 0;

      etapa = new EtapaIdle(this);
      // etapa = new EtapaRuleta(this);
      // etapa = new EtapaBonus(this);
      // etapa = new EtapaTest(this);
   }

   public GroupLayer layer() {
      return layer;
   }

   public void onPointerStart(float x, float y) {
      etapa.onPointerStart(x, y);
   }

   public void onPointerEnd(float x, float y) {
      etapa.onPointerEnd(x, y);
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
      int cuantos = StartupLatch.sectorListoParaEmpezar(this);
      setEtapa(new EtapaEsperandoOtros(this, cuantos));
   }

   /**
    * (EsperandoOtros|Arquerito) -> Ruleta
    */
   public void ruleta() {
      setEtapa(new EtapaRuleta(this));
   }

   /**
    * Ruleta -> Pregunta
    */
   public void pregunta(Personaje personaje) {
      setEtapa(new EtapaPregunta(this, personaje));
   }

   /**
    * Pregunta -> Arquerito
    */
   public void arquerito(boolean ganoMedalla) {
      if (ganoMedalla) {
         medallas++;
      }

      setEtapa(new EtapaBonus(this));
   }

   public void finArquerito(boolean ganoMedalla) {
      if (ganoMedalla) {
         medallas++;
      }

      // termino una ronda
      rondas++;

      if (rondas == 3) {
         // FIXME esperandoFinOtros();
         // FIXME medallero();
         System.out.println("GAME OVER para " + nombre + " | " + medallas + " medallas");
      }
      else {
         // a la ruleta
         ruleta();
      }
   }

   /**
    * cuando se suma alguien a mi equipo en la etapa EsperandoOtros
    */
   public void sumoseUno() {
      if (etapa instanceof EtapaEsperandoOtros) {
         ((EtapaEsperandoOtros) etapa).sumoseUno();
      }
   }

   public int medallas() {
      return medallas;
   }

   @Override
   public String toString() {
      return "[Sector] " + nombre;
   }

   private void setEtapa(AbstractEtapa nueva) {
      etapa = nueva;
   }
}