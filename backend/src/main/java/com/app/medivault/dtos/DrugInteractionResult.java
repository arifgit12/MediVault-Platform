package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DrugInteractionResult {
    private String riskLevel;
    private List<String> warnings;
}

