package org.liara.request.parser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.APIRequest;

import java.util.Arrays;
import java.util.Collection;

public class CompoundAPIRequestParser<Output> implements APIRequestParser<Output[]>
{
  @NonNull
  private final APIRequestParser<Output>[] _parsers;

  public CompoundAPIRequestParser (@NonNull final APIRequestParser<Output> ...parsers) {
    _parsers = Arrays.copyOf(parsers, parsers.length);
  }

  public CompoundAPIRequestParser (@NonNull final CompoundAPIRequestParser<Output> parser) {
    _parsers = parser._parsers;
  }

  public CompoundAPIRequestParser (@NonNull final Collection<APIRequestParser<Output>> parsers) {
    _parsers = parsers.toArray((APIRequestParser<Output>[]) new APIRequestParser[0]);
  }

  @Override
  public @NonNull Output [] parse (
    @NonNull final APIRequest request
  )
  {
    @NonNull final Output[] outputs = (Output[]) new Object[_parsers.length];

    for (int index = 0; index < _parsers.length; ++index) {
      outputs[index] = _parsers[index].parse(request);
    }

    return outputs;
  }
}
