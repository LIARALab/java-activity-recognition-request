package org.liara.request.validator;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.liara.request.APIRequest;
import org.liara.request.validator.error.APIRequestError;
import org.liara.request.validator.error.InvalidAPIRequestException;

import java.util.*;

public class APIRequestValidation implements Iterable<@NonNull APIRequestError>
{
  @NonNull
  private final APIRequest _request;

  @NonNull
  private final Set<@NonNull APIRequestError> _errors;

  public APIRequestValidation (@NonNull final APIRequest request) {
    _request = request;
    _errors = new HashSet<>();
  }

  public APIRequestValidation (@NonNull final APIRequestValidation toCopy) {
    _request = toCopy.getRequest();
    _errors = new HashSet<>(toCopy.getErrors());
  }

  public void addError (@NonNull final APIRequestError error) {
    _errors.add(error);
  }

  public void addErrors (@NonNull final APIRequestError ...errors) {
    _errors.addAll(Arrays.asList(errors));
  }

  public void addErrors (@NonNull final APIRequestValidation validation) {
    _errors.addAll(validation.getErrors());
  }
  public void assertRequestIsValid () throws InvalidAPIRequestException  {
    if (hasErrors()) throw new InvalidAPIRequestException(this);
  }

  public boolean isValid () {
    return _errors.size() <= 0;
  }

  public boolean hasErrors () {
    return _errors.size() > 0;
  }

  public @NonNull Set<@NonNull APIRequestError> getErrors () {
    return Collections.unmodifiableSet(_errors);
  }

  public void setErrors (@NonNull final Iterator<@NonNull APIRequestError> errors) {
    _errors.clear();
    errors.forEachRemaining(_errors::add);
  }

  public void setErrors (@NonNull final Iterable<@NonNull APIRequestError> errors) {
    setErrors(errors.iterator());
  }

  public void setErrors (@NonNull final Set<@NonNull APIRequestError> errors) {
    setErrors(errors.iterator());
  }

  public void setErrors (@NonNull final Collection<@NonNull APIRequestError> errors) {
    setErrors(errors.iterator());
  }

  public @NonNull APIRequest getRequest () {
    return _request;
  }

  @Override
  public @NonNull Iterator<@NonNull APIRequestError> iterator () {
    return Collections.unmodifiableSet(_errors).iterator();
  }

  @Override
  public int hashCode () {
    return Objects.hash(_request, _errors);
  }

  @Override
  public boolean equals (@Nullable final Object other) {
    if (other == null) return false;
    if (other == this) return true;

    if (other instanceof APIRequestValidation) {
      @NonNull final APIRequestValidation otherValidation = (APIRequestValidation) other;

      return Objects.equals(_errors, otherValidation.getErrors()) &&
             Objects.equals(_request, otherValidation.getRequest());
    }

    return false;
  }
}
