/*******************************************************************************
 * Copyright (C) 2018 Cedric DEMONGIVERT <cedric.demongivert@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.liara.request.validator;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.APIRequest;
import org.liara.request.APIRequestParameter;
import org.liara.request.validator.error.APIRequestParameterError;
import org.liara.request.validator.error.APIRequestParameterValueError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author C&eacute;dric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 *
 * An object that validate an API request.
 */
@FunctionalInterface
public interface APIRequestValidator
{
  /**
   * Return a validator that apply all the givens validators in order to its query.
   *
   * @param validators Validators to apply.
   * @return A validator that apply all the givens validators in order to its query.
   */
  static @NonNull APIRequestValidator all (@NonNull final APIRequestValidator ...validators) {
    return all(Arrays.asList(validators));
  }

  /**
   * Return a validator that apply all the givens validators in order to its query.
   *
   * @param validators Validators to apply.
   * @return A validator that apply all the givens validators in order to its query.
   */
  static @NonNull APIRequestValidator all (@NonNull final List<@NonNull APIRequestValidator> validators) {
    @NonNull final List<@NonNull APIRequestValidator> copy = new ArrayList<>(validators);

    return (@NonNull final APIRequest request) -> {
      @NonNull final APIRequestValidation result = new APIRequestValidation(request);

      for (@NonNull final APIRequestValidator validator : copy) {
        result.addErrors(validator.validate(request));
      }

      return result;
    };
  }

  /**
   * Return a validator that assert that a field has at least one value.
   *
   * @param name Name of the field to check.
   *
   * @return A validator that assert that a field has at least one value.
   */
  static @NonNull APIRequestValidator required (
    @NonNull final String name
  ) {
    return required(name, "The field " + name + " is required.");
  }
  /**
   * Return a validator that assert that a field has at least one value.
   *
   * @param name Name of the field to check.
   * @param message A message to return if the field does not have any values.
   *
   * @return A validator that assert that a field has at least one value.
   */
  static @NonNull APIRequestValidator required (
    @NonNull final String name,
    @NonNull final String message
  ) {
    return (@NonNull final APIRequest request) -> {
      @NonNull final APIRequestValidation validation = new APIRequestValidation(request);

      if (request.contains(name)) {
        validation.addError(new APIRequestParameterError(
          request.getParameter(name),
          message
        ));
      }

      return validation;
    };
  }

  /**
   * Return a validator that apply another validator on each value of a field of its query.
   *
   * @param name Name of the field to validate.
   * @param validator A validator to call on each values of the given field.
   *
   * @return A validator that apply another validator on each value of a field of its query.
   */
  static @NonNull APIRequestValidator field (
    @NonNull final String name,
    @NonNull final APIRequestFieldValidator validator
  ) {
    return (@NonNull final APIRequest request) -> {
      @NonNull final APIRequestValidation validation = new APIRequestValidation(request);
      @NonNull final APIRequestParameter parameter = request.getParameter(name);

      for (int index = 0; index < parameter.getSize(); ++index) {
        @NonNull final String field = parameter.get(index).get();

        for (@NonNull final String error : validator.validate(field).getErrors()) {
          validation.addError(APIRequestParameterValueError.create(parameter, index, error));
        }
      }

      return validation;
    };
  }

  /**
   * Return a validator that apply another validator on a child request.
   *
   * @param name Name of the child request.
   * @param validator A validator to call on the child request identified by the given name.
   *
   * @return A validator that apply another validator on a child request.
   */
  static @NonNull APIRequestValidator childRequest (
    @NonNull final String name,
    @NonNull final APIRequestValidator validator
  ) {
    return (@NonNull final APIRequest request) -> validator.validate(request.getRequest(name));
  }

  /**
   * Validate the given request.
   * 
   * @param request A request to validate.
   * @return An api request validation.
   */
  @NonNull APIRequestValidation validate (@NonNull final APIRequest request);
}
