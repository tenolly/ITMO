package web.api.check.beans;

import web.api.check.beans.mbean.HitStatisticsMXBean;

public class HitStatistics implements HitStatisticsMXBean {
    private int totalPoints = 0;
    private int missedPoints = 0;

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getMissedPoints() {
        return missedPoints;
    }

    public double getHitPercentage() {
        return totalPoints == 0 ? 0 : ((totalPoints - missedPoints) * 100.0) / totalPoints;
    }

    public synchronized void increment(boolean isHit) {
        ++totalPoints;
        if (!isHit) {
            ++missedPoints;
        }
    }
}
