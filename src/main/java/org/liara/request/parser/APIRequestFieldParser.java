package org.liara.request.parser;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.tainting.qual.Untainted;

/**
 * @author C&eacute;dric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 * @param <Output>
 *
 * An object that parse an api request field and return some result.
 */
@FunctionalInterface
public interface APIRequestFieldParser<Output>
{
  /**
   * Parse the given field content and return a result.
   *
   * @param field A valid field content to parse.
   *
   * @return The result of the given operation on the field content.
   */
  Output parse (@NonNull @Untainted final String field);
}
