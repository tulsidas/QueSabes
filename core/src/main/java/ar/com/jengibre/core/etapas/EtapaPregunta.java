package ar.com.jengibre.core.etapas;

import static ar.com.jengibre.core.QueSabes.bgPregunta;
import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.util.Clock;
import ar.com.jengibre.core.Pregunta;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

public class EtapaPregunta extends AbstractEtapa {
   private int personaje;

   public EtapaPregunta(Sector sector, int personaje) {
      super(sector);
      this.personaje = personaje;

      Pregunta pregunta = rnd.pick(QueSabes.preguntas, null);

      CanvasImage cImg = graphics().createImage(Sector.WIDTH, Sector.HEIGHT);
      cImg.canvas().drawImage(bgPregunta, 0, 0);

      cImg.canvas().drawText(pregunta.getPregunta(), 200, 300);
      // cImg.canvas().fillText(text, x, y)

      layer.add(graphics().createImageLayer(cImg));
   }

   @Override
   public void doPaint(Clock clock) {
   }

   @Override
   public void update(int delta) {
   }

   @Override
   public void clicked(float x, float y) {
   }
}