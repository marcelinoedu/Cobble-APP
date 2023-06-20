package com.manager.finance.services;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Service
public class RequestService {
        public boolean areAllFieldsEmpty(Object request) {
            Field[] fields = request.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (field.get(request) != null) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        public String[] getNullPropertyNames(Object request) {
            BeanWrapper source = new BeanWrapperImpl(request);
            PropertyDescriptor[] pds = source.getPropertyDescriptors();
            Set<String> emptyNames = new HashSet<>();
            for (PropertyDescriptor pd : pds) {
                Object sourceValue = source.getPropertyValue(pd.getName());
                if (sourceValue == null) emptyNames.add(pd.getName());
            }
            return emptyNames.toArray(new String[0]);
        }
}
