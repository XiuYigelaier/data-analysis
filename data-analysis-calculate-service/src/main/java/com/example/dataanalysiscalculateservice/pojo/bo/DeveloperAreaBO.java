package com.example.dataanalysiscalculateservice.pojo.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class DeveloperAreaBO implements Serializable {
    Set<String> languages;
    String blo;

}
