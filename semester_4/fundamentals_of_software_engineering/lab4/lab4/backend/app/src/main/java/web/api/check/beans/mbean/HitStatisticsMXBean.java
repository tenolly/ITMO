package web.api.check.beans.mbean;

public interface HitStatisticsMXBean {
    int getTotalPoints();
    int getMissedPoints();
    double getHitPercentage();
    void increment(boolean isHit);
}