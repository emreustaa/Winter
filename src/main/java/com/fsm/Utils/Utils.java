package com.fsm.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsm.Annotation.Controller;
import com.fsm.Annotation.Mapping;
import com.fsm.Annotation.QueryParam;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    private static final Utils u = new Utils();

    private Utils() {
    }

    private ObjectMapper mapper = new ObjectMapper();

    private List<Class<?>> controllers = getControllers();

    private List<Class<?>> getControllers() {
        return PathScanner.getInstance()
                .getClassesOf("com.fsm",
                        cls ->
                                cls.isAnnotationPresent(Controller.class));
    }

    public static Utils getInstance() {
        return u;
    }

    private Class<?> getControllerByPath(String path) {
        System.out.println("controller: " + path);
        return controllers.stream()
                .filter(cls -> cls.getAnnotation(Controller.class).path().equals(path))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Controller with path: " + path + " not found"));
    }

    public List<Method> getMappingsOfClass(Class<?> owner) {
        var mappings = new ArrayList<Method>();
        for (Method method : owner.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(Mapping.class)) {
                mappings.add(method);
            }
        }
        return mappings;
    }

    public Method getMappingByPath(Class<?> controller, Path path) {
        System.out.println("action: " + path.action);
        Stream<Method> stream = List.of(controller.getMethods())
                .stream()
                .filter(method -> method.isAnnotationPresent(Mapping.class))
                .filter(method -> method.getAnnotation(Mapping.class).path().equals(path.action))
                .filter(method -> method.getParameters().length == path.parameters.size())
                .filter(checkParameterMatch(path.parameters));
        return stream.findFirst().orElseThrow();
    }

    Predicate<Method> checkParameterMatch(Map<String, Object> map) {
        return (Method method) -> {
            for (Parameter parameter : method.getParameters()) {
                if (!parameter.isAnnotationPresent(QueryParam.class)) {
                    return false;
                }
                String paramName = parameter.getAnnotation(QueryParam.class).name();
                if (!map.containsKey(paramName)) return false;
                if (!cast(map.get(paramName), parameter.getType())) return false;
            }
            return true;
        };
    }

    boolean cast(Object toCast, Class<?> toType) {
        try {
            mapper.convertValue(toCast, toType);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    List<String> getParameterNamesOf(Method method) {
        return List.of(method.getParameters())
                .stream()
                .map(param -> param.getName())
                .collect(Collectors.toList());
    }

    public Object invoke(Path path) {
        Class<?> cls = getControllerByPath(path.controller);
        Method toInvoke = getMappingByPath(cls, path);
        toInvoke.setAccessible(true);
        System.out.println("method found: " + toInvoke.toString());
        Object[] parameterValues = doTypeConversions(
                toInvoke.getParameterTypes(),
                path.parameters.values().toArray());
        Object o = null;
        try {
            o = toInvoke.invoke(getInstance(cls), parameterValues);
        } catch (IllegalAccessException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
        return o;
    }

    private Object[] doTypeConversions(Class<?>[] types, Object[] params) {
        int len = params.length;
        Object[] newParams = new Object[len];
        if (len != 0) {
            for (int i = 0; i < len; i++) {
                int lastIndex = getLastIndex(len, i);
                newParams[i] = mapper.convertValue(params[lastIndex], types[i]);
            }
        }
        List.of(newParams).forEach(System.out::println);
        return newParams;
    }

    private int getLastIndex(int paramsLength, int i) {
        return (paramsLength - i) - 1;
    }

    private Object getInstance(Class<?> cls) {
        Object instance = null;
        try {
            instance = Optional.of(cls.
                    getDeclaredConstructor(null)
                    .newInstance(null))
                    .orElseThrow(() -> new RuntimeException("instance could not be created"));
        } catch (IllegalAccessException |
                InvocationTargetException |
                InstantiationException |
                NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
