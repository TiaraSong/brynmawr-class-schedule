public class TimeSlot {
    private int timeId;
    private String startTime;
    private String endTime;
    private String days;

    public TimeSlot(int timeId, String startTime, String endTime, String days) {
        this.timeId = timeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }

    public int getTimeId() {
        return timeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDays() {
        return days;
    }

    @Override
    public String toString() {
        return "TimeSlot{timeId=" + timeId + ", startTime='" + startTime + "', endTime='" + endTime + "', days='" + days + "'}";
    }
}