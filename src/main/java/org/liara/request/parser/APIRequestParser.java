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
import org.checkerframework.checker.nullness.qual.Nullable;
import org.liara.request.APIRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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
  /**
   * Return a parser that apply given parsers in order and return their result as a list.
   *
   * @param parsers Parsers to pack.
   * @param <Output> Output type of each composed parser.
   * @return A parser that apply given parsers in order and return their result as a list.
   */
  static <Output> @NonNull APIRequestParser<List<@NonNull Output>> all (
    @NonNull final APIRequestParser<Output> ...parsers
  ) {
    return all(Arrays.asList(parsers));
  }

  /**
   * Return a parser that apply given parsers in order and return their result as a list.
   *
   * @param parsers Parsers to pack.
   * @param <Output> Output type of each composed parser.
   * @return A parser that apply given parsers in order and return their result as a list.
   */
  static <Output> @NonNull APIRequestParser<List<@NonNull Output>> all (
    @NonNull final List<@NonNull APIRequestParser<Output>> parsers
  ) {
    @NonNull final List<@NonNull APIRequestParser<Output>> copy = new ArrayList<>(parsers);

    return (@NonNull final APIRequest request) -> {
      @NonNull final List<@NonNull Output> outputs = new ArrayList<>(copy.size());

      for (@NonNull final APIRequestParser<Output> parser : copy) {
        @Nullable final Output output = parser.parse(request);
        if (output != null) outputs.add(output);
      }

      return outputs;
    };
  }

  static <Output> @NonNull APIRequestParser<Output> factory (
    @NonNull final Supplier<APIRequestParser<Output>> supplier
  ) {
    return (@NonNull final APIRequest request) -> {
      if (request.getSize() <= 0) {
        return null;
      } else {
        return supplier.get().parse(request);
      }
    };
  }

  /**
   * Return a parser that apply the given parser to each value of an APIRequest field.
   *
   * @param name Name of the field to parse.
   * @param parser Parser to apply to each values of the given field.
   * @param <Output> Output type of the field parser.
   * @return A parser that apply the given parser to each fields of its given query and then return the result as a list.
   */
  static <Output> @NonNull APIRequestParser<@Nullable List<@NonNull Output>> field (
    @NonNull final String name,
    @NonNull final APIRequestFieldParser<Output> parser
  ) {
    return (@NonNull final APIRequest request) -> {
      @NonNull final List<@NonNull Output> outputs = new ArrayList<>(request.getParameter(name).getSize());

      for (@NonNull final String field : request.getParameter(name)) {
        @Nullable final Output output = parser.parse(field);
        if (output != null) outputs.add(output);
      }

      return outputs.isEmpty() ? null : outputs;
    };
  }

  /**
   * Return a parser that apply the given parser to a child request of its given request.
   *
   * @param name Name of the child request.
   * @param parser A parser to apply to the child request.
   * @param <Output> Output of the child parser.
   * @return A parser that apply the given parser to a child request of its given request.
   */
  static <Output> @NonNull APIRequestParser<Output> childRequest (
    @NonNull final String name,
    @NonNull final APIRequestParser<Output> parser
  ) {
    return (@NonNull final APIRequest request) -> parser.parse(request.getRequest(name));
  }

  /**
   * Parse the given request and output a result.
   * 
   * @param request The request to parse.
   * @return The result of the parse operation.
   */
  Output parse (@NonNull final APIRequest request);

  /**
   * Apply an operation on the output of this parser.
   *
   * @param mapper An operation to apply to the output of this parser.
   * @param <NextOutput> The operation output type.
   * @return An APIRequestParser that is the result of this parser transformed by the given operation.
   */
  default <NextOutput> @NonNull APIRequestParser<NextOutput> map (
    @NonNull final Function<Output, NextOutput> mapper
  ) {
    return (@NonNull final APIRequest request) -> mapper.apply(parse(request));
  }

  /**
   * Like a map but does not call the given mapper and return null if the result of this parser is null.
   *
   * @param mapper An operation to apply to the output of this parser.
   * @param <NextOutput> The operation output type.
   * @return An APIRequestParser that is the result of this parser transformed by the given operation.
   */
  default <NextOutput> @NonNull APIRequestParser<NextOutput> mapNonNull (
    @NonNull final Function<@NonNull Output, NextOutput> mapper
  ) {
    return (@NonNull final APIRequest request) -> {
      @Nullable final Output output = parse(request);
      return output == null ? null : mapper.apply(output);
    };
  }

  /**
   * Return a parser that returns a default value if this one return null.
   *
   * @param defaultValue A value to return if this parser returns null.
   * @return A parser that returns a default value if this one return null.
   */
  default @NonNull APIRequestParser<@NonNull Output> orElse (@NonNull final Output defaultValue) {
    return (@NonNull final APIRequest request) -> {
      @Nullable final Output output = parse(request);
      return output == null ? defaultValue : output;
    };
  }
}
