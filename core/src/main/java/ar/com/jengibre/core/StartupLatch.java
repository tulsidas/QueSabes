package ar.com.jengibre.core;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * todo estático, viene a ser un object de Scala
 */
public class StartupLatch {

   private static final int TIMEOUT = /*10*/5_000;

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

   public synchronized static int sectorListoParaEmpezar(Sector sector) {
      // se sumó uno al equipo actual
      equipos.get(equipoActual).forEach(s -> s.sumoseUno());
      equipos.put(equipoActual, sector);

      System.out.println(equipos);

      if (lugaresDisponibles() == 0) {
         // arranco sin esperar
         empiezaSector();
         return -1;
      }
      else {
         // arranco el timer
         if (counter <= 0) {
            counter = TIMEOUT;
         }
      }

      // devuelvo cuántos hay en este equipo
      return equipos.get(equipoActual).size();
   }

   public synchronized static void sectorTermino(Sector sector) {
      int equipo = equipo(sector);

      boolean todosTerminaron = equipos.get(equipo).stream().allMatch(s -> s.termino());

      if (todosTerminaron) {
         Collection<Sector> sectores = equipos.get(equipo);

         int _medallas = 0;
         for (Sector s : sectores) {
            _medallas += s.medallas();
         }

         final int medallas = _medallas; // puaj

         sectores.forEach(s -> s.medallero(medallas));

         // chau chau adios
         equipos.removeAll(equipo);
      }
      else {
         sector.esperarFinOtros();
      }
   }

   public static void update(int delta) {
      if (counter > 0) {
         counter -= delta;

         if (counter <= 0) {
            empiezaSector();
         }
      }
   }

   public static int counter() {
      return counter;
   }

   private static int lugaresDisponibles() {
      return 4 - equipos.size();
   }

   private static void empiezaSector() {
      // aviso que arranquen los sectores de equipoActual
      equipos.get(equipoActual).forEach(sector -> sector.ruleta());

      // nuevo equipo
      equipoActual++;
      counter = 0;
   }

   private static int equipo(Sector sector) {
      // busco el equipo del sector
      for (Map.Entry<Integer, Sector> entry : equipos.entries()) {
         if (entry.getValue() == sector) {
            return entry.getKey();
         }
      }

      System.err.println("Sector " + sector + " sin equipo");
      return -1;
   }
}