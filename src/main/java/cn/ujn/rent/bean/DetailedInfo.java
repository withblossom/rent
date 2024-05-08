package cn.ujn.rent.bean;

import lombok.Data;
import java.util.Collection;

@Data
public class DetailedInfo<T> {
    T obj;
    String profile;
    Collection<String> labels;
}
