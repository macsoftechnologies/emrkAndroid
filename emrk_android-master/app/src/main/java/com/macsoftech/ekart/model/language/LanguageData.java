package com.macsoftech.ekart.model.language;

import java.util.List;

public class LanguageData {
    private List<SelectedLanguages> selectedLanguages;

    private String Count;

    public List<SelectedLanguages> getSelectedLanguages() {
        return selectedLanguages;
    }

    public void setSelectedLanguages(List<SelectedLanguages> selectedLanguages) {
        this.selectedLanguages = selectedLanguages;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }

    @Override
    public String toString() {
        return "ClassPojo [selectedLanguages = " + selectedLanguages + ", Count = " + Count + "]";
    }
}
			