package org.liara.request.parser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.liara.request.APIRequest;

public class APIRequestDefaultValueParser<@Nullable Output> implements APIRequestParser<@Nullable Output>
{
  @Nullable
  private final Output _defaultValue;
  
  @NonNull
  private final APIRequestParser<Output> _wrapped;

  public APIRequestDefaultValueParser(
    @Nullable final Output defaultValue,
    @NonNull final APIRequestParser<Output> wrapped
  ) {
    _defaultValue = defaultValue;
    _wrapped = wrapped;
  }
  
  @Override
  public @Nullable Output parse (@NonNull final APIRequest request) {
    @Nullable final Output result = _wrapped.parse(request);

    return (result == null) ? _defaultValue : result;
  }
}
