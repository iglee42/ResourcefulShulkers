package fr.iglee42.resourcefulshulkers.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public class RSExtraCodecs {

    public static final Codec<Integer> COLOR = Codec.withAlternative(
            Codec.INT,
            Codec.STRING.flatXmap(s->{
                try {
                    String formatted = s.replace("#", "");
                    return DataResult.success(Integer.parseInt(formatted, 16));
                } catch (NumberFormatException e) {
                    return DataResult.error(()->"Invalid color format: " + s,0);
                }
            }, i-> DataResult.success(String.format("#%06X", i)))
    );

}
