package ar.com.jengibre.core.etapas;

import static playn.core.PlayN.graphics;

import java.util.List;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextWrap;
import playn.core.util.TextBlock;
import playn.core.util.TextBlock.Align;
import pythagoras.f.Point;
import tripleplay.anim.Flipbook;
import tripleplay.util.Colors;
import ar.com.jengibre.core.Personaje;
import ar.com.jengibre.core.Pregunta;
import ar.com.jengibre.core.QueSabes;
import ar.com.jengibre.core.Sector;

import com.google.common.collect.Lists;

public class EtapaPregunta extends AbstractEtapa {
   private ImageLayer respuesta1, respuesta2, respuesta3;

   private GroupLayer flipbookGroup, papelitosGroup; // flipbooks

   private Personaje personaje;

   private ImageLayer reloj;

   private int falta;

   public EtapaPregunta(Sector sector, Personaje personaje) {
      super(sector);

      this.personaje = personaje;
      personaje.cargarSonidos();

      flipbookGroup = graphics().createGroupLayer();
      papelitosGroup = graphics().createGroupLayer();

      layer.add(graphics().createImageLayer(QueSabes.bgPregunta));

      Pregunta pregunta = rnd.pick(personaje.preguntas(), null);

      Font font = graphics().createFont("Benton", Font.Style.BOLD, 32);
      TextFormat format = new TextFormat().withAntialias(true).withFont(font);

      TextBlock texto = new TextBlock(graphics()
            .layoutText(pregunta.getPregunta(), format, new TextWrap(750)));
      layer.addAt(graphics().createImageLayer(QueSabes.zocaloAlto), 0, 20);
      ImageLayer preguntaIL = graphics().createImageLayer(texto.toImage(Align.CENTER, Colors.WHITE));
      layer.addAt(preguntaIL, (Sector.WIDTH - preguntaIL.width()) / 2, 30);

      List<Float> posRespuestas = Lists.newArrayList(200F, 290F, 380F);
      float y = rnd.pluck(posRespuestas, 0F);
      TextWrap wrap = new TextWrap(800);
      respuesta1 = pad(new TextBlock(graphics().layoutText(pregunta.getRespuestas().get(0), format, wrap))
            .toImage(Align.CENTER, Colors.WHITE));
      respuesta1.setInteractive(true);
      layer.addAt(graphics().createImageLayer(QueSabes.zocalo), 0, y - 20);
      layer.addAt(respuesta1, (Sector.WIDTH - respuesta1.width()) / 2, y);

      y = rnd.pluck(posRespuestas, 0F);
      respuesta2 = pad(new TextBlock(graphics().layoutText(pregunta.getRespuestas().get(1), format, wrap))
            .toImage(Align.CENTER, Colors.WHITE));
      respuesta2.setInteractive(true);
      layer.addAt(graphics().createImageLayer(QueSabes.zocalo), 0, y - 20);
      layer.addAt(respuesta2, (Sector.WIDTH - respuesta2.width()) / 2, y);

      y = rnd.pluck(posRespuestas, 0F);
      respuesta3 = pad(new TextBlock(graphics().layoutText(pregunta.getRespuestas().get(2), format, wrap))
            .toImage(Align.CENTER, Colors.WHITE));
      respuesta3.setInteractive(true);
      layer.addAt(graphics().createImageLayer(QueSabes.zocalo), 0, y - 20);
      layer.addAt(respuesta3, (Sector.WIDTH - respuesta3.width()) / 2, y);

      this.falta = TIMEOUT;
      reloj = graphics().createImageLayer();
      layer.addAt(reloj, 0, 330);
   }

   @Override
   public void timeout() {
      if (!fin) {
         showAnim(personaje.fbPierde(), false);
      }
   }

   @Override
   public void doUpdate(int delta) {
      falta -= delta;
      reloj.setImage(QueSabes.reloj.get(Math.max(falta, 0) / 1000));
   }

   @Override
   public void touchEnd(float x, float y) {
      Layer hit = layer.hitTest(new Point(x, y));

      if (hit == respuesta1) {
         showAnim(personaje.fbGana(), true);
      }
      else if (hit == respuesta2 || hit == respuesta3) {
         showAnim(personaje.fbPierde(), false);
      }
   }

   // mete una imagen dentro de otra con más espacio (para darle lugar a las
   // preguntas
   private ImageLayer pad(Image orig) {
      float pad = 25;
      CanvasImage image = graphics().createImage(orig.width() + 20 * pad, orig.height() + 2 * pad);

      image.canvas().drawImage(orig, 10 * pad, pad);

      return graphics().createImageLayer(image);
   }

   private boolean fin = false;

   private void showAnim(Flipbook flipbook, boolean ganoMedalla) {
      if (!fin) {
         fin = true;

         // se va todo
         layer.removeAll();

         // vuelve el fondo
         layer.add(graphics().createImageLayer(QueSabes.bgPregunta));

         layer.add(flipbookGroup);
         anim.flipbook(flipbookGroup, flipbook);

         anim.addBarrier();

         if (ganoMedalla) {
            anim.play(personaje.soundGana());
            //layer.add(papelitosGroup);
            //anim.flipbook(papelitosGroup, QueSabes.papelitos);

            ImageLayer medalla = graphics().createImageLayer(QueSabes.medalla);
            medalla.setOrigin(medalla.width() / 2, medalla.height() / 2);

            anim.addAt(layer, medalla, Sector.WIDTH / 2, Sector.HEIGHT / 2).then().tweenScale(medalla).from(2)
               .to(0.6F).in(500).easeOutBack();
         }
         else {
            anim.play(personaje.soundPierde());
         }

         anim.addBarrier(1_000);

         anim.action(() -> {
            personaje.liberarSonidos();
            sector.arquerito(ganoMedalla);
         });
      }
   }
}
