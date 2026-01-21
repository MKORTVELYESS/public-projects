package org.example.domain.mma;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.example.entity.FighterDetails;
import org.jetbrains.annotations.NotNull;

public class FeatureFilters {
  @NotNull
  static Predicate<CleanBout> boutsParticipantsHaveMappedDetails(
      Map<String, List<FighterDetails>> fighterDetailsMap) {
    return b ->
        fighterDetailsMap.containsKey(b.getFighterId())
            && fighterDetailsMap.containsKey(b.getOpponentId())
            && !fighterDetailsMap.get(b.getFighterId()).isEmpty()
            && !fighterDetailsMap.get(b.getOpponentId()).isEmpty();
  }
}
