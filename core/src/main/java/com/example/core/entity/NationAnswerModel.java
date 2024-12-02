package com.example.core.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class NationAnswerModel implements Serializable {
    String nation;
    String credence;
}
