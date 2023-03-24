package uk.gov.companieshouse.overseasentitiesapi.model;

/**
 * Recognised model schema versions.
 */
public enum SchemaVersion {
    // These 'historical' versions will never be set within the JSON documents:
    VERSION_1_0("1.0"),
    VERSION_2_0("2.0"),
    VERSION_3_0("3.0"),

    // Newer versions, that will be set:
    VERSION_3_1("3.1");

    private final String version;

    SchemaVersion(String version) {
        this.version = version;
    }

    public static SchemaVersion fromString(String versionString) {
        for (SchemaVersion schemaVersion : SchemaVersion.values()) {
            if (schemaVersion.version.equalsIgnoreCase(versionString)) {
                return schemaVersion;
            }
        }
        throw new IllegalArgumentException("No enum value with version string " + versionString + " found");
    }

    public String getVersion() {
        return version;
    }
}
