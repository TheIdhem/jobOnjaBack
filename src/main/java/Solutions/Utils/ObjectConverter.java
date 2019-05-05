package Solutions.Utils;


public class ObjectConverter {

    public static <T> T stringToSerializable(String s, Class<T> tClass){
        if (String.class == tClass)
            return (T) s;
        else if(Integer.class == tClass)
            return (T) Integer.valueOf(s);
        else
            throw new RuntimeException("Unexpected serializable id.");
    }
}
