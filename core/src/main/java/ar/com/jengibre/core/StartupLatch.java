package ar.com.jengibre.core;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * todo estático, viene a ser un object de Scala
 */
public class StartupLatch {

   private static final int TIMEOUT = /*10*/3_000;

   private static Multimap<Integer, Sector> equipos;

   // a qué equipo se unen los que tocan ahora
   private static int equipoActual;

   private static int counter = 0;

   static {
      reset();
   }

   public static void reset() {
      equipos = HashMultimap.create();
      equipoActual = 1;
   }

   public static int sectorListoParaEmpezar(Sector sector) {
      // se sumó uno al equipo actual
      equipos.get(equipoActual).forEach(sectorActual -> sectorActual.sumoseUno());

      equipos.put(equipoActual, sector);

      System.out.println(equipos);

      // arranco el timer
      if (counter <= 0) {
         counter = TIMEOUT;
      }

      // devuelvo cuántos hay en este equipo
      return equipos.get(equipoActual).size();
   }

   public static void update(int delta) {
      if (counter > 0) {
         counter -= delta;

         if (counter <= 0) {
            // aviso que arranquen los sectores de equipoActual
            equipos.get(equipoActual).forEach(sector -> sector.ruleta());

            // nuevo equipo
            equipoActual++;

            System.out.println("equipoActual = " + equipoActual);

            counter = 0;
         }
      }
   }

   public static int counter() {
      return counter;
   }

   private static int lugaresDisponibles() {
      return 4 - equipos.size();
   }
}
