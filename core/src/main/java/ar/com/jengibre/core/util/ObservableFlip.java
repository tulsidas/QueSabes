package ar.com.jengibre.core.util;

import playn.core.ImageLayer;
import tripleplay.anim.Animation.Flip;
import tripleplay.anim.Flipbook;

public class ObservableFlip extends Flip {

   public ObservableFlip(ImageLayer target, Flipbook book) {
      super(target, book);
   }

   public int getFrame() {
      return _curIdx;
   }
}
