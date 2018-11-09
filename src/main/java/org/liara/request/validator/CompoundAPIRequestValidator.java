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

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.APIRequest;

import java.util.*;

public class CompoundAPIRequestValidator implements APIRequestValidator
{
  @NonNull
  private final Set<@NonNull APIRequestValidator> _validators = new HashSet<>();

  public CompoundAPIRequestValidator () { }
  
  public CompoundAPIRequestValidator (@NonNull final Iterable<@NonNull APIRequestValidator> validators) {
    Iterables.addAll(_validators, validators);
  }
  
  public CompoundAPIRequestValidator (@NonNull final Iterator<@NonNull APIRequestValidator> validators) {
    Iterators.addAll(_validators, validators);
  }
  
  public CompoundAPIRequestValidator (@NonNull final APIRequestValidator... validators) {
    _validators.addAll(Arrays.asList(validators));
  }

  public @NonNull CompoundAPIRequestValidator addValidator (@NonNull final APIRequestValidator validator) {
    return new CompoundAPIRequestValidator(Iterables.concat(_validators, Arrays.asList(validator)));
  }

  public @NonNull CompoundAPIRequestValidator addValidators (@NonNull final APIRequestValidator ...validators) {
    return new CompoundAPIRequestValidator(Iterables.concat(_validators, Arrays.asList(validators)));
  }

  @Override
  public @NonNull APIRequestValidation validate (@NonNull final APIRequest request) {
    @NonNull final Iterator<APIRequestValidator> validators = _validators.iterator();
    APIRequestValidation result = new APIRequestValidation(request);

    while (validators.hasNext()) {
      result = result.addErrors(validators.next().validate(request));
    }

    return result;
  }
}
