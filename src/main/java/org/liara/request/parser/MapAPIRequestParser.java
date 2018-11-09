package org.liara.request.parser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.liara.request.APIRequest;

import java.util.function.Function;

public class MapAPIRequestParser<Input, Output> implements APIRequestParser<Output>
{
  @NonNull
  private final APIRequestParser<Input> _parser;

  @NonNull
  private final Function<@NonNull Input, @NonNull Output> _mapper;

  public MapAPIRequestParser (
    @NonNull final APIRequestParser<Input> parser,
    @NonNull final Function<@NonNull Input, @NonNull Output> mapper
  ) {
    _parser = parser;
    _mapper = mapper;
  }

  @Override
  public @NonNull Output parse (@NonNull final APIRequest request) {
    return _mapper.apply(_parser.parse(request));
  }
}
