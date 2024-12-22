package com.example.core.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class DeveloperAreaAnswer implements Serializable {
    Set<String> languages;
    String blo;

}
