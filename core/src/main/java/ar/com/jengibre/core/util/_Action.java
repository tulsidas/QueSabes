package ar.com.jengibre.core.util;

import tripleplay.anim.Animation;

/**
 * backport de #74a166b854c124295b9ef71a0ffea660caaf3d60
 */
public class _Action extends Animation {
   public _Action(Runnable action) {
      _action = action;
   }

   @Override
   protected void init(float time) {
      super.init(time);
      _complete = false;
   }

   @Override
   protected float apply(float time) {
      makeComplete();
      return _start - time;
   }

   @Override
   protected void makeComplete() {
      if (!_complete) {
         _action.run();
         _complete = true;
      }
   }

   protected final Runnable _action;

   protected boolean _complete;
}
