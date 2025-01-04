package cn.wd.work.comparator;

import cn.wd.common.entity.FieldChangeEntity;
import lombok.Data;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 实体类比较
 */
public class EntityComparator {

    public static List<FieldChangeEntity> compareEntities(Object oldEntity, Object newEntity) throws IllegalAccessException {
        if (oldEntity == null || newEntity == null || !oldEntity.getClass().equals(newEntity.getClass())) {
            throw new IllegalArgumentException("Entities must be non-null and of the same type");
        }

        List<FieldChangeEntity> list = new ArrayList<>();
        compareFields(oldEntity, newEntity, "", list);
        return list;
    }

    private static void compareFields(Object oldEntity, Object newEntity, String path, List<FieldChangeEntity> changes) throws IllegalAccessException {
        Class<?> clazz = oldEntity.getClass();
        if (oldEntity instanceof List) {
            compareLists((List<?>) oldEntity, (List<?>) newEntity, path, changes);
            return;
        } else if (oldEntity.getClass().isArray() && newEntity.getClass().isArray()) {
            compareArrays(oldEntity, newEntity, path, changes);
            return;
        } else if (isWrapperType(oldEntity.getClass())) {
            if (oldEntity != newEntity){
                changes.add(new FieldChangeEntity(path, oldEntity, newEntity));
            }
            return;
        } else if (oldEntity.getClass().equals(String.class)) {
            if (!oldEntity.equals(newEntity)){
                changes.add(new FieldChangeEntity(path, oldEntity, newEntity));
            }
            return;
        }
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object oldValue = field.get(oldEntity);
            Object newValue = field.get(newEntity);
            if ((oldValue == null && newValue != null) || (oldValue != null && newValue == null)){
                changes.add(new FieldChangeEntity(path, oldValue, newValue));
            }
            if (oldValue == null || newValue == null){
                return;
            }
            String currentPath = path.isEmpty() ? field.getName() : path + "." + field.getName();
            compareFields(oldValue, newValue, currentPath, changes);
        }
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static final Set<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Void.class
    ));

    private static void compareLists(List<?> list1, List<?> list2, String path, List<FieldChangeEntity> changes) throws IllegalAccessException {
        int maxSize = Math.max(list1.size(), list2.size());
        for (int i = 0; i < maxSize; i++) {
            String currentPath = path + "[" + i + "]";
            if (i >= list1.size()) {
                changes.add(new FieldChangeEntity(currentPath, null, list2.get(i)));
            } else if (i >= list2.size()) {
                changes.add(new FieldChangeEntity(currentPath,list1.get(i), null));
            } else {
                Object oldValue = list1.get(i);
                Object newValue = list2.get(i);
                compareFields(oldValue,newValue, currentPath, changes);
            }
        }
    }

    private static void compareArrays(Object array1, Object array2, String path, List<FieldChangeEntity> changes) throws IllegalAccessException {
        int length1 = Array.getLength(array1);
        int length2 = Array.getLength(array2);
        int maxLength = Math.max(length1, length2);
        for (int i = 0; i < maxLength; i++) {
            String currentPath = path + "[" + i + "]";
            if (i >= length1) {
                changes.add(new FieldChangeEntity(currentPath, null, Array.get(array2, i)));
            } else if (i >= length2) {
                changes.add(new FieldChangeEntity(currentPath, Array.get(array1, i), null));
            } else {
                Object oldValue = Array.get(array1, i);
                Object newValue = Array.get(array2, i);
                compareFields(oldValue,newValue, currentPath, changes);
            }
        }
    }

    public static void main(String[] args) {
        try {
            User user1 = new User();
            user1.setId("2");
            user1.setName("Alice");
            user1.setAge(30);
            user1.setEmail("alice@example.com");
            user1.setRoles(Arrays.asList("admin", "user"));
            user1.setAddresses(new Address[]{new Address("123 Main St"), new Address("456 Elm St")});
            Person p1 = new Person();
            p1.setAge(12);
            p1.setName("wd");
            user1.setPerson(p1);

            User user2 = new User();
            user2.setId("1");
            user2.setName("Alice Smith");
            user2.setAge(31);
            user2.setEmail("alice.smith@example.com");
            user2.setRoles(Arrays.asList("admin", "editor"));
            user2.setAddresses(new Address[]{new Address("123 Main St"), new Address("789 Oak St")});
            Person p2 = new Person();
            p2.setAge(13);
            p2.setName("cy");
            user2.setPerson(p2);

            List<FieldChangeEntity> changes = compareEntities(user1, user2);
            for (FieldChangeEntity entry : changes) {
                System.out.println("Field: " + entry.getFieldName() + " changed from " + entry.getOldValue() + " to " + entry.getNewValue());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

@Data
class Person {
    private String name;
    private Integer age;
}

@Data
class User {
    private String id;
    private String name;
    private Integer age;
    private String email;
    private List<String> roles;
    private Address[] addresses;
    private Person person;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", addresses=" + Arrays.toString(addresses) +
                '}';
    }
}

class Address {
    private String street;

    public Address(String street) {
        this.street = street;
    }

    // Getter and Setter
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                '}';
    }
}


