package de.gzockoll.prototype.templates.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JiraTicket extends ValidateableObject {

    @NotNull
    private String id;

    @NotNull
    private String description;


}
