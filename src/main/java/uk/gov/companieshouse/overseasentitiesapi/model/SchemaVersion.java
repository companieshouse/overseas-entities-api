package uk.gov.companieshouse.overseasentitiesapi.model;

/*
* Starting at version 3.1 a new top-level 'schema_version' field with an initial value is to be included
*/
public enum SchemaVersion {
    VERSION_3_1(3.1);

    private final double versionNumber;

    SchemaVersion(double version) {
       this.versionNumber = version;
    }

    public double getVersionNumber() {
        return this.versionNumber;
    }

    public static SchemaVersion getSchemaVersionByConfigVersionNumber(double configVersionNumber) {
        for(SchemaVersion schemaVersion : values()) {
            if(schemaVersion.getVersionNumber() == configVersionNumber) {
                return schemaVersion;
            }
        }
        return null;
    }
}
