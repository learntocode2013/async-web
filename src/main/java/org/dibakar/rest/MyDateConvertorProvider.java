package org.dibakar.rest;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

public class MyDateConvertorProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type type, Annotation[] annotations) {
        if( rawType.getName().equals(MyDate.class.getName()) ) {
            return new ParamConverter<T>() {
                @Override
                public T fromString(String dateString) {
                    LocalDate date ;
                    MyDate customDate ;
                    switch(dateString) {
                        case "today" :
                            date = LocalDate.now();
                            break ;
                        case "tomorrow" :
                            date = LocalDate.of(LocalDate.now().getYear(), LocalDate
                                    .now().getMonthValue(), LocalDate.now().getDayOfMonth() + 1);
                            break ;
                        case "yesterday" :
                            date = LocalDate.of(LocalDate.now().getYear(), LocalDate
                                    .now().getMonthValue(), LocalDate.now().getDayOfMonth() - 1);
                            break;
                        default:
                            return null ;
                    }
                    customDate =  new MyDate().setDay(date.getDayOfMonth())
                            .setMonth(date.getMonthValue()).setYear(date.getYear());
                    return rawType.cast(customDate);
                }

                @Override
                public String toString(T t) {
                    if( null == t ) return null;
                    return rawType.toString();
                }
            } ;
        }
        return null ;
    }
}
