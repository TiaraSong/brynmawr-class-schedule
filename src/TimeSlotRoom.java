public class TimeSlotRoom {
    private TimeSlot timeSlot;
    private Room room;

    public TimeSlotRoom(TimeSlot timeSlot, Room room) {
        this.timeSlot = timeSlot;
        this.room = room;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public Room getRoom() {
        return room;
    }
}

