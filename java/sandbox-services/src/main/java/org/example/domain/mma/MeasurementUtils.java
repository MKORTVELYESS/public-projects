package org.example.domain.mma;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.entity.FighterDetails;

public class MeasurementUtils {

  static final Byte YES = 1;
  static final Byte NO = 0;
  private static final Pattern HEIGHT_CM_FORMAT = Pattern.compile("(\\d+)cm");

  static Byte yesNoIfNotNull(Object value) {
    return value != null ? YES : NO;
  }

  static Float diff(Float x, Float y) {
    if (x != null && y != null) {
      return x - y;
    } else {
      return null;
    }
  }

  static byte knownDiff(byte x, byte y) {
    return (byte) (x - y);
  }

  static Float getHeightCm(FighterDetails fighter) {
    if (fighter.getHeight() == null) return null;
    Matcher m = HEIGHT_CM_FORMAT.matcher(fighter.getHeight());
    if (m.find()) {
      var result = (Integer) Integer.parseInt(m.group(1));
      return result.floatValue();
    } else {
      return null;
    }
  }
}
