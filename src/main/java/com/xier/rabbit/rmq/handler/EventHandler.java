package com.xier.rabbit.rmq.handler;

import java.lang.reflect.Method;

/**
 * 相当于订阅者的代理
 */
public class EventHandler {
    private final Object target;
    private final Method method;

    public EventHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
        method.setAccessible(true);
    }

    public void handleEvent(Object event) throws Exception {
        preHandle(event);
        try { 
                method.invoke(target, new Object[]{event});          
        } catch (IllegalArgumentException e) {
            throw new Error("Method rejected target/argument: " + event, e);
        } catch (IllegalAccessException e) {
            throw new Error("Method became inaccessible: " + event, e);
        } catch (Throwable e) {
            throw new Exception(e);
        } finally {
            afterHandle(event);
        }
    }

    protected void afterHandle(Object event) {
        //后置
    }

    protected void preHandle(Object event) {
        //前置
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        return (PRIME + method.hashCode()) * PRIME + target.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }

        final EventHandler other = (EventHandler) obj;
        return method.equals(other.method) && target == other.target;
    }

    public String expression() {
        return target.getClass().getCanonicalName() + "#" + method.getName();
    }
}
