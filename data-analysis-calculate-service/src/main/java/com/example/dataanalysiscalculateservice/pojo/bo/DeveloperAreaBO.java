package com.example.dataanalysiscalculateservice.pojo.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class DeveloperAreaBO implements Serializable {
    Set<String> languages;
    String bio;

    public DeveloperAreaBO() {
        languages = new HashSet<>();
    }
}
