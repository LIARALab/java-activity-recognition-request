package org.liara.request.validator;

import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.validator.error.APIRequestError;
import org.liara.request.APIRequest;
import org.liara.request.validator.error.InvalidAPIRequestException;

import java.util.*;

public class APIRequestValidation implements Iterable<@NonNull APIRequestError>
{
  @NonNull
  private final APIRequest _validated;

  @NonNull
  private final Set<@NonNull APIRequestError> _errors;

  public static @NonNull APIRequestValidation concat (@NonNull final APIRequestValidation ...validations) {
    @NonNull APIRequestValidation validation = new APIRequestValidation(validations[0].getRequest());

    for (int index = 0; index < validations.length; ++index) {
      validation = validation.addErrors(validations[index]);
    }

    return validation;
  }

  public APIRequestValidation (@NonNull final APIRequest validated) {
    _validated = validated;
    _errors = new HashSet<>();
  }

  protected APIRequestValidation (
    @NonNull final APIRequest validated,
    @NonNull final Iterable<@NonNull APIRequestError> errors
  ) {
    _validated = validated;
    _errors = new HashSet<>();
    errors.forEach(_errors::add);
  }

  protected APIRequestValidation (
    @NonNull final APIRequest validated,
    @NonNull final Iterator<@NonNull APIRequestError> errors
  ) {
    _validated = validated;
    _errors = new HashSet<>();
    while (errors.hasNext()) {
      _errors.add(errors.next());
    }
  }

  protected APIRequestValidation (
    @NonNull final APIRequest validated,
    @NonNull final APIRequestError[] errors
  ) {
    _validated = validated;
    _errors = new HashSet<>(Arrays.asList(errors));
  }

  public @NonNull APIRequestValidation addError (@NonNull final APIRequestError error) {
    return new APIRequestValidation(_validated, Iterables.concat(_errors, Arrays.asList(error)));
  }

  public @NonNull APIRequestValidation addErrors (@NonNull final APIRequestError ...errors) {
    return new APIRequestValidation(_validated, Iterables.concat(_errors, Arrays.asList(errors)));
  }

  public @NonNull APIRequestValidation addErrors (@NonNull final APIRequestValidation validation) {
    return new APIRequestValidation(_validated, Iterables.concat(this, validation));
  }

  public @NonNull APIRequest getRequest () {
    return _validated;
  }

  public void throwIfInvalid () {
    throw new InvalidAPIRequestException(this);
  }

  public boolean isValid () {
    return _errors.size() <= 0;
  }

  public boolean isInvalid () {
    return _errors.size() > 0;
  }

  public @NonNull Set<@NonNull APIRequestError> getErrors () {
    return Collections.unmodifiableSet(_errors);
  }

  @Override
  public @NonNull Iterator<@NonNull APIRequestError> iterator () {
    return Collections.unmodifiableSet(_errors).iterator();
  }
}
