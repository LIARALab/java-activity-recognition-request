package org.liara.request;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChildAPIRequest implements APIRequest
{
  @NonNull
  private final APIRequest _parent;

  @NonNull
  private final String _prefix;

  public ChildAPIRequest (@NonNull final String prefix, @NonNull final APIRequest parent) {
    _prefix = prefix + ".";
    _parent = parent;
  }

  public ChildAPIRequest (@NonNull final ChildAPIRequest toCopy) {
    _prefix = toCopy.getPrefix();
    _parent = toCopy.getParent();
  }


  @Override
  public boolean contains (final @NonNull String name) {
    return _parent.contains(_prefix + name);
  }

  @Override
  public int getSize () {
    return (int) StreamSupport.stream(_parent.spliterator(), false).filter(
      parameter -> parameter.getName().startsWith(_prefix)
    ).count();
  }

  @Override
  public @NonNull APIRequestParameter getParameter (final @NonNull String name) {
    return _parent.getParameter(_prefix + name);
  }

  @Override
  public @NonNull Set<@NonNull APIRequestParameter> getParameters () {
    return StreamSupport.stream(_parent.spliterator(), false).filter(
      parameter -> parameter.getName().startsWith(_prefix)
    ).collect(Collectors.toSet());
  }

  @Override
  public @NonNull APIRequest getRequest (final @NonNull String prefix) {
    return new ChildAPIRequest(_prefix + prefix, _parent);
  }

  @Override
  public Iterator<@NonNull APIRequestParameter> iterator () {
    return StreamSupport.stream(_parent.spliterator(), false).filter(
      parameter -> parameter.getName().startsWith(_prefix)
    ).iterator();
  }

  public @NonNull APIRequest getParent () {
    return _parent;
  }

  public @NonNull String getPrefix () {
    return _prefix;
  }

  @Override
  public boolean equals (@Nullable final Object other) {
    if (other == null) return false;
    if (other == this) return true;

    if (other instanceof ChildAPIRequest) {
      @NonNull final ChildAPIRequest otherChildAPIRequest = (ChildAPIRequest) other;

      return Objects.equals(
        _parent,
        otherChildAPIRequest.getParent()
      ) && Objects.equals(
        _prefix,
        otherChildAPIRequest.getPrefix()
      );
    }

    return false;
  }

  @Override
  public int hashCode () {
    return Objects.hash(_parent, _prefix);
  }
}
