package org.liara.request.validator;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public class APIRequestFieldValidation
{
  @NonNull
  private Set<@NonNull String> _errors;

  public APIRequestFieldValidation () {
    _errors = new HashSet<>();
  }

  public APIRequestFieldValidation (@NonNull final APIRequestFieldValidation toCopy) {
    _errors = new HashSet<>(toCopy.getErrors());
  }

  public boolean isValid () {
    return _errors.size() <= 0;
  }

  public boolean hasErrors () {
    return _errors.size() > 0;
  }

  public void addError (@NonNull final String error) {
    _errors.add(error);
  }

  public @NonNull Set<@NonNull String> getErrors () {
    return Collections.unmodifiableSet(_errors);
  }

  public void setErrors (@NonNull final Collection<@NonNull String> errors) {
    setErrors(errors.iterator());
  }

  public void setErrors (@NonNull final Set<@NonNull String> errors) {
    setErrors(errors.iterator());
  }

  public void setErrors (@NonNull final Iterable<@NonNull String> errors) {
    setErrors(errors.iterator());
  }

  public void setErrors (@NonNull final Iterator<@NonNull String> errors) {
    _errors.clear();
    errors.forEachRemaining(_errors::add);
  }
}
