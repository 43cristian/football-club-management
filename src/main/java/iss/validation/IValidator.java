package iss.validation;

import iss.model.Entity;

public interface IValidator<ID, E extends Entity<ID>> {
    void validate(E entity);
}
