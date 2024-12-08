package cn.wd.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldChangeEntity {
    private String fieldName;
    private Object oldValue;
    private Object newValue;
}
