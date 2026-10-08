package com.bcadigital.creditsimulator.service;

import com.bcadigital.creditsimulator.model.CalculationResult;
import com.bcadigital.creditsimulator.model.Sheet;

import java.util.*;

public class SheetManager {

    private final Map<String, Sheet> sheetMap;
    private String activeSheetId;
    private int autoIncrementId;

    public SheetManager() {
        this.sheetMap = new LinkedHashMap<>();
        this.autoIncrementId = 1;
    }

    public Sheet saveSheet(String customName, CalculationResult result) {
        String sheetId = "Sheet-" + (autoIncrementId++);
        String name = (customName != null && !customName.isBlank()) ? customName.trim() : sheetId;

        Sheet sheet = new Sheet(sheetId, name, result);
        sheetMap.put(sheetId.toLowerCase(), sheet);
        this.activeSheetId = sheetId.toLowerCase();

        return sheet;
    }

    public boolean switchSheet(String searchKey) {
        if (searchKey == null || searchKey.isBlank()) {
            return false;
        }

        String targetKey = searchKey.trim().toLowerCase();

        if (sheetMap.containsKey(targetKey)) {
            activeSheetId = targetKey;
            return true;
        }

        for (Map.Entry<String, Sheet> entry : sheetMap.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(targetKey)) {
                activeSheetId = entry.getKey();
                return true;
            }
        }

        return false;
    }

    public Sheet getActiveSheet() {
        if (activeSheetId == null) {
            return null;
        }
        return sheetMap.get(activeSheetId);
    }

    public Collection<Sheet> getAllSheets() {
        return Collections.unmodifiableCollection(sheetMap.values());
    }

    public boolean isEmpty() {
        return sheetMap.isEmpty();
    }
}
