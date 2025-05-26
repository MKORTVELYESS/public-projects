package org.example.jobs;

import java.util.Map;

public class Job {

    private Map<String, String> attributes;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


    protected Job(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
