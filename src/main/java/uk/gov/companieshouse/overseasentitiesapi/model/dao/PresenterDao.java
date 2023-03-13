package uk.gov.companieshouse.overseasentitiesapi.model.dao;

import org.springframework.data.mongodb.core.mapping.Field;

public class PresenterDao {

    // This temp change was only made to prove that the annotations are being used by the Mongo default converter
    // when converting fields that have not been subject to structure changes
    @Field("full_nameXXX")
    private String fullName;

    @Field("email")
    private String email;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
