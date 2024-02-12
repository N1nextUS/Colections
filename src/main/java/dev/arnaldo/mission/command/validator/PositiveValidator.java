package dev.arnaldo.mission.command.validator;

import dev.arnaldo.mission.command.annotation.Positive;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.CommandParameter;
import revxrsal.commands.exception.InvalidNumberException;
import revxrsal.commands.process.ParameterValidator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PositiveValidator implements ParameterValidator<Number> {

    @Override
    public void validate(Number number, @NotNull CommandParameter parameter, @NotNull CommandActor actor) {
        Positive positive = parameter.getAnnotation(Positive.class);
        if (positive == null) return;

        if (number == null || !NumberComparator.validate(number, positive)) {
            throw new InvalidNumberException(parameter, number == null ? "null" : number.toString());
        }
    }

    private enum NumberComparator {

        BYTE(Byte.class, number -> number.compareTo((byte) 0)),
        SHORT(Short.class, number -> number.compareTo((short) 0)),
        FLOAT(Float.class, number -> number.compareTo(0F)),
        INTEGER(Integer.class, number -> number.compareTo(0)),
        DOUBLE(Double.class, number -> number.compareTo(0D)),
        LONG(Long.class, number -> number.compareTo(0L)),

        BIG_DECIMAL(BigDecimal.class, number -> number.compareTo(BigDecimal.ZERO)),
        BIG_INTEGER(BigInteger.class, number -> number.compareTo(BigInteger.ZERO));

        private final Class<? extends Number> clazz;
        private final Function<? extends Number, Integer> function;

        private static final Map<Class<? extends Number>, Function<? extends Number, Integer>> MAP = new HashMap<>();

        static {
            for (NumberComparator comparator : NumberComparator.values()) {
                MAP.put(comparator.clazz, comparator.function);
            }
        }

        <T extends Number> NumberComparator(@NotNull Class<T> clazz, @NotNull Function<T, Integer> function) {
            this.clazz = clazz;
            this.function = function;
        }

        @SuppressWarnings("unchecked cast")
        public static boolean validate(@NotNull Number number, @NotNull Positive positive) {
            Function<Number, Integer> function = (Function<Number, Integer>) MAP.get(number.getClass());
            if (function == null) return false;

            int compare = function.apply(number);
            return positive.ignoreZero() ? compare > 0 : compare >= 0;
        }
    }

}