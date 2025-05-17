package web.api.check.beans;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import web.api.check.beans.mbean.ConsecutiveMissesMXBean;

public class ConsecutiveMisses extends NotificationBroadcasterSupport implements ConsecutiveMissesMXBean {
    private int consecutiveMisses = 0;

    public int getConsecutiveMisses() {
        return consecutiveMisses;
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { "consecutive-misses" };
        String name = Notification.class.getName();
        String description = "Notification was sent when 3 consecutive misses was recorded";
        return new MBeanNotificationInfo[] { new MBeanNotificationInfo(types, name, description) };
    }

    public synchronized void increment(boolean isHit) {
        if (isHit) {
            consecutiveMisses = 0;
        } else {
            if (++consecutiveMisses >= 3) {
                notifyListeners();
                consecutiveMisses = 0;
            }
        }
    }

    private void notifyListeners() {
        sendNotification(new Notification(
            "consecutive-misses",
            this,
            System.currentTimeMillis(),
            "ALERT: User has 3 consecutive misses!"
        ));
    }
}