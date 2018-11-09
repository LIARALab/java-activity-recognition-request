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
package org.liara.request.validator.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.APIRequestParameter;

/**
 * Any error related to a value of a parameter of a request.
 * 
 * @author C&eacute;dric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 */
public class APIRequestParameterValueError extends APIRequestParameterError
{
  @NonNegative
  private final int _index;

  /**
   * Create a new APIRequestParameterValueError for a given parameter value.
   * 
   * @param parameter Invalid parameter.
   * @param index Index of the invalid value.
   * @param description A description of the error.
   * @return A new APIRequestParameterValueError.
   */
  public static APIRequestParameterValueError create (
    @NonNull final APIRequestParameter parameter,
    @NonNegative final int index,
    @NonNull final String description
  ) {
    if (parameter.getSize() <= index) {
      throw new Error(String.join(
        "",
        "Unable to create an error for a parameter value that does not exists. The value at index  ",
        String.valueOf(index), " does not exists."
      ));
    }
    
    return new APIRequestParameterValueError(
      parameter,
      index, 
      description
    );
  }
   
  /**
   * Create a new error for a given parameter value.
   *
   * @param parameter Name of the invalid parameter.
   * @param index Index of the invalid parameter value.
   * @param description A description of the error.
   */
  protected APIRequestParameterValueError(
    @NonNull final APIRequestParameter parameter,
    @NonNull final int index,
    @NonNull final String description
  )
  {
    super(parameter, description);
    _index = index;
  }

  /**
   * Return the index of the invalid parameter value.
   * 
   * @return The index of the invalid parameter value.
   */
  @JsonProperty("index")
  public @NonNegative int getInvalidValueIndex () {
    return _index;
  }
  
  /**
   * Return the value of the invalid parameter.
   * 
   * @return The value of the invalid parameter.
   */
  @JsonIgnore
  public @NonNull String getInvalidValue () {
    return getInvalidParameter().get(_index).get();
  }
}
