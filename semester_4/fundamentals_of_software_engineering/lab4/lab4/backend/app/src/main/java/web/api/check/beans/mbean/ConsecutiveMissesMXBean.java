package web.api.check.beans.mbean;

public interface ConsecutiveMissesMXBean {
    int getConsecutiveMisses();
    void increment(boolean isHit);
}