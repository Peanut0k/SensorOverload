package manager;

public class ChaosController {
    public double intensity;
    public double speedCorrection;
    public int phase;
    public double shake;
    public double spawnInterval;
    public double speedMultiplier;
    public double floorMovement;
    public double rotation;
    private int lastIntensityMilestone;

    public ChaosController() {
        setDefaults();
    }

    public void setDefaults() {
        this.intensity = GameState.intensity;
        this.speedCorrection = GameState.speedCorrection;
        this.phase = 1;
        this.shake = 0;
        this.spawnInterval = 0.3;
        this.speedMultiplier = 1;
        floorMovement = 0;
        rotation = 0;
        this.lastIntensityMilestone = 0;
    }

    public void update() {
        this.intensity = GameState.intensity;
        updatePhase();
    }

    public void updatePhase() {
        int currentMilestone = (int) Math.floor(intensity / 5);

        if (currentMilestone > lastIntensityMilestone) {
            lastIntensityMilestone = currentMilestone;
            phase = Math.min(currentMilestone + 1, 5);
            setPhase();
        }
    }

    public void setPhase() {
        switch (phase) {
            case 1 -> phase1();
            case 2 -> phase2();
            case 3 -> phase3();
            case 4 -> phase4();
            case 5 -> phase5();
            default -> phaseDefault();
        }
    }

    private void phase1() {
        // defaults
        shake = 0;
        spawnInterval = 0.27;
        speedMultiplier = 1;
        floorMovement = 0;
        rotation = 0;
    }

    private void phase2() {
        shake = 1;
        spawnInterval = 0.23;
        speedMultiplier = 1;
        floorMovement = 0;
        rotation = 0;
    }

    private void phase3() {
        shake = 1.5;
        speedMultiplier = 1.2;
        floorMovement = 1;
        spawnInterval = 0.22;
        rotation = 0;
    }

    private void phase4() {
        rotation = 1;
        shake = 2;
        spawnInterval = 0.17;
        speedMultiplier = 1.25;
        floorMovement = 1;
    }

    private void phase5() {
        rotation = 2;
        shake = 3;
        spawnInterval = 0.15;
        speedMultiplier = 1.25;
        floorMovement = 1;
    }

    private void phaseDefault() {
        rotation = 2.5;
        shake = 4;
        spawnInterval = Math.max(0.15, 0.2 - (phase - 5) * 0.01);
        speedMultiplier = 0.5 + intensity / 40;
        floorMovement = 0.6 + intensity / 60;
    }
}