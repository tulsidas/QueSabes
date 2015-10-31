package ar.com.jengibre.core;

import java.util.List;

public class Pregunta {

   private String pregunta;

   private List<String> respuestas;

   public Pregunta(String pregunta, List<String> respuestas) {
      this.pregunta = pregunta;
      this.respuestas = respuestas;
   }

   public String getPregunta() {
      return pregunta;
   }

   public List<String> getRespuestas() {
      return respuestas;
   }

   @Override
   public String toString() {
      return "Pregunta [pregunta=" + pregunta + ", respuestas=" + respuestas + "]";
   }
}