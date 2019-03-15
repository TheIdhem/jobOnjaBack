package Solutions.Presentation.Parsers.PathVariableResolvers;

import Solutions.Core.Exceptions.IllegalFormat;
import Solutions.Core.Exceptions.MissingParameter;
import Solutions.Presentation.Controller.ControllerParameterResolver;

import java.util.Optional;

public class JavaTypesControllerParameterResolver {

    public static class IntegerControllerParameterResolver implements ControllerParameterResolver<Integer> {
        @Override
        public Optional<? extends Integer> resolve(String param, Class<? extends Integer> contextType) {

            try {
                return Optional.of(Integer.valueOf(param));
            }catch (NumberFormatException e){
                throw new IllegalFormat("Conversion of controller parameter failed: "+e.getMessage());
            }
        }

        @Override
        public String getRegex(Class<? extends Integer> contextType) {
            return "\\d+";
        }
    }

    public static class StringControllerParameterResolver implements ControllerParameterResolver<String> {
        @Override
        public Optional<? extends String> resolve(String param, Class<? extends String> contextType) {
            return Optional.of(param);
        }

        @Override
        public String getRegex(Class<? extends String> contextType) {
            return "\\.+";
        }
    }
}
