/*
 * Copyright (c) 2012, FOSS Nova Software foundation (FNSF),
 * and individual contributors as indicated by the @author tags.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.fossnova.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.fossnova.json.JsonObject;
import org.fossnova.json.JsonValue;

import com.fossnova.json.stream.JsonWriterImpl;

/**
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
final class JsonObjectImpl extends JsonStructureImpl implements JsonObject {

    private static final long serialVersionUID = 1L;

    private final Map< String, JsonValue > map;

    private final Map< String, JsonValue > userView;

    JsonObjectImpl() {
        map = new TreeMap< String, JsonValue >();
        userView = Collections.unmodifiableMap( map );
    }

    public JsonValue put( final String key, final String value ) {
        return putInternal( key, toJsonString( value ) );
    }

    public JsonValue put( final String key, final Boolean value ) {
        return putInternal( key, toJsonBoolean( value ) );
    }

    public JsonValue put( final String key, final Number value ) {
        return putInternal( key, toJsonNumber( value ) );
    }

    public JsonValue put( final String key, final JsonValue value ) {
        return putInternal( key, value );
    }

    public boolean containsKey( final Object key ) {
        return containsKey( ( String ) key );
    }

    public boolean containsKey( final String key ) {
        return map.containsKey( key );
    }

    public boolean containsValue( final Object value ) {
        if ( ( value == null ) || ( value instanceof String ) ) {
            return containsValue( ( String ) value );
        } else if ( value instanceof Number ) {
            return containsValue( ( Number ) value );
        } else if ( value instanceof Boolean ) {
            return containsValue( ( Boolean ) value );
        } else {
            return containsValue( ( JsonValue ) value );
        }
    }

    public boolean containsValue( final Boolean value ) {
        return map.containsValue( toJsonBoolean( value ) );
    }

    public boolean containsValue( final Number value ) {
        return map.containsValue( toJsonNumber( value ) );
    }

    public boolean containsValue( final String value ) {
        return map.containsValue( toJsonString( value ) );
    }

    public boolean containsValue( final JsonValue value ) {
        return map.containsValue( value );
    }

    public Collection< JsonValue > values() {
        return userView.values();
    }

    public Set< Entry< String, JsonValue >> entrySet() {
        return userView.entrySet();
    }

    public Set< String > keySet() {
        return userView.keySet();
    }

    public JsonValue get( final String key ) {
        return map.get( key );
    }

    public JsonValue get( final Object key ) {
        return get( ( String ) key );
    }

    public JsonValue remove( final String key ) {
        return map.remove( key );
    }

    public JsonValue remove( final Object key ) {
        return remove( ( String ) key );
    }

    public void putAll( final Map< ? extends String, ? extends JsonValue > jsonObject ) {
        map.putAll( jsonObject );
    }

    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals( final Object o ) {
        if ( o == this ) return true;
        if ( !( o instanceof JsonObjectImpl ) ) return false;
        final JsonObjectImpl a = ( JsonObjectImpl ) o;
        return map.equals( a.map );
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public JsonObjectImpl clone() {
        final JsonObjectImpl retVal = new JsonObjectImpl();
        JsonValue jsonValue = null;
        for ( final String jsonKey : map.keySet() ) {
            jsonValue = map.get( jsonKey );
            retVal.put( jsonKey, jsonValue != null ? jsonValue.clone() : null );
        }
        return retVal;
    }

    @Override
    protected void writeTo( final JsonWriterImpl jsonWriter ) throws IOException {
        jsonWriter.writeObjectStart();
        JsonValue jsonValue = null;
        for ( final String jsonKey : map.keySet() ) {
            jsonWriter.writeString( jsonKey );
            jsonValue = map.get( jsonKey );
            if ( jsonValue == null ) {
                jsonWriter.writeNull();
            } else if ( jsonValue instanceof JsonBooleanImpl ) {
                jsonWriter.writeBoolean( ( ( JsonBooleanImpl ) jsonValue ).getBoolean() );
            } else if ( jsonValue instanceof JsonNumberImpl ) {
                jsonWriter.writeNumber( ( ( JsonNumberImpl ) jsonValue ).toString() );
            } else if ( jsonValue instanceof JsonStringImpl ) {
                jsonWriter.writeString( ( ( JsonStringImpl ) jsonValue ).getString() );
            } else if ( jsonValue instanceof JsonStructureImpl ) {
                ( ( JsonStructureImpl ) jsonValue ).writeTo( jsonWriter );
            } else {
                throw new IllegalStateException();
            }
        }
        jsonWriter.writeObjectEnd();
        jsonWriter.flush();
    }

    JsonValue putInternal( final String key, final JsonValue value ) {
        if ( key == null ) {
            throw new IllegalArgumentException( "JSON key cannot be null" );
        }
        return map.put( key, value );
    }
}
