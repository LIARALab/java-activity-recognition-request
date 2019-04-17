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
package org.liara.request;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Cédric DEMONGIVERT [cedric.demongivert@gmail.com](mailto:cedric.demongivert@gmail.com)
 */
public class StaticAPIRequest implements APIRequest
{
  @NonNull
  private final BiMap<@NonNull String, @NonNull APIRequestParameter> _parameters;
  
  /**
   * Create a new empty StaticAPIRequest instance.
   */
  public StaticAPIRequest () {
    _parameters = HashBiMap.create();
  }
  
  /**
   * Create a new deep copy of another StaticAPIRequest instance.
   * 
   * @param toCopy A request to copy.
   */
  public StaticAPIRequest (@NonNull final StaticAPIRequest toCopy) {
    _parameters = HashBiMap.create();
    
    for (@NonNull final APIRequestParameter parameter : toCopy) {
      _parameters.put(parameter.getName(), new RegisteredAPIRequestParameter(this, parameter));
    }
  }

  /**
   * Create a new StaticAPIRequest instance with all the parameters stored in a given Map.
   *
   * @param parameters A map of parameters key, values pair.
   */
  public StaticAPIRequest (@NonNull final Map<@NonNull String, @NonNull List<@Nullable String>> parameters) {
    _parameters = HashBiMap.create();

    for (final Map.@NonNull Entry<@NonNull String, @NonNull List<@Nullable String>> entry : parameters.entrySet()) {
      _parameters.put(
        entry.getKey(),
        new RegisteredAPIRequestParameter(
          this,
          entry.getKey(),
          entry.getValue().stream().map(x -> x == null ? "" : x).collect(Collectors.toList())
        )
      );
    }
  }
  
  /**
   * @see Iterable#iterator()
   */
  public @NonNull Iterator<@NonNull APIRequestParameter> iterator () {
    return Collections.unmodifiableSet(_parameters.values()).iterator();
  }

  /**
   * Check if a parameter is registered in this request.
   *
   * @param name The name of the parameter to find.
   *
   * @return True if the given parameter exists in this request, false otherwise.
   */
  @Override
  public boolean contains (@NonNull final String name) {
    return _parameters.containsKey(name);
  }

  /**
   * Return the number of parameter registered in this request.
   *
   * @return The number of parameter registered in this request.
   */
  @Override
  public int getSize () {
    return _parameters.size();
  }

  /**
   * Return a parameter of this request.
   *
   * @param name The name of the parameter to find.
   *
   * @return The requested parameter.
   */
  @Override
  public @NonNull APIRequestParameter getParameter (@NonNull final String name) {
    return _parameters.containsKey(name) ? _parameters.get(name)
                                         : new UnregisteredAPIRequestParameter(this, name);
  }

  /**
   * Return all parameters of this request.
   *
   * @return All parameters of this request.
   */
  @Override
  public @NonNull Set<@NonNull APIRequestParameter> getParameters () {
    return Collections.unmodifiableSet(_parameters.values());
  }

  /**
   * Return a child request with all parameters of this request that share a common prefix.
   *
   * @param prefix A prefix to use for the extraction.
   *
   * @return A child request with all parameters of this request that share a common prefix.
   */
  @Override
  public @NonNull APIRequest getRequest (@NonNull final String prefix) {
    return new ChildAPIRequest(prefix,this);
  }
}
