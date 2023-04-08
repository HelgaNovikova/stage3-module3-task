package com.mjc.school.controller.command;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import com.mjc.school.service.dto.AuthorCreateDto;
import com.mjc.school.service.dto.NewsCreateDto;
import com.mjc.school.service.dto.TagCreateDto;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommandExecutor {

    private final Map<String, Handler> methods;

    @Autowired
    public CommandExecutor(List<BaseController<?, ?, ?>> controllers) {
        this.methods = controllers.stream()
                .flatMap(this::gatHandlers)
                .collect(Collectors.toMap(this::getHandlerName, h -> h));
    }

    private Stream<Handler> gatHandlers(BaseController<?, ?, ?> c) {
        return Arrays.stream(AopUtils.getTargetClass(c).getDeclaredMethods())
                .filter(m -> !m.isSynthetic())
                .filter(m -> m.isAnnotationPresent(CommandHandler.class))
                .map(m -> new Handler(c, m));
    }

    private String getHandlerName(Handler handler) {
        CommandHandler annotation = handler.method().getAnnotation(CommandHandler.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Method %s should have @CommandHandler".formatted(handler.method));
        }
        return annotation.value();
    }

    public <R> R execute(String handlerName, Map<String, String> body, Map<String, Object> params) {
        Handler handler = methods.get(handlerName);
        if (handler == null) {
            throw new IllegalArgumentException("Handler for %s not found. Only %s exists."
                    .formatted(handlerName, methods.keySet()));
        }
        Parameter[] parameters = handler.method().getParameters();
        List<Object> args = new ArrayList<>();
        for (Parameter p : parameters) {
            CommandParam commandParam = p.getAnnotation(CommandParam.class);
            if (p.isAnnotationPresent(CommandBody.class)) {

                args.add(convertBody(body, p.getType()));
            } else if (commandParam != null) {
                args.add(params.get(commandParam.value()));
            } else {
                throw new IllegalArgumentException("Parameter should be marked with either @CommandBody or @CommandParam");
            }
        }
        try {
            //noinspection unchecked
            return (R) handler.method().invoke(handler.controller(), args.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException r)
                throw r;
            throw new RuntimeException(e.getTargetException());
        }
    }

    private Object convertBody(Map<String, String> body, Class<?> dtoClass) {
        if (dtoClass.equals(AuthorCreateDto.class)) {
            return DtoMapper.INSTANCE.toAuthors(body);
        } else if (dtoClass.equals(NewsCreateDto.class)) {
            return DtoMapper.INSTANCE.toNews(body);
        } else if (dtoClass.equals(TagCreateDto.class)) {
            return DtoMapper.INSTANCE.toTags(body);
        } else throw new IllegalArgumentException(String.format("Class %s is not supported for conversion", dtoClass));

    }

    private record Handler(BaseController<?, ?, ?> controller, Method method) {
    }
}
