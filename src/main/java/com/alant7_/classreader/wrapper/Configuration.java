package com.alant7_.classreader.wrapper;

import com.alant7_.classreader.io.ComponentType;
import com.alant7_.classreader.wrapper.enums.Filter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Accessors(fluent = true)
public class Configuration {

    private final Map<ComponentType, Map<Filter<?>, Object[]>> filters = new HashMap<>();

    @Getter @Setter

    private boolean ignoreFields;

    @Getter @Setter
    private boolean ignoreMethods;

    @Getter @Setter
    private boolean ignoreAnnotations;

    @SafeVarargs
    public final <V> void addFilter(ComponentType type, Filter<V> filter, V... values) {
        filters.computeIfAbsent(type, v -> new HashMap<>()).put(filter, values);
    }

    public Map<Filter<?>, Object[]> getFilters(ComponentType type) {
        return filters.get(type);
    }

}
