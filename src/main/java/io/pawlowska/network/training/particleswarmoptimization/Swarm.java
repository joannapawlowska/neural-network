package io.pawlowska.network.training.particleswarmoptimization;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Swarm {

    @Setter
    private Particle bestGlobalParticle;
    private Particle [] particles;

    public Swarm(int swarmSize) {
        particles = new Particle[swarmSize];
    }

    public void fixBestGlobalParticle() {

        for(Particle particle : particles) {
            if (isBetterThanBestGlobalParticle(particle))
                bestGlobalParticle = particle;
        }
    }

    private boolean isBetterThanBestGlobalParticle(Particle particle){
        return particle.getBestLocalPositionValue() < bestGlobalParticle.getBestLocalPositionValue();
    }
}