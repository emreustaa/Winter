package com.fsm.Utlis;

import com.fsm.Annotation.*;
import com.fsm.Utils.PathScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utils {

    private static final Utils u = new Utils();
    private Utils() {}
    List<Class<?>> controllers = getControllers();

    public static Utils getInstance() {
        return u;
    }

    public InvokeResult invokeMethod(String methodName, InvokeParams params) {
        Method toInvoke = getMappingByName(methodName, params.parameterTypes);
        return invoke(toInvoke, params.parameters);
    }

    private Class<?> getControllerByPath(String path) {
        controllers.stream()
                .map(cls -> cls.getDeclaredAnnotation()



    }

    private InvokeResult invoke(Method toInvoke, Object... parameters) {
        Class<?> declaringClass = toInvoke.getDeclaringClass();
        Object instance = getInstanceFromClass(declaringClass, new ConstructorParams());
        Object result = null;
        try {
            result = toInvoke.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new InvokeResult(toInvoke.getReturnType(), result);
    }

    //dönüş tipi optional yapılabilir
    public Method getMappingByName(String name, Class<?>... parameterTypes) {
        System.out.println("name: " + name);
        Method m = null;
        try {
            m = Optional.
                    ofNullable(Object.class.
                            getMethod(name, parameterTypes))
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "mapping " + name + " could not be found"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return m;
    }

    private Object getInstanceFromClass(Class<?> declaringClass, ConstructorParams params) {
        Object instance = null;
        try {
            instance = Optional.of(declaringClass.
                    getDeclaredConstructor(params.parameterTypes)
                    .newInstance(params.parameters))
                    .orElseThrow();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return instance;
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

    public List<String> getControllerNames() {
        return getControllers().stream()
                .map(controller -> controller.getSimpleName())
                .collect(Collectors.toList());
    }

    public List<Class<?>> getControllers() {
        return PathScanner.getInstance()
                .getClassesOf("com.fsm",
                        cls -> cls.isAnnotationPresent(Controller.class));
    }
}
