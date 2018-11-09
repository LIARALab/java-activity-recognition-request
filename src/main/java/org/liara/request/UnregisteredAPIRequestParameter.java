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
/**
 * 
 */
package org.liara.request;

import com.google.common.collect.Iterators;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

/**
 * @author Cédric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 *
 */
public class UnregisteredAPIRequestParameter implements APIRequestParameter {
  @NonNull
  private static final String[] EMPTY_ARRAY = new String[0];

  @NonNull
  private final APIRequest _request;

  @NonNull
  private final String     _name;

  public UnregisteredAPIRequestParameter (
    @NonNull final APIRequest request,
    @NonNull final String name
  ) {
    _request = request;
    _name = name;
  }
  
  /**
   * @see Iterable#iterator()
   */
  @Override
  public @NonNull Iterator<@NonNull String> iterator () {
    return Iterators.forArray(EMPTY_ARRAY);
  }

  /**
   * @see APIRequestParameter#getRequest()
   */
  @Override
  public @NonNull APIRequest getRequest () {
    return _request;
  }

  /**
   * @see APIRequestParameter#getName()
   */
  @Override
  public @NonNull String getName () {
    return _name;
  }

  /**
   * @see APIRequestParameter#getSize()
   */
  @Override
  public @NonNegative int getSize () {
    return 0;
  }

  /**
   * @see APIRequestParameter#get(int)
   */
  @Override
  public @NonNull Optional<String> get (final int index) {
    return Optional.empty();
  }

  /**
   * @see APIRequestParameter#get()
   */
  @Override
  public @NonNull String[] get () {
    return Arrays.copyOf(EMPTY_ARRAY, 0);
  }
}
