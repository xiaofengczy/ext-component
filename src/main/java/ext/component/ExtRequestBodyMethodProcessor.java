package ext.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class ExtRequestBodyMethodProcessor implements HandlerMethodArgumentResolver {

    private static final String SET = "set";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExtRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        ExtRequestBody ann = parameter.getParameterAnnotation(ExtRequestBody.class);
        Assert.state(ann != null, "No GetBody annotation");
        Class<?> clazz = parameter.getParameterType();
        Object target = null;
        try {
            target = processFiledValue(webRequest, clazz);
        } catch (Exception e) {
        	e.printStackTrace();

        }
        return target;
    }

    /**
     * @description:处理请求参数值 <br>
     * @author:zycao
     * @date: 2020/1/8 下午1:36
     */
    private Object processFiledValue(NativeWebRequest webRequest, Class<?> clazz) throws Exception {
        Object target = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            JsonProperty annotation = declaredField.getAnnotation(JsonProperty.class);
            String filedName = Objects.nonNull(annotation) ? annotation.value() : declaredField.getName();
            String value = getParamValue(webRequest, filedName);
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            Object parseObj = processFiledType(declaredField, value);
            Method declaredMethod = clazz
                    .getMethod(getMethodName(declaredField.getName()), declaredField.getType());
            declaredField.setAccessible(true);
            declaredMethod.invoke(target, parseObj);
        }
        return target;
    }

    /**
     * @description:参数值类型转换 <br>
     * @author:zycao
     * @date: 2020/1/8 下午2:51
     */
    private Object processFiledType(Field declaredField, String value) {
        Object resultValue = value;
        String fieldType = declaredField.getType().getName();
        try {
            if (Objects.equals(fieldType, Integer.class.getName())) {
                resultValue = Integer.valueOf(value);
            } else if (Objects.equals(fieldType, String.class.getName())) {
                resultValue = value;
            } else if (Objects.equals(fieldType, Byte.class.getName())) {
                resultValue = Byte.valueOf(value);
            } else if (Objects.equals(fieldType, Long.class.getName())) {
                resultValue = Long.valueOf(value);
            } else if (Objects.equals(fieldType, Double.class.getName())) {
                resultValue = Double.valueOf(value);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return resultValue;
    }

    /**
     * @description:获取set方法名 <br>
     * @author:zycao
     * @date: 2020/1/8 下午1:56
     */
    private String getMethodName(String name) {
        return SET + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @description:获取请求参数 <br>
     * @author:zycao
     * @date: 2020/1/8 下午1:32
     */
    private String getParamValue(NativeWebRequest webRequest, String fieldName) {
        return webRequest.getParameter(fieldName);
    }
}