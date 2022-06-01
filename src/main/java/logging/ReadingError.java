package logging;

public class ReadingError {

    private String entity;

    private String entityId;
    private String entityAttribute;
    private String message;

    public ReadingError(String entity, String entityId, String entityAttribute, String message) {
        this.entity   = entity;
        this.entityId = entityId;
        this.entityAttribute = entityAttribute;
        this.message  = message;
    }

    @Override
    public String toString() {
        String s = entity + ";" + entityId + ";" + entityAttribute + ";" + message;

        return s;

    }
}
