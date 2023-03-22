package uk.gov.companieshouse.overseasentitiesapi.model;

import org.bson.Document;

public interface Translator {
    void translate(Document submissionDocument);
}
