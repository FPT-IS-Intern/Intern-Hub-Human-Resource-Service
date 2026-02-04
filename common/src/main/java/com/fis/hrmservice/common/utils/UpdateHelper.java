package com.fis.hrmservice.common.utils;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateHelper {
  public static <T> boolean applyIfChanged(
      T newValue, Supplier<T> getter, Consumer<T> setter, BiPredicate<T, T> equals) {
    if (newValue == null) return false;

    T current = getter.get();

    if (!equals.test(current, newValue)) {
      setter.accept(newValue);
      return true;
    }
    return false;
  }
}
