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
package org.liara.request.parser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.APIRequest;

import java.util.function.Function;

/**
 * @author C&eacute;dric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 *
 * An object that parse an APIRequest and output some result.
 *
 * @param <Output> Output of the parser.
 */
@FunctionalInterface
public interface APIRequestParser<Output>
{
  static <Output> @NonNull APIRequestParser<Output[]> compose (
    @NonNull final APIRequestParser<Output> ...parsers
  ) {
    return new CompoundAPIRequestParser<>(parsers);
  }

  /**
   * Parse the given request and output a result.
   * 
   * @param request The request to parse.
   * @return The result of the parse operation.
   */
  @NonNull Output parse (@NonNull final APIRequest request);

  default <NextOutput> @NonNull APIRequestParser<NextOutput> map (@NonNull final Function<Output, NextOutput> mapper) {
    return new MapAPIRequestParser<>(this, mapper);
  }
}
