package com.example.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class DeveloperArea implements Serializable {
    Set<String> languages;
    String blo;

}
