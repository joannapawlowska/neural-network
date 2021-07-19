package io.pawlowska.network.training.particleswarmoptimization;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Swarm extends ArrayList<Particle> {

    @Setter @Getter private Particle bestGlobalParticle;

    public void fixBestGlobalParticle() {

        for (Particle particle : this) {
            if (isBetterThanBestGlobalParticle(particle)){
                bestGlobalParticle = particle;
            }
        }
    }

    private boolean isBetterThanBestGlobalParticle(Particle particle) {
        return particle.compareTo(bestGlobalParticle) < 0;
    }
}