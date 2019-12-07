package io.github.yashchenkon.banking.api.rest.common.body;

public class EntityCreatedResponseV1_0 {
    private final String id;

    public EntityCreatedResponseV1_0(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
