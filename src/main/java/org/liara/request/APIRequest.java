package org.liara.request;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;

public interface APIRequest
  extends Iterable<@NonNull APIRequestParameter>
{
  boolean contains (@NonNull final String name);

  int getSize ();

  @NonNull APIRequestParameter getParameter (@NonNull final String name);

  @NonNull Set<@NonNull APIRequestParameter> getParameters ();

  @NonNull APIRequest getRequest (@NonNull final String prefix);
}
