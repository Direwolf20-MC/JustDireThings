package com.direwolf20.justdirethings.client;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ShaderMods {
    private static boolean usingOptifine;
    private static boolean usingIris;
    private static MethodHandle shaderAccess;
    private static Object instance;

    static {
        try {
            Class<?> clazz = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            shaderAccess = MethodHandles.lookup().findVirtual(clazz, "isShaderPackInUse", MethodType.methodType(boolean.class));
            instance = MethodHandles.lookup().findStatic(clazz, "getInstance", MethodType.methodType(clazz)).invoke();
            usingIris = true;
        } catch (Throwable ignored) {

        }

        if (shaderAccess == null) {
            // Try Optifine
            try {
                shaderAccess = MethodHandles.lookup().findStatic(Class.forName("net.optifine.Config"), "isShaders", MethodType.methodType(boolean.class));
                usingOptifine = true;
            } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException ignored) {

            }
        }
    }

    public static boolean usingShaders() {
        if (usingIris) {
            try {
                return (boolean) shaderAccess.invoke(instance);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        if (usingOptifine) {
            try {
                return (boolean) shaderAccess.invoke();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }
}
