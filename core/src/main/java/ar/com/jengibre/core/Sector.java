package ar.com.jengibre.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import pythagoras.f.FloatMath;
import ar.com.jengibre.core.etapas.AbstractEtapa;
import ar.com.jengibre.core.etapas.EtapaBonus;
import ar.com.jengibre.core.etapas.EtapaEsperandoFinOtros;
import ar.com.jengibre.core.etapas.EtapaEsperandoOtros;
import ar.com.jengibre.core.etapas.EtapaIdle;
import ar.com.jengibre.core.etapas.EtapaMedallero;
import ar.com.jengibre.core.etapas.EtapaPregunta;
import ar.com.jengibre.core.etapas.EtapaRuleta;

public class Sector {

   private AbstractEtapa etapa;

   private GroupLayer.Clipped topLayer;

   private GroupLayer layer;

   public static final int WIDTH = 960;

   public static final int HEIGHT = 540;

   private String nombre;

   // cuantas rondas (ruleta->pregunta->arquerito) completó
   private int rondas;

   private int medallas;

   public Sector(String nombre, boolean flipped) {
      this.nombre = nombre;

      topLayer = graphics().createGroupLayer(WIDTH, HEIGHT);

      layer = graphics().createGroupLayer();

      if (flipped) {
         layer.setRotation(FloatMath.PI);
         topLayer.addAt(layer, WIDTH, HEIGHT);
      }
      else {
         topLayer.add(layer);
      }

      reset();
   }

   public GroupLayer.Clipped topLayer() {
      return topLayer;
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

   public void reset() {
      rondas = 0;
      medallas = 0;

      etapa = new EtapaIdle(this);
   }

   /**
    * Idle -> EsperandoOtros
    */
   public void empezarJuego() {
      int cuantos = StartupLatch.sectorListoParaEmpezar(this);
      if (cuantos == -1) {
         // todo listo, ir directo a ruleta
         ruleta();
      }
      else {
         setEtapa(new EtapaEsperandoOtros(this, cuantos));
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

      if (termino()) {
         StartupLatch.sectorTermino(this);
      }
      else {
         // a la ruleta
         ruleta();
      }
   }

   public void esperarFinOtros() {
      setEtapa(new EtapaEsperandoFinOtros(this));
   }

   public void medallero(int medallas) {
      setEtapa(new EtapaMedallero(this, medallas));
   }

   public boolean termino() {
      return rondas == 3;
   }

   public int medallas() {
      return medallas;
   }

   @Override
   public String toString() {
      return nombre;
   }

   private void setEtapa(AbstractEtapa nueva) {
      etapa = nueva;
   }
}