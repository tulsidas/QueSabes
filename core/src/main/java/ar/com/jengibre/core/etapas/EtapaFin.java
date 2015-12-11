package ar.com.jengibre.core.etapas;

import ar.com.jengibre.core.Sector;

public class EtapaFin extends AbstractEtapa {
   public EtapaFin(Sector sector) {
      super(sector);
   }

   public void timeout() {
      // FIXME volver a EtapaIdle
   }
}