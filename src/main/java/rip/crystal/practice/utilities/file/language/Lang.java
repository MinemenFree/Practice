package rip.crystal.practice.utilities.file.language;

public enum Lang {

    ENGLISH("en"),
    SPANISH("es");
    //FRENCH("fr"),
    //PORTUGUESE("pt");
    
    private final String abbreviation;
    
    Lang(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    
    public static Lang getByAbbreviation(final String abbreviation) {
        for (final Lang locale : values()) {
            if (locale.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                return locale;
            }
        }
        return Lang.ENGLISH;
    }
    
    public static Lang getByName(final String name) {
        for (final Lang locale : values()) {
            if (locale.getAbbreviation().equalsIgnoreCase(name)) {
                return locale;
            }
        }
        return Lang.ENGLISH;
    }
    
    public String getAbbreviation() {
        return this.abbreviation;
    }
}
