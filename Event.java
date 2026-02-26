public class Event {
    private EventType type = null;
    private long time;
    public Event(EventType cur_type, long cur_time)
    {
        type = cur_type;
        time = cur_time;
    }
    public EventType getType() {
        return type;
    }
    public long getTime() {
        return time;
    }
}