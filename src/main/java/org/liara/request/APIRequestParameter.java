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
package org.liara.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

/**
 * @author C&eacute;dric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 * 
 * A parameter of an API Request.
 */
public interface APIRequestParameter extends Iterable<@NonNull String>
{
  /**
   * Return the parent request of this parameter.
   * 
   * @return The parent request of this parameter.
   */
  @JsonIgnore
  @NonNull APIRequest getRequest ();

  /**
   * Return the name of this parameter.
   * 
   * @return The name of this parameter.
   */
  @NonNull String getName ();

  /**
   * Return the number of values registered for this parameter.
   * 
   * @return The number of values registered for this parameter.
   */
  @NonNegative int getSize ();

  /**
   * Return a value of this parameter.
   *
   * @param index Index of the value to return.
   * @return The value defined at the given index of this parameter.
   */
  @NonNull Optional<String> get (final int index);

  default Optional<Integer> getAsInteger (final int index) {
    @NonNull final Optional<String> result = get(index);

    return (result.isPresent()) ? Optional.of(Integer.parseInt(result.get())) : Optional.empty();
  }

  default Optional<Long> getAsLong (final int index) {
    @NonNull final Optional<String> result = get(index);

    return (result.isPresent()) ? Optional.of(Long.parseLong(result.get())) : Optional.empty();
  }

  default Optional<Double> getAsDouble (final int index) {
    @NonNull final Optional<String> result = get(index);

    return (result.isPresent()) ? Optional.of(Double.parseDouble(result.get())) : Optional.empty();
  }

  default Optional<Float> getAsFloat (final int index) {
    @NonNull final Optional<String> result = get(index);

    return (result.isPresent()) ? Optional.of(Float.parseFloat(result.get())) : Optional.empty();
  }

  default Optional<Boolean> getAsBoolean (final int index) {
    @NonNull final Optional<String> result = get(index);

    if (result.isPresent()) {
      @NonNull final String content = result.get().toLowerCase().trim();
      return Optional.of(content.equals("") || content.equals("true") || content.equals("1"));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Return all values of this parameter.
   * 
   * @return All values of this parameter.
   */
  @JsonProperty("values")
  @NonNull String[] get ();
}
