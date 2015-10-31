package ar.com.jengibre.core;

/**
 * todo estático, viene a ser un Object de Scala
 */
public class StartupLatch {

   private static int milisHastaElComienzo;

   private static int sectoresListos;

   static {
      reset();
   }

   public static void reset() {
      milisHastaElComienzo = -1;
      sectoresListos = 0;
   }

   /**
    * un sector tocó la pantalla y está listo para empezar, arranca el timer si
    * es que no arrancó aún
    */
   public static void sectorListoParaEmpezar() {
      sectoresListos++;

      if (milisHastaElComienzo == -1) {
         // FIXME milisHastaElComienzo = 10_000;
         milisHastaElComienzo = 2_000;
      }
   }

   public static int segundosHastaElComienzo() {
      return (int) (milisHastaElComienzo / 1000);
   }

   public static boolean empezoElJuego() {
      return sectoresListos == 4 || milisHastaElComienzo == 0;
   }

   public static void update(int delta) {
      if (milisHastaElComienzo > 0) {
         milisHastaElComienzo = Math.max(0, milisHastaElComienzo - delta);
      }
   }
}
