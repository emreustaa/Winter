package com.fsm.Utils;

import com.fsm.Annotation.Controller;
import com.fsm.Annotation.Mapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Utils {
    private static final Utils u = new Utils();

    private Utils() {
    }

    List<Class<?>> controllers = getControllers();

    private List<Class<?>> getControllers() {
        return PathScanner.getInstance()
                .getClassesOf("com.fsm",
                        cls ->
                                cls.isAnnotationPresent(Controller.class));
    }

    public static Utils getInstance() {
        return u;
    }

    public Object invokeMethod(Path path, InvokeParams params) {
        Class<?> controller = getControllerByPath(path.controller);
        Method toInvoke = getAction(controller, path.action,
                params.parameterTypes);
        return invoke(toInvoke, params);
    }

    private Object invoke(Method toInvoke, Object... parameters) {
        Class<?> declaringClass = toInvoke.getDeclaringClass();
        Object instance = getInstanceFromClass(declaringClass, new ConstructorParams());
        Optional<Object> result = Optional.empty();
        try {
            result = Optional.of(toInvoke.invoke(instance, parameters));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result.orElseThrow(() ->
                new RuntimeException(
                        toInvoke.getName() + " could not be invoked"));
    }

    //dönüş tipi optional yapılabilir
    public Method getAction(Class<?> controller, String action, Class<?>... parameterTypes) {
        Method m = null;
        try {
            m = Optional.
                    ofNullable(controller.
                            getDeclaredMethod(action, parameterTypes))
                    .orElseThrow(() ->
                            new NoSuchElementException(
                                    "action " + action + " could not be found"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return m;
    }

    private Class<?> getControllerByPath(String path) {
        System.out.println("path: " + path);
        return controllers.stream()
                .filter(cls -> cls.getAnnotation(Controller.class).path().equals(path))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Controller with path: " + path + " not found"));
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

    public Method getMappingByPath(Class<?> controller, String path) {
        return List.of(controller.getMethods())
                .stream()
                .filter(method -> method.isAnnotationPresent(Mapping.class))
                .filter(method -> method.getAnnotation(Mapping.class).path().equals(path))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Method with path: " + path +
                                        "could not be found in " + controller.getSimpleName()));
    }

    public Object invoke2(Path path) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Class<?> clss = controllers
//                .stream()
//                .filter(cls -> cls.getSimpleName().equals(path.controller))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("no method found"));
        Class<?> cls = getControllerByPath(path.controller);

        //Method toInvoke = clss.getMethod(path.action, null);
        Method toInvoke = getMappingByPath(cls, path.action);
        toInvoke.setAccessible(true);
        Object o = toInvoke.invoke(
                Optional.of(cls.
                        getDeclaredConstructor(null)
                        .newInstance(null))
                        .orElseThrow(() -> new RuntimeException("instance could not be created"))
                , null);
        return o;
    }

}
