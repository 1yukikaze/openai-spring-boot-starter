package io.github.yukikaze.insert_chatgpt.enums;

public enum HeaderTypes {
    AUTHORIZATION("Authorization");
    final String type;

    HeaderTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

