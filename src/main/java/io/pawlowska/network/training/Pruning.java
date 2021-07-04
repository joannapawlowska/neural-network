package io.pawlowska.network.training;

import lombok.Builder;

@Builder
public class Pruning {

    @Builder.Default
    private double removalThreshold = 0.001;

    @Builder.Default
    private int minLayerSize = 1;
}
